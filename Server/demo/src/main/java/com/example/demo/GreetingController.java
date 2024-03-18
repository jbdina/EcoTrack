package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {
    private static final String template = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "Sunshine") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @PostMapping("/number")
    public Greeting processNumber(@RequestBody NumberRequest request) {
        // Generate the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Create a greeting message with the received number and the current date and time
        String content = String.format(template, request.getNumber()) + " | " + now.toString();

        // Return the Greeting object
        return new Greeting(counter.incrementAndGet(), content);
    }

    private final Map<LocalDate, List<Integer>> savedEntries = new HashMap<>();

    @PostMapping("/saveNumber")
    public ResponseEntity<String> saveNumber(@RequestBody NumberRequest numberRequest) {
        // Get the number from the request
        int number = numberRequest.getNumber();

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Check if the date already exists in the map
        if (!savedEntries.containsKey(currentDate)) {
            // If the date doesn't exist, create a new list for it
            savedEntries.put(currentDate, new ArrayList<>());
        }

        // Add the number to the list associated with the current date
        savedEntries.get(currentDate).add(number);

        // Respond with success message
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Number saved successfully: " + number + " at " + currentDate);
    }
}
