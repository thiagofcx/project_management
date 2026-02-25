import { Component, inject, signal, computed } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ProjectService } from '../../core/services/project.service';
import type { ProjectStatus } from '../../models/project.model';

const STATUS_OPTIONS: { value: ProjectStatus; label: string }[] = [
  { value: 'PLANNING', label: 'Planejamento' },
  { value: 'IN_PROGRESS', label: 'Em andamento' },
  { value: 'ON_HOLD', label: 'Pausado' },
  { value: 'COMPLETED', label: 'Concluído' },
];

@Component({
  selector: 'app-project-form',
  imports: [RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './project-form.html',
  styleUrl: './project-form.scss',
})
export class ProjectForm {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly auth = inject(AuthService);
  private readonly projectService = inject(ProjectService);

  user = this.auth.getStoredUser();
  email = this.user?.email ?? '';

  id = signal<number | null>(null);
  name = signal('');
  description = signal('');
  startDate = signal('');
  endDate = signal('');
  status = signal<ProjectStatus>('PLANNING');

  loading = signal(false);
  saving = signal(false);
  error = signal<string | null>(null);
  statusOptions = STATUS_OPTIONS;

  isEdit = computed(() => this.id() != null);

  constructor() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const idNum = Number(idParam);
      if (!Number.isNaN(idNum)) {
        this.id.set(idNum);
        this.loadProject(idNum);
      }
    }
  }

  private loadProject(id: number): void {
    this.loading.set(true);
    this.error.set(null);
    this.projectService.getById(id).subscribe({
      next: (p) => {
        this.name.set(p.name);
        this.description.set(p.description ?? '');
        this.startDate.set(p.start_date);
        this.endDate.set(p.end_date);
        this.status.set(p.status as ProjectStatus);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Projeto não encontrado.');
        this.loading.set(false);
      },
    });
  }

  onSubmit(): void {
    const nameVal = this.name().trim();
    const startVal = this.startDate();
    const endVal = this.endDate();
    if (!nameVal || !startVal || !endVal) {
      this.error.set('Preencha nome, data de início e data de fim.');
      return;
    }
    this.error.set(null);
    this.saving.set(true);
    const idVal = this.id();
    const body = {
      name: nameVal,
      description: this.description().trim() || undefined,
      start_date: startVal,
      end_date: endVal,
      status: this.status(),
    };
    const req = idVal != null
      ? this.projectService.update(idVal, body)
      : this.projectService.create(body);
    req.subscribe({
      next: () => {
        this.saving.set(false);
        this.router.navigate(['/projetos']);
      },
      error: (err) => {
        this.saving.set(false);
        const body = err?.error;
        const msg = body?.errors?.length
          ? body.errors.map((e: { field?: string; message?: string }) => `${e.field || 'Campo'}: ${e.message || ''}`).join('. ')
          : (body?.message ?? body?.error ?? 'Erro ao salvar. Tente novamente.');
        this.error.set(msg);
      },
    });
  }

  logout(): void {
    this.auth.clearAuth();
  }
}
