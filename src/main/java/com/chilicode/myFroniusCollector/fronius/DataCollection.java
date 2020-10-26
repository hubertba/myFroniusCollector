package com.chilicode.myFroniusCollector.fronius;

public class DataCollection {
	
	Double p_pv = 0.0;
	//TODO check if datatype is big enough
	Double e_total = 0.0;
	
	public DataCollection(double p_pv2, double e_total2) {
		this.p_pv = p_pv2;
		this.e_total = e_total2;
	}

}
