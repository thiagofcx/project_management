import type { Project } from './project.model';
import type { Resource } from './resource.model';

/** Alocação (recurso vinculado ao projeto) - API snake_case */
export interface Allocation {
  id: number;
  project: Project;
  resource: Resource;
  created_at: string;
}

/** POST /api/allocations - vincular recurso ao projeto */
export interface CreateAllocationRequest {
  project_id: number;
  resource_id: number;
}

/** Resposta paginada GET /api/allocations */
export interface PagedAllocations {
  content: Allocation[];
  total_elements: number;
  total_pages: number;
  number: number;
  size: number;
}
