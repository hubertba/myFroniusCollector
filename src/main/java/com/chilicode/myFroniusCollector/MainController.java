package com.chilicode.myFroniusCollector;

import com.chilicode.myFroniusCollector.fronius.FroniusDataCollector;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    
    @GetMapping("/pull/{ipAddress}")
    public String pollData(@PathVariable String ipAddress) {

        FroniusDataCollector  collector = new FroniusDataCollector();
        collector.run(ipAddress);
        return ipAddress;
        
    }
    
}
