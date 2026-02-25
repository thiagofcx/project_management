import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api.constants';
import type {
  PagedResources,
  Resource,
  ResourceSortBy,
  CreateResourceRequest,
  UpdateResourceRequest,
} from '../../models/resource.model';

export type SortOrder = 'ASC' | 'DESC';

export interface ResourcesListParams {
  page?: number;
  size?: number;
  sort_by?: ResourceSortBy;
  sort_order?: SortOrder;
  name?: string;
  skills?: string;
}

@Injectable({ providedIn: 'root' })
export class ResourceService {
  private readonly http = inject(HttpClient);

  list(params: ResourcesListParams = {}): Observable<PagedResources> {
    let httpParams = new HttpParams();
    if (params.page != null) httpParams = httpParams.set('page', params.page);
    if (params.size != null) httpParams = httpParams.set('size', params.size);
    if (params.sort_by) httpParams = httpParams.set('sort_by', params.sort_by);
    if (params.sort_order) httpParams = httpParams.set('sort_order', params.sort_order);
    if (params.name?.trim()) httpParams = httpParams.set('name', params.name.trim());
    if (params.skills?.trim()) httpParams = httpParams.set('skills', params.skills.trim());
    return this.http.get<PagedResources>(API_ENDPOINTS.resources, { params: httpParams });
  }

  getById(id: number): Observable<Resource> {
    return this.http.get<Resource>(`${API_ENDPOINTS.resources}/${id}`);
  }

  create(body: CreateResourceRequest): Observable<Resource> {
    return this.http.post<Resource>(API_ENDPOINTS.resources, body);
  }

  update(id: number, body: UpdateResourceRequest): Observable<Resource> {
    return this.http.put<Resource>(`${API_ENDPOINTS.resources}/${id}`, body);
  }
}
