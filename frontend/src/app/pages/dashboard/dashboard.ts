import {
  Component,
  inject,
  ViewChild,
  ElementRef,
  AfterViewInit,
  OnDestroy,
  signal,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { Chart } from 'chart.js/auto';
import { AuthService } from '../../core/services/auth.service';
import { DashboardService } from '../../core/services/dashboard.service';
import type { DashboardSummary } from '../../models/dashboard.model';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements AfterViewInit, OnDestroy {
  private readonly auth = inject(AuthService);
  private readonly dashboardService = inject(DashboardService);

  @ViewChild('chartProjects') chartProjectsRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('chartResources') chartResourcesRef!: ElementRef<HTMLCanvasElement>;

  user = this.auth.getStoredUser();
  email = this.user?.email ?? '';

  summary = signal<DashboardSummary | null>(null);
  loading = signal(true);
  error = signal<string | null>(null);

  private chartProjects: Chart | null = null;
  private chartResources: Chart | null = null;

  ngOnInit(): void {
    this.dashboardService.getSummary().subscribe({
      next: (data) => {
        this.summary.set(data);
        this.loading.set(false);
        this.error.set(null);
        setTimeout(() => this.createCharts(), 0);
      },
      error: (error: Error) => {
        console.error(error);
        this.loading.set(false);
        this.error.set('Não foi possível carregar o resumo do dashboard.');
      },
    });
  }

  ngAfterViewInit(): void {
    const data = this.summary();
    if (data) this.createCharts();
  }

  ngOnDestroy(): void {
    this.chartProjects?.destroy();
    this.chartResources?.destroy();
  }

  private createCharts(): void {
    const data = this.summary();
    if (!data) return;

    this.createProjectsChart(data);
    this.createResourcesChart(data);
  }

  private createProjectsChart(data: DashboardSummary): void {
    const el = this.chartProjectsRef?.nativeElement;
    if (!el) return;
    this.chartProjects?.destroy();

    const byStatus = data.projects.by_status;
    this.chartProjects = new Chart(el, {
      type: 'bar',
      data: {
        labels: ['Planejamento', 'Em andamento', 'Pausado', 'Concluído'],
        datasets: [
          {
            label: 'Projetos',
            data: [
              byStatus.planning,
              byStatus.in_progress,
              byStatus.on_hold,
              byStatus.completed,
            ],
            backgroundColor: [
              'rgba(99, 102, 241, 0.8)',
              'rgba(34, 197, 94, 0.8)',
              'rgba(234, 179, 8, 0.8)',
              'rgba(107, 114, 128, 0.8)',
            ],
            borderColor: ['#6366f1', '#22c55e', '#eab308', '#6b7280'],
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          title: { display: true, text: 'Projetos por status' },
        },
        scales: {
          y: { beginAtZero: true, ticks: { stepSize: 1 } },
        },
      },
    });
  }

  private createResourcesChart(data: DashboardSummary): void {
    const el = this.chartResourcesRef?.nativeElement;
    if (!el) return;
    this.chartResources?.destroy();

    const { allocated, unallocated } = data.resources;
    this.chartResources = new Chart(el, {
      type: 'doughnut',
      data: {
        labels: ['Alocados', 'Não alocados'],
        datasets: [
          {
            data: [allocated, unallocated],
            backgroundColor: ['rgba(34, 197, 94, 0.8)', 'rgba(148, 163, 184, 0.8)'],
            borderColor: ['#22c55e', '#94a3b8'],
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: 'bottom' },
          title: { display: true, text: 'Recursos (alocados Vs não alocados)' },
        },
      },
    });
  }

  logout(): void {
    this.auth.clearAuth();
  }
}
