import { Component, inject, signal, computed, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { AuthService } from '../../core/services/auth.service';
import { ProjectService } from '../../core/services/project.service';
import { ResourceService } from '../../core/services/resource.service';
import { AllocationService } from '../../core/services/allocation.service';
import type { Project } from '../../models/project.model';
import type { Resource } from '../../models/resource.model';
import type { Allocation } from '../../models/allocation.model';

@Component({
  selector: 'app-project-allocations',
  imports: [RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './project-allocations.html',
  styleUrl: './project-allocations.scss',
})
export class ProjectAllocations implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly auth = inject(AuthService);
  private readonly projectService = inject(ProjectService);
  private readonly resourceService = inject(ResourceService);
  private readonly allocationService = inject(AllocationService);

  user = this.auth.getStoredUser();
  email = this.user?.email ?? '';

  projectId = signal<number | null>(null);
  project = signal<Project | null>(null);
  allocations = signal<Allocation[]>([]);
  allResources = signal<Resource[]>([]);
  selectedResourceId = signal<number | null>(null);

  loading = signal(true);
  loadingResources = signal(false);
  saving = signal(false);
  error = signal<string | null>(null);

  /** IDs dos recursos já alocados neste projeto (para esconder do select) */
  allocatedResourceIds = computed(() =>
    this.allocations().map((a) => a.resource.id)
  );

  /** Recursos disponíveis para vincular (não alocados neste projeto) */
  availableResources = computed(() => {
    const ids = new Set(this.allocatedResourceIds());
    return this.allResources().filter((r) => !ids.has(r.id));
  });

  constructor() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = Number(idParam);
      if (!Number.isNaN(id)) {
        this.projectId.set(id);
        this.loadProjectAndAllocations(id);
      } else {
        this.loading.set(false);
        this.error.set('ID do projeto inválido.');
      }
    } else {
      this.loading.set(false);
      this.error.set('Projeto não informado.');
    }
  }

  ngOnInit(): void {
    if (this.projectId() != null) {
      this.loadResources();
    }
  }

  private loadProjectAndAllocations(id: number): void {
    this.loading.set(true);
    this.error.set(null);
    this.projectService.getById(id).subscribe({
      next: (p) => {
        this.project.set(p);
        this.loadAllocations(p.name);
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Projeto não encontrado.');
      },
    });
  }

  private loadAllocations(projectName: string): void {
    this.allocationService
      .list({ project_name: projectName, size: 100 })
      .subscribe({
        next: (page) => {
          this.allocations.set(page.content);
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
          this.error.set('Não foi possível carregar os desenvolvedores do projeto.');
        },
      });
  }

  /** Busca todos os desenvolvedores (recursos) em GET /api/resources, paginando se necessário. */
  private loadResources(): void {
    this.loadingResources.set(true);
    this.allResources.set([]);
    // Primeira página: page=0, size=100 (máximo permitido pela API)
    this.resourceService.list({ page: 0, size: 100 }).subscribe({
      next: (firstPage) => {
        let list = [...firstPage.content];
        const totalPages = firstPage.total_pages;
        if (totalPages <= 1) {
          this.allResources.set(list);
          this.loadingResources.set(false);
          return;
        }
        // Buscar páginas restantes
        const rest$ = Array.from({ length: totalPages - 1 }, (_, i) =>
          this.resourceService.list({ page: i + 1, size: 100 })
        );
        forkJoin(rest$).subscribe({
          next: (pages) => {
            pages.forEach((p) => (list = list.concat(p.content)));
            this.allResources.set(list);
            this.loadingResources.set(false);
          },
          error: () => this.loadingResources.set(false),
        });
      },
      error: () => this.loadingResources.set(false),
    });
  }

  vincular(): void {
    const pid = this.projectId();
    const rid = this.selectedResourceId();
    if (pid == null || rid == null) {
      this.error.set('Selecione um desenvolvedor.');
      return;
    }
    this.error.set(null);
    this.saving.set(true);
    this.allocationService.create(pid, rid).subscribe({
      next: () => {
        this.selectedResourceId.set(null);
        this.saving.set(false);
        const p = this.project();
        if (p) this.loadAllocations(p.name);
      },
      error: (err) => {
        this.saving.set(false);
        const body = err?.error;
        const msg = body?.errors?.length
          ? body.errors.map((e: { field?: string; message?: string }) => `${e.field || ''}: ${e.message || ''}`).join('. ')
          : (body?.message ?? body?.error ?? 'Erro ao vincular. O recurso pode já estar alocado em outro projeto.');
        this.error.set(msg);
      },
    });
  }

  desvincular(allocation: Allocation): void {
    if (!confirm(`Desvincular ${allocation.resource.name} do projeto?`)) return;
    this.error.set(null);
    this.allocationService.delete(allocation.id).subscribe({
      next: () => {
        const p = this.project();
        if (p) this.loadAllocations(p.name);
      },
      error: () => {
        this.error.set('Não foi possível desvincular.');
      },
    });
  }

  logout(): void {
    this.auth.clearAuth();
  }
}
