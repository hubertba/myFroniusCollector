package com.chilicode.myFroniusCollector.fronius;

public class FroniusDataCollector {

    public FroniusDataCollector(){

    }

	public void run(String ipAddress) {

        DataCollection myData = ReadFronius.calcMainData(ipAddress);
        


    }
    
}
