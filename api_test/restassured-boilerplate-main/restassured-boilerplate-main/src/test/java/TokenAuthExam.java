import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Post;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


public class TokenAuthExam extends BaseTest {

    private static Integer n_Comment = 0;
    private static Integer n_Post= 0;
    private static String resourcePath   = "/v1/post";


    @BeforeGroups ("crete_post")
    public static Integer newPostDetails(){

        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePath);


        JsonPath jsonPathEvaluator = response.jsonPath();
        n_Post = jsonPathEvaluator.get("id");

        return n_Post;
    }

    //Elaboración de las pruebas - Validaciónes a, b y c

    @Test
    public void A_Get_CreatePost_Success(){

        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePath)
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void B_Get_CreatePost_Negative(){

        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateToken())
                .post(resourcePath)
                .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void C_Get_CreatePost_Security(){

        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateFakeToken())
                .body(testPost)
                .post(resourcePath)
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void A_Get_AllPost_Success(){

        given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath + "s")
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test
    public void A_Get_AllPost_Schema(){

        Response response = given()

                .spec(RequestSpecs.generateToken())
                .get(resourcePath + "s");
        assertThat(response.asString(), matchesJsonSchemaInClasspath("posts.schema.json"));
    }

    @Test
    public void B_Get_AllPost_Negative(){

        given()
                .spec(RequestSpecs.generateToken())
                .post(resourcePath + "s")
                .then()
                .statusCode(404)
                .spec(ResponseSpecs.WebSpec());
    }

    @Test
    public void C_Get_AllPost_Security() {

        given()
                .spec(RequestSpecs.generateFakeToken())
                .get(resourcePath + "s")
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void A_Get_onePost_Success(){

       given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath + "/" + n_Post.toString())
                .then()
                .statusCode(200)
                .assertThat().body("data.id", equalTo(n_Post.intValue()))
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void B_Get_onePost_Negative(){

        Integer PosCount = n_Post + 1;

        given()
                .spec(RequestSpecs.generateToken())
                .get(resourcePath + "/" + PosCount.toString())
                .then()
                .statusCode(404)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void C_Get_onePost_Security(){

        given()
                .spec(RequestSpecs.generateFakeToken())
                .get(resourcePath + "/" + n_Post.toString())
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void A_Get_updatePost_Success(){

        Post updPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateToken())
                .body(updPost)
                .put(resourcePath + "/" + n_Post.toString())
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void B_Get_updatePost_Negative(){

        Post updPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateToken())
                .put(resourcePath + "/" + n_Post.toString())
                .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void C_Get_updatePost_Security(){

        Post updPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateFakeToken())
                .body(updPost)
                .put(resourcePath + "/" + n_Post.toString())
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());

    }

    @Test
    public void A_Get_deletePost_Success(){

       given()
                .spec(RequestSpecs.generateToken())
                .put(resourcePath + "/" + n_Post.toString())
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void B_Get_deletePost_Negative(){

        given()
                .spec(RequestSpecs.generateToken())
                .put(resourcePath + "/" + n_Post.toString())
                .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void C_Get_deletePost_Security(){

        given()
                .spec(RequestSpecs.generateFakeToken())
                .put(resourcePath + "/" + n_Post.toString() )
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }


    @Test(groups = "create_article")
    public void Test_Articles_Schema(){
        Response response = given()
                .spec(RequestSpecs.generateToken())
            .get(resourcePath + "s");

        assertThat(response.asString(), matchesJsonSchemaInClasspath("articles.schema.json"));
        assertThat(response.path("results[0].data[0].id"),equalTo(802));
    }

}
