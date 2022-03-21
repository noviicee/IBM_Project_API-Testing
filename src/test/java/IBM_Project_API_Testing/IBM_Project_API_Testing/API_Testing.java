package IBM_Project_API_Testing.IBM_Project_API_Testing;

import static io.restassured.RestAssured.given;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import POJO.pojoClass;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class API_Testing {

	@Test
	public void createUser(ITestContext val) throws JsonProcessingException {
		io.restassured.RestAssured.baseURI = "https://petstore.swagger.io/v2";

		pojoClass pojo = new pojoClass();

		pojo.setId("0");
		pojo.setUsername("robot");
		pojo.setFirstName("mr");
		pojo.setEmail("mr@robot.com");
		pojo.setPassword("iamrobot");
		pojo.setPhone("9876543210");
		pojo.setUserStatus("0");

		ObjectMapper objmapper = new ObjectMapper();

		Response resp = given().contentType(ContentType.JSON).body(objmapper.writeValueAsString(pojo)).when()
				.post("/user").then().statusCode(200).extract().response();

		String username = resp.jsonPath().getString("username");
		val.setAttribute("username", username);

		String passwd = resp.jsonPath().getString("password");
		val.setAttribute("pasword", passwd);

	}

	@Test(dependsOnMethods = "createUser")
	public void modifyUser(ITestContext val) {
		io.restassured.RestAssured.baseURI = "https://petstore.swagger.io/v2";

		JSONObject userDetails = new JSONObject();
		userDetails.put("username", "robo2");
		userDetails.put("firstName", "miss");
		userDetails.put("lastName", "robo2");
		userDetails.put("email", "miss@robo2.com");
		userDetails.put("password", "iamrobo2");
		userDetails.put("phone", "9182736450");
		userDetails.put("userstatus", "1");

		given().contentType(ContentType.JSON).body(userDetails.toJSONString()).when()
				.put("/user/" + val.getAttribute("username")).then().statusCode(200).log().all();

	}

	@Test(dependsOnMethods = "login")
	public void logout(ITestContext val) {
		io.restassured.RestAssured.baseURI = "https://petstore.swagger.io/v2";

		given().queryParam("username", val.getAttribute("username")).get("/user/login").then().statusCode(200).log()
				.all();
	}

	@Test(dependsOnMethods = "modifyUser")
	public void login(ITestContext val) {
		io.restassured.RestAssured.baseURI = "https://petstore.swagger.io/v2";

		given().get("/user/logout").then().statusCode(200).log().all();
	}

	@Test(dependsOnMethods = "logout")
	public void delete(ITestContext val) {
		io.restassured.RestAssured.baseURI = "https://petstore.swagger.io/v2";

		String username = val.getAttribute("username").toString();

		given().delete("/user/" + username).then().statusCode(200).log().all();
	}

}
