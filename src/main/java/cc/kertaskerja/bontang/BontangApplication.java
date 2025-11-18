package cc.kertaskerja.bontang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BontangApplication {

	public static void main(String[] args) {
		SpringApplication.run(BontangApplication.class, args);
	}

}
