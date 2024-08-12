package fantastic.faniverse;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FaniverseApplication {

	public static void main(String[] args) {
		//Dotenv dotenv = Dotenv.load();
		SpringApplication.run(FaniverseApplication.class, args);
	}
}
