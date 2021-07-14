package ws.operateur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class OperateurApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperateurApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				CorsRegistration map = registry.addMapping("/**"); // all routes
				map.allowedHeaders("*"); // authorization ... etc
				map.allowedMethods("POST", "GET", "PUT", "DELETE", "HEAD");
			}
		};
	}
}
