import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api.constants';
import type {
  PagedProjects,
  Project,
  ProjectSortBy,
  CreateProjectRequest,
  UpdateProjectRequest,
} from '../../models/project.model';

export type SortOrder = 'ASC' | 'DESC';

export interface ProjectsListParams {
  page?: number;
  size?: number;
  sort_by?: ProjectSortBy;
  sort_order?: SortOrder;
  name?: string;
  status?: string;
}

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private readonly http = inject(HttpClient);

  list(params: ProjectsListParams = {}): Observable<PagedProjects> {
    let httpParams = new HttpParams();
    if (params.page != null) httpParams = httpParams.set('page', params.page);
    if (params.size != null) httpParams = httpParams.set('size', params.size);
    if (params.sort_by) httpParams = httpParams.set('sort_by', params.sort_by);
    if (params.sort_order) httpParams = httpParams.set('sort_order', params.sort_order);
    if (params.name?.trim()) httpParams = httpParams.set('name', params.name.trim());
    if (params.status) httpParams = httpParams.set('status', params.status);
    return this.http.get<PagedProjects>(API_ENDPOINTS.projects, { params: httpParams });
  }

  getById(id: number): Observable<Project> {
    return this.http.get<Project>(`${API_ENDPOINTS.projects}/${id}`);
  }

  create(body: CreateProjectRequest): Observable<Project> {
    return this.http.post<Project>(API_ENDPOINTS.projects, body);
  }

  update(id: number, body: UpdateProjectRequest): Observable<Project> {
    return this.http.put<Project>(`${API_ENDPOINTS.projects}/${id}`, body);
  }
}
