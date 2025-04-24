package dataDriven;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class JsonDataReader {

    // This method reads data from a JSON file and provides it as a DataProvider for TestNG
    @DataProvider(name = "loginDataFromJson")
    public static Object[][] provideLoginDataFromJson() throws IOException {
        String jsonFilePath = "/testdata/InvalidCredentials_Data.json"; // Assuming file is in src/test/resources
        return readJsonData(jsonFilePath);
    }

    // This method reads the JSON file and converts it into a 2D Object array
    public static Object[][] readJsonData(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = JsonDataReader.class.getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IOException("Could not find file: " + filePath);
        }

        // Read the JSON file and convert it to a List of Maps
        List<Map<String, String>> dataList = mapper.readValue(inputStream, new TypeReference<List<Map<String, String>>>() {
        });
        Object[][] dataArray = new Object[dataList.size()][dataList.get(0).size()];

        // Fill the 2D array with data from the List of Maps
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> dataMap = dataList.get(i);
            dataArray[i][0] = dataMap.get("username");
            dataArray[i][1] = dataMap.get("password");
            dataArray[i][2] = dataMap.get("errorMessage");
            // Add more columns as needed based on your JSON structure
        }

        return dataArray;
    }
}