import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { LoginLayout } from './components/login-layout/login-layout';
import { Dashboard } from './pages/dashboard/dashboard';

export const routes: Routes = [
  { path: 'login', component: LoginLayout },
  { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  { path: '**', redirectTo: 'dashboard' },
];
