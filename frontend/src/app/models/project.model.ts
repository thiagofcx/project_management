/** API usa snake_case - GET /api/projects, POST/PUT projeto */
export interface Project {
  id: number;
  name: string;
  description: string | null;
  start_date: string;
  end_date: string;
  status: ProjectStatus;
  created_at: string;
}

export type ProjectStatus = 'PLANNING' | 'IN_PROGRESS' | 'ON_HOLD' | 'COMPLETED';

/** Resposta paginada GET /api/projects */
export interface PagedProjects {
  content: Project[];
  total_elements: number;
  total_pages: number;
  number: number;
  size: number;
}

/** POST /api/projects - API snake_case */
export interface CreateProjectRequest {
  name: string;
  description?: string;
  start_date: string;
  end_date: string;
  status?: ProjectStatus;
}

/** PUT /api/projects/{id} - API snake_case */
export interface UpdateProjectRequest {
  name?: string;
  description?: string;
  start_date?: string;
  end_date?: string;
  status?: ProjectStatus;
}

/** Campos permitidos para ordenação (api_doc.yaml) */
export type ProjectSortBy =
  | 'id'
  | 'name'
  | 'description'
  | 'startDate'
  | 'endDate'
  | 'status'
  | 'createdAt';
