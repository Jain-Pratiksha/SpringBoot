# 📂 Logback – Separate Log File for a Specific Package (Spring Boot 3 + Java 21)

This guide explains how to configure **Logback** to write logs from a specific package (e.g. `com.shows_watchlist.controllers`) into its own separate log file like:


---

## ✅ Use Case

You want logs from only your controller classes (e.g. `UserController`, `ShowWatchListController`) to go into a dedicated file for easier debugging and separation.

---

## 🧩 Step-by-Step Configuration

### 1. Add a dedicated appender in `logback-spring.xml`

This appender will handle log writing for the `controllers` package.

```xml
<appender name="CONTROLLER_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/controllers.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>
                logs/controllers-%d{yyyy-MM-dd}.log
            </fileNamePattern>
            <maxHistory>30</maxHistory>
            <totalSizeCap>1GB</totalSizeCap>
        </rollingPolicy>
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
                %d{yyyy-mm-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36}.%M:%L - %msg%n
            </pattern>
        </encoder>
    </appender>
```
### 2.  Add a logger for the target package
Add this below the appender configuration:

```xml
    <logger name="com.shows_watchlist.controllers" level="INFO" additivity="false">
        <!-- This logger is for the controllers package -->
        <!-- It will log INFO level messages and higher -->
        <appender-ref ref="CONTROLLER_FILE"/>
    </logger>
```

🧠 additivity="false" means logs from this package will only go to controllers.log.

✅ Use additivity="true" if you also want these logs to appear in your root log file.

Run the app and you'll find a controllers file created under logs/ folder

Wohooo, you are done.

<img width="885" alt="image" src="https://github.com/user-attachments/assets/c2d645b9-dda4-4039-b132-4945f69cf4ed" />



