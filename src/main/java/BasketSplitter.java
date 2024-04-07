import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BasketSplitter {
    private Map<String, List<String>> deliveryOptions;

    public BasketSplitter(String absolutePathToConfigFile) {
        this.deliveryOptions = loadDeliveryOptions(absolutePathToConfigFile);
    }

    Map<String, List<String>> loadDeliveryOptions(String absolutePathToConfigFile) {
        // ładowanie z pliku json
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, List<String>> productDeliveryOptions = mapper.readValue(new File(absolutePathToConfigFile), new TypeReference<Map<String, List<String>>>(){});
            return productDeliveryOptions;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public Map<String, List<String>> split(List<String> items) {

        // filtering to get only these products which are in the basket
       Map<String, List<String>> filteredProducts = filterDeliveryOptions(deliveryOptions,items);
        Map<String, List<String>> resultDeliveries = new HashMap<>();

        while (!filteredProducts.isEmpty()) {
            // Map(delivery method, count)
            // we choose the methos which applies for most of the products in the basket
            // continue until the list is empy
            Map<String, Integer> deliveryFrequency = calculateDeliveryFrequency(filteredProducts);
            String mostCommonDelivery = findMostCommonDelivery(deliveryFrequency);

            List<String> assignedProducts = new ArrayList<>();
            for (Iterator<Map.Entry<String, List<String>>> it = filteredProducts.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, List<String>> entry = it.next();
                if (entry.getValue().contains(mostCommonDelivery)) {
                    assignedProducts.add(entry.getKey());
                    it.remove();
                }
            }

            resultDeliveries.put(mostCommonDelivery, assignedProducts);
        }

        //sorting
        return resultDeliveries.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    Map<String, List<String>> filterDeliveryOptions(Map<String, List<String>> deliveryOptions, List<String> items) {
        return deliveryOptions
                .entrySet().stream()
                .filter(entry -> items.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Map<String, Integer> calculateDeliveryFrequency(Map<String, List<String>> products) {
        Map<String, Integer> frequency = new HashMap<>();
        for (List<String> deliveryOptions : products.values()) {
            for (String option : deliveryOptions) {
                frequency.put(option, frequency.getOrDefault(option, 0) + 1);
            }
        }
        return frequency;
    }

    String findMostCommonDelivery(Map<String, Integer> frequency) {
        return frequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}

