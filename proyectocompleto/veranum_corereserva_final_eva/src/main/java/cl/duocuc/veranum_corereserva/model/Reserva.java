package cl.duocuc.veranum_corereserva.model;


import jakarta.persistence.*;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
@Table(name= "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 12)
    private String rutUsuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @Column(nullable = false)
    private LocalDate fechaIngreso;

    @Column(nullable = false)
    private LocalDate fechaSalida;

    @Column(nullable = false)
    private Integer cantidadHuespedes;

    @Column(nullable = false)
    private Double costoTotal; 

    @Column(nullable = false)
    private String estado;
}