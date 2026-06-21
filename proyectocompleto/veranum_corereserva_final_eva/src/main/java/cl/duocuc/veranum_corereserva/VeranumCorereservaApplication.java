package cl.duocuc.veranum_corereserva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VeranumCorereservaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeranumCorereservaApplication.class, args);
	}

}
