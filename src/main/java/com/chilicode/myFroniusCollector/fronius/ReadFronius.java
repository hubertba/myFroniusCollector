package com.chilicode.myFroniusCollector.fronius;

import java.io.IOException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReadFronius {

	public static void main(String[] args) {
		// TODO check error handling eg if no valid data comes over request
		// TODO create config file
		
		// Papa IP
		String ipadresse = "192.168.1.3";

		// Josef IP
		//String ipadresse = "192.168.5.28";

		// String jsonString = jsonGetRequest("http://" + ipadresse +
		// "/solar_api/v1/GetInverterRealtimeData.cgi?Scope=System");

		// String jsonString = jsonGetRequest("http://" + ipadresse +
		// "/solar_api/v1/GetMeterRealtimeData.cgi?Scope=System");
		// String jsonString = jsonGetRequest("http://" + ipadresse +
		// "/solar_api/v1/GetMeterRealtimeData.cgi?Scope=Device&DeviceId=0");
		// String jsonString = jsonGetRequest("http://" + ipadresse +
		// "/solar_api/v1/GetArchiveData.cgi?Scope=System&StartDate=30.5.2020&EndDate=30.5.2020&Channel=EnergyReal_WAC_Sum_Produced");

		// get values for the 2 different Straenge
		// http://192.168.1.3/solar_api/v1/GetArchiveData.cgi?Scope=System&StartDate=20.5.2020&EndDate=20.5.2020&Channel=EnergyReal_WAC_Plus_Absolute&Channel=EnergyReal_WAC_Minus_Absolute&Channel=Current_DC_String_1&Channel=Current_DC_String_2&Channel=Voltage_DC_String_1&Channel=Voltage_DC_String_2

		// System.out.println(jsonString);

		DataCollection myData = calcMainData(ipadresse);
		//TODO just do this once per day
		calcStringData(ipadresse);

	}

	public static void calcStringData(String ipadresse) {
		// TODO think about Date handling
		Date date= new Date(); // your date
		// Choose time zone in which you want to interpret your Date
		Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
		cal1.setTime(date);
		cal1.add(Calendar.DATE, -1); // number represents number of days
		
		int year = cal1.get(Calendar.YEAR);
		int month = cal1.get(Calendar.MONTH);
		int day = cal1.get(Calendar.DAY_OF_MONTH);
		
		month++;
		
		String startDate = day +"." + month+"." + year;
		String endDate = "";
		endDate = startDate;
		String jsonString = jsonGetRequest("http://" + ipadresse
				+ "/solar_api/v1/GetArchiveData.cgi?Scope=System&StartDate=" + startDate + "&EndDate=" + endDate
				+ "&Channel=Current_DC_String_1&Channel=Current_DC_String_2&Channel=Voltage_DC_String_1&Channel=Voltage_DC_String_2");

		try {
			final JSONObject obj = new JSONObject(jsonString);
			JSONObject body = obj.getJSONObject("Body");
			JSONObject data = body.getJSONObject("Data");
			// TODO check possibility of more inverters
			JSONObject inverter = data.getJSONObject("inverter/1");
			JSONObject data_inv = inverter.getJSONObject("Data");
			JSONArray strg_names =data_inv.names();
			JSONArray jsonArray = data_inv.toJSONArray(strg_names);
			System.out.println("*****data 2 check start*****");
			//System.out.println(jsonArray);
			for (int i = 0; i < jsonArray.length();i++){
				
		          JSONObject string_cont = jsonArray.getJSONObject(i);
		          String strg_name = strg_names.getString(i);
		          System.out.println(strg_names.getString(i));
		          System.out.println(string_cont);
			}
			System.out.println("*****data 2 check end*******");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static DataCollection calcMainData(String ipadresse) {
		double p_pv = 0.0;
		double e_total = 0.0;

		String jsonString = jsonGetRequest("http://" + ipadresse + "/solar_api/v1/GetPowerFlowRealtimeData.fcgi");

		try {

			final JSONObject obj = new JSONObject(jsonString);
			JSONObject body = obj.getJSONObject("Body");
			JSONObject data = body.getJSONObject("Data");
			JSONObject site = data.getJSONObject("Site");

			// System.out.println(site.toString());

			System.out.println("P_PV: " + site.getString("P_PV"));
			if(!(site.getString("P_PV").contains("null"))){
				
				p_pv = Double.valueOf(site.getString("P_PV"));
			}
			System.out.println("E_Total: " + site.getString("E_Total"));
			e_total = Double.valueOf(site.getString("E_Total"));
			System.out.println("E_Year: " + site.getString("E_Year"));
			System.out.println("P_Grid: " + site.getString("P_Grid"));
			System.out.println("P_Load: " + site.getString("P_Load"));
			System.out.println("E_Day: " + site.getString("E_Day"));
			System.out.println("rel_Autonomy: " + site.getString("rel_Autonomy"));
			System.out.println("rel_SelfConsumption: " + site.getString("rel_SelfConsumption"));

			JSONObject head = obj.getJSONObject("Head");
			System.out.println("Timestamp: " + head.getString("Timestamp"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new DataCollection(p_pv, e_total);
	}

	private static String streamToString(InputStream inputStream) {
		return new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
	}

	public static String jsonGetRequest(String urlQueryString) {
		String json = null;
		try {
			URL url = new URL(urlQueryString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "utf-8");
			connection.connect();
			InputStream inStream = connection.getInputStream();
			json = streamToString(inStream); // input stream to string
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return json;
	}

}
