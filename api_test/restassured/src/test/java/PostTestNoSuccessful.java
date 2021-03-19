import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Post;
import model.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import specifications.RequestSpecs;

import static helpers.DataHelper.generateRandomEmail;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class PostTestNoSuccessful extends BaseTest {

    private static String resourcePath = "/v1/post";
    private static String idPost  = "";

    @BeforeGroups(groups = {"postDelete","postCreate"})
    public String checkPost() {

        Response jsonData = given().spec(RequestSpecs.generateToken()).when().get(String.format("%ss",resourcePath));
        JsonPath jsonPathEvaluator = jsonData.jsonPath();
        idPost = jsonPathEvaluator.get("results[0].data[0].id").toString();

        return idPost;
    }

    @Test
    public void Test_31Creat_Post_Unsuccessful(){

        Post post = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        System.out.println("Test title ->  " + post.getTitle());
        System.out.println("Test content ->  " + post.getContent());

        given()
                .spec(RequestSpecs.generateToken())
                .body(post)
                .when()
                .post(resourcePath + "s")
                .then()
                .and()
                .statusCode(404);
    }

    @Test
    public void Test_32Call_Post_Unsuccessful_All(){

        given()
                .spec(RequestSpecs.generateBasicAccess())
                .when()
                .get(String.format("%ss",resourcePath))
                .then()
                .and()
                .statusCode(401);
    }

    @Test (groups = "postCreate")
    public void Test_33Call_Post_Unsuccessful_One(){

        given()
                .spec(RequestSpecs.generateToken())
                .when()
                .get(resourcePath + "/s/" + idPost)
                .then()
                .and()
                .statusCode(404);
    }

    @Test (groups = "postCreate")
    public void Test_34Update_Post_Unsuccessful(){

        Post post = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateToken())
                .body(post)
                .when()
                .put(resourcePath + "/s/" + idPost)
                .then()
                .statusCode(404);
    }

    @Test (groups = "postCreate")
    public void Test_35Delete_Post_Unsuccessful(){


        given()
                .spec(RequestSpecs.generateToken())
                .when()
                .delete(resourcePath + "/s/" + idPost)
                .then()
                .statusCode(404);
    }

}