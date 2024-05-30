import { NgFor, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { InventarioService } from '../servicios/inventario.service';
import { Coche } from '../objetos/Coche';
import { CocheHTML } from '../objetos/CocheHTML';
import { DomSanitizer } from '@angular/platform-browser';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-lista-vehiculos',
  standalone: true,
  imports: [NgFor,NgIf,FormsModule],
  templateUrl: './lista-vehiculos.component.html',
  styleUrl: './lista-vehiculos.component.css'
})
export class ListaVehiculosComponent {

  coches: CocheHTML[] = [];

  imagen:any;

  selectedCocheVer!: CocheHTML | null;
  selectedCocheEditar!: CocheHTML | null;

  constructor(private inventarioService: InventarioService, private sanitizier:DomSanitizer) { }

  ngOnInit() {
    this.inventarioService.obtenerTodosLosCoches().subscribe(coches => {
      this.coches = coches;
      coches.forEach(coche => {
        this.inventarioService.obtenerImagenCoche(coche.id).subscribe(detalleCoche => {
          //let objectUrl= 'data:image/jpeg;base64,' + detalleCoche;
          this.imagen = this.sanitizier.bypassSecurityTrustUrl('data:image/jpeg;base64,' + detalleCoche);
        });
      });
    });
  }

  onSubmit(form: NgForm) {
    
    
    
  }

}
