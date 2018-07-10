package util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/*
 * Created by Anton Rusanov on 7/10/2018
 */
public class ReadFromJsonFile {

	

	public static List<String> jenkinsData(String fileName, String property1,String property2,String property3) {
		return getExpectedProjectBuildData(fileName, property1,property2,property3);
	}

	


	private static List<String> getExpectedProjectBuildData(String fileName, String property1,String property2,String property3) {
		List<String> list = new ArrayList<>();
		
		JsonObject file = null;
		JsonParser parser = new JsonParser();
		try {
			file = (JsonObject) parser.parse(new FileReader(fileName));

		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		if(property1.equals("name")) {
			String recordsProjectBuildsToArray = file.get(property1).getAsString();
			if (recordsProjectBuildsToArray != null) {
					list.add(recordsProjectBuildsToArray);
			}
			}

		if(property2.equals("url")){
			String recordUrl = file.get(property2).getAsString();
			if (recordUrl != null) {
					list.add(recordUrl);
			}
			}
		//to load the data from nested json objects use here JsonArray, because in the json file they still will be in the array. 	
		if(property3.equals("lastBuild")) {
			JsonArray recordsProjectChmToArray = (JsonArray) file.get(property3);
			if (recordsProjectChmToArray != null) {
				for (int i = 0; i < recordsProjectChmToArray.size(); i++) {
					list.add(recordsProjectChmToArray.get(i).toString().replace('"', '/').replace("/", ""));
				}
			}
			

		
		}
		return list;

	}

	

}
