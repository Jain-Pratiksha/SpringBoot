## 🪵 Basic Logging Setup in Spring Boot 3

Enable simple logging using SLF4J (included by default in Spring Boot).

---

### 1. Use SLF4J Logger in Your Classes

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowsService {

    private static final Logger logger = LoggerFactory.getLogger(ShowsService.class);

    public void performTask() {
        logger.info("Performing a task...");
        logger.debug("Task details: {}", "some debug info");
        logger.error("Something went wrong");
    }
}
```
### 2. Log Configuration in application.properties (application.yml)

Below formatting for application.properties:
```java
# Logging level
logging.level.root=INFO
# replace shows_watchlist with your application name
logging.level.com.shows_watchlist=DEBUG 

# Log file path
logging.file.name=logs/app.log

# (Optional) Spring Boot will rotate files using default Logback policy

```
Below formatting for application.yml:
```java
spring:
  application:
    name: shows_watchlist
#logging configuration
# replace shows_watchlist with your application name
logging:
  level:
    root: INFO
    com:
      shows_watchlist: DEBUG # Enable debug logging for the shows_watchlist package

  file:
    name: logs/app.log # Log file location
    max-size: 10MB # Maximum size of the log file
    max-history: 30 # Keep logs for 30 days
```

### 3. Restart & Check Output

Console will show logs based on level

File logs/app.log will be auto-created in the project root

Wohooo, we are done with simple and quick logging.

<img width="897" alt="image" src="https://github.com/user-attachments/assets/41a7bf58-26a6-4d23-a539-922e760a71cc" />
