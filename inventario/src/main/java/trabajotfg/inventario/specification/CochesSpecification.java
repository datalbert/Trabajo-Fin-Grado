package trabajotfg.inventario.specification;

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server.Spec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import trabajotfg.inventario.entity.Coches;

@Component
public class CochesSpecification {

    public  static Specification<Coches> withMarca(String marca){
        return (root, query, builder) -> builder.like(root.get("marca"), "%" + marca + "%");
    }

    public static Specification<Coches> withModelo(String modelo){
        return (root, query, builder) -> builder.like(root.get("modelo"), "%" + modelo + "%");
    }

    public static Specification<Coches> withCombustible(String combustible){
        return (root, query, builder) -> builder.like(root.get("combustible"), "%" + combustible + "%");
    }

    public  static Specification<Coches> withTransmision(String transmision){
        return (root, query, builder) -> builder.like(root.get("transmision"), "%" + transmision + "%");

    }

    public static Specification<Coches> withNumAsientos(int numAsientos){
        return (root, query, builder) -> builder.equal(root.get("numAsientos"), numAsientos);
    }

    public static Specification<Coches> withEstado(String estado) {
        return (root, query, builder) -> {
            // Join with Estado and apply the condition on estado field
            return builder.equal(root.join("estado").get("estado"), estado);
        };
    }


    public static Specification<Coches> conditionalSearch(String marca, String modelo, String combustible, String transmision,
     Integer numAsientos, String estado){
        //comprobamos que los campos no esten vacios

        Specification<Coches> specification = null;

        if(marca != null && !marca.isEmpty()){
            specification = withMarca(marca);
        }

        if(modelo != null && !modelo.isEmpty()){
            if(specification != null){
                specification = specification.and(withModelo(modelo));
            }else{
                specification = withModelo(modelo);
            }
        }

        if(combustible != null && !combustible.isEmpty()){
            if(specification != null){
                specification = specification.and(withCombustible(combustible));
            }else{
                specification = withCombustible(combustible);
            }
        }

        if(transmision != null && !transmision.isEmpty()){
            if(specification != null){
                specification = specification.and(withTransmision(transmision));
            }else{
                specification = withTransmision(transmision);
            }
        }

        if(numAsientos != null){
            if(specification != null){
                specification = specification.and(withNumAsientos(numAsientos));
            }else{
                specification = withNumAsientos(numAsientos);
            }
        }

        if (estado != null && !estado.isEmpty()) {
            if (specification != null) {
                specification = specification.and(withEstado(estado));
            } else {
                specification = withEstado(estado);
            }
        } 


        return specification;


    }

    

}
