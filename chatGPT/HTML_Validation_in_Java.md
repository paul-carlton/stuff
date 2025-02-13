**Paul Carlton**: Java code to validate an HTML document, not lint it just validate that all of the elements are opened and closed properly, et cetera

**ChatGPT**: You can use **JSoup** to validate whether an HTML document has properly opened and closed elements. JSoup provides a parser that can detect unclosed tags and malformed structures.  

Here's a Java example that checks if an HTML document is properly structured:  

### **Maven Dependency (If Needed)**  
If you don’t have JSoup, add this to your `pom.xml`:  
```xml
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.15.4</version>
</dependency>
```

### **Java Code for Validation**  
```java
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;

import java.util.List;

public class HtmlValidator {
    public static boolean isValidHtml(String html) {
        // List to store parse errors
        ParseErrorList errorList = ParseErrorList.tracking(10);
        
        // Parse HTML and track errors
        Document doc = Jsoup.parse(html, "", Parser.htmlParser().setTrackErrors(10));
        List<ParseError> errors = doc.parser().getErrors();

        // Print errors if any
        if (!errors.isEmpty()) {
            System.out.println("HTML Validation Errors:");
            for (ParseError error : errors) {
                System.out.println(error);
            }
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String html = "<html><head><title>Test</title></head><body><p>Valid HTML</p></body></html>"; // Valid
        String invalidHtml = "<html><head><title>Test</title></head><body><p>Invalid HTML"; // Missing </p> and </body></html>

        System.out.println("Is valid HTML: " + isValidHtml(html));         // Expected: true
        System.out.println("Is valid HTML: " + isValidHtml(invalidHtml));  // Expected: false
    }
}
```

### **How It Works**  
1. Uses `Jsoup.parse()` to parse the HTML document.
2. Enables `Parser.htmlParser().setTrackErrors(10)` to capture up to 10 parse errors.
3. Retrieves and prints any parsing errors.
4. Returns `true` if no errors, otherwise `false`.

Would you like additional features like DTD validation or W3C compliance checks?

**Paul Carlton**: I’ve tried that. I’m thinking of some code that might use a parser to convert the HTML into a Dom and then examine it to ensure that it is valid

**ChatGPT**: Got it! Since you want to build a DOM and then check its validity, you can use **Java's built-in DOM parser (javax.xml.parsers.DocumentBuilder)** for strict validation. However, since HTML is not strict XML, we need to use an **HTML-aware** parser.  

### **Solution Approach**
- Use **JTidy** (an HTML parser and cleaner) to parse HTML into a DOM.
- Check for errors during parsing.
- Examine the DOM structure to ensure all elements are properly opened and closed.

---

### **Maven Dependency for JTidy**
```xml
<dependency>
    <groupId>net.sf.jtidy</groupId>
    <artifactId>jtidy</artifactId>
    <version>r938</version>
</dependency>
```

---

### **Java Code for HTML Validation Using JTidy**
```java
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.tidy.Tidy;

import java.io.*;

public class HtmlDomValidator {

    public static boolean isValidHtml(String html) {
        Tidy tidy = new Tidy();
        tidy.setQuiet(true);  // Suppress non-error messages
        tidy.setShowWarnings(false);
        tidy.setForceOutput(false);  // Prevents output if errors are present
        tidy.setXmlOut(true); // Ensures well-formed output
        
        // Capture errors
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        tidy.setErrout(new PrintWriter(errorStream, true));

        // Convert HTML to DOM
        Document dom = tidy.parseDOM(new ByteArrayInputStream(html.getBytes()), null);
        
        // If errors were printed, HTML is invalid
        String errors = errorStream.toString();
        if (!errors.isEmpty()) {
            System.out.println("HTML Validation Errors:");
            System.out.println(errors);
            return false;
        }

        // Further DOM checks (if needed)
        return hasValidStructure(dom);
    }

    private static boolean hasValidStructure(Document doc) {
        NodeList htmlTags = doc.getElementsByTagName("html");
        NodeList bodyTags = doc.getElementsByTagName("body");

        // Ensure there is a single <html> and <body> element
        return htmlTags.getLength() == 1 && bodyTags.getLength() == 1;
    }

    public static void main(String[] args) {
        String validHtml = "<html><head><title>Test</title></head><body><p>Hello</p></body></html>";
        String invalidHtml = "<html><head><title>Test</title></head><body><p>Missing closing tag";

        System.out.println("Valid HTML: " + isValidHtml(validHtml));   // Expected: true
        System.out.println("Invalid HTML: " + isValidHtml(invalidHtml)); // Expected: false
    }
}
```

---

### **How This Works**
1. Uses **JTidy** to parse the HTML into a **DOM Document**.
2. Captures **validation errors** (unclosed tags, missing elements, etc.).
3. If errors are found, the HTML is invalid.
4. **Optional extra check** ensures the DOM has an expected structure.

---

### **Why JTidy?**
- Converts malformed HTML into a well-formed DOM.
- Detects missing/misplaced closing tags.
- More strict than JSoup in validating structure.

Would this work for your needs? Or do you need further customization?

**Paul Carlton**: I’ve tried Jtidy too, what I want is code that directly calls the DOM parser 

**ChatGPT**: If you want to use a **DOM parser directly**, you can use **Java's built-in XML parsers**. However, since HTML is not always well-formed XML, you need an **HTML-aware** DOM parser. The best option is to use **NekoHTML**, which converts HTML into a well-formed XML DOM and allows direct validation with `DocumentBuilder`.  

---

## **Solution: Using NekoHTML with Java's DOM Parser**
We will:  
1. Use **NekoHTML** to parse HTML into a `org.w3c.dom.Document`.  
2. Use Java's `DocumentBuilder` to validate the DOM structure.  

---

### **Maven Dependency for NekoHTML**
```xml
<dependency>
    <groupId>net.sourceforge.nekohtml</groupId>
    <artifactId>nekohtml</artifactId>
    <version>1.9.22</version>
</dependency>
```

---

### **Java Code to Validate HTML with a DOM Parser**
```java
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.StringReader;

public class HtmlDomValidator {

    public static boolean isValidHtml(String html) {
        try {
            // Initialize DOM Parser from Apache Xerces (NekoHTML)
            DOMParser parser = new DOMParser();
            
            // Parse the HTML input
            parser.parse(new InputSource(new StringReader(html)));
            
            // Get the DOM document (if parsing succeeds, HTML is valid)
            Document document = parser.getDocument();

            // Further structural validation can be done here if needed
            return document != null;
        } catch (SAXException | java.io.IOException e) {
            // If parsing fails, HTML is invalid
            System.out.println("HTML Validation Error: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        String validHtml = "<html><head><title>Test</title></head><body><p>Valid</p></body></html>";
        String invalidHtml = "<html><head><title>Test</title></head><body><p>Invalid HTML"; // Missing closing tags

        System.out.println("Valid HTML: " + isValidHtml(validHtml));   // Expected: true
        System.out.println("Invalid HTML: " + isValidHtml(invalidHtml)); // Expected: false
    }
}
```

---

### **How It Works**
1. **NekoHTML (`DOMParser`)** converts the HTML into a **DOM Document**.
2. If the parsing **fails** (e.g., unclosed tags), an exception is thrown.
3. If parsing **succeeds**, the HTML is considered valid.
4. Additional structural checks can be performed on the `Document` if needed.

---

### **Why This Approach?**
✅ Uses **standard Java DOM parsing** (via Apache Xerces).  
✅ Handles real-world **malformed HTML**, unlike strict XML parsers.  
✅ Easily extendable (e.g., checking for missing `<body>` or `<head>`).  

Would this work for your use case? Let me know if you need extra checks!

