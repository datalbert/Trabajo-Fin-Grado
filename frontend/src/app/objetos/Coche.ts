
export class Coche {

    constructor(
        public marca: string, 
        public modelo: string, 
        public matricula: string,
        public combustible:string,
        public transmision:string,
        public numasientos:string) {}

    public fotos: File | undefined;

    

    public setFotos(fotos: File) {
        this.fotos = fotos;
    }

    public getFotos(): File | undefined {
        return this.fotos;

    }

    
    

    }