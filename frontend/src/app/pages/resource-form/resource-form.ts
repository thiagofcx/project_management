import { Component, inject, signal, computed } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ResourceService } from '../../core/services/resource.service';

@Component({
  selector: 'app-resource-form',
  imports: [RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './resource-form.html',
  styleUrl: './resource-form.scss',
})
export class ResourceForm {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly auth = inject(AuthService);
  private readonly resourceService = inject(ResourceService);

  user = this.auth.getStoredUser();
  email = this.user?.email ?? '';

  id = signal<number | null>(null);
  name = signal('');
  emailField = signal('');
  skills = signal('');

  loading = signal(false);
  saving = signal(false);
  error = signal<string | null>(null);

  isEdit = computed(() => this.id() != null);

  constructor() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const idNum = Number(idParam);
      if (!Number.isNaN(idNum)) {
        this.id.set(idNum);
        this.loadResource(idNum);
      }
    }
  }

  private loadResource(id: number): void {
    this.loading.set(true);
    this.error.set(null);
    this.resourceService.getById(id).subscribe({
      next: (r) => {
        this.name.set(r.name);
        this.emailField.set(r.email);
        this.skills.set(r.skills ?? '');
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Recurso não encontrado.');
        this.loading.set(false);
      },
    });
  }

  onSubmit(): void {
    const nameVal = this.name().trim();
    const emailVal = this.emailField().trim();
    if (!nameVal || !emailVal) {
      this.error.set('Preencha nome e e-mail.');
      return;
    }
    this.error.set(null);
    this.saving.set(true);
    const idVal = this.id();
    const body = {
      name: nameVal,
      email: emailVal,
      skills: this.skills().trim() || undefined,
    };
    const req = idVal != null
      ? this.resourceService.update(idVal, body)
      : this.resourceService.create(body);
    req.subscribe({
      next: () => {
        this.saving.set(false);
        this.router.navigate(['/recursos']);
      },
      error: (err) => {
        this.saving.set(false);
        const body = err?.error;
        const msg = body?.errors?.length
          ? body.errors.map((e: { field?: string; message?: string }) => `${e.field || 'Campo'}: ${e.message || ''}`).join('. ')
          : (body?.message ?? body?.error ?? 'Erro ao salvar. Tente novamente.');
        this.error.set(msg);
      },
    });
  }

  logout(): void {
    this.auth.clearAuth();
  }
}
