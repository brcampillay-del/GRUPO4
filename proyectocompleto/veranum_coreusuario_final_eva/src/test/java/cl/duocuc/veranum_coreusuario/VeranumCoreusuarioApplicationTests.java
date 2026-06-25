package cl.duocuc.veranum_coreusuario;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
class VeranumCoreusuarioApplicationTests {

	@Mock
	Private UsuarioRepository usuarioRepository;

	@Mock
	Private RolRepository rolRepository;

	@InjectMocks
	private UsuarioService usuarioService;

	@Test
	@DisplayName("Debe validar Rut inexistente sin levantar contexto Spring ni base de datos")
	void existePorRut_NoExiste_SinBD(){
		when(usuarioRepository.findByRut("19313837-3")).thenReturn(Optional.empty());

		boolean resultado = usuarioService.existePorRut("19313837-3");

		assertFalse(resultado);
		verify(usuarioRepository, times(1)).findByRut("19313837-3");
		verifyNoInteractions(rolRepository);

	}
}
