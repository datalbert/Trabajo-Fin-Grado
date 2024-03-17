import { Routes } from '@angular/router';
import { CrearcuentaComponent } from './crearcuenta/crearcuenta.component';
import { InicioComponent } from './inicio/inicio.component';

export const routes: Routes = [

    { path: 'crear-usuario', component:  CrearcuentaComponent},
    { path: '', component:  InicioComponent}
    
];
