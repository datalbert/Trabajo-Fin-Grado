package trabajotfg.inventario.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "`Coches`")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Coches {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcoche;

    private String marca;

    private String modelo;

    private String matricula;

    @Lob
    private byte[] fotos;

    private String combustible;

    private String transmision;

    @Column(name = "numasientos")
    private int numAsientos;

    private int idPropietario;

    @OneToOne(mappedBy = "coche", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Estado estado;



}
