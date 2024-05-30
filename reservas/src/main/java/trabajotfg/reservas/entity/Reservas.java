package trabajotfg.reservas.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString  @AllArgsConstructor @NoArgsConstructor
public class Reservas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idreserva;

    private int id_vehiculo;

    private int id_usuario;

    private Date fecha_inicio;

    private Date fecha_fin;

    private String estado;

    @OneToOne(mappedBy = "reserva" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Pagos pago;

}
