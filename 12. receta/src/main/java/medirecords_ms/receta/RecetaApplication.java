package medirecords_ms.receta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class RecetaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecetaApplication.class, args);
	}

}
