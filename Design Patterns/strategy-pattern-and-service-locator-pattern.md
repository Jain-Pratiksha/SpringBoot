# 🚀 Strategy Pattern with Service Locator Pattern

This demonstrates **Strategy Pattern** and **Service Locator Pattern**using **one real-world example: Notification Service**.

The goal is to understand:
- 🤔 What each pattern is
- 🎯 Why we use them
- 🛠️ How they are useful in Spring Boot applications

---

## 💳 Example: Notification Service

Supported channels:
- 📧 Email
- 📱 SMS
- 💬 WhatsApp

### 🎯 Problem Statement

A backend system must:
- Send notifications
- Support multiple channels
- Select channel dynamically at runtime
- Avoid large `if-else` blocks
---

## 🧠 Strategy Pattern — Choosing *HOW* to Pay

Strategy Pattern lets you define multiple ways to do one task and switch between them at runtime. It allows you to switch business logic at runtime without changing the main code.

### 🧩 Real-life Analogy
📌 **In this example**  
Strategy decides **HOW** a notification is sent either via SMS, WhatsApp or Email.

---

### ❓ What is Strategy Pattern?
Strategy Pattern allows you to:
- Define multiple algorithms (strategies/ways)
- Encapsulate them behind a common interface
- Switch behavior at runtime (way of doing a task)

---

### ✅ Why Use Strategy Pattern?
- Avoids large `if-else` / `switch` blocks
- Follows **Open–Closed Principle**
- Easy to add new messaging methods

---

## 🧠 SERVICE LOCATOR PATTERN
### 🧩 What is Service Locator Pattern?

👉 Service Locator Pattern provides a central place to locate services.
📌 In this example, Service Locator decides WHICH object (sms, whatsapp, email) to use.

| 🧩 Pattern              | 🎯 Responsibility        |
| ----------------------- | ------------------------ |
| Strategy Pattern        | HOW logic is executed    |
| Service Locator Pattern | WHICH object is selected |


### ☕ Spring Boot Code — Strategy Pattern

#### 🔹 Strategy Interface
```java
public interface NotificationStrategy {
    void send(String message);
}
```
```
@Service("emailNotificationService")
public class EmailNotificationStrategy implements NotificationStrategy {

    @Override
    public void send(String message) {
        System.out.println("Sending EMAIL: " + message);
    }
}
```

```
@Service("smsNotificationService")
public class SmsNotificationStrategy implements NotificationStrategy {

    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
```

```
@Service("whatsappNotificationService")
public class WhatsappNotificationStrategy implements NotificationStrategy {

    @Override
    public void send(String message) {
        System.out.println("Sending WHATSAPP: " + message);
    }
}

```

**Service Locator - Spring ApplicationContext**
📌 Responsibility: find the correct strategy dynamically
```
@Component
public class NotificationServiceLocator {

    @Autowired
    private ApplicationContext applicationContext;

    public NotificationStrategy getStrategy(String beanName) {
        return applicationContext.getBean(beanName, NotificationStrategy.class);
    }
}
```
Runtime selection (the “Strategy” part)

```
@Service
public class NotificationProcessor {

    @Autowired
    private NotificationServiceLocator serviceLocator;

    public void send(String channel, String message) {

        String beanName = channel + "NotificationService";

        NotificationStrategy strategy = serviceLocator.getStrategy(beanName);

        strategy.send(message);
    }
}
```

The API
```
@RestController
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    private NotificationProcessor notificationProcessor;

    @PostMapping
    public String sendNotification(
            @RequestParam String channel,
            @RequestParam String message) {

        notificationProcessor.send(channel, message);
        return "Notification sent via " + channel;
    }
}
```
✔ Same interface
✔ Different behavior
✔ Bean names are important

## Example:
POST /notify?channel=email&message=Keep Learning and Growing

emailNotificationService -> EmailNotificationStrategy

Output: **Sending EMAIL: Keep Learning and Growing**

### 🔥One-liner
> **Strategy chooses the algorithm.  
> Service Locator finds the implementation.**
