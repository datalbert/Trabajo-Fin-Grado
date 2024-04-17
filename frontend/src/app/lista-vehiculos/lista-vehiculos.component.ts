import { NgFor } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-lista-vehiculos',
  standalone: true,
  imports: [NgFor],
  templateUrl: './lista-vehiculos.component.html',
  styleUrl: './lista-vehiculos.component.css'
})
export class ListaVehiculosComponent {

  coches: any[] = [1, 2, 3, 4];

}
