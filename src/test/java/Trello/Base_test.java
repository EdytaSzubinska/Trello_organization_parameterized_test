package Trello;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class Base_test {

    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String ORGANIZATIONS = "organizations";

    protected static String KEY = "KEY";
    protected static String TOKEN = "TOKEN";

    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;

    @BeforeAll
    public static void beforeAll() {

        KEY = System.getProperty("KEY");
        TOKEN = System.getProperty("TOKEN");

        reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("KEY", KEY);
        reqBuilder.addQueryParam("TOKEN", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);

        reqSpec = reqBuilder.build();
    }
}
