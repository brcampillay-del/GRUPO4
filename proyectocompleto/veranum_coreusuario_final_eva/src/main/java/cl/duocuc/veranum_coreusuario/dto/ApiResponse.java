package cl.duocuc.veranum_coreusuario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T>
{
    private String mensaje;
    private T data;
}
