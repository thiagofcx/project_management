import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { LoginLayout } from './components/login-layout/login-layout';
import { Dashboard } from './pages/dashboard/dashboard';
import { ProjectsList } from './pages/projects-list/projects-list';
import { ResourcesList } from './pages/resources-list/resources-list';
import { ProjectForm } from './pages/project-form/project-form';
import { ResourceForm } from './pages/resource-form/resource-form';

export const routes: Routes = [
  { path: 'login', component: LoginLayout },
  { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },
  { path: 'projetos', component: ProjectsList, canActivate: [authGuard] },
  { path: 'projetos/novo', component: ProjectForm, canActivate: [authGuard] },
  { path: 'projetos/:id/editar', component: ProjectForm, canActivate: [authGuard] },
  { path: 'recursos', component: ResourcesList, canActivate: [authGuard] },
  { path: 'recursos/novo', component: ResourceForm, canActivate: [authGuard] },
  { path: 'recursos/:id/editar', component: ResourceForm, canActivate: [authGuard] },
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  { path: '**', redirectTo: 'dashboard' },
];
