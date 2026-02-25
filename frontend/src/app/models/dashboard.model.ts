/** Resposta GET /api/dashboard/summary - API usa snake_case */
export interface DashboardSummary {
  projects: {
    total: number;
    by_status: {
      planning: number;
      in_progress: number;
      on_hold: number;
      completed: number;
    };
  };
  resources: {
    total: number;
    allocated: number;
    unallocated: number;
  };
  allocations: {
    total: number;
  };
}
