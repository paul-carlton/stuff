**Paul Carlton**: Show me examples of calling methods with varags in Java 

**ChatGPT**: In Java, **varargs** (variable-length arguments) allow you to pass a variable number of arguments to a method. They are defined using **three dots (`...`)** after the data type.

Here are some examples of calling methods with varargs:

### **Example 1: Simple varargs method**
```java
public class VarargsExample {
    public static void printNumbers(int... numbers) {
        for (int num : numbers) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        printNumbers();               // No arguments
        printNumbers(1);              // Single argument
        printNumbers(1, 2, 3, 4, 5);  // Multiple arguments
    }
}
```
**Output:**
```
(empty line)
1 
1 2 3 4 5 
```

---

### **Example 2: Varargs with other parameters**
A varargs parameter must be **the last parameter** in the method.

```java
public class VarargsExample {
    public static void greet(String message, String... names) {
        System.out.println(message);
        for (String name : names) {
            System.out.println("Hello, " + name);
        }
    }

    public static void main(String[] args) {
        greet("Greetings!"); // No names
        greet("Welcome", "Alice");
        greet("Hi everyone", "Bob", "Charlie", "David");
    }
}
```
**Output:**
```
Greetings!
Welcome
Hello, Alice
Hi everyone
Hello, Bob
Hello, Charlie
Hello, David
```

---

### **Example 3: Overloading with varargs**
Java will prefer an exact match over a varargs method.

```java
public class VarargsOverload {
    public static void print(String str) {
        System.out.println("Single string: " + str);
    }

    public static void print(String... strings) {
        System.out.println("Varargs method:");
        for (String str : strings) {
            System.out.println(str);
        }
    }

    public static void main(String[] args) {
        print("Hello");               // Calls single-string method
        print("Hello", "World");      // Calls varargs method
    }
}
```
**Output:**
```
Single string: Hello
Varargs method:
Hello
World
```

---

### **Example 4: Passing an array explicitly**
Varargs internally use an array, so you can also pass an array directly.

```java
public class VarargsExample {
    public static void printNumbers(int... numbers) {
        for (int num : numbers) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr = {10, 20, 30};
        printNumbers(arr); // Passing an array explicitly
    }
}
```
**Output:**
```
10 20 30 
```

Let me know if you need more details!

