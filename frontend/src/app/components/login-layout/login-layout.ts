import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login-layout',
  imports: [ReactiveFormsModule],
  templateUrl: './login-layout.html',
  styleUrl: './login-layout.scss',
})
export class LoginLayout {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly validationErrors = signal<Array<{ field: string; message: string }>>([]);

  readonly form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
  });

  onSubmit(): void {
    this.errorMessage.set(null);
    this.validationErrors.set([]);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { email, password } = this.form.getRawValue();
    this.loading.set(true);

    this.auth.login({ email, password }).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/dashboard']);
      },
      error: (err: { message?: string; validation?: Array<{ field: string; message: string }> }) => {
        this.loading.set(false);
        this.errorMessage.set(err?.message ?? 'Falha ao autenticar.');
        this.validationErrors.set(err?.validation ?? []);
      },
    });
  }
}
