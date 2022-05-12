package Utils;

import static io.restassured.RestAssured.*;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;

import static org.hamcrest.Matchers.*;

import org.junit.BeforeClass;

public class BaseTest implements Constantes {
    @BeforeClass
    public static void setup(){
        baseURI = BASE_URL;

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setContentType(BASE_CONTENT_TYPE);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectResponseTime(lessThanOrEqualTo(MAX_TIMEOUT));
        responseSpecification = responseSpecBuilder.build();

        enableLoggingOfRequestAndResponseIfValidationFails();
    }


}
