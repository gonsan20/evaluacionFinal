import model.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import specifications.ResponseSpecs;

import static helpers.DataHelper.generateRandomEmail;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserTests extends BaseTest {

    private static String resourcePath = "/v1/user";

    @BeforeClass
    public void showStatus() {
        System.out.println("---------------------Start Testing---------------------");
    }


    @Test
    public void Test_00Creat_User_Already_Exist(){

        User user = new User("Mauricio","castro@test.com","castro");

        given()
                .body(user)
                .when()
                .post(String.format("%s/register",resourcePath))
                .then()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(406);

    }

    @Test
    public void Test_00Creat_User_Successful(){

        User user = new User("Pepe", generateRandomEmail(),"cristancho");

        System.out.println("Test email" + user.getEmail());

        given()
                .body(user)
                .when()
                .post(String.format("%s/register",resourcePath))
                .then()
                .body("message", equalTo("Successfully registered"))
                .and()
                .statusCode(200);
    }
}
