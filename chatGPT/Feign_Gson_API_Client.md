**Paul Carlton**: Feign.builder().decoder(new GsonDecoder()).target example

**ChatGPT**: Here's an example of how to use **Feign** with `Feign.builder()`, `GsonDecoder()`, and `target()` to make a REST API client in Java:

### **Example: Feign Client with GsonDecoder**
```java
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.Param;
import feign.RequestLine;
import java.util.List;

// Define an API interface
interface GitHubApi {
    @RequestLine("GET /users/{user}/repos")
    List<Repository> getRepositories(@Param("user") String user);
}

// Define a model class
class Repository {
    String name;
    String description;

    @Override
    public String toString() {
        return "Repository{name='" + name + "', description='" + description + "'}";
    }
}

public class FeignExample {
    public static void main(String[] args) {
        // Create a Feign client
        GitHubApi github = Feign.builder()
                .decoder(new GsonDecoder())  // Use Gson for JSON deserialization
                .encoder(new GsonEncoder())  // (Optional) Use Gson for JSON serialization
                .target(GitHubApi.class, "https://api.github.com");  // Target base URL

        // Call the API
        List<Repository> repositories = github.getRepositories("octocat");

        // Print results
        repositories.forEach(System.out::println);
    }
}
```

### **Explanation:**
1. **Define an Interface (`GitHubApi`)**  
   - Uses Feign's `@RequestLine` to define an API method.
   - Uses `@Param` to inject path variables.
   
2. **Create a Model Class (`Repository`)**  
   - Represents a repository object returned by GitHub API.

3. **Build the Feign Client**  
   - Uses `Feign.builder()`.
   - Uses `GsonDecoder()` for JSON responses.
   - Uses `target()` to specify the base URL.

4. **Invoke the API & Print Response**  
   - Calls `getRepositories("octocat")`.
   - Prints repository names and descriptions.

### **Output (Example)**
```
Repository{name='Hello-World', description='My first repository on GitHub!'}
Repository{name='Spoon-Knife', description='This repo is for demonstration purposes only.'}
...
```
Would you like me to add error handling or logging? 🚀

**Paul Carlton**: not annotated with HTTP method type (ex. GET, POST)
Warnings:
- Class GitHubApiGateway has annotations [FeignClient, Component] that are not used by contract Default
- Method getChangedFilesInPR has an annotation GetMapping that is not used by contract Default
Cause: null

**ChatGPT**: The error messages suggest a mix-up between **Spring Cloud OpenFeign** and **vanilla Feign**. If you're using **Spring Cloud Feign**, you should use `@FeignClient` and Spring-style annotations like `@GetMapping`. But if you're using **pure Feign**, you should use `@RequestLine`.

---

### **Solution 1: Using Spring Cloud OpenFeign (Recommended if using Spring Boot)**
If you're using Spring Boot with OpenFeign, you should **not** use `Feign.builder()`. Instead:

#### ✅ **Correct Way (Spring Feign)**
```java
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "githubClient", url = "https://api.github.com")
public interface GitHubApiGateway {
    
    @GetMapping("/users/{user}/repos")
    List<Repository> getChangedFilesInPR(@PathVariable("user") String user);
}
```
✔ No need for `Feign.builder()`  
✔ Uses `@FeignClient` and Spring MVC annotations  

🔹 **Enable Feign in Spring Boot**:  
Make sure you have `@EnableFeignClients` in your main application class.
```java
@SpringBootApplication
@EnableFeignClients
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}
```

---

### **Solution 2: Using Raw Feign (Standalone Feign, without Spring)**
If you're **not using Spring Boot**, use `@RequestLine` from Feign:

#### ✅ **Correct Way (Standalone Feign)**
```java
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import java.util.List;

interface GitHubApi {
    @RequestLine("GET /users/{user}/repos")
    List<Repository> getChangedFilesInPR(@Param("user") String user);
}

public class FeignExample {
    public static void main(String[] args) {
        GitHubApi github = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GitHubApi.class, "https://api.github.com");

        List<Repository> repos = github.getChangedFilesInPR("octocat");
        repos.forEach(System.out::println);
    }
}
```
✔ Uses `@RequestLine("GET ...")`  
✔ Uses `@Param("{user}")` for path variables  
✔ Works **without** Spring Boot  

---

### **Key Takeaways**
| If You're Using | Use This |
|---------------|-------------|
| **Spring Boot** (`spring-cloud-starter-openfeign`) | `@FeignClient`, `@GetMapping`, `@PathVariable` |
| **Standalone Feign** (`feign-core`, `feign-gson`) | `@RequestLine`, `@Param`, `Feign.builder()` |

Let me know if you're using Spring Boot or not, and I can help tailor the solution further! 🚀

**Paul Carlton**: Raw feign pass body

**ChatGPT**: If you are using **raw Feign** (not Spring Cloud Feign) and need to **pass a body** in a POST request, you should use `@RequestLine("POST ...")` and `@Body`.  

---

### **Example: Feign POST Request with JSON Body**
Let's say you're sending a `POST` request to `https://api.example.com/users` with a JSON body.

#### **Step 1: Define the API Interface**
```java
import feign.Feign;
import feign.RequestLine;
import feign.Body;
import feign.Param;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

class User {
    String name;
    String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', email='" + email + "'}";
    }
}

interface UserApi {
    @RequestLine("POST /users")
    @Body("%7B\"name\": \"{name}\", \"email\": \"{email}\"%7D")  // Manually formatted JSON with placeholders
    User createUser(@Param("name") String name, @Param("email") String email);
}
```

---

#### **Step 2: Build the Feign Client and Call the API**
```java
public class FeignPostExample {
    public static void main(String[] args) {
        UserApi userApi = Feign.builder()
                .encoder(new GsonEncoder())  // Encode JSON body
                .decoder(new GsonDecoder())  // Decode JSON response
                .target(UserApi.class, "https://api.example.com");

        // Create a new user
        User newUser = userApi.createUser("John Doe", "john@example.com");
        System.out.println(newUser);
    }
}
```

---

### **Alternative: Using a POJO Instead of `@Body`**
If you prefer passing an object directly instead of string interpolation, change the API method to:

```java
interface UserApi {
    @RequestLine("POST /users")
    User createUser(User user);
}
```
And then call it like this:

```java
User newUser = userApi.createUser(new User("John Doe", "john@example.com"));
```

This method is cleaner and avoids manually formatting JSON.

---

### **Key Points**
✅ Use `@RequestLine("POST ...")` to define a `POST` request.  
✅ Use `@Body` with placeholders or pass a Java object (if using `GsonEncoder`).  
✅ Use `GsonEncoder()` for encoding and `GsonDecoder()` for decoding.  

Would you like an example with headers or authentication? 🚀

**Paul Carlton**: raw feign pass header values

**ChatGPT**: In **raw Feign** (standalone Feign, without Spring Boot), you can pass header values using the `@Headers` annotation or dynamically using `RequestInterceptor`.  

---

## **1️⃣ Passing Static Headers Using `@Headers`**
If your API requires a static header (e.g., an API key), you can define it like this:

```java
import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

class User {
    String name;
    String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

interface UserApi {
    @RequestLine("POST /users")
    @Headers({
        "Content-Type: application/json",
        "Authorization: Bearer YOUR_API_KEY"  // Static header
    })
    User createUser(User user);
}

public class FeignHeaderExample {
    public static void main(String[] args) {
        UserApi userApi = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(UserApi.class, "https://api.example.com");

        User newUser = userApi.createUser(new User("John Doe", "john@example.com"));
        System.out.println(newUser);
    }
}
```
✅ **Use `@Headers` for static headers**  
✅ Works well for simple cases  

---

## **2️⃣ Passing Dynamic Headers Using Method Parameters**
If the header value changes dynamically, you can use `@Param`:

```java
interface UserApi {
    @RequestLine("POST /users")
    @Headers({
        "Content-Type: application/json",
        "Authorization: Bearer {token}"
    })
    User createUser(@Param("token") String token, User user);
}
```
And then call it like this:
```java
User newUser = userApi.createUser("my-dynamic-token-123", new User("John Doe", "john@example.com"));
```
✅ **Use `@Param` for dynamic headers**  
✅ Useful for tokens, session IDs, etc.  

---

## **3️⃣ Passing Headers Dynamically with `RequestInterceptor`**
For more flexibility (e.g., adding headers globally), use a `RequestInterceptor`:

```java
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class AuthInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer DYNAMIC_TOKEN");
        template.header("Custom-Header", "MyValue");
    }
}
```

Then, apply it to your Feign client:
```java
UserApi userApi = Feign.builder()
        .encoder(new GsonEncoder())
        .decoder(new GsonDecoder())
        .requestInterceptor(new AuthInterceptor())  // Add interceptor
        .target(UserApi.class, "https://api.example.com");
```
✅ **Best for global headers (e.g., authentication, logging, tracing)**  
✅ Cleaner than manually passing headers in every method  

---

### **🔹 Summary of Approaches**
| Use Case | Approach |
|----------|----------|
| **Static headers** (e.g., API keys) | `@Headers({"Authorization: Bearer XYZ"})` |
| **Dynamic headers per method call** | `@Param("token") String token` in method |
| **Global headers for all requests** | `RequestInterceptor` (best for auth, tracing) |

Do you need an example with OAuth, retry logic, or error handling? 🚀

**Paul Carlton**: raw feign pass body pojo and path params

**ChatGPT**: In **raw Feign**, you can pass both **a POJO as a request body** and **path parameters** using `@RequestLine`, `@Param`, and a **GsonEncoder** for JSON serialization.  

---

## **Example: Feign `POST` with Path Params & JSON Body**
Imagine you're calling this API:  
```http
POST https://api.example.com/users/{userId}/orders
Content-Type: application/json

{
  "productId": 123,
  "quantity": 2
}
```

---

### **1️⃣ Define POJO Classes**
Create a class for the request body:
```java
class OrderRequest {
    int productId;
    int quantity;

    public OrderRequest(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
```
And a class for the response:
```java
class OrderResponse {
    int orderId;
    String status;

    @Override
    public String toString() {
        return "OrderResponse{orderId=" + orderId + ", status='" + status + "'}";
    }
}
```

---

### **2️⃣ Define Feign Interface**
Use **`@RequestLine`** for the `POST` request, **`@Param`** for path parameters, and pass the POJO directly as a method argument:
```java
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

interface OrderApi {
    @RequestLine("POST /users/{userId}/orders")
    OrderResponse createOrder(@Param("userId") int userId, OrderRequest order);
}
```

---

### **3️⃣ Build Feign Client and Call API**
```java
public class FeignExample {
    public static void main(String[] args) {
        // Build the Feign client
        OrderApi orderApi = Feign.builder()
                .encoder(new GsonEncoder())  // Converts Java objects to JSON
                .decoder(new GsonDecoder())  // Converts JSON response to Java objects
                .target(OrderApi.class, "https://api.example.com");

        // Create an order request
        OrderRequest orderRequest = new OrderRequest(123, 2);

        // Call the API with a path param (userId = 42)
        OrderResponse response = orderApi.createOrder(42, orderRequest);

        // Print response
        System.out.println(response);
    }
}
```

---

## **💡 Key Takeaways**
✅ **Pass Path Params** using `@Param("userId")`  
✅ **Pass JSON Body** as a method argument (uses `GsonEncoder()`)  
✅ **Auto-serialize/deserialize** using Gson  

Would you like an example with **custom headers**, **error handling**, or **query parameters**? 🚀

