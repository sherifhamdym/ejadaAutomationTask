package dataDriven;

import org.testng.annotations.DataProvider;
import utility.PropertyManager;

public class ClassDataReader {

    // This class is used to read data from the config.properties file
    // to cover the invalid usernames and passwords
    private final PropertyManager prop = PropertyManager.getInstance();

    // usernames
    private final  String standard_user = prop.getProperty("standard_user");
    private final String problem_user = prop.getProperty("problem_user");
    private final String performance_glitch_user = prop.getProperty("performance_glitch_user");
    private final String error_user = prop.getProperty("error_user");
    private final String visual_user = prop.getProperty("visual_user");
    private final String locked_out_user = prop.getProperty("locked_out_user");
    private final String empty_username = prop.getProperty("empty_username");
    private final String empty_password = prop.getProperty("empty_password");
    private final String invalid_username = prop.getProperty("invalid_username");

    // passwords
    private final String validPassword = prop.getProperty("validPassword");
    private final String invalidPassword = prop.getProperty("invalidPassword");

    // expected messages
    private final String lockedOutMessage = prop.getProperty("locked_out_msg");
    private final String notMatchMessage = prop.getProperty("not_match_msg");
    private final String emptyUsernameMessage = prop.getProperty("empty_username_msg");
    private final String emptyPasswordMessage = prop.getProperty("empty_password_msg");

    // InValid user combinations
    @DataProvider(name = "loginData")
    public Object[][] provideLoginData() {
        return new Object[][]{
                // Locked out user
                {locked_out_user, validPassword,lockedOutMessage }, //lockedOutMessage

                // Invalid username cases
                {invalid_username, validPassword, notMatchMessage},//notMatchMessage
                {invalid_username, invalidPassword, notMatchMessage},

                // Empty username cases
                {empty_username, validPassword, emptyUsernameMessage},
                {empty_username, invalidPassword, emptyUsernameMessage},
                {empty_username, empty_password, emptyUsernameMessage},

                // Empty password cases
                {standard_user, empty_password, emptyPasswordMessage},
                {problem_user, empty_password, emptyPasswordMessage},
                {performance_glitch_user, empty_password, emptyPasswordMessage},
                {error_user, empty_password, emptyPasswordMessage},
                {visual_user, empty_password, emptyPasswordMessage},
                {locked_out_user, empty_password, emptyPasswordMessage},
                {invalid_username, empty_password, emptyPasswordMessage},

                // Invalid password cases (with valid usernames)
                {standard_user, invalidPassword, notMatchMessage},
                {problem_user, invalidPassword, notMatchMessage},
                {performance_glitch_user, invalidPassword, notMatchMessage},
                {error_user, invalidPassword, notMatchMessage},
                {visual_user, invalidPassword, notMatchMessage},

                // Both empty case
                {empty_username, empty_password, emptyUsernameMessage}
        };
    }
}