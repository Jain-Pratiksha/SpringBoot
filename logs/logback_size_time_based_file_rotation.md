## 🪵 Logging Setup Overview (Spring Boot 3 + Logback)

This project uses **Logback** (via SLF4J) as the default logging framework in Spring Boot.

---

### ✅ Current Logging Features

- 📄 Logs written to: `logs/shows_watchlist.log` (can be renamed as needed)
- 🔁 **Size + Time-based rotation**: A new file is created:
- Daily OR when current log file reaches **10MB**
- Example: `shows_watchlist-2025-07-09.0.log`, `shows_watchlist-2025-07-09.1.log`, etc.
- 🗂️ **Retention**: Keeps log files for up to **30 days**
- 💾 **Storage cap**: Deletes oldest logs if total size exceeds **1GB**
- 💻 **Console output**: Logs also appear in the terminal
- 🔠 **Formatted output**: 2025-07-09 12:10:45 [http-nio-8080-exec-1] INFO com.example.Service - Task started

## 🧾 Steps to Set Up File Logging Using `logback-spring.xml`

### 1. Create `logback-spring.xml`

Create the file at this location:

**src/main/resources/logback-spring.xml**

### 2. Paste the below code in logback-spring.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- Rolling File Appender -->
    <!-- This appender writes logs to a file and automatically rotates the file daily -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- Logging path-->
        <!-- Path and filename of the current log file -->
        <!-- A new file is created here; older ones are rotated using the policy below -->
        <file>logs/shows_watchlist.log</file>
        <!-- Rotation policy for log file (size-time-based rotation) -->
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- Pattern for rotated log file names -->
            <!-- %d{yyyy-MM-dd} means the date will be inserted into the filename, %i is in incremental for same day files -->
            <fileNamePattern>
                logs/shows_watchlist-%d{yyyy-MM-dd}.%i.log
            </fileNamePattern>
            <maxFileSize>10MB</maxFileSize> <!-- Each log file will be rotated when it reaches 10MB -->
            <maxHistory>30</maxHistory> <!-- Retain logs for 30 days -->
            <totalSizeCap>1GB</totalSizeCap> <!-- Optional: total disk space used by all log files should not exceed 1GB -->
        </rollingPolicy>
        <!-- Encoder defines how each log line will appear in the file -->
        <encoder>
            <!-- Format:
                %d = Date (full format)
                [%thread] = Thread name
                %-5level = Log level (right-aligned to 5 characters)
                %logger{36} = Class name (up to 36 characters)
                %msg = Log message
                %n = New line
            -->
            <pattern>
                %d{yyyy-mm-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
            </pattern>
        </encoder>
    </appender>

    <!-- Console Appender-->
    <!-- This appender writes logs to the terminal/console -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <!-- Logging pattern for console-->
            <pattern>
                %d{yyyy-mm-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
            </pattern>
        </encoder>
    </appender>

    <!-- This sets the default log level and attaches both appenders -->
    <!-- All logs with level INFO or higher will be handled by these -->
    <root level="INFO">
        <!-- Send log output to the console -->
        <appender-ref ref="CONSOLE"/>
        <!-- Send log output to the rolling file -->
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

### 3. Comment Logging Config in application.properties or application.yml (if any)
Spring Boot uses either the .properties/.yml or logback-spring.xml — not both.
```java
logging.file.name=logs/app.log
logging.level.root=INFO
```
👉 Comment it out or delete it to avoid conflicts.

### 4. Run the App
Logs will show in the console

Log files will be generated automatically under the logs/ folder.
Rotated files will follow this pattern:

logs/shows_watchlist-2025-07-09.0.log
logs/shows_watchlist-2025-07-09.1.log

Wohooo, we are done🎉!
You now have a complete logging solution where:

- Logs are stored in a file

- Rotated daily or when file size exceeds 10MB

- Older logs are retained for 30 days or until disk limit (1GB) is hit

- Console and file both receive logs in clean, readable format

<img width="888" alt="image" src="https://github.com/user-attachments/assets/ff5b0fe5-9388-41b6-ae04-1d703e1a6852" />
