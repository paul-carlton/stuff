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

