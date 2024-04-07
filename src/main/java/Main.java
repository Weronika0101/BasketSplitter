import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            //loading json file
            File jsonFile = new File("C:\\Users\\weron\\IdeaProjects\\BasketSplitter\\src\\main\\java\\basket-1.json");

            List<String> basket = mapper.readValue(jsonFile, new TypeReference<List<String>>(){});

            BasketSplitter basketSplitter = new BasketSplitter("C:\\Users\\weron\\IdeaProjects\\BasketSplitter\\src\\main\\java\\config.json");
            Map<String, List<String>> splitResult = basketSplitter.split(basket);
            System.out.println("Wynik: " + splitResult);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
