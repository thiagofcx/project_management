import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api.constants';
import type { Allocation, PagedAllocations } from '../../models/allocation.model';

export interface AllocationsListParams {
  page?: number;
  size?: number;
  sort_by?: string;
  sort_order?: 'ASC' | 'DESC';
  resource_name?: string;
  project_name?: string;
}

@Injectable({ providedIn: 'root' })
export class AllocationService {
  private readonly http = inject(HttpClient);

  list(params: AllocationsListParams = {}): Observable<PagedAllocations> {
    let httpParams = new HttpParams();
    if (params.page != null) httpParams = httpParams.set('page', params.page);
    if (params.size != null) httpParams = httpParams.set('size', params.size);
    if (params.sort_by) httpParams = httpParams.set('sort_by', params.sort_by);
    if (params.sort_order) httpParams = httpParams.set('sort_order', params.sort_order);
    if (params.resource_name?.trim()) httpParams = httpParams.set('resource_name', params.resource_name.trim());
    if (params.project_name?.trim()) httpParams = httpParams.set('project_name', params.project_name.trim());
    return this.http.get<PagedAllocations>(API_ENDPOINTS.allocations, { params: httpParams });
  }

  create(project_id: number, resource_id: number): Observable<Allocation> {
    return this.http.post<Allocation>(API_ENDPOINTS.allocations, { project_id, resource_id });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API_ENDPOINTS.allocations}/${id}`);
  }
}
