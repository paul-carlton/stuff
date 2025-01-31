**Paul Carlton**: Spring boot application using couchbase

**ChatGPT**: Creating a **Spring Boot** application with **Couchbase** as the database involves several steps. Here's a guide to get you started:

---

## 1. **Set Up the Project**
Create a Spring Boot project using tools like [Spring Initializr](https://start.spring.io/) or your favorite IDE. Include the following dependencies:
- Spring Data Couchbase
- Spring Web (if building a REST API)
- Lombok (optional, for reducing boilerplate code)

### Example `pom.xml` Dependencies:
```xml
<dependencies>
    <!-- Spring Boot Starter for Couchbase -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-couchbase</artifactId>
    </dependency>
    <!-- Spring Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

## 2. **Configure Couchbase**
Update the `application.properties` or `application.yml` file with your Couchbase cluster details.

### `application.properties` Example:
```properties
spring.couchbase.connection-string=127.0.0.1
spring.couchbase.username=admin
spring.couchbase.password=password
spring.couchbase.bucket.name=exampleBucket
```

### `application.yml` Example:
```yaml
spring:
  couchbase:
    connection-string: 127.0.0.1
    username: admin
    password: password
    bucket:
      name: exampleBucket
```

---

## 3. **Define Your Entity**
Create a Couchbase entity class annotated with `@Document`.

### Example Entity:
```java
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import lombok.Data;

@Data
@Document
public class User {
    @Id
    private String id;
    private String name;
    private String email;
}
```

---

## 4. **Create a Repository**
Create a repository interface that extends `CouchbaseRepository`.

### Example Repository:
```java
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CouchbaseRepository<User, String> {
    // Custom query methods (if needed)
    User findByEmail(String email);
}
```

---

## 5. **Write a Service Layer**
Use the repository in a service class to interact with Couchbase.

### Example Service:
```java
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
```

---

## 6. **Build a REST Controller**
Expose APIs to interact with the service layer.

### Example Controller:
```java
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
```

---

## 7. **Run the Application**
Run the Spring Boot application and test the APIs using tools like Postman or cURL.

---

## 8. **(Optional) Custom Query with N1QL**
If you need advanced queries, you can write custom N1QL queries.

### Example:
```java
@Query("#{#n1ql.selectEntity} WHERE email = $1")
User findByEmail(String email);
```

---

This setup creates a complete Spring Boot application with Couchbase integration, allowing you to perform CRUD operations on your data. Let me know if you want to dive deeper into any section!

**Paul Carlton**: How do I run couchbase on Kubernetes 

**ChatGPT**: Running Couchbase on **Kubernetes** involves deploying Couchbase clusters using Couchbase’s official Kubernetes Operator. The operator simplifies deployment, management, scaling, and upgrades. Below is a step-by-step guide to get started:

---

## **1. Prerequisites**
1. **Kubernetes Cluster**: Ensure you have a running Kubernetes cluster (Minikube, AKS, EKS, GKE, etc.).
2. **kubectl**: Install and configure `kubectl` to interact with your cluster.
3. **Helm**: Install Helm for deploying the Couchbase Operator.

---

## **2. Install Couchbase Operator**
### Option 1: Install Using Helm
The Couchbase Operator Helm chart simplifies installation.

1. **Add the Couchbase Helm repository:**
   ```bash
   helm repo add couchbase https://couchbase-partners.github.io/helm-charts/
   helm repo update
   ```

2. **Install the Couchbase Operator chart:**
   ```bash
   helm install couchbase-operator couchbase/couchbase-operator --namespace couchbase --create-namespace
   ```

3. Verify the Operator pods are running:
   ```bash
   kubectl get pods -n couchbase
   ```

---

## **3. Deploy a Couchbase Cluster**
Once the operator is installed, deploy a Couchbase cluster.

1. **Create a `CouchbaseCluster` YAML file** (e.g., `couchbase-cluster.yaml`):
   ```yaml
   apiVersion: couchbase.com/v2
   kind: CouchbaseCluster
   metadata:
     name: couchbase-cluster
     namespace: couchbase
   spec:
     cluster:
       dataServiceMemoryQuota: 512
       indexServiceMemoryQuota: 512
       searchServiceMemoryQuota: 512
       eventingServiceMemoryQuota: 256
       analyticsServiceMemoryQuota: 1024
     servers:
     - size: 3
       services:
         - data
         - index
         - query
   ```

2. **Apply the cluster configuration:**
   ```bash
   kubectl apply -f couchbase-cluster.yaml
   ```

3. **Verify the cluster is running:**
   ```bash
   kubectl get pods -n couchbase
   ```

---

## **4. Access Couchbase UI**
To access the Couchbase Web Console:

1. **Expose the service via port-forwarding:**
   ```bash
   kubectl port-forward service/couchbase-cluster-ui 8091:8091 -n couchbase
   ```

2. Open `http://localhost:8091` in your browser.

---

## **5. (Optional) Persistent Storage**
For production, enable **persistent storage** by creating a `StorageClass` and attaching PersistentVolumeClaims (PVCs) to your Couchbase cluster.

### Example with Persistent Volume:
Add this to your `couchbase-cluster.yaml`:
```yaml
spec:
  buckets:
    managed: true
  volumeClaimTemplates:
  - metadata:
      name: couchbase-volume
    spec:
      accessModes: ["ReadWriteOnce"]
      resources:
        requests:
          storage: 10Gi
```

---

## **6. Scaling the Cluster**
To scale up or down, modify the `size` in your cluster specification:
```yaml
servers:
- size: 5
  services:
    - data
    - index
    - query
```

Apply the updated configuration:
```bash
kubectl apply -f couchbase-cluster.yaml
```

---

## **7. Monitoring and Logs**
- View logs:
  ```bash
  kubectl logs -n couchbase <pod-name>
  ```
- Monitor using the Couchbase Web Console.

---

## **8. Clean Up**
To delete the cluster and operator:
```bash
kubectl delete couchbasecluster couchbase-cluster -n couchbase
helm uninstall couchbase-operator -n couchbase
```

---

This setup allows you to run Couchbase on Kubernetes while leveraging the operator for streamlined management. Let me know if you'd like more details on persistent storage, advanced configurations, or multi-cluster setups!

