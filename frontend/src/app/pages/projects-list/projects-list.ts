import {
  Component,
  inject,
  signal,
  computed,
  effect,
} from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ProjectService } from '../../core/services/project.service';
import type { PagedProjects, ProjectSortBy } from '../../models/project.model';
import type { SortOrder } from '../../core/services/project.service';

const PROJECT_SORT_OPTIONS: { value: ProjectSortBy; label: string }[] = [
  { value: 'id', label: 'ID' },
  { value: 'name', label: 'Nome' },
  { value: 'description', label: 'Descrição' },
  { value: 'startDate', label: 'Data início' },
  { value: 'endDate', label: 'Data fim' },
  { value: 'status', label: 'Status' },
  { value: 'createdAt', label: 'Criado em' },
];

const PROJECT_STATUS_OPTIONS = [
  { value: '', label: 'Todos' },
  { value: 'PLANNING', label: 'Planejamento' },
  { value: 'IN_PROGRESS', label: 'Em andamento' },
  { value: 'ON_HOLD', label: 'Pausado' },
  { value: 'COMPLETED', label: 'Concluído' },
];

@Component({
  selector: 'app-projects-list',
  imports: [RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './projects-list.html',
  styleUrl: './projects-list.scss',
})
export class ProjectsList {
  private readonly auth = inject(AuthService);
  private readonly projectService = inject(ProjectService);

  user = this.auth.getStoredUser();
  email = this.user?.email ?? '';

  page = signal(0);
  size = signal(20);
  sortBy = signal<ProjectSortBy>('name');
  sortOrder = signal<SortOrder>('ASC');
  nameFilter = signal('');
  statusFilter = signal('');

  data = signal<PagedProjects | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);

  sortOptions = PROJECT_SORT_OPTIONS;
  statusOptions = PROJECT_STATUS_OPTIONS;

  totalElements = computed(() => this.data()?.total_elements ?? 0);
  totalPages = computed(() => this.data()?.total_pages ?? 0);
  currentPage = computed(() => this.data()?.number ?? 0);
  content = computed(() => this.data()?.content ?? []);

  /** Dispara recarga ao clicar em Aplicar (filtro por nome) */
  private reloadTrigger = signal(0);

  constructor() {
    effect(() => {
      this.page();
      this.size();
      this.sortBy();
      this.sortOrder();
      this.statusFilter();
      this.reloadTrigger();
      this.loadProjects(
        this.page(),
        this.size(),
        this.sortBy(),
        this.sortOrder(),
        this.nameFilter(),
        this.statusFilter()
      );
    });
  }

  private loadProjects(
    page: number,
    size: number,
    sortBy: ProjectSortBy,
    sortOrder: SortOrder,
    name: string,
    status: string
  ): void {
    this.loading.set(true);
    this.error.set(null);
    this.projectService
      .list({ page, size, sort_by: sortBy, sort_order: sortOrder, name: name || undefined, status: status || undefined })
      .subscribe({
        next: (res) => {
          this.data.set(res);
          this.loading.set(false);
        },
        error: () => {
          this.error.set('Não foi possível carregar os projetos.');
          this.loading.set(false);
        },
      });
  }

  onSortByChange(value: string): void {
    this.sortBy.set(value as ProjectSortBy);
    this.page.set(0);
  }

  onSortOrderChange(value: string): void {
    this.sortOrder.set(value as SortOrder);
    this.page.set(0);
  }

  onNameFilterInput(value: string): void {
    this.nameFilter.set(value);
    this.page.set(0);
  }

  onStatusFilterChange(value: string): void {
    this.statusFilter.set(value);
    this.page.set(0);
  }

  applyFilters(): void {
    this.page.set(0);
    this.reloadTrigger.update((v) => v + 1);
  }

  goToPage(p: number): void {
    if (p >= 0 && p < this.totalPages()) this.page.set(p);
  }

  statusLabel(status: string): string {
    const o = PROJECT_STATUS_OPTIONS.find((x) => x.value === status);
    return o?.label ?? status;
  }

  logout(): void {
    this.auth.clearAuth();
  }
}
