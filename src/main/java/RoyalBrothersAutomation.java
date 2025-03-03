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

        
        setBookingTimeInterval(page);

       
        page.locator("//div[@id=\"booking-pc\"]//button[@type=\"submit\"][normalize-space()=\"Search\"]").click();
        System.out.println("Clicked on Search button");

       
        page.waitForLoadState(LoadState.NETWORKIDLE);

       
        collectBikeDetails(page);


       
        browser.close();
        playwright.close();
    }

    private static void selectCity(Page page, String cityName) {
       
        page.waitForSelector("a.city-box");
    
       
        page.click("a.city-box[data-city*='" + cityName + "']");
    
        System.out.println("Selected city: " + cityName);
    }
    
    private static void setBookingTimeInterval(Page page) {
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy");
        String formattedDate = LocalDateTime.now().format(dateFormatter);
        String tomorrowDate = LocalDateTime.now().plusDays(1).format(dateFormatter);
    
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        String formattedTime = LocalDateTime.now().format(timeFormatter);
    
        
        page.fill("#pickup-date-other", formattedDate);  
        page.fill("#dropoff-date-other", tomorrowDate);  
    
        
        page.fill("#pickup-time-other", "8:30 AM");  
        page.fill("#dropoff-time-other", "6:00 PM");  
    
        System.out.println("Set booking date: " + formattedDate + " | Pickup Time: " + formattedTime);
    }

    private static void collectBikeDetails(Page page) {
        
        page.waitForSelector("//div[contains(@class, 'search_page_row each_card_form bike_model')]", 
            new Page.WaitForSelectorOptions().setTimeout(30000));
        
       
        Locator bikeCards = page.locator("//div[contains(@class, 'search_page_row each_card_form bike_model')]");

        int bikeCount = bikeCards.count();
        System.out.println("Found " + bikeCount + " bikes.");
        
        List<BikeDetails> bikeDetailsList = new ArrayList<>();
        
        for (int i = 0; i < bikeCount; i++) {
            Locator bikeCard = bikeCards.nth(i);

           
            String modelName = bikeCard.locator("h6").textContent();
            
            
            String priceText = bikeCard.locator("span#rental_amount").textContent();
            double price = extractPrice(priceText);
            
            
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
           
            System.out.printf("%-30s Rs.%-14.2f %-15s%n", bike.getModelName(), bike.getPrice(), bike.getAvailableKms());
        }
    }
    
    private static double extractPrice(String priceText) {
       
        return Double.parseDouble(priceText.replaceAll("[^0-9.]", ""));
    }
    
    private static String extractCleanKms(String kmsText) {
       
        if (kmsText == null || kmsText.isEmpty()) {
            return "0 km";
        }
        
        
        String cleaned = kmsText.trim();
        if (cleaned.matches(".?(\\d+)\\s*km.")) {
            return cleaned.replaceAll(".?(\\d+)\\s*km.", "$1 km");
        } else {
           
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
