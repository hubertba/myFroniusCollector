package com.chilicode.myFroniusCollector.fronius;

import java.util.Map;

public class StringData {
	String name;
	Map <Integer,Double> values;
	
	StringData(String name1,Map <Integer,Double>values1){
		this.name = name1;
		this.values = values1;
	}
	
}
