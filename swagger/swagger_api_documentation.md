## 📘 Swagger Integration (Springdoc OpenAPI for Spring Boot 3 & Java 21)

This project uses **Springdoc OpenAPI 2.x** to generate interactive Swagger UI for testing and documenting REST APIs.

✅ Fully compatible with **Spring Boot 3.x** and **Java 21**.

---

### 🚀 One-Step Swagger Setup Guide

1. **Add Dependency**  
   Add this to your `pom.xml`:

   ```xml
   <dependency>
       <groupId>org.springdoc</groupId>
       <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
       <version>2.0.2</version>
   </dependency>

2. Permit (Allow) Swagger URLs in SecurityConfig

Update your SecurityConfig.java to allow public access to Swagger endpoints (this is only needed if you have authorization setup for urls):

```java
@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity
                .authorizeHttpRequests(request -> request
                        // Allow public access to specific APIs
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**")
                        .permitAll()
                        // Require authentication for all other endpoints
                        .anyRequest().authenticated())
                .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
```

3. Add OpenAPI Info (Optional but Recommended)

Add this to your @SpringBootApplication class or a separate config class:
```java
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.*;

@SpringBootApplication
@EnableWebSecurity
@OpenAPIDefinition(
	info = @Info(
			title = "Show Watchlist APIs",
			version = "1.0",
			description = "API for managing a shows watchlist",
			contact = @Contact(
					name = "Show Watchlist Team : Pratiksha"
			)
	)
)
public class ShowWatchlistApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShowWatchlistApplication.class, args);
	}
}
```

4. Run and Access Swagger UI

Start your Spring Boot app and open:

http://{base-url}

Swagger UI: http://localhost:8080/swagger-ui.html

OR: http://localhost:8080/swagger-ui/index.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

Wohoo, you are all done in just 4 simple steps.
