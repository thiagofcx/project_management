/** Base URL da API (conforme api_doc.yaml servers) */
export const API_BASE_URL = 'http://localhost:8080';

export const API_ENDPOINTS = {
  auth: `${API_BASE_URL}/api/auth`,
  dashboardSummary: `${API_BASE_URL}/api/dashboard/summary`,
  projects: `${API_BASE_URL}/api/projects`,
  resources: `${API_BASE_URL}/api/resources`,
  allocations: `${API_BASE_URL}/api/allocations`,
} as const;
