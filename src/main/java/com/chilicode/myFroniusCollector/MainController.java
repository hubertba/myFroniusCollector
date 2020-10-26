package com.chilicode.myFroniusCollector;

import com.chilicode.myFroniusCollector.fronius.FroniusDataCollector;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private static final String PULL = "/pull";

    @GetMapping(PULL)
    public void pollData() {

        FroniusDataCollector  collector = new FroniusDataCollector();
        collector.run();
        
    }
    
}
