## 🔐 JWT Authentication Setup (Spring Boot 3 + Java 21)

**JWT (JSON Web Token)** for stateless authentication, secured via **Spring Security 6**.

✅ Fully compatible with **Spring Boot 3.x**, **Java 21**, and **MongoDB**

---

### 🔑 Key Features

- Secure login with email & password
- JWT generated and returned on successful login
- Token validated with every request
- Stateless authentication using `SecurityContextHolder`
- Custom error handling for expired or invalid tokens

---

### 🔧 Setup Guide

---

**1. Add Spring Security Dependency**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

**2. Add JWT Library (jjwt)**

```java
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```
**3. Create JwtUtil.java**

```java
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.security.*;
import java.util.*;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "your-secure-base64-generated-secret-key";
    private static final long EXPIRATION_TIME = 5 * 60 * 1000; // 5 minutes
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

//    public static void main(String[] args) {
//    For generating a secret key, uncomment the following code block
//        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//            keyGenerator.init(256); // 256 bits
//            SecretKey secretKey = keyGenerator.generateKey();
//            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//            System.out.println("Generated secret key: " + encodedKey);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("JWT token is invalid: {}", e.getMessage());
            return false;
        }
    }
}
```


**4. Add RequestFilter.java** (I have named it as RequestFilter, because all the request will land here by default)

```java
@Component
public class RequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String authToken = request.getHeader("Authorization");

            if (authToken != null && authToken.startsWith("Bearer ")) {
                String token = authToken.substring(7);
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractEmail(token);
                    UserDetails userDetails = new User(email, "", new ArrayList<>());

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    logger.warn("JWT token is invalid or expired");
                    throw new JwtException("Invalid or expired JWT token");
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            logger.error("JWT token processing failed: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized - Invalid or expired token\"}");
        }
        catch (Exception e) {
            logger.error("An error occurred while processing the JWT token: {}", e.getMessage());
        }

    }
```
**5. Configure SecurityConfig.java**

```java
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;

@Configuration
public class SecurityConfig {

    @Autowired
    private RequestFilter requestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity
                .authorizeHttpRequests(request -> request
                        // Allow public access to specific APIs
                        .requestMatchers("/auth/**")
                        .permitAll()
                        // Require authentication for all other endpoints
                        .anyRequest().authenticated())
                .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

**6. Add the User Controller**

```java
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserExecutor userExecutor;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public String signup(@RequestBody UserSignUpDTO userSignUpDTO) {
        // Logic to handle user signup
        // This should include saving the user to the database and returning a success message
        logger.info("Signing up user with email: {}", userSignUpDTO.getEmailId());
        userExecutor.signUpUser(userSignUpDTO);
        return "User signed up successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserSignUpDTO userLoginDTO) {
        // Logic to handle user login
        // This should include validating the user's credentials and returning a success message or token
        logger.info("Logging in user with email: {}", userLoginDTO.getEmailId());
        return userExecutor.loginUser(userLoginDTO);
    }
}
```

**7. Add User Executor and the desired logic in it**

```java
@Component
public class UserExecutor {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void signUpUser(UserSignUpDTO userSignUpDTO) {
        User user = new User();
        user.setEmailId(userSignUpDTO.getEmailId());
        user.setPassword(passwordEncoder.encode(userSignUpDTO.getPassword()));
        userService.signUpUser(user);
    }

    public ResponseEntity<?> loginUser(UserSignUpDTO userLoginDTO) {
        User user = userService.findUserByEmail(userLoginDTO.getEmailId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        if(passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.ok("{\"token\": \"" + jwtUtil.generateToken(user.getEmailId()) + "\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ShowWatchListResponseDTO("Invalid email or password"));
        }
    }

}
```

**8. Configure bean for PasswordEncoder**

```java
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;

@Configuration
public class AuthFilter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

**9. The User Service & Repository has your logic for saving and fetching data from db**

**10. Use Token in Requests with Postman**

Hit the login request from Postman and you are all good to use the token

After successful login, you'll get a token which needs to be included in the header of all protected requests:

Authorization: Bearer <your_jwt_token>

And that’s it — JWT authentication is now fully integrated into your Spring Boot 3 + MongoDB application!

<img width="635" alt="image" src="https://github.com/user-attachments/assets/0228783c-d6c0-4583-8c3e-6faca916f302" />


<img width="645" alt="image" src="https://github.com/user-attachments/assets/605608e7-e73a-44ed-8d2c-1ad384202771" />


<img width="648" alt="image" src="https://github.com/user-attachments/assets/56f0b773-7be2-43c4-abde-ae3a19d460a0" />

