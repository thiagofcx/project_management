/** API usa snake_case - GET /api/resources, POST/PUT recurso */
export interface Resource {
  id: number;
  name: string;
  email: string;
  skills: string | null;
  created_at: string;
}

/** Resposta paginada GET /api/resources */
export interface PagedResources {
  content: Resource[];
  total_elements: number;
  total_pages: number;
  number: number;
  size: number;
}

/** POST /api/resources - API snake_case */
export interface CreateResourceRequest {
  name: string;
  email: string;
  skills?: string;
}

/** PUT /api/resources/{id} - API snake_case */
export interface UpdateResourceRequest {
  name?: string;
  email?: string;
  skills?: string;
}

/** Campos permitidos para ordenação (api_doc.yaml) */
export type ResourceSortBy = 'id' | 'name' | 'email' | 'skills' | 'createdAt';
