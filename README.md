# Royal Brothers Bike Rental Automation

This Java application automates the process of searching and comparing bike rentals on the [Royal Brothers](https://www.royalbrothers.com/) website using Playwright.

## Description

This project automates the following tasks on the Royal Brothers website:
- Navigating to the site
- Selecting a city from available options
- Setting booking dates and times
- Performing a search for available bikes
- Collecting and displaying bike details (model, price, included kilometers)
- Sorting results by price for easy comparison

## Prerequisites

- Java 11 or higher
- Maven for dependency management
- Playwright for Java

## Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>com.microsoft.playwright</groupId>
        <artifactId>playwright</artifactId>
        <version>1.40.0</version>
    </dependency>
</dependencies>
```

## Installation

1. Clone this repository:
   ```
   git clone https://github.com/yourusername/royal-brothers-automation.git
   cd royal-brothers-automation
   ```

2. Install Playwright browsers:
   ```
   mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"
   ```

3. Build the project:
   ```
   mvn clean compile
   ```

## Usage

Run the application using Maven:

```
mvn exec:java -D exec.mainClass="RoyalBrothersAutomation"
```

Or run the compiled class directly:

```
java -cp target/classes RoyalBrothersAutomation
```

When prompted, enter the city where you want to rent a bike (e.g., "Bangalore", "Agra", "Coorg").

## Features

1. **Dynamic City Selection**:
   - Supports exact and partial matching of city names
   - Displays all available cities
   - Falls back to closest match if exact city isn't found

2. **Automated Date and Time Setting**:
   - Sets booking from current date to the next day
   - Uses configurable pickup and drop-off times

3. **Comprehensive Results**:
   - Collects model name, rental price, and included kilometers
   - Sorts results by price in ascending order
   - Displays results in an easy-to-read table format

## How It Works

1. The application launches a browser and navigates to the Royal Brothers website
2. It identifies all available cities and attempts to select the user-specified city
3. Booking dates and times are automatically set
4. After searching, it extracts information about all available bikes
5. Results are sorted by price and displayed in a formatted table

## Project Structure

- `RoyalBrothersAutomation.java`: Main class containing the automation logic
- `BikeDetails`: Inner class to store and sort bike rental information

## Customization

- Modify the `setBookingTimeInterval()` method to change the default booking times
- Adjust timeouts in `waitForSelector()` calls if you have slower internet connections

## Troubleshooting

- If the city selection fails, check if the city name is spelled correctly or try a major city name
- If elements are not being found, the website structure may have changed, requiring selector updates
- For connection issues, increase the timeout values in the code

## License

[MIT License](LICENSE)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
 
