**Paul Carlton**: When I see a class in html what does that mean

**ChatGPT**: In HTML, a **class** is an attribute that you can add to an element to assign it a specific name. This name can then be used to apply CSS styles or select elements using JavaScript. 

For example:  

```html
<div class="highlight">This is a highlighted section.</div>
```

Here, `class="highlight"` assigns the name **"highlight"** to the `<div>` element.  

### Why Use Classes?
1. **Styling with CSS**  
   You can target all elements with the same class in CSS:  
   ```css
   .highlight {
       background-color: yellow;
   }
   ```

2. **Selecting Elements in JavaScript**  
   You can use JavaScript to manipulate elements with a specific class:  
   ```js
   document.querySelector(".highlight").textContent = "Updated text!";
   ```

3. **Reusability**  
   You can apply the same class to multiple elements to style or manipulate them together:  
   ```html
   <p class="highlight">First paragraph</p>
   <p class="highlight">Second paragraph</p>
   ```

Unlike **id**, which must be unique per element, **classes** can be shared across multiple elements.

**Paul Carlton**: My html document doesn’t have a definition of a CSS, where can I find the definition 

**ChatGPT**: If your HTML document does not contain a CSS definition, the styles might be coming from one of these sources:  

### 1. **External CSS File**  
Your HTML might link to an external CSS file using the `<link>` tag in the `<head>` section:  
```html
<link rel="stylesheet" href="styles.css">
```
- Look for a file named **styles.css** (or a similar name) in your project folder.  
- Open it to check the CSS definitions.  

### 2. **Internal CSS**  
Sometimes, CSS is written inside the HTML document within a `<style>` tag in the `<head>` section:  
```html
<style>
  .highlight {
    background-color: yellow;
  }
</style>
```
- Check your HTML file to see if there’s a `<style>` block.  

### 3. **Inline CSS**  
CSS can also be applied directly inside an element using the `style` attribute:  
```html
<p style="color: red;">This text is red.</p>
```
- If an element has a `style` attribute, it defines the CSS for that specific element.  

### 4. **CSS from a Framework (Bootstrap, Tailwind, etc.)**  
If your HTML includes a link to an external framework, styles might be coming from there:  
```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
```
- In this case, styles are loaded from an online source like Bootstrap or Tailwind CSS.  

### 5. **Browser Default Styles**  
If no CSS is defined, your browser applies its default styles (e.g., margins, fonts, colors). Different browsers may have slightly different defaults.  

To find out where a style is coming from, you can:  
1. **Right-click an element** in your browser and select **Inspect (Chrome, Edge) or Inspect Element (Firefox)**.  
2. In the **Elements tab**, check the **Styles panel** to see which CSS rules are being applied and where they come from.

**Paul Carlton**: What css are included with chrome

**ChatGPT**: Chrome (and all modern browsers) includes **default CSS styles** known as the **User-Agent Stylesheet**. These are built-in styles that browsers apply to HTML elements when no custom CSS is provided.  

### **Where to See Chrome’s Default CSS?**  
1. Open your webpage in Chrome.  
2. **Right-click an element** and select **Inspect**.  
3. In the **Elements tab**, look at the **Styles panel**.  
4. Scroll down to find styles labeled **user agent stylesheet**—these are Chrome’s default styles.  

### **Examples of Chrome’s Default CSS Styles**
Here are some examples of how Chrome styles elements by default:  

```css
/* Headings */
h1 {
  font-size: 2em;
  font-weight: bold;
  margin: 0.67em 0;
}

/* Paragraphs */
p {
  margin: 1em 0;
}

/* Lists */
ul, ol {
  padding-left: 40px;
}

/* Links */
a {
  color: -webkit-link;
  text-decoration: underline;
  cursor: pointer;
}

/* Buttons */
button {
  display: inline-block;
  padding: 2px 6px;
  cursor: pointer;
}
```

### **How to Override Chrome’s Default CSS**
Since these styles are applied automatically, you can override them using your own CSS:  

```css
/* Custom styles */
p {
  margin: 0;  /* Removes default margins */
  color: blue; /* Changes text color */
}
```

If you want a **completely clean slate**, you can use a **CSS reset** or a **normalize.css** file to remove or standardize default browser styles.

**Paul Carlton**: Run a spring boot server application in GitHub action 

**ChatGPT**: To run a **Spring Boot server application** in a **GitHub Action**, follow these steps:

---

### **1. Create a GitHub Actions Workflow File**
Add a workflow file inside `.github/workflows/`, e.g., `.github/workflows/spring-boot.yml`.

```yaml
name: Run Spring Boot Application

on:
  push:
    branches:
      - main
  pull_request:
    branches:
      - main
  workflow_dispatch:  # Allows manual triggering

jobs:
  run-spring-boot:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Cache Maven Dependencies
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: maven-${{ hashFiles('**/pom.xml') }}
          restore-keys: maven-

      - name: Build with Maven
        run: mvn clean package

      - name: Run Spring Boot Application
        run: java -jar target/*.jar &
        
      - name: Wait for Application Startup
        run: sleep 20  # Adjust based on your app's startup time

      - name: Check Application Health
        run: curl --fail http://localhost:8080/actuator/health || exit 1
```

---

### **2. Explanation of the Workflow**
- **Checkout Repository**: Pulls the latest code.
- **Set up JDK 17**: Configures Java using Temurin (adjust Java version if needed).
- **Cache Maven Dependencies**: Speeds up the build process.
- **Build with Maven**: Runs `mvn clean package` to build the JAR file.
- **Run Spring Boot Application**: Starts the Spring Boot app in the background.
- **Wait for Startup**: Gives the application time to start.
- **Health Check**: Uses `curl` to verify if the app is running (requires `actuator` dependency).

---

### **3. Make Sure Your Application Supports Health Checks**
If you use **Spring Boot Actuator**, add it to your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Then, enable the `/actuator/health` endpoint in `application.properties`:
```properties
management.endpoints.web.exposure.include=health
```

---

### **4. Push and Verify**
- Commit and push the changes.
- Go to **GitHub → Actions** and check if the workflow runs successfully.

---

### **Optional Enhancements**
- **Use PostgreSQL or MySQL**: If your app needs a database, start a service in the workflow.
- **Run Integration Tests**: Instead of just running the server, execute tests with `mvn verify`.

Would you like help with database integration or deployment after the server runs?

**Paul Carlton**: Build multiple jar files in maven project 

**ChatGPT**: ### **Building Multiple JAR Files in a Maven Project**  
In a Maven project, you can build multiple JAR files by using different approaches, such as:  
1. **Using Multiple Modules (Recommended for Multi-JAR Projects)**
2. **Using the Maven Assembly Plugin**
3. **Using the Maven Shade Plugin**  

---

## **1. Using Multiple Modules (Recommended)**
If you have different functionalities, you can create a **multi-module Maven project** and build separate JAR files for each module.

### **Project Structure**
```
my-maven-project/
│── parent-pom.xml
│── module-a/
│   ├── pom.xml
│── module-b/
│   ├── pom.xml
```

### **Parent `pom.xml` (Root Project)**
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>my-maven-project</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>

    <modules>
        <module>module-a</module>
        <module>module-b</module>
    </modules>
</project>
```

### **Module A (`module-a/pom.xml`)**
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.example</groupId>
        <artifactId>my-maven-project</artifactId>
        <version>1.0.0</version>
    </parent>
    <artifactId>module-a</artifactId>
    <packaging>jar</packaging>
</project>
```

### **Module B (`module-b/pom.xml`)**
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.example</groupId>
        <artifactId>my-maven-project</artifactId>
        <version>1.0.0</version>
    </parent>
    <artifactId>module-b</artifactId>
    <packaging>jar</packaging>
</project>
```

### **Build Command**
Run this in the root project directory:
```sh
mvn clean install
```
This will generate separate JARs:
- `module-a/target/module-a-1.0.0.jar`
- `module-b/target/module-b-1.0.0.jar`

---

## **2. Using the Maven Assembly Plugin**
This approach helps when you want multiple JARs from the **same project**.

### **Add to `pom.xml`**
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>3.6.0</version>
            <executions>
                <execution>
                    <id>custom-jar</id>
                    <phase>package</phase>
                    <goals>
                        <goal>single</goal>
                    </goals>
                    <configuration>
                        <descriptors>
                            <descriptor>src/assembly/custom-jar.xml</descriptor>
                        </descriptors>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### **Create `src/assembly/custom-jar.xml`**
```xml
<assembly xmlns="http://maven.apache.org/ASSEMBLY/2.2.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/ASSEMBLY/2.2.0 http://maven.apache.org/xsd/assembly-2.2.0.xsd">
    <id>custom-jar</id>
    <formats>
        <format>jar</format>
    </formats>
    <fileSets>
        <fileSet>
            <directory>target/classes</directory>
            <outputDirectory>/</outputDirectory>
        </fileSet>
    </fileSets>
</assembly>
```

### **Build Command**
```sh
mvn clean package
```
This will generate multiple JARs in `target/`.

---

## **3. Using the Maven Shade Plugin (For Fat JARs)**
If you want multiple **fat JARs (JARs with dependencies included)**, use the **Maven Shade Plugin**.

### **Add to `pom.xml`**
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.5.0</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <finalName>app-shaded</finalName>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### **Build Command**
```sh
mvn clean package
```
This will generate:
- `target/app-shaded.jar`
- `target/original-<artifactId>-<version>.jar`

---

## **Which Approach to Choose?**
| Approach | Use Case |
|----------|---------|
| **Multi-Module Project** | If you need separate logical components as JARs |
| **Maven Assembly Plugin** | If you need different types of JARs in the same project |
| **Maven Shade Plugin** | If you need fat JARs (Spring Boot runnable JARs) |

Which one fits your needs?

**Paul Carlton**: I want it intent 20% in html/css

**ChatGPT**: If you want to **indent** an element by **20%** in HTML/CSS, you can use **margin, padding, or transform** properties.  

---

### **1. Using `margin-left` (Recommended)**
```css
.indented {
    margin-left: 20%;
}
```
```html
<div class="indented">This text is indented by 20%.</div>
```
✅ **Best for block elements like `<div>`, `<p>`, etc.**

---

### **2. Using `padding-left` (For internal spacing)**
```css
.indented {
    padding-left: 20%;
}
```
```html
<div class="indented">This text has 20% padding inside.</div>
```
✅ **Good if you want to keep background color intact while indenting.**

---

### **3. Using `transform: translateX(20%)` (For precise positioning)**
```css
.indented {
    transform: translateX(20%);
}
```
```html
<div class="indented">This text is shifted 20% to the right.</div>
```
✅ **Good for animating or dynamic positioning.**

---

### **Which One to Use?**
| Method | When to Use |
|--------|------------|
| `margin-left` | Best for shifting elements without affecting inside content |
| `padding-left` | Best for indenting text inside an element |
| `transform: translateX(20%)` | Best for animations or precise positioning |

Would you like this for a specific layout or design? 🚀

**Paul Carlton**: html/css how to set width of table field

**ChatGPT**: To **set the width of a table field (column or cell)** in HTML/CSS, you can use several methods:  

---

### **1. Using the `<colgroup>` and `<col>` (Recommended)**
```html
<table border="1">
    <colgroup>
        <col style="width: 50%;">
        <col style="width: 30%;">
        <col style="width: 20%;">
    </colgroup>
    <tr>
        <th>Column 1</th>
        <th>Column 2</th>
        <th>Column 3</th>
    </tr>
    <tr>
        <td>Data 1</td>
        <td>Data 2</td>
        <td>Data 3</td>
    </tr>
</table>
```
✅ **Best for defining column widths in a structured way.**  

---

### **2. Using `width` in `<th>` or `<td>`**
```html
<table border="1">
    <tr>
        <th style="width: 50%;">Column 1</th>
        <th style="width: 30%;">Column 2</th>
        <th style="width: 20%;">Column 3</th>
    </tr>
    <tr>
        <td>Data 1</td>
        <td>Data 2</td>
        <td>Data 3</td>
    </tr>
</table>
```
✅ **Simple but needs to be set in every row.**  

---

### **3. Using CSS for Table Columns**
```css
table {
    width: 100%;
    border-collapse: collapse;
}
th, td {
    border: 1px solid black;
}
th:nth-child(1), td:nth-child(1) { width: 50%; }
th:nth-child(2), td:nth-child(2) { width: 30%; }
th:nth-child(3), td:nth-child(3) { width: 20%; }
```
```html
<table>
    <tr>
        <th>Column 1</th>
        <th>Column 2</th>
        <th>Column 3</th>
    </tr>
    <tr>
        <td>Data 1</td>
        <td>Data 2</td>
        <td>Data 3</td>
    </tr>
</table>
```
✅ **Best for applying styles dynamically without modifying HTML.**  

---

### **4. Using `table-layout: fixed;` for Equal Columns**
```css
table {
    width: 100%;
    table-layout: fixed;
}
th, td {
    width: 33%;
}
```
```html
<table border="1">
    <tr>
        <th>Column 1</th>
        <th>Column 2</th>
        <th>Column 3</th>
    </tr>
    <tr>
        <td>Data 1</td>
        <td>Data 2</td>
        <td>Data 3</td>
    </tr>
</table>
```
✅ **Ensures fixed column sizes, even if content is larger.**  

---

### **Which One to Use?**
| Method | Best For |
|--------|---------|
| `<colgroup>` | Defining column widths cleanly at the top |
| `width` in `<th>` or `<td>` | Quick inline styles for individual columns |
| CSS `nth-child` | Applying widths dynamically with styles |
| `table-layout: fixed` | Equal column widths & consistent layout |

Would you like to make it responsive or add styling? 🚀

**Paul Carlton**: html span specify text wrap optionsa

**ChatGPT**: In **HTML/CSS**, you can control **text wrapping** inside a `<span>` using different properties depending on how you want the text to behave.  

---

### **1. Default Behavior (Wraps Automatically)**
By default, text inside a `<span>` will wrap based on the parent container's width. Example:

```html
<p>This is a <span>wrapped text</span> inside a paragraph.</p>
```
✅ The text will wrap naturally when the container is too small.

---

### **2. Prevent Text Wrapping (`white-space: nowrap;`)**
Use `white-space: nowrap;` to **prevent** text from wrapping inside a `<span>`:

```css
.no-wrap {
    white-space: nowrap;
}
```
```html
<p>This is a <span class="no-wrap">non-wrapping text</span> inside a paragraph.</p>
```
✅ The text **won't break** to a new line even if the container is too small.

---

### **3. Force Word Break (`word-break`)**
If you want long words to break onto a new line, use `word-break: break-word;`:

```css
.break-word {
    word-break: break-word;
}
```
```html
<p>This is a <span class="break-word">veryveryverylongwordthatneedstobreak</span> inside a paragraph.</p>
```
✅ This forces long words to **break at any point** to prevent overflow.

---

### **4. Hyphenate Words (`hyphens: auto;`)**
To **hyphenate** words instead of breaking them abruptly:

```css
.hyphenate {
    hyphens: auto;
}
```
```html
<p>This is a <span class="hyphenate">supercalifragilisticexpialidocious</span> word that might hyphenate.</p>
```
✅ Works best when the browser supports **hyphenation**.

---

### **5. Ellipsis for Overflow (`text-overflow: ellipsis;`)**
If you want text to **truncate with "..."** when it overflows, use:

```css
.ellipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    width: 150px;
    display: inline-block;
}
```
```html
<p>This is a <span class="ellipsis">very long text that will be truncated...</span></p>
```
✅ Great for limiting text display without wrapping.

---

### **Which One to Use?**
| Use Case | CSS Property |
|----------|--------------|
| Prevent wrapping | `white-space: nowrap;` |
| Break long words | `word-break: break-word;` |
| Hyphenate words | `hyphens: auto;` |
| Show "..." when overflowing | `text-overflow: ellipsis;` |

Would you like to apply these inside a specific design (e.g., tables, cards, etc.)? 🚀

**Paul Carlton**: span text on same line as image

**ChatGPT**: If you want **text inside a `<span>` to be on the same line as an image**, here are some ways to do it in **HTML & CSS**:  

---

### **1. Default Inline Behavior (Best for Simple Cases)**
By default, `<span>` and `<img>` are **inline elements**, so they appear on the same line naturally.  
```html
<img src="image.jpg" alt="Example" width="50">
<span>Text next to the image</span>
```
✅ **Works as long as there’s enough space.**  

---

### **2. Adjust Alignment with `vertical-align`**
If the text and image **don't align properly**, use `vertical-align: middle;`:
```css
img {
    vertical-align: middle;
}
```
```html
<img src="image.jpg" alt="Example" width="50">
<span>Text next to the image</span>
```
✅ **Keeps text aligned to the middle of the image.**

---

### **3. Use `display: flex;` for Better Control**
If you want **perfect alignment**, wrap them in a `<div>` and use `display: flex;`:
```css
.container {
    display: flex;
    align-items: center; /* Aligns text with image */
    gap: 10px; /* Adds spacing */
}
```
```html
<div class="container">
    <img src="image.jpg" alt="Example" width="50">
    <span>Text next to the image</span>
</div>
```
✅ **Best for precise control and spacing.**  

---

### **4. Use `display: inline-block;` for More Control**
If the text and image don’t align properly, wrap the `<span>` in `inline-block`:
```css
img, span {
    display: inline-block;
    vertical-align: middle;
}
```
```html
<img src="image.jpg" alt="Example" width="50">
<span>Text next to the image</span>
```
✅ **Allows width & height adjustments while staying inline.**

---

### **Which One to Use?**
| Use Case | Solution |
|----------|---------|
| Simple inline text next to an image | Default `<img> <span>` |
| Better alignment | `vertical-align: middle;` |
| Full control & responsiveness | `display: flex;` |
| Custom inline styling | `display: inline-block;` |

Would you like additional styling, such as spacing or font adjustments? 🚀

**Paul Carlton**: html inside td href and span text should be on same line

**ChatGPT**: If you want a **link (`<a href>`)** and **text inside a `<span>`** to be on the **same line** inside a `<td>` (table cell), follow these approaches:  

---

### **1. Default Inline Behavior (Simple Solution)**
Since `<a>` and `<span>` are **inline elements**, they will be on the same line by default:
```html
<table border="1">
    <tr>
        <td>
            <a href="#">Click Here</a> <span>Some Text</span>
        </td>
    </tr>
</table>
```
✅ **Works by default if there’s enough space.**  

---

### **2. Use `white-space: nowrap;` to Prevent Wrapping**
If the link and text **break into separate lines**, prevent wrapping with CSS:
```css
td {
    white-space: nowrap;
}
```
```html
<table border="1">
    <tr>
        <td>
            <a href="#">Click Here</a> <span>Some Text</span>
        </td>
    </tr>
</table>
```
✅ **Ensures both elements stay on the same line.**  

---

### **3. Align Using `display: inline-block;` (For Better Control)**
If alignment is off, force them to behave as **inline-block** elements:
```css
a, span {
    display: inline-block;
    vertical-align: middle;
}
```
```html
<table border="1">
    <tr>
        <td>
            <a href="#">Click Here</a>
            <span>Some Text</span>
        </td>
    </tr>
</table>
```
✅ **Keeps proper alignment while allowing styling.**  

---

### **4. Use `display: flex;` for Perfect Alignment**
For **full control**, use `display: flex;` inside `<td>`:
```css
td {
    display: flex;
    align-items: center;
    gap: 10px; /* Adds spacing between link and text */
}
```
```html
<table border="1">
    <tr>
        <td>
            <a href="#">Click Here</a>
            <span>Some Text</span>
        </td>
    </tr>
</table>
```
✅ **Best for precise alignment and spacing.**  

---

### **Which Solution to Choose?**
| Issue | Solution |
|-------|---------|
| Default inline behavior is fine | Just use `<a> <span>` |
| Link and text wrapping | `white-space: nowrap;` |
| Need better alignment | `display: inline-block;` |
| Full control with spacing | `display: flex;` |

Would you like additional styling, such as padding or color changes? 🚀

**Paul Carlton**: html make table below previous table

**ChatGPT**: If you want to place a new **table below a previous table** in HTML, there are a few methods to ensure correct spacing and alignment.  

---

### **1. Default Block Behavior (Simplest Method)**
By default, tables are **block-level elements**, so they naturally appear **below each other**:
```html
<table border="1">
    <tr>
        <td>First Table</td>
    </tr>
</table>

<table border="1">
    <tr>
        <td>Second Table Below</td>
    </tr>
</table>
```
✅ **Works automatically as long as no conflicting styles exist.**

---

### **2. Add `margin-top` for Spacing**
If the tables are too close, add **CSS margin**:
```css
table {
    margin-top: 20px;
}
```
```html
<table border="1">
    <tr>
        <td>First Table</td>
    </tr>
</table>

<table border="1">
    <tr>
        <td>Second Table Below</td>
    </tr>
</table>
```
✅ **Ensures proper spacing between tables.**  

---

### **3. Use a `<div>` Wrapper for Better Control**
If you need more styling control, wrap each table in a `<div>`:
```css
.table-container {
    margin-bottom: 20px;
}
```
```html
<div class="table-container">
    <table border="1">
        <tr>
            <td>First Table</td>
        </tr>
    </table>
</div>

<div class="table-container">
    <table border="1">
        <tr>
            <td>Second Table Below</td>
        </tr>
    </table>
</div>
```
✅ **Useful for applying spacing consistently across multiple tables.**  

---

### **4. Use `display: block;` (If Tables Are Inline)**
If something forces the tables to be **inline**, set `display: block;`:
```css
table {
    display: block;
    margin-top: 20px;
}
```
✅ **Forces tables to appear one below the other.**

---

### **Which Solution to Use?**
| Issue | Solution |
|-------|---------|
| Default behavior | Just place `<table>` elements one after another |
| Need more spacing | Use `margin-top` in CSS |
| Better control & styling | Wrap tables in `<div>` |
| Tables appearing side by side | Use `display: block;` |

Would you like to adjust the alignment or styling further? 🚀

**Paul Carlton**: html/css wrap text at 80% of page width

**ChatGPT**: If you want to **wrap text at 80% of the page width** in HTML/CSS, you can use the following methods:

---

### **1. Using `max-width` (Recommended)**
This method ensures the text wraps at **80% of the page width**:
```css
.text-container {
    max-width: 80%;
    word-wrap: break-word;
}
```
```html
<div class="text-container">
    This text will wrap when it reaches 80% of the page width.
</div>
```
✅ **Best for responsive design.**  

---

### **2. Using `width: 80%`**
If you want the text container itself to be **80% of the page width**:
```css
.text-container {
    width: 80%;
}
```
```html
<div class="text-container">
    This text will wrap at 80% of the page width.
</div>
```
✅ **Works well but doesn't limit the max width.**

---

### **3. Wrapping Long Words with `word-break`**
To prevent long words from overflowing:
```css
.text-container {
    max-width: 80%;
    word-break: break-word;
}
```
```html
<div class="text-container">
    A verylongwordthatwouldnormallyoverflowthisdiv will break correctly.
</div>
```
✅ **Ensures long words wrap properly.**  

---

### **4. Centering the Text Block**
If you also want to center the text block:
```css
.text-container {
    max-width: 80%;
    margin: 0 auto; /* Centers the text */
    text-align: justify; /* Optional: Justify text */
}
```
```html
<div class="text-container">
    This text is wrapped at 80% of the width and centered.
</div>
```
✅ **Keeps text readable and neatly aligned.**  

---

### **Which Solution to Use?**
| Requirement | Solution |
|------------|---------|
| Simple text wrapping | `max-width: 80%` |
| Force long words to break | `word-break: break-word` |
| Center the text block | `margin: 0 auto;` |

Would you like additional styling, such as font adjustments? 🚀

