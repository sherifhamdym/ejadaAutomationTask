package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
    private static volatile PropertyManager instance;
    private static final Object lock = new Object();
    private final Properties props = new Properties();

    // Private constructor to enforce singleton pattern
    private PropertyManager() {
        String propertyFilePath = "src/test/resources/TestData/config.properties"; // Updated path
        try (FileInputStream input = new FileInputStream(propertyFilePath)) {
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new PropertyManager();
                }
            }
        }
        return instance;
    }

    // Method to get the property value by key
    public String getProperty(String propertyName) {
        return props.getProperty(propertyName);
    }


}
