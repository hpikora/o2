import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CatFact {
    private static RequestSpecification requestSpec;

    @BeforeClass
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("https://catfact.ninja").
                build();
    }

    @Test
    public void oneFactFoundTestCase() {
        given().spec(requestSpec).
                when().get("/fact?max_length=30").
                then().log().body().
                assertThat().
                    statusCode(200).contentType(ContentType.JSON).
                    body("fact.length()", lessThanOrEqualTo(30)).
                    body("length", greaterThan(0));
    }

    @Test
    public void noFactFoundWithStringInputTestCase() {
        given().spec(requestSpec).
                when().get("/fact?max_length=aaa").
                then().log().body().
                assertThat().statusCode(404);
    }

    @Test
    public void randomLengthFactWithNoInputParameterFoundTestCase() {
        given().spec(requestSpec).
                when().get("/fact").
                then().log().body().
                assertThat().statusCode(200).contentType(ContentType.JSON).
                body("fact", isA(String.class)).
                body("length", isA(Integer.class));
    }

    @Test
    public void nFactsFoundTestCase() {
        given().spec(requestSpec).
                when().get("/facts?max_length=50").
                then().log().body().
                assertThat().
                    statusCode(200).contentType(ContentType.JSON).
                    body("data", not(emptyArray())).
                    body("data[0].fact", isA(String.class)).
                    body("data[0].length", isA(Integer.class));
    }

    @Test
    public void threeFactsFoundTestCase() {
        given().spec(requestSpec).
                when().get("/facts?max_length=50&limit=3").
                then().log().body().
                assertThat().
                    statusCode(200).contentType(ContentType.JSON).
                    body("data", hasSize(3));
    }

    @Test
    public void randomNumberFactsFoundTestCase() {
        given().spec(requestSpec).
                when().get("/facts").
                then().log().body().
                assertThat().
                statusCode(200).contentType(ContentType.JSON).
                body("data", not(emptyArray()));
    }
}
