package cl.duocuc.veranum_coreusuario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario
{
 @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(unique = true, nullable = false, length = 12)
 private String rut;

 @Column(nullable = false, length = 100)
 private String nombre;

 @Column(unique = true, nullable = false)
 private String email;

 @Column(nullable = false)
 private String password;


 @ManyToOne(fetch = FetchType.EAGER)
 @JoinColumn(name = "rol_id", nullable = false)
 private Rol rol;

 private boolean bloqueado = false;
 private int intentosFallidos = 0;
}
