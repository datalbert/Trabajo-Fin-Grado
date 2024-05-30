package trabajotfg.inventario.entity;


import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "`estado`")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcoche")
    private Coches coche;

    private String estado;

    @CreatedDate
    @Column(name="fecha_creacion",updatable = false, insertable = true)
    private LocalDateTime fecha_creacion;

    @LastModifiedDate
    @Column(name = "fecha_modificacion",insertable  = false, updatable = true)
    private LocalDateTime fecha_modificacion;



}
