import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, catchError, throwError } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api.constants';
import type { AuthRequest, AuthResponse, ApiError, ApiValidationError } from '../../models/auth.model';

const TOKEN_KEY = 'auth_token';
const USER_KEY = 'auth_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);

  login(credentials: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(API_ENDPOINTS.auth, credentials).pipe(
      tap((res) => {
        this.setToken(res.token);
        this.setUser({ email: res.email, user_id: res.user_id, role: res.role });
      }),
      catchError((err) => throwError(() => this.normalizeError(err)))
    );
  }

  getToken(): string | null {
    return typeof localStorage !== 'undefined' ? localStorage.getItem(TOKEN_KEY) : null;
  }

  setToken(token: string): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(TOKEN_KEY, token);
    }
  }

  clearAuth(): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(USER_KEY);
    }
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getStoredUser(): { email: string; user_id: number; role: string } | null {
    if (typeof localStorage === 'undefined') return null;
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as { email: string; user_id: number; role: string };
    } catch {
      return null;
    }
  }

  private setUser(user: { email: string; user_id: number; role: string }): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(USER_KEY, JSON.stringify(user));
    }
  }

  private normalizeError(err: unknown): { message: string; validation?: Array<{ field: string; message: string }> } {
    // HttpClient coloca o corpo da resposta em .error (HttpErrorResponse)
    if (err && typeof err === 'object' && 'error' in err) {
      const body = (err as { error?: ApiError | ApiValidationError }).error;
      if (body && typeof body === 'object' && 'message' in body) {
        const msg = String(body.message || 'Falha ao autenticar. Tente novamente.');
        const validation = Array.isArray((body as ApiValidationError).errors)
          ? (body as ApiValidationError).errors!.map((e) => ({
              field: e.field ?? '',
              message: e.message ?? '',
            }))
          : undefined;
        return { message: msg, validation };
      }
    }
    if (err instanceof Error && err.message) {
      return { message: err.message };
    }
    return { message: 'Erro de conexão. Verifique se o servidor está em execução.' };
  }
}
