# 🪵 Logback Console Logging – Colors & Timestamp (Spring Boot 3 + Java 21)

This guide includes short, direct explanations and configs for setting up **colored console logs** and correct **timestamp formatting** in Spring Boot 3 using Logback.

---

## 🔧 Sample Pattern for your logging-spring.xml

```xml
<pattern>
%d{yyyy-MM-dd HH:mm:ss.SSS} %highlight(%-5level) [%yellow(%thread)] %cyan(%logger{36}) - %green(%msg)%n
</pattern>
```

## 📆 Timestamp Formatting Cheatsheet (`%d{}`)

| Symbol | Purpose            | Description                      |
|--------|--------------------|----------------------------------|
| `yyyy` | Year               | Displays full year like `2025`   |
| `MM`   | Month              | Month in two digits (01–12)      |
| `mm`   | Minutes            | Minutes in two digits (00–59)    |
| `dd`   | Day of month       | Day in two digits (01–31)        |
| `HH`   | Hour (24-hour)     | Hours in 24-hour format (00–23)  |
| `hh`   | Hour (12-hour)     | Hours in 12-hour format (01–12)  |
| `ss`   | Seconds            | Seconds in two digits (00–59)    |
| `SSS`  | Milliseconds       | Milliseconds (e.g. 123)          |

✅ Use `MM` for **month**, not `mm`  
✅ Recommended pattern: `%d{yyyy-MM-dd HH:mm:ss.SSS}`

---

## 🎨 Console Color Tags in Logback

| Tag                 | Description                                 |
|---------------------|---------------------------------------------|
| `%highlight(...)`   | Auto-colors log level (INFO, ERROR, etc.)   |
| `%red(...)`         | Applies red color (e.g., for ERRORs)        |
| `%green(...)`       | Applies green color (great for messages)    |
| `%yellow(...)`      | Applies yellow color (e.g., threads)        |
| `%cyan(...)`        | Applies cyan color (e.g., for loggers)      |
| `%blue(...)`        | Applies blue color                          |
| `%magenta(...)`     | Applies magenta color                       |
| `%white(...)`       | Applies white color                         |

✅ Works only in **ConsoleAppender**  
❌ Do not use in **file logs** (files should remain plain text)

---

<img width="897" alt="image" src="https://github.com/user-attachments/assets/aaccd1eb-bf2b-4287-8a9f-2605a7e3b5a7" />


