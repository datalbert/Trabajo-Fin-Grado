import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MessageService } from './message.service';
import { Coche } from '../objetos/Coche';
import { Observable, catchError, of, tap } from 'rxjs';
import { CocheHTML } from '../objetos/CocheHTML';

@Injectable({
  providedIn: 'root'
})
export class InventarioService {

  constructor(private http: HttpClient, private messageService: MessageService) { }

  private inventarioUrl = 'http://localhost:8081/inventario';

  imageoptions = new HttpHeaders().set('responseType', 'blob');





  createInventario(nuevocoche: Coche,imagen:File) :Observable<any> {
    
    const urlcrearcoche= `${this.inventarioUrl}/nuevoCoche`;
    
    const formData = new FormData();
    formData.append('nuevocoche', new Blob([JSON.stringify(nuevocoche)], { type: 'application/json' }));

    //const file = new File([imagen], "filename", { type: 'image/jpeg' });
    formData.append('fotos', imagen);

    return this.http.post<any>(urlcrearcoche,formData).pipe( 
      tap((respuesta: string) => this.log(`Coche creado`)),
      catchError(this.handleError<Coche>('createInventario'))
    );
  }

  obtenerTodosLosCoches(): Observable<CocheHTML[]> {

    const url = `${this.inventarioUrl}/obtenerTodos`;

    return this.http.get<CocheHTML[]>(url).pipe(
      tap(_ => this.log('Coches obtenidos')),
      catchError(this.handleError<CocheHTML[]>('obtenerTodosLosCoches', []))
    );

  }

  obtenerImagenCoche(id: number): Observable<any> {

    const url = `${this.inventarioUrl}/obtenerImagen?id=${id}`;

    return this.http.get(url, { responseType: 'blob' }).pipe(
      tap(_ => this.log('Imagen obtenida')),
      catchError(this.handleError<any>('obtenerImagenCoche'))
    );

  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
  
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead
  
      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);
  
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
  

  /** Log a HeroService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`UsuariosService: ${message}`);
  }

}
