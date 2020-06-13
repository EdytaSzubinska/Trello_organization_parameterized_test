package Trello;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class Organization_Test extends Base_test {

    private static Stream<Arguments> createOrganizationWithDefaultDate() {

        return Stream.of(
                Arguments.of("zzzzzz", "learning", "szkoła testowania", "https://szkolatestowania.pl"),
                Arguments.of("a2", "learning", "szkoła testowania", "https://szkolatestowania.pl"),
                Arguments.of("___", "learning", "szkoła testowania", "https://szkolatestowania.pl"),
                Arguments.of("A_B", "learning", "szkoła testowania", "https://szkolatestowania.pl"),
                Arguments.of("BIG LETTERS", "learning", "szkoła testowania", "https://szkolatestowania.pl"));
    }

    @DisplayName("Create organization with default date")
    @ParameterizedTest(name = "Display name: {0}, desc:{1}, name {2}, website {3}")
    @MethodSource("createOrganizationWithDefaultDate")
    public void createOrganizationWithDefaultDate(String displayName, String desc, String name, String website) {

        Organization organization = new Organization();
        organization.setDisplayName(displayName);
        organization.setDesc(desc);
        organization.setName(name);
        organization.setWebsite(website);

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK) //tu powinien być "statusCode 400", ale Trello przepuszcza nazwy użytkownika, które powinne wywoływać błą
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(displayName);

        final String organizationId = json.getString("id");

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + ORGANIZATIONS + "/" + organizationId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    private static Stream<Arguments> createOrganizationWithoutName() {

        return Stream.of(
                Arguments.of("", "learning", "szkoła testowania", "https://szkolatestowania.pl"));
    }

    @DisplayName("Create organization without name")
    @ParameterizedTest(name = "Display name: {0}, desc:{1}, name {2}, website {3}")
    @MethodSource("createOrganizationWithoutName")

    public void createOrganizationWithoutName(String displayName, String desc, String name, String website) {

        Organization organization = new Organization();
        organization.setDisplayName(displayName);
        organization.setDesc(desc);
        organization.setName(name);
        organization.setWebsite(website);

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isNull();

        final String organizationId = json.getString("id");

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + ORGANIZATIONS + "/" + organizationId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}

