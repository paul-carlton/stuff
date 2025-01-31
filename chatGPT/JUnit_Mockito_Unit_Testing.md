**Paul Carlton**: Java unit testing with junit  and mockup

**ChatGPT**: Unit testing in Java using **JUnit** and **Mockito** is a common practice for ensuring code quality. Here’s a quick guide:

---

## **1. JUnit Basics**
JUnit is a widely used testing framework for Java.

### **Setup (JUnit 5)**
Add the following dependencies to your `pom.xml` (Maven):

```xml
<dependencies>
    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.8.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.8.1</version>
    </dependency>
</dependencies>
```

For **Gradle**:

```groovy
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.8.1'
}
```

### **Basic JUnit Test**
```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void testAddition() {
        Calculator calculator = new Calculator();
        int result = calculator.add(2, 3);
        assertEquals(5, result, "2 + 3 should equal 5");
    }
}
```

---

## **2. Mockito for Mocking Dependencies**
Mockito is used to mock dependencies in unit tests.

### **Setup Mockito**
Add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>4.0.0</version>
    <scope>test</scope>
</dependency>
```

For **Gradle**:

```groovy
dependencies {
    testImplementation 'org.mockito:mockito-core:4.0.0'
}
```

### **Example: Mocking a Service**
Imagine we have a `UserService` that depends on a `UserRepository`:

#### **UserRepository (Dependency)**
```java
public interface UserRepository {
    User findByUsername(String username);
}
```

#### **UserService (Class to Test)**
```java
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserValid(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }
}
```

#### **Unit Test with Mockito**
```java
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void testUserValidation() {
        // Mock the repository
        UserRepository mockRepo = mock(UserRepository.class);
        
        // Define behavior
        when(mockRepo.findByUsername("testUser")).thenReturn(new User("testUser"));

        // Inject mock into service
        UserService userService = new UserService(mockRepo);

        // Test the service method
        assertTrue(userService.isUserValid("testUser"));
        assertFalse(userService.isUserValid("unknownUser"));

        // Verify interactions
        verify(mockRepo).findByUsername("testUser");
        verify(mockRepo).findByUsername("unknownUser");
    }
}
```

---

## **3. Using @Mock and @InjectMocks (Mockito Annotations)**
Instead of manually creating mocks, we can use annotations:

### **Updated Test Class**
```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserValidation() {
        when(userRepository.findByUsername("testUser")).thenReturn(new User("testUser"));

        assertTrue(userService.isUserValid("testUser"));
        assertFalse(userService.isUserValid("unknownUser"));

        verify(userRepository).findByUsername("testUser");
        verify(userRepository).findByUsername("unknownUser");
    }
}
```

---

## **4. Advanced Mockito Features**
### **1. Argument Captors**
Capture method arguments passed to mocked methods.
```java
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
verify(userRepository).findByUsername(captor.capture());
assertEquals("testUser", captor.getValue());
```

### **2. Exception Handling**
Mock exceptions for testing error handling.
```java
when(userRepository.findByUsername(anyString())).thenThrow(new RuntimeException("Database error"));
```

### **3. Verify Method Calls**
Ensure methods are called specific times.
```java
verify(userRepository, times(1)).findByUsername("testUser");
verify(userRepository, never()).findByUsername("unknownUser");
```

---

## **Conclusion**
- **JUnit** is used for writing test cases.
- **Mockito** is used to mock dependencies and isolate test logic.
- Use **@Mock and @InjectMocks** to simplify mock injection.
- Use **verify() and when()** to define and check mock behaviors.

Would you like more details on a specific topic, such as **Spring Boot testing** or **parameterized tests**?

