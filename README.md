# Royal Brothers Automation

A Java-based automation testing tool that uses Playwright to interact with the Royal Brothers website for searching and retrieving bike rental information.

## Overview

This project automates the process of searching for bike rentals on the Royal Brothers website. It navigates to the site, selects a city, specifies booking dates and times, and retrieves a list of available bikes sorted by price.

## Features

- **City Selection**: Automatically selects a specified city from the available options
- **Date & Time Setting**: Configures pickup and drop-off dates and times
- **Results Collection**: Gathers details of all available bikes including:
  - Model name
  - Rental price
  - Available kilometers
- **Price Sorting**: Displays bikes sorted by price in ascending order

## Prerequisites

- Java 8 or higher
- Maven
- Playwright for Java

## Dependencies

Add the following dependencies to your `pom.xml`:

```xml
<dependencies>
    <!-- Playwright -->
    <dependency>
        <groupId>com.microsoft.playwright</groupId>
        <artifactId>playwright</artifactId>
        <version>1.42.0</version>
    </dependency>
    
    <!-- JUnit Jupiter API -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
    
    <!-- JUnit Jupiter Engine -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Also add the Surefire plugin to run tests:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.5</version>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.12.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/royal-brothers-automation.git
   cd royal-brothers-automation
   ```

2. Install Playwright dependencies:
   ```bash
   mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
   ```

## Running Tests

To run the automation tests, use the Maven test command:

```bash
mvn clean test
```

This command will:
1. Clean the project
2. Compile the source code
3. Run the RoyalBrothersAutomationTest

## Customizing Tests

You can modify these parameters in the test code:

- City selection:
  ```java
  String cityToSearch = "Bangalore"; // Change to your preferred city
  ```

- Booking dates and times:
  ```java
  page.fill("#pickup-time-other", "8:30 AM");  // Change pickup time as needed
  page.fill("#dropoff-time-other", "6:00 PM");  // Change drop-off time as needed
  ```

## Output

The test will print a table of bike rentals sorted by price, displaying:
- Model name
- Price (in Indian Rupees)
- Available kilometers

Example output:
```
Bike Details (sorted by price in ascending order):
----------------------------------------------
Model Name                     Price           Available KMs   
----------------------------------------------
Honda Activa                   Rs.350.00       100 km         
TVS Jupiter                    Rs.399.00       120 km         
Royal Enfield Classic 350      Rs.1200.00      150 km         
```

## Troubleshooting

- **Element Not Found**: If you encounter element selection issues, check if the website structure has changed and update the selectors accordingly.
- **Timing Issues**: If the automation runs too fast for the website to respond, you may need to add additional wait conditions.
- **Maven Issues**: Make sure your pom.xml is correctly configured with all required dependencies and plugins.

## License

[MIT License](LICENSE)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
