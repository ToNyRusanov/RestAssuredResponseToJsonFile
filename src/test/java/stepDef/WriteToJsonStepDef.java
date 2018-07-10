package stepDef;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import util.ReadFromJsonFile;
import util.SQLRequest;

/**
 * 
 * Created by Anton Rusanov on 7/10/2018
 *
 */
public class WriteToJsonStepDef {
	
	private static final String PROJECT_END_POINT = "/api/json";
	private static final String PROJECT_LAST_BUILD_END_POINT = "/api/json?tree=lastBuild[number,status,timestamp,id,result]";
	private static final String Date_Pattern = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String Jenkins_Query = "type here the query for the DB checking";
	@Before("@jenkins")
	public void setUp() {
		//set up the URI(this will be loaded automatically in the given() method)
		RestAssured.baseURI = "http://localhost:8080";

	}

	private static Response response;
	private static Response lastBuildResponse;
	private static JSONObject json = new JSONObject();
	private static JSONArray jsonArray1 = new JSONArray();
	private static JSONArray jsonArray2 = new JSONArray();
	private static List<String> expectedJenkinsdata = new ArrayList<>();
	private static SimpleDateFormat format = new SimpleDateFormat(Date_Pattern);

	@SuppressWarnings("unchecked")
	@Given("^send request to rest service$")
	public void sendRequest() {
		//load the end points in the get() method
		response = RestAssured.given().auth().preemptive().basic("user", "password")
				.contentType("application/json").when().get(PROJECT_END_POINT);
		System.out.println(response.getStatusCode());
		assertTrue(response.getStatusCode() == 200);
		lastBuildResponse = RestAssured.given().auth().preemptive().basic("user", "password")
				.contentType("application/json").when().get(PROJECT_LAST_BUILD_END_POINT);
		assertTrue(lastBuildResponse.getStatusCode() == 200);
		System.out.println(response.asString());
		System.out.println(lastBuildResponse.asString());
		
		//put json object in the jason array
		// the getMap and get methods are used to get the nested json object in the response
		jsonArray1.add(lastBuildResponse.jsonPath().getMap("lastBuild").get("number"));
		//converting the timestamp to the simple date format
		jsonArray1.add(format.format(lastBuildResponse.jsonPath().getMap("lastBuild").get("timestamp")));
		jsonArray1.add(lastBuildResponse.jsonPath().getMap("lastBuild").get("result"));
		System.out.println(jsonArray1.toJSONString());
		
		//if the jason object is not nested, can be used getJsonObject method.
		jsonArray2.add(response.jsonPath().getJsonObject("name"));
		jsonArray2.add(response.jsonPath().getJsonObject("url"));

		json.put("lastBuild", jsonArray1);
		json.put("name", jsonArray2.get(0));
		json.put("url", jsonArray2.get(1));

	}

	@Given("^write the information to json file$")
	public void writeJsonFile() {

		try {

			FileWriter write = new FileWriter("src/test/resources/json/jenkinsFile.json");
			
			write.write(json.toJSONString());
			write.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	@Then("^read the information from the file and save it$")
	public void readInfoAndSave() {
		expectedJenkinsdata.addAll(ReadFromJsonFile.jenkinsData("src\\test\\resources\\json\\jenkinsFile.json", "name",
				"url", "lastBuild"));
		System.out.println(expectedJenkinsdata);

	}


	@Then("^check the jenkins DB and verify the records with the saved information$")
	public void checkDbRecordsAndVerify() {
		List<String> actualJenkinsData = SQLRequest.jenkinsDBCheck(Jenkins_Query);
		assertArrayEquals("The actual data is not as expected!", expectedJenkinsdata.toArray(), actualJenkinsData.toArray());

	}

}
