package trabajotfg.pago.entity;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
@Table(name = "transacciones")
public class Transacciones {

    @Id
    private String id_transaccion;

    private String id_pagador;

    @Column(name="id_reserva")
    private int idReserva;

    private String email;
    
    @CreatedDate
    @Column(name="fecha",updatable = false, insertable = true)
    private LocalDateTime fecha;

    //cantidad de dinero que se va a pagar
    private Double monto;

    private String moneda;

    private String estado;

    private String descripcion_metodo_pago;

    

}
