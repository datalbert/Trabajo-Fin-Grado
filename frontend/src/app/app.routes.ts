import { Routes } from '@angular/router';
import { CrearcuentaComponent } from './crearcuenta/crearcuenta.component';
import { InicioComponent } from './inicio/inicio.component';
import { PrincipalComponent } from './principal/principal.component';
import { FormulariocontactoComponent } from './formulariocontacto/formulariocontacto.component';
import { PerfilComponent } from './perfil/perfil.component';
import { InventarioComponent } from './inventario/inventario.component';
import { AgregarVehiculoComponent } from './agregar-vehiculo/agregar-vehiculo.component';
import { ListaVehiculosComponent } from './lista-vehiculos/lista-vehiculos.component';

export const routes: Routes = [

    { path: '', component:  InicioComponent},
    { 
        path: 'inicio_app', 
        component: PrincipalComponent,
        children: [
          { path: 'perfil', component: PerfilComponent },
          { path: 'crear-usuario', component: CrearcuentaComponent },
          { path: 'inventario', component:InventarioComponent,
            children: [
              { path: 'agregar_vehiculo', component: AgregarVehiculoComponent },
              { path: 'lista_vehiculos', component:  ListaVehiculosComponent }
          ]
          }

        ]
      }
];
