import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Comment;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


public class basicAuthExam extends BaseTest {

    private static Integer n_Comment = 0;
    private static Integer n_Post= 0;
    private static String resourcePath   = "/v1/comment";


    public static void newComment (int postid){

        Comment testComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Response response = given()
                .spec(RequestSpecs.generateBasicAuth())
                .body(testComment)
                .post(resourcePath + "/" + postid);

        JsonPath jsonPathEvaluator = response.jsonPath();
        n_Comment = jsonPathEvaluator.get("id");
    }


    @BeforeGroups("crete_comment")
    public static Integer NewPostorNull() {

        n_Post = TokenAuthExam.newPostDetails();
        return n_Post;
    }

    @BeforeGroups("crete_comment")
    public static Integer NewPostNotNull() {

        n_Post = TokenAuthExam.newPostDetails();
        newComment(n_Post.intValue());
        return n_Comment;
    }

    //Elaboración de las pruebas - Validaciónes a, b y c

    @Test
    public void A_Get_CreateComment_Success(){

        Comment testComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .body(testComment)
                .post(resourcePath + "/" + n_Post.toString())
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void B_Get_CreateComment_Negative(){

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .post(resourcePath + "/" + n_Post.toString())
                .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void C_Get_CreateComment_Security(){

        Comment testComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        given()
                .spec(RequestSpecs.generateFakeBasicAuth())
                .body(testComment)
                .post(resourcePath + "/" + n_Post.toString())
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void A_Get_AllComment_Success(){

        //n_Comment(n_Post.intValue());

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .get(resourcePath + "s/" + n_Post.toString())
                .then()
                .statusCode(200)
                .assertThat().body("All_Comments_[0]", equalTo(1))
                .spec(ResponseSpecs.defaultSpec());

    }


    @Test
    public void A_Get_AllComment_Schema(){

        Response response = given()

                .spec(RequestSpecs.generateBasicAuth())
                .get(resourcePath + "s/" + n_Post.toString());

        assertThat(response.asString(), matchesJsonSchemaInClasspath("comments.schema.json"));
    }

    @Test
    public void B_Get_AllComment_Negative(){

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .post(resourcePath + "s/" + n_Post.toString())
                .then()
                .statusCode(404)
                .spec(ResponseSpecs.WebSpec());
    }

    @Test
    public void C_Get_AllComment_Security() {

        given()
                .spec(RequestSpecs.generateFakeBasicAuth())
                .get(resourcePath + "s/" + n_Post.toString())
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test
    public void A_Get_oneComment_Success(){

       given()
                .spec(RequestSpecs.generateBasicAuth())
                .get(resourcePath + "/" + n_Post.toString() + "/" + n_Comment.toString())
                .then()
                .statusCode(200)
                .assertThat().body("data.id", equalTo(n_Comment.intValue()))
                .assertThat().body("data.post_id", equalTo(n_Post.toString()))
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void B_Get_oneComment_Negative(){

        Integer ComCount = n_Comment + 1;

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .get(resourcePath + "/" + n_Post.toString() + "/" + ComCount.toString())
                .then()
                .statusCode(404)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void C_Get_oneComment_Security(){

        given()
                .spec(RequestSpecs.generateFakeBasicAuth())
                .get(resourcePath + "/" + n_Post.toString() + "/" + n_Comment.toString())
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test
    public void A_Get_updateComment_Success(){

        Comment updComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .body(updComment)
                .put(resourcePath + "/" + n_Post.toString() + "/" + n_Comment.toString())
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void B_Get_updateComment_Negative(){

        Comment updComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        Integer ComCount = n_Comment + 1;

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .body(updComment)
                .put(resourcePath + "/" + n_Post.toString() + "/" + ComCount.toString())
                .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void C_Get_updateComment_Security(){


        Comment updComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomComment());

        given()
                .spec(RequestSpecs.generateFakeBasicAuth())
                .body(updComment)
                .put(resourcePath + "/" + n_Post.toString() + "/" + n_Comment.toString())
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test
    public void A_Get_deleteComment_Success(){

       given()
                .spec(RequestSpecs.generateBasicAuth())
                .put(resourcePath + "/" + n_Post.toString() + "/" + n_Comment.toString())
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void B_Get_deleteComment_Negative(){

        Integer ComCount = n_Comment + 1;

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .put(resourcePath + "/" + n_Post.toString() + "/" + ComCount.toString())
                .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void C_Get_deleteComment_Security(){

        given()
                .spec(RequestSpecs.generateFakeBasicAuth())
                .put(resourcePath + "/" + n_Post.toString() + "/" + n_Comment.toString())
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());

    }


   /* @Test(groups = "create_article")
    public void Test_Articles_Schema(){
        Response response = given()
                .spec(RequestSpecs.generateToken())
            .get(resourcePath + "s");

        assertThat(response.asString(), matchesJsonSchemaInClasspath("articles.schema.json"));
        assertThat(response.path("results[0].data[0].id"),equalTo(802));
    }*/

}
