package cl.duocuc.veranum_corereserva.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Se cambia el localhost estático por una variable de entorno
@FeignClient(name = "veranum-coreusuario", url="${usuario.api.url}")
public interface UsuarioClient {

    @GetMapping("/existe/{rut}")
    boolean verificarSiExisteRut(@PathVariable("rut") String rut);
}
