import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Post;
import model.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import static helpers.DataHelper.generateRandomEmail;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class PostTest extends BaseTest {

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
    public void Test_11Creat_Post_Successful(){

        Post post = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        System.out.println("Test title ->  " + post.getTitle());
        System.out.println("Test content ->  " + post.getContent());

        given()
                .spec(RequestSpecs.generateToken())
                .body(post)
                .when()
                .post(resourcePath)
                .then()
                .and()
                .statusCode(200);
    }

    @Test
    public void Test_12Call_Post_Successful_All(){

        given()
                .spec(RequestSpecs.generateToken())
                .when()
                .get(String.format("%ss",resourcePath))
                .then()
                .and()
                .statusCode(200);
    }

    @Test (groups = "postCreate")
    public void Test_13Call_Post_Successful_One(){

        given()
                .spec(RequestSpecs.generateToken())
                .when()
                .get(resourcePath + "/" + idPost)
                .then()
                .and()
                .statusCode(200);
    }

    @Test (groups = "postCreate")
    public void Test_14Update_Post_Successful(){

        Post post = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateToken())
                .body(post)
                .when()
                .put(resourcePath + "/" + idPost)
                .then()
                .statusCode(200);
    }

    @Test (groups = "postCreate")
    public void Test_15Delete_Post_Successful(){


        given()
                .spec(RequestSpecs.generateToken())
                .when()
                .delete(resourcePath + "/" + idPost)
                .then()
                .body("message", equalTo("Post deleted"))
                .and()
                .statusCode(200);
    }

}