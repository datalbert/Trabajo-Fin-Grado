package trabajotfg.usuarios.entity;

import java.sql.Date;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.annotation.Generated;
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
@Table(name = "usuarios_rol")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class UsuariosRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario", referencedColumnName = "idUsuarios")
    private Usuarios usuario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idrol", referencedColumnName = "idRol")
    private Rol rol;

    @Column(name = "fecha_registro", insertable = true, updatable = false)
    @CreatedDate
    private LocalDateTime fecharegistro;

    @Column(name="fecha_conexion", insertable = false, updatable = true)
    @LastModifiedDate
    private LocalDateTime fechaconexion;

   

}
