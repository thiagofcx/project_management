/** Corpo da requisição POST /api/auth (Swagger: AuthRequest) */
export interface AuthRequest {
  email: string;
  password: string;
}

/** Resposta da autenticação - API usa snake_case */
export interface AuthResponse {
  token: string;
  email: string;
  user_id: number;
  role: 'ADMIN' | 'USER';
}

/** Erro padrão da API (Swagger: ApiError) */
export interface ApiError {
  timestamp?: string;
  error?: string;
  message: string;
}

/** Erro de validação 400 - lista de erros por campo */
export interface ApiValidationError extends ApiError {
  errors?: Array<{ field: string; message: string }>;
}
