import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RoyalBrothersAutomationTest {

    private static Playwright playwright;
    private static Browser browser;
    private Page page;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void createPage() {
        page = browser.newPage();
    }

    @Test
    void testRoyalBrothersBooking() {
        // Navigate to website
        page.navigate("https://www.royalbrothers.com/");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        System.out.println("Navigated to Royal Brothers website");

        // Select city
        String cityToSearch = "Bangalore";
        selectCity(page, cityToSearch);

        // Set booking interval
        setBookingTimeInterval(page);

        // Click search button
        page.locator("//div[@id=\"booking-pc\"]//button[@type=\"submit\"][normalize-space()=\"Search\"]").click();
        System.out.println("Clicked on Search button");

        // Wait for results
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Collect bike details
        collectBikeDetails(page);
    }

    @AfterEach
    void closePage() {
        page.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    private void selectCity(Page page, String cityName) {
        page.waitForSelector("a.city-box");
        page.click("a.city-box[data-city*='" + cityName + "']");
        System.out.println("Selected city: " + cityName);
    }

    private void setBookingTimeInterval(Page page) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy");
        String formattedDate = LocalDateTime.now().format(dateFormatter);
        String tomorrowDate = LocalDateTime.now().plusDays(1).format(dateFormatter);

        page.fill("#pickup-date-other", formattedDate);
        page.fill("#dropoff-date-other", tomorrowDate);
        page.fill("#pickup-time-other", "8:30 AM");
        page.fill("#dropoff-time-other", "6:00 PM");

        System.out.println("Set booking date: " + formattedDate);
    }

    private void collectBikeDetails(Page page) {
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
            double price = Double.parseDouble(priceText.replaceAll("[^0-9.]", ""));
            String kmsText = bikeCard.locator("span#free_kms").textContent();
            String availableKms = kmsText.replaceAll("[^0-9]", "") + " km";
            bikeDetailsList.add(new BikeDetails(modelName, price, availableKms));
        }

        bikeDetailsList.sort(Comparator.comparingDouble(BikeDetails::getPrice));
        System.out.println("\nBike Details:");
        System.out.printf("%-30s %-15s %-15s%n", "Model Name", "Price", "Available KMs");
        for (BikeDetails bike : bikeDetailsList) {
            System.out.printf("%-30s Rs.%-14.2f %-15s%n", bike.getModelName(), bike.getPrice(), bike.getAvailableKms());
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

        public String getModelName() { return modelName; }
        public double getPrice() { return price; }
        public String getAvailableKms() { return availableKms; }
    }
}
