import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class BasketSplitterTest {

    private BasketSplitter basketSplitter;

    @BeforeEach
    void setUp() {
        basketSplitter = new BasketSplitter("C:\\Users\\weron\\IdeaProjects\\BasketSplitter\\src\\main\\java\\config.json");
    }

    @Test
    void testFilterDeliveryOptions() {
        Map<String, List<String>> mockDeliveryOptions = Map.of(
                "Product1", Arrays.asList("Delivery1", "Delivery2"),
                "Product2", Arrays.asList("Delivery2"),
                "Product3", Arrays.asList("Delivery3")
        );
        List<String> items = Arrays.asList("Product1", "Product3");

        Map<String, List<String>> filteredOptions = basketSplitter.filterDeliveryOptions(mockDeliveryOptions, items);

        assertEquals(2, filteredOptions.size(), "Filtered options should contain exactly two products.");
        assertTrue(filteredOptions.containsKey("Product1") && filteredOptions.containsKey("Product3"), "Filtered options should match the specified items.");
    }

    @Test
    void testCalculateDeliveryFrequency() {
        Map<String, List<String>> mockProducts = Map.of(
                "Product1", Arrays.asList("Delivery1", "Delivery2"),
                "Product2", Arrays.asList("Delivery1"),
                "Product3", Arrays.asList("Delivery2", "Delivery3")
        );

        Map<String, Integer> frequencies = basketSplitter.calculateDeliveryFrequency(mockProducts);

        assertEquals(3, frequencies.size(), "There should be three distinct delivery options.");
        assertEquals(Integer.valueOf(2), frequencies.get("Delivery1"), "Delivery1 should appear twice.");
    }

    @Test
    void testFindMostCommonDelivery() {
        Map<String, Integer> mockFrequencies = Map.of(
                "Delivery1", 2,
                "Delivery2", 3,
                "Delivery3", 1
        );

        String mostCommon = basketSplitter.findMostCommonDelivery(mockFrequencies);

        assertEquals("Delivery2", mostCommon, "The most common delivery option should be 'Delivery2'.");
    }

    @Test
    void testSplit() {
        List<String> testBasket = Arrays.asList("Cookies Oatmeal Raisin", "Cheese Cloth", "Ecolab - Medallion");
        Map<String, List<String>> expected = Map.of(
                "Parcel locker", Arrays.asList("Cookies Oatmeal Raisin", "Cheese Cloth","Ecolab - Medallion")
        );

        Map<String, List<String>> actual = basketSplitter.split(testBasket);

        assertEquals(expected, actual);
    }

}