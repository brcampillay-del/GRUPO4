package cl.duocuc.veranum_coreusuario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioResponse
{
    private Long id;
    private String rut;
    private String nombre;
    private String email;
    private String rol;
    private boolean bloqueado;
}
