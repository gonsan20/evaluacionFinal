import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Comment;
import model.User;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import static helpers.DataHelper.generateRandomEmail;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class CommentTest extends BaseTest {

    private static String resourcePath = "/v1/comment";
    private static String idPost  = "";
    private static Integer idComment  = 0;

    @BeforeClass
    public void updateStaticVariables(){
        PostTest post = new PostTest();
        idPost = post.checkPost();
    }

    @AfterGroups(groups = "postDelete")
    @BeforeGroups(groups = {"postDelete","postCreate"})
    public void checkComment() {
        Comment comment = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        Response response = given()
                .spec(RequestSpecs.generateBasicAccess())
                .body(comment)
                .post(resourcePath + "/"+ idPost);

        JsonPath jsonPathEvaluator = response.jsonPath();
        idComment = jsonPathEvaluator.get("id");
    }


    @Test
    public void Test_01Creat_Comment_Successful(){

        Comment comment = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateBasicAccess())
                .body(comment)
                .when()
                .post(resourcePath+"/"+ idPost)
                .then()
                .body("message", equalTo("Comment created"))
                .and()
                .statusCode(200);
    }


    @Test(groups = "postCreate")
    public void Test_02Call_Comment_Successful_All(){

        given()
                .spec(RequestSpecs.generateBasicAccess())
                .when()
                .get(resourcePath + "s/" + idPost)
                .then()
                .and()
                .statusCode(200);
    }

    @Test(groups = "postCreate")
    public void Test_03Call_Comment_Successful_One(){

        given()
                .spec(RequestSpecs.generateBasicAccess())
                .when()
                .get(resourcePath + "/" + idPost + "/" + idComment)
                .then()
                .and()
                .statusCode(200);
    }

    @Test(groups = "postCreate")
    public void Test_04Update_Comment_Successful(){

        Comment comment = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateBasicAccess())
                .body(comment)
                .when()
                .put(resourcePath + "/" + idPost + "/" + idComment)
                .then()
                .and()
                .statusCode(200);
    }

    @Test(groups = "postCreate")
    public void Test_05Delete_Comment_Successful(){

        given()
                .spec(RequestSpecs.generateBasicAccess())
                .when()
                .delete(resourcePath + "/" + idPost + "/" + idComment)
                .then()
                .body("message", equalTo("Comment deleted"))
                .and()
                .statusCode(200);
    }

}