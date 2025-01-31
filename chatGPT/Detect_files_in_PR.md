**Paul Carlton**: Java program to detect files changed in a GitHub pull request 

**ChatGPT**: To detect files changed in a GitHub pull request using Java, you can use the GitHub API. Here’s a step-by-step guide:

### Steps:
1. Use GitHub's REST API to fetch the list of changed files.
2. Authenticate using a personal access token (PAT).
3. Parse the response and extract file names.

---

### **Java Code (Using `HttpClient`)**
This program fetches the list of changed files in a pull request.

#### **Requirements:**
- Java 11+ (for `HttpClient`)
- A GitHub personal access token (PAT)

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class GitHubPRFiles {
    private static final String GITHUB_TOKEN = "your_personal_access_token";
    private static final String REPO_OWNER = "your_github_username";
    private static final String REPO_NAME = "your_repository";
    private static final int PR_NUMBER = 1;  // Change to your pull request number

    public static void main(String[] args) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = String.format("https://api.github.com/repos/%s/%s/pulls/%d/files",
                    REPO_OWNER, REPO_NAME, PR_NUMBER);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + GITHUB_TOKEN)
                    .header("Accept", "application/vnd.github.v3+json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                parseResponse(response.body());
            } else {
                System.out.println("Error: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseResponse(String responseBody) {
        JSONArray files = new JSONArray(responseBody);
        System.out.println("Files changed in the PR:");
        for (int i = 0; i < files.length(); i++) {
            JSONObject file = files.getJSONObject(i);
            System.out.println("- " + file.getString("filename"));
        }
    }
}
```

---

### **How It Works**
- Uses `HttpClient` to send a GET request to the GitHub API.
- Passes the authorization token in the request header.
- Parses the JSON response to extract file names.

---

### **Example Output**
```
Files changed in the PR:
- src/Main.java
- src/Utils.java
```

---

### **Additional Enhancements**
- Use **OAuth** or **GitHub App authentication** for better security.
- Implement pagination (default API limit is 30 files per request).
- Filter files based on extension (`.java`, `.xml`, etc.).

Let me know if you need further improvements!

**Paul Carlton**: Java program to detect files changed in a GitHub pull request and also a merge to main branch using a main and a separate class and generating unit tests

**ChatGPT**: Here's a well-structured Java program that detects:  
1. **Files changed in a GitHub Pull Request**  
2. **Files changed in a merge to the main branch**  

The program consists of:  
- **`GitHubChangeDetector`**: A separate class that handles API calls.  
- **`Main`**: The entry point to invoke methods.  
- **JUnit tests** for unit testing.

---

### **Project Structure**
```
/GitHubChangeDetector
│── /src
│   ├── /main
│   │   ├── GitHubChangeDetector.java
│   │   ├── Main.java
│   ├── /test
│   │   ├── GitHubChangeDetectorTest.java
│── pom.xml  (if using Maven)
```

---

### **1. `GitHubChangeDetector.java` (Core Logic)**
This class handles API requests to detect changed files.

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class GitHubChangeDetector {
    private static final String GITHUB_TOKEN = "your_personal_access_token";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String API_BASE_URL = "https://api.github.com/repos/";

    private String repoOwner;
    private String repoName;

    public GitHubChangeDetector(String repoOwner, String repoName) {
        this.repoOwner = repoOwner;
        this.repoName = repoName;
    }

    public List<String> getChangedFilesInPR(int prNumber) throws Exception {
        String url = API_BASE_URL + repoOwner + "/" + repoName + "/pulls/" + prNumber + "/files";
        return fetchChangedFiles(url);
    }

    public List<String> getChangedFilesInMerge(String branch) throws Exception {
        String url = API_BASE_URL + repoOwner + "/" + repoName + "/commits/" + branch;
        return fetchChangedFiles(url);
    }

    private List<String> fetchChangedFiles(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + GITHUB_TOKEN)
                .header("Accept", "application/vnd.github.v3+json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("GitHub API Error: " + response.statusCode() + " - " + response.body());
        }

        return parseResponse(response.body());
    }

    private List<String> parseResponse(String responseBody) {
        List<String> changedFiles = new ArrayList<>();
        JSONArray files = new JSONArray(responseBody);

        for (int i = 0; i < files.length(); i++) {
            JSONObject file = files.getJSONObject(i);
            changedFiles.add(file.getString("filename"));
        }
        return changedFiles;
    }
}
```

---

### **2. `Main.java` (Entry Point)**
This class calls the methods in `GitHubChangeDetector`.

```java
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String repoOwner = "your_github_username";
        String repoName = "your_repository";
        int prNumber = 1;  // Example PR number
        String branch = "main"; // Example branch

        GitHubChangeDetector detector = new

**Paul Carlton**: Java client to detect files changed in a GitHub pull request and also a merge to main branch using a main and a separate class and generating unit tests using feign client

**ChatGPT**: To create a **Java client** that detects:  
1. **Files changed in a GitHub Pull Request**  
2. **Files changed in a merge to the `main` branch**  

Using **Feign Client**, we will:  
- Use **Spring Boot + OpenFeign** for API interaction.  
- Implement a **service layer (`GitHubService`)** to handle requests.  
- Create a **unit test (`GitHubServiceTest`)** using **Mockito**.

---

## **Project Structure**
```
/GitHubChangeDetector
│── /src
│   ├── /main/java/com/example/github
│   │   ├── GitHubFeignClient.java   # Feign Client Interface
│   │   ├── GitHubService.java       # Service Layer
│   │   ├── Main.java                # Main Application
│   ├── /test/java/com/example/github
│   │   ├── GitHubServiceTest.java   # Unit Test (JUnit & Mockito)
│── pom.xml  (Maven Dependencies)
```

---

## **1. Add Dependencies (Feign, Spring Boot, JSON Processing)**  
Add the following to `pom.xml`:

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- OpenFeign for GitHub API -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <!-- JSON Processing -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>

    <!-- JUnit & Mockito -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## **2. `GitHubFeignClient.java` (Feign Client Interface)**  
This interface makes requests to the GitHub API.

```java
package com.example.github;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;

@FeignClient(name = "github-client", url = "https://api.github.com")
public interface GitHubFeignClient {

    @GetMapping("/repos/{owner}/{repo}/pulls/{prNumber}/files")
    List<Map<String, Object>> getChangedFilesInPR(
            @RequestHeader("Authorization") String token,
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("prNumber") int prNumber
    );

    @GetMapping("/repos/{owner}/{repo}/commits/{branch}")
    Map<String, Object> getChangedFilesInMerge(
            @RequestHeader("Authorization") String token,
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("branch") String branch
    );
}
```

---

## **3. `GitHubService.java` (Service Layer)**
Handles API calls and processes responses.

```java
package com.example.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GitHubService {
    private static final String GITHUB_TOKEN = "Bearer your_personal_access_token";

    @Autowired
    private GitHubFeignClient gitHubFeignClient;

    public List<String> getChangedFilesInPR(String owner, String repo, int prNumber) {
        List<Map<String, Object>> response = gitHubFeignClient.getChangedFilesInPR(GITHUB_TOKEN, owner, repo, prNumber);
        List<String> changedFiles = new ArrayList<>();

        for (Map<String, Object> file : response) {
            changedFiles.add((String) file.get("filename"));
        }

        return changedFiles;
    }

    public List<String> getChangedFilesInMerge(String owner, String repo, String branch) {
        Map<String, Object> response = gitHubFeignClient.getChangedFilesInMerge(GITHUB_TOKEN, owner, repo, branch);
        List<String> changedFiles = new ArrayList<>();

        if (response.containsKey("files")) {
            List<Map<String, Object>> files = (List<Map<String, Object>>) response.get("files");
            for (Map<String, Object> file : files) {
                changedFiles.add((String) file.get("filename"));
            }
        }

        return changedFiles;
    }
}
```

---

## **4. `Main.java` (Entry Point)**
This calls `GitHubService` and prints changed files.

```java
package com.example.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner {
    @Autowired
    private GitHubService gitHubService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        String owner = "your_github_username";
        String repo = "your_repository";
        int prNumber = 1;
        String branch = "main";

        List<String> prFiles = gitHubService.getChangedFilesInPR(owner, repo, prNumber);
        System.out.println("Changed files in PR: " + prFiles);

        List<String> mergeFiles = gitHubService.getChangedFilesInMerge(owner, repo, branch);
        System.out.println("Changed files in merge: " + mergeFiles);
    }
}
```

---

## **5. `GitHubServiceTest.java` (Unit Test)**
Tests `GitHubService` using **Mockito**.

```java
package com.example.github;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class GitHubServiceTest {
    @Mock
    private GitHubFeignClient gitHubFeignClient;

    @InjectMocks
    private GitHubService gitHubService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetChangedFilesInPR() {
        List<Map<String, Object>> mockResponse = new ArrayList<>();
        mockResponse.add(Map.of("filename", "src/Main.java"));
        mockResponse.add(Map.of("filename", "src/Utils.java"));

        when(gitHubFeignClient.getChangedFilesInPR(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(mockResponse);

        List<String> changedFiles = gitHubService.getChangedFilesInPR("owner", "repo", 1);
        assertEquals(2, changedFiles.size());
        assertTrue(changedFiles.contains("src/Main.java"));
        assertTrue(changedFiles.contains("src/Utils.java"));
    }

    @Test
    void testGetChangedFilesInMerge() {
        Map<String, Object> mockResponse = new HashMap<>();
        List<Map<String, Object>> files = new ArrayList<>();
        files.add(Map.of("filename", "src/App.java"));
        mockResponse.put("files", files);

        when(gitHubFeignClient.getChangedFilesInMerge(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockResponse);

        List<String> changedFiles = gitHubService.getChangedFilesInMerge("owner", "repo", "main");
        assertEquals(1, changedFiles.size());
        assertTrue(changedFiles.contains("src/App.java"));
    }
}
```

---

## **Running the Application**
1. **Start the Spring Boot App:**
   ```sh
   mvn spring-boot:run
   ```
2. **Run Tests:**
   ```sh
   mvn test
   ```

---

## **Final Notes**
- **Feign simplifies API calls** with minimal code.
- **Spring Boot provides dependency injection** for better testability.
- **Mockito ensures isolated unit tests**.

Would you like further enhancements, such as **pagination** or **error handling**? 🚀

**Paul Carlton**: Java command line client to detect files changed in a GitHub pull request and also a merge to main branch using a main and a separate class and generating unit tests

**ChatGPT**: Here’s a **Java Command Line Client** that detects:  
1. **Files changed in a GitHub Pull Request**  
2. **Files changed in a merge to the `main` branch**  

The program consists of:  
- **`GitHubClient`**: Handles API communication.  
- **`Main`**: Entry point for command-line execution.  
- **JUnit tests** for unit testing.  

---

## **Project Structure**
```
/GitHubChangeDetector
│── /src
│   ├── /main/java/com/github/client
│   │   ├── GitHubClient.java       # API Handling Class
│   │   ├── Main.java               # CLI Entry Point
│   ├── /test/java/com/github/client
│   │   ├── GitHubClientTest.java   # Unit Tests (JUnit & Mockito)
│── pom.xml  (Maven Dependencies)
```

---

## **1. Add Dependencies (Maven)**
Add the following dependencies to `pom.xml`:

```xml
<dependencies>
    <!-- HTTP Client for API Requests -->
    <dependency>
        <groupId>org.apache.httpcomponents.client5</groupId>
        <artifactId>httpclient5</artifactId>
        <version>5.2</version>
    </dependency>

    <!-- JSON Processing -->
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20220320</version>
    </dependency>

    <!-- JUnit & Mockito for Testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## **2. `GitHubClient.java` (API Handling)**
Handles GitHub API requests and processes responses.

```java
package com.github.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class GitHubClient {
    private static final String GITHUB_TOKEN = "your_personal_access_token";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String API_BASE_URL = "https://api.github.com/repos/";

    private String repoOwner;
    private String repoName;

    public GitHubClient(String repoOwner, String repoName) {
        this.repoOwner = repoOwner;
        this.repoName = repoName;
    }

    public List<String> getChangedFilesInPR(int prNumber) throws Exception {
        String url = API_BASE_URL + repoOwner + "/" + repoName + "/pulls/" + prNumber + "/files";
        return fetchChangedFiles(url);
    }

    public List<String> getChangedFilesInMerge(String commitSha) throws Exception {
        String url = API_BASE_URL + repoOwner + "/" + repoName + "/commits/" + commitSha;
        return fetchChangedFiles(url);
    }

    private List<String> fetchChangedFiles(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + GITHUB_TOKEN)
                .header("Accept", "application/vnd.github.v3+json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("GitHub API Error: " + response.statusCode() + " - " + response.body());
        }

        return parseResponse(response.body());
    }

    private List<String> parseResponse(String responseBody) {
        List<String> changedFiles = new ArrayList<>();
        JSONArray files = new JSONArray(responseBody);

        for (int i = 0; i < files.length(); i++) {
            JSONObject file = files.getJSONObject(i);
            changedFiles.add(file.getString("filename"));
        }
        return changedFiles;
    }
}
```

---

## **3. `Main.java` (Command Line Interface)**
This class allows users to run the client from the command line.

```java
package com.github.client;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter GitHub Repository Owner: ");
        String repoOwner = scanner.nextLine();

        System.out.print("Enter Repository Name: ");
        String repoName = scanner.nextLine();

        GitHubClient gitHubClient = new GitHubClient(repoOwner, repoName);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Check files changed in a Pull Request");
            System.out.println("2. Check files changed in a Merge (commit SHA)");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            try {
                if (choice == 1) {
                    System.out.print("Enter Pull Request Number: ");
                    int prNumber = scanner.nextInt();
                    scanner.nextLine();

                    List<String> files = gitHubClient.getChangedFilesInPR(prNumber);
                    System.out.println("Files changed in PR #" + prNumber + ": " + files);
                } else if (choice == 2) {
                    System.out.print("Enter Commit SHA: ");
                    String commitSha = scanner.nextLine();

                    List<String> files = gitHubClient.getChangedFilesInMerge(commitSha);
                    System.out.println("Files changed in commit " + commitSha + ": " + files);
                } else if (choice == 3) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid choice, please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
```

---



**Paul Carlton**: Java program to detect files changed in a GitHub pull request and also a merge to main branch using a main and a separate class and generating unit tests using feign client


**ChatGPT**: Here’s a **Java program** that detects:  
1. **Files changed in a GitHub Pull Request**  
2. **Files changed in a merge to the `main` branch**  

Using **Feign Client**, we will:  
- Use **Spring Boot + OpenFeign** for API interaction.  
- Implement a **service layer (`GitHubService`)** to handle requests.  
- Create **unit tests (`GitHubServiceTest`)** using **Mockito**.

---

## **Project Structure**
```
/GitHubChangeDetector
│── /src
│   ├── /main/java/com/github/client
│   │   ├── GitHubFeignClient.java   # Feign Client Interface
│   │   ├── GitHubService.java       # Service Layer
│   │   ├── Main.java                # CLI Entry Point
│   ├── /test/java/com/github/client
│   │   ├── GitHubServiceTest.java   # Unit Tests (JUnit & Mockito)
│── pom.xml  (Maven Dependencies)
```

---

## **1. Add Dependencies (Maven)**
Add the following dependencies to `pom.xml`:

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- OpenFeign for GitHub API -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <!-- JSON Processing -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>

    <!-- JUnit & Mockito -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## **2. `GitHubFeignClient.java` (Feign Client Interface)**  
This interface makes requests to the GitHub API.

```java
package com.github.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;

@FeignClient(name = "github-client", url = "https://api.github.com")
public interface GitHubFeignClient {

    @GetMapping("/repos/{owner}/{repo}/pulls/{prNumber}/files")
    List<Map<String, Object>> getChangedFilesInPR(
            @RequestHeader("Authorization") String token,
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("prNumber") int prNumber
    );

    @GetMapping("/repos/{owner}/{repo}/commits/{commitSha}")
    Map<String, Object> getChangedFilesInMerge(
            @RequestHeader("Authorization") String token,
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("commitSha") String commitSha
    );
}
```

---

## **3. `GitHubService.java` (Service Layer)**
Handles API calls and processes responses.

```java
package com.github.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GitHubService {
    private static final String GITHUB_TOKEN = "Bearer your_personal_access_token";

    @Autowired
    private GitHubFeignClient gitHubFeignClient;

    public List<String> getChangedFilesInPR(String owner, String repo, int prNumber) {
        List<Map<String, Object>> response = gitHubFeignClient.getChangedFilesInPR(GITHUB_TOKEN, owner, repo, prNumber);
        List<String> changedFiles = new ArrayList<>();

        for (Map<String, Object> file : response) {
            changedFiles.add((String) file.get("filename"));
        }

        return changedFiles;
    }

    public List<String> getChangedFilesInMerge(String owner, String repo, String commitSha) {
        Map<String, Object> response = gitHubFeignClient.getChangedFilesInMerge(GITHUB_TOKEN, owner, repo, commitSha);
        List<String> changedFiles = new ArrayList<>();

        if (response.containsKey("files")) {
            List<Map<String, Object>> files = (List<Map<String, Object>>) response.get("files");
            for (Map<String, Object> file : files) {
                changedFiles.add((String) file.get("filename"));
            }
        }

        return changedFiles;
    }
}
```

---

## **4. `Main.java` (Command Line Interface)**
This class allows users to run the client from the command line.

```java
package com.github.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Main implements CommandLineRunner {
    @Autowired
    private GitHubService gitHubService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter GitHub Repository Owner: ");
        String repoOwner = scanner.nextLine();

        System.out.print("Enter Repository Name: ");
        String repoName = scanner.nextLine();

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Check files changed in a Pull Request");
            System.out.println("2. Check files changed in a Merge (commit SHA)");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            try {
                if (choice == 1) {
                    System.out.print("Enter Pull Request Number: ");
                    int prNumber = scanner.nextInt();
                    scanner.nextLine();

                    List<String> files = gitHubService.getChangedFilesInPR(repoOwner, repoName, prNumber);
                    System.out.println("Files changed in PR #" + prNumber + ": " + files);
                } else if (choice == 2) {
                    System.out.print("Enter Commit SHA: ");
                    String commitSha = scanner.nextLine();

                    List<String> files = gitHubService.getChangedFilesInMerge(repoOwner, repoName, commitSha);
                    System.out.println("Files changed in commit " + commitSha + ": " + files);
                } else if (choice == 3) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid choice, please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
```

---

## **5. `GitHubServiceTest.java` (Unit Test)**
Tests `GitHubService` using **Mockito**.

```java
package com.github.client;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class GitHubServiceTest {
    @Mock
    private GitHubFeignClient gitHubFeignClient;

    @InjectMocks
    private GitHubService gitHubService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetChangedFilesInPR() {
        List<Map<String, Object>> mockResponse = List.of(
                Map.of("filename", "src/Main.java"),
                Map.of("filename", "src/Utils.java")
        );

        when(gitHubFeignClient.getChangedFilesInPR(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(mockResponse);

        List<String> changedFiles = gitHubService.getChangedFilesInPR("owner", "repo", 1);
        assertEquals(List.of("src/Main.java", "src/Utils.java"), changedFiles);
    }
}
```

---

## **Running the Application**
1. **Start the Spring Boot App:**  
   ```sh
   mvn spring-boot:run
   ```
2. **Run Tests:**  
   ```sh
   mvn test
   ```

Let me know if you need enhancements! 🚀

