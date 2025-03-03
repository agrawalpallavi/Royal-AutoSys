import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RoyalBrothersAutomation {
    public static void main(String[] args) {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();
        
       
        page.navigate("https://www.royalbrothers.com/");
        System.out.println("Navigated to Royal Brothers website");

        
        String cityToSearch = "Bangalore"; 
        selectCity(page, cityToSearch);

        // Enter booking time interval
        setBookingTimeInterval(page);

        // Click search button
        page.locator("//div[@id=\"booking-pc\"]//button[@type=\"submit\"][normalize-space()=\"Search\"]").click();
        System.out.println("Clicked on Search button");

        // Wait for results to load
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Collect and display bike details
        collectBikeDetails(page);


        // Close browser - only close after the steps above complete
        browser.close();
        playwright.close();
    }

    private static void selectCity(Page page, String cityName) {
        // Wait for the city selection elements to load
        page.waitForSelector("a.city-box");
    
        // Click on the correct city by matching text or attribute
        page.click("a.city-box[data-city*='" + cityName + "']");
    
        System.out.println("Selected city: " + cityName);
    }
    
    private static void setBookingTimeInterval(Page page) {
        // Format date separately as "04 Mar, 2025"
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy");
        String formattedDate = LocalDateTime.now().format(dateFormatter);
        String tomorrowDate = LocalDateTime.now().plusDays(1).format(dateFormatter);
    
        // Format time separately as "h:mm a"
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        String formattedTime = LocalDateTime.now().format(timeFormatter);
    
        // Enter only the date in the date input field
        page.fill("#pickup-date-other", formattedDate);  // Pickup date
        page.fill("#dropoff-date-other", tomorrowDate);  // Dropoff date
    
        // Enter only the time in the time input field
        page.fill("#pickup-time-other", "8:30 AM");  // Change time as needed
        page.fill("#dropoff-time-other", "6:00 PM");  
    
        System.out.println("Set booking date: " + formattedDate + " | Pickup Time: " + formattedTime);
    }

    private static void collectBikeDetails(Page page) {
        // Wait for bike cards to be visible after search
        page.waitForSelector("//div[contains(@class, 'search_page_row each_card_form bike_model')]", 
            new Page.WaitForSelectorOptions().setTimeout(30000));
        
        // Use the XPath to get all bike cards
        Locator bikeCards = page.locator("//div[contains(@class, 'search_page_row each_card_form bike_model')]");

        int bikeCount = bikeCards.count();
        System.out.println("Found " + bikeCount + " bikes.");
        
        List<BikeDetails> bikeDetailsList = new ArrayList<>();
        
        for (int i = 0; i < bikeCount; i++) {
            Locator bikeCard = bikeCards.nth(i);

            // Extract bike model name
            String modelName = bikeCard.locator("h6").textContent();
            
            // Extract price
            String priceText = bikeCard.locator("span#rental_amount").textContent();
            double price = extractPrice(priceText);
            
            // Extract available kilometers
            String kmsText = bikeCard.locator("span#free_kms").textContent();
            String availableKms = extractCleanKms(kmsText);

            bikeDetailsList.add(new BikeDetails(modelName, price, availableKms));
        }

        bikeDetailsList.sort(Comparator.comparingDouble(BikeDetails::getPrice));

        System.out.println("\nBike Details (sorted by price in ascending order):");
        System.out.println("----------------------------------------------");
        System.out.printf("%-30s %-15s %-15s%n", "Model Name", "Price", "Available KMs");
        System.out.println("----------------------------------------------");

        for (BikeDetails bike : bikeDetailsList) {
            // Use Unicode for rupee symbol directly in the format string
            System.out.printf("%-30s Rs.%-14.2f %-15s%n", bike.getModelName(), bike.getPrice(), bike.getAvailableKms());
        }
    }
    
    private static double extractPrice(String priceText) {
        // Handle potential non-numeric characters in price text
        return Double.parseDouble(priceText.replaceAll("[^0-9.]", ""));
    }
    
    private static String extractCleanKms(String kmsText) {
        // Extract only the kilometer value without the "(km included)" part
        if (kmsText == null || kmsText.isEmpty()) {
            return "0 km";
        }
        
        // Extract the numeric part before "km"
        String cleaned = kmsText.trim();
        if (cleaned.matches(".?(\\d+)\\s*km.")) {
            return cleaned.replaceAll(".?(\\d+)\\s*km.", "$1 km");
        } else {
            // Try to extract just numbers if the pattern doesn't match
            cleaned = cleaned.replaceAll("[^0-9]", "");
            return cleaned.isEmpty() ? "0 km" : cleaned + " km";
        }
    }

    static class BikeDetails {
        private final String modelName;
        private final double price;
        private final String availableKms;

        public BikeDetails(String modelName, double price, String availableKms) {
            this.modelName = modelName;
            this.price = price;
            this.availableKms = availableKms;
        }

        public String getModelName() {
            return modelName;
        }

        public double getPrice() {
            return price;
        }

        public String getAvailableKms() {
            return availableKms;
        }
    }
}
