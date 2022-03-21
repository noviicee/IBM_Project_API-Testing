package IBM_Project_API_Testing.IBM_Project_API_Testing;

import static io.restassured.RestAssured.given;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import POJO.pojoClass;
import io.restassured.http.ContentType;

public class API_Testing {

	@Test
	public void createUser(ITestContext val) throws JsonProcessingException {

		try {
			io.restassured.RestAssured.baseURI = "https://petstore.swagger.io/v2";

			pojoClass pojo = new pojoClass();

			pojo.setUsername("robot");
			pojo.setFirstName("mr");
			pojo.setEmail("mr@robot.com");
			pojo.setPassword("iamrobot");
			pojo.setPhone("9876543210");
			pojo.setUserStatus("0");

			ObjectMapper objmapper = new ObjectMapper();

			given()
				.contentType(ContentType.JSON)
				.body(objmapper.writeValueAsString(pojo)).
			when()
				.post("/user").
			then()
				.statusCode(200);
				//.log().all();
		
		String username=pojo.getUsername();
		val.setAttribute("username", username);
		//System.out.println(username);
		
		String passwd=pojo.getPassword();
		val.setAttribute("pasword", passwd);	
		//System.out.println(passwd);
		
		} catch (Exception ex) {
			System.out.println("The 'Create User' method failed.");
			// System.out.println("The exception message was:"+ex);
		}
	}

	@Test(dependsOnMethods = "createUser")
	public void modifyUser(ITestContext val) {

		try {
			io.restassured.RestAssured.baseURI = "https://petstore.swagger.io/v2";

			JSONObject userDetails = new JSONObject();
			userDetails.put("username", "robo2");
			userDetails.put("firstName", "miss");
			userDetails.put("lastName", "robo2");
			userDetails.put("email", "miss@robo2.com");
			userDetails.put("password", "iamrobo2");
			userDetails.put("phone", "9182736450");
			userDetails.put("userstatus", "1");

			given()
				.contentType(ContentType.JSON)
				.body(userDetails.toJSONString()).
			when()
				.put("/user/" + val.getAttribute("username")).
			then()
				.statusCode(200);
				//.log().all();

		} catch (Exception ex) {
			System.out.println("The 'Modify User' method failed.");
			// System.out.println("The exception message was:"+ex);
		}
	}

	@Test(dependsOnMethods = "modifyUser")
	public void login(ITestContext val) {
		try {
			io.restassured.RestAssured.baseURI = "https://petstore.swagger.io/v2";

			given()
				.queryParam("username", val.getAttribute("username"))
				.get("/user/login").
			then()
				.statusCode(200);
				//.log().all();
			
		} catch (Exception ex) {
			System.out.println("The LOGIN method failed.");
			// System.out.println("The exception message was:"+ex);
		}
	}

	@Test(dependsOnMethods = "login")
	public void logout(ITestContext val) {
		try {
			io.restassured.RestAssured.baseURI = "https://petstore.swagger.io/v2";

			given()
				.get("/user/logout").
			then()
				.statusCode(200);
				//.log().all();
			
		} catch (Exception ex) {
			System.out.println("The LOGOUT method failed.");
			// System.out.println("The exception message was:"+ex);
		}
	}

	@Test(dependsOnMethods = "logout")
	public void delete(ITestContext val) {

		try {
			io.restassured.RestAssured.baseURI = "https://petstore.swagger.io/v2";

			String username = val.getAttribute("username").toString();

			given()
				.delete("/user/" + username).
			then()
				.statusCode(200);
				//.log().all();
			
		} catch (AssertionError ex) {
			System.out.println("The DELETE method failed.");
			// System.out.println("The exception message was:"+ex);
		}
	}

}