import { CommonModule } from '@angular/common';
import { HttpClientModule,HttpClient } from '@angular/common/http';
import { Component, Injector } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule,Router, ActivatedRoute } from '@angular/router';
import { UsuariosService } from '../usuarios.service';
import { Usuario2 } from '../objetos/Usuario2';




@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [RouterModule,FormsModule,HttpClientModule,CommonModule],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css'
})
export class InicioComponent {
  
  email: string = '';
  password: string = '';
  usuarios!: Usuario2;
  errorMessage: string | null = null;


  constructor(private usuariosService: UsuariosService,private router:Router,private route:ActivatedRoute) { }


  onSubmit() {
    // Aquí puedes utilizar this.email y this.password para obtener los valores del formulario
    // Luego, puedes llamar a un método del servicio UsuariosServicio para realizar alguna acción, como iniciar sesión

    this.usuariosService.getUsuarios(this.email, this.password)
      .subscribe(
        response => {
            this.usuarios = response.usuario;
          
            //this.router.navigate(['inicio_app']);
            
            this.router.navigate(['inicio_app'], { relativeTo: this.route });
          
        },
        error => {
          console.error('Ocurrió un error:', error);
          this.errorMessage = 'El usuario o la contraseña son incorrectos.';
          //this.router.navigate(['/']);
        }
      );
  }
}