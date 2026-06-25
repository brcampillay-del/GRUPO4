package cl.duoc.api_gateway;

import cl.duoc.api_gateway.controller.TestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiGatewayApplicationTests {

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {

		this.mockMvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
	}

	@Test
	@DisplayName("Debe verificar que el contexto básico de la aplicación carga")
	void contextLoads() {
	}

	@Test
	@DisplayName("Debe retornar el estado operativo del Gateway con un código 200 OK")
	void testGatewayStatus() throws Exception {
		mockMvc.perform(get("/api/gateway/status"))
				.andExpect(status().isOk())
				.andExpect(content().string("API Gateway Veranum operativo al 100%"));
	}
}