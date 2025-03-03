# Royal Brothers Automation

A Java-based automation tool that uses Playwright to interact with the Royal Brothers website for searching and retrieving bike rental information.

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
- Maven or Gradle
- Playwright for Java

## Dependencies

- Microsoft Playwright (com.microsoft.playwright)
- Java Time API (java.time)

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

3. Build the project:
   ```bash
   mvn clean package
   ```

## Usage

1. Run the application:
   ```bash
   java -cp target/royal-brothers-automation-1.0-SNAPSHOT.jar RoyalBrothersAutomation
   ```

2. Customizing Parameters:
   
   You can modify these parameters in the code:
   
   - City selection:
     ```java
     String cityToSearch = "Bangalore"; // Change to your preferred city
     ```
   
   - Booking dates and times:
     ```java
     page.fill("#pickup-date-other", formattedDate);   // Current date
     page.fill("#dropoff-date-other", tomorrowDate);   // Tomorrow's date
     page.fill("#pickup-time-other", "8:30 AM");       // Pickup time
     page.fill("#dropoff-time-other", "6:00 PM");      // Drop-off time
     ```

## Output

The program will print a table of bike rentals sorted by price, displaying:
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

## How It Works

1. The application launches a Chromium browser using Playwright
2. Navigates to the Royal Brothers website
3. Selects the specified city
4. Sets booking date and time parameters
5. Clicks the search button
6. Waits for results to load
7. Collects and processes bike details
8. Displays results sorted by price

## Troubleshooting

- **Element Not Found**: If you encounter element selection issues, check if the website structure has changed and update the selectors accordingly.
- **Timing Issues**: If the automation runs too fast for the website to respond, you may need to add additional wait conditions.

## License

[MIT License](LICENSE)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
