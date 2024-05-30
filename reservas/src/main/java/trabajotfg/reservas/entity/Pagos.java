package trabajotfg.reservas.entity;

import java.sql.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString  @AllArgsConstructor @NoArgsConstructor
public class Pagos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPagos;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva")
    private Reservas reserva;

    private int cantidad;

    private Date fecha;

    private String estado;

    @Column(name = "metodo_pago")
    private String metodoPago;

}
