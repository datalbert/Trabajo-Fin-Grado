import { Component } from '@angular/core';
import { Coche} from '../objetos/Coche';
import { FormsModule } from '@angular/forms';
import { InventarioService } from '../servicios/inventario.service';



@Component({
  selector: 'app-agregar-vehiculo',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './agregar-vehiculo.component.html',
  styleUrl: './agregar-vehiculo.component.css'
})
export class AgregarVehiculoComponent {

  marca!: string;
  modelo!: string;
  matricula!: string;
  fotos!: File;
  combustible!: string;
  transmision!: string;
  numasientos!: string;


  nuevocoche=new Coche(this.marca, this.modelo, this.matricula, this.combustible, this.transmision, this.numasientos);

  constructor(private inventarioService: InventarioService) { }


  onSubmit() {

    console.log(this.nuevocoche);
    console.log(this.fotos);

    this.inventarioService.createInventario(this.nuevocoche,this.fotos)
    .subscribe();

  }

  onFileSelected(event:any){
    this.fotos = <File>event.target.files[0];
  }

  
}
