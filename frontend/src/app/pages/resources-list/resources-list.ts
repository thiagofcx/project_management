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
import { ResourceService } from '../../core/services/resource.service';
import type { PagedResources, ResourceSortBy } from '../../models/resource.model';
import type { SortOrder } from '../../core/services/resource.service';

const RESOURCE_SORT_OPTIONS: { value: ResourceSortBy; label: string }[] = [
  { value: 'id', label: 'ID' },
  { value: 'name', label: 'Nome' },
  { value: 'email', label: 'E-mail' },
  { value: 'skills', label: 'Skills' },
  { value: 'createdAt', label: 'Criado em' },
];

@Component({
  selector: 'app-resources-list',
  imports: [RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './resources-list.html',
  styleUrl: './resources-list.scss',
})
export class ResourcesList {
  private readonly auth = inject(AuthService);
  private readonly resourceService = inject(ResourceService);

  user = this.auth.getStoredUser();
  email = this.user?.email ?? '';

  page = signal(0);
  size = signal(20);
  sortBy = signal<ResourceSortBy>('name');
  sortOrder = signal<SortOrder>('ASC');
  nameFilter = signal('');
  skillsFilter = signal('');

  data = signal<PagedResources | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);

  sortOptions = RESOURCE_SORT_OPTIONS;

  totalElements = computed(() => this.data()?.total_elements ?? 0);
  totalPages = computed(() => this.data()?.total_pages ?? 0);
  currentPage = computed(() => this.data()?.number ?? 0);
  content = computed(() => this.data()?.content ?? []);

  /** Dispara recarga ao clicar em Aplicar (filtros nome/skills) */
  private reloadTrigger = signal(0);

  constructor() {
    effect(() => {
      this.page();
      this.size();
      this.sortBy();
      this.sortOrder();
      this.reloadTrigger();
      this.loadResources(
        this.page(),
        this.size(),
        this.sortBy(),
        this.sortOrder(),
        this.nameFilter(),
        this.skillsFilter()
      );
    });
  }

  private loadResources(
    page: number,
    size: number,
    sortBy: ResourceSortBy,
    sortOrder: SortOrder,
    name: string,
    skills: string
  ): void {
    this.loading.set(true);
    this.error.set(null);
    this.resourceService
      .list({
        page,
        size,
        sort_by: sortBy,
        sort_order: sortOrder,
        name: name || undefined,
        skills: skills || undefined,
      })
      .subscribe({
        next: (res) => {
          this.data.set(res);
          this.loading.set(false);
        },
        error: () => {
          this.error.set('Não foi possível carregar os recursos.');
          this.loading.set(false);
        },
      });
  }

  onSortByChange(value: string): void {
    this.sortBy.set(value as ResourceSortBy);
    this.page.set(0);
  }

  onSortOrderChange(value: string): void {
    this.sortOrder.set(value as SortOrder);
    this.page.set(0);
  }

  onNameFilterInput(value: string): void {
    this.nameFilter.set(value);
  }

  onSkillsFilterInput(value: string): void {
    this.skillsFilter.set(value);
  }

  applyFilters(): void {
    this.page.set(0);
    this.reloadTrigger.update((v) => v + 1);
  }

  goToPage(p: number): void {
    if (p >= 0 && p < this.totalPages()) this.page.set(p);
  }

  logout(): void {
    this.auth.clearAuth();
  }
}
