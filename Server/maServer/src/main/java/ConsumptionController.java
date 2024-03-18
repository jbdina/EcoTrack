import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class ConsumptionController {

    @PostMapping("/submit_consumption")
    public ResponseEntity<String> submitConsumption(@RequestBody Map<String, String> data) {
        // Extract data from the request
        String date = data.get("date");
        String consumptionAmount = data.get("consumptionAmount");
        String counterNumber = data.get("counterNumber");
        String apartmentInfo = data.get("apartmentInfo");

        // For now, you can just print the data (you'll need to store it in a CSV file or database)
        System.out.println("Date: " + date);
        System.out.println("Consumption Amount: " + consumptionAmount);
        System.out.println("Counter Number: " + counterNumber);
        System.out.println("Apartment Info: " + apartmentInfo);

        // You should store the data in your CSV file or database here

        return ResponseEntity.ok("Data received successfully");
    }
}
