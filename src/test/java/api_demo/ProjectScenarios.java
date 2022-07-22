package api_demo;

import org.testng.annotations.Test;
import org.testng.Assert;

import dataentities.Project;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;

public class ProjectScenarios {
    
    private static RequestSpecification requestSpect;
    private static ResponseSpecification responseSpect;
    public Long projectId;

    @org.testng.annotations.BeforeClass
    public static void createRequestSpecification(){
        
        requestSpect = new RequestSpecBuilder().
            setBaseUri("https://api.todoist.com/rest/v1").
            addHeader("Authorization", "Bearer 597d34dad7b067299872ed73e0ec280e975d03f1").
            addHeader("Content-Type", "application/json").
            build();
            
    }

    @org.testng.annotations.BeforeClass
    public static void createResponseSpecification(){
        
        responseSpect = new ResponseSpecBuilder().
            expectContentType(ContentType.JSON).
            build();
    }
    
    @Test
    public void getProjects_checkStatusCodeAndResponseSize()
    {
        Response response =
        given().
            spec(requestSpect).
        when().
            get("/projects").
        then().
            spec(responseSpect).
            extract().response();

        int responseLength = response.body().jsonPath().getList("id").size();
        System.out.println(" The response size is = "+ responseLength);

        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals("application/json", response.contentType());
        //Assert.assertEquals(2, responseLength);
    }

    @Test
    public void createProject_checkStatusCodeAndResponseContent()
    {
        //serialization
        Project project = new Project();
        project.setName("Name project 1");

        Response response =
            given().
                spec(requestSpect).
                body(project).
                log().body().
            when().
                post("/projects").
            then().
                spec(responseSpect).
                extract().response();

        String json = response.asString();
        JsonPath jp = new JsonPath(json);

        projectId = response.body().jsonPath().get("id");
        project.setId(projectId);
        System.out.println(" projectId = "+ projectId);
        Assert.assertEquals("Name project 1", jp.get("name"));
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals("application/json", response.contentType());
    }

    @Test
    public void getAProject_checkStatusCodeAndResponseContent()
    {
        Project project = new Project();
        projectId=project.getId();
        Response response =
            given().
                spec(requestSpect).
            when().
                get("/projects/{projectId}",projectId).
            then().
                spec(responseSpect).
                extract().response();

        String json = response.asString();
        JsonPath jp = new JsonPath(json);

        Assert.assertEquals("Demo Project", jp.get("name"));
    }

    @Test
    public void updateProject_checkStatusCode()
    {
        Project project = new Project();
        projectId=project.getId();
        project.setName("Assured update_Project");
        
        Response response =
            given().
                spec(requestSpect).
                body(project).
            when().
                post("/projects/{projectId}",projectId).
            then().
                extract().response();

        Assert.assertEquals(204, response.statusCode());

    }

    @Test
    public void deleteProject_checkStatusCode()
    {
        Project project = new Project();
        projectId=project.getId();
        
        Response response =
            given().
                spec(requestSpect).
            when().
                delete("/projects/{projectId}",projectId).
            then().
                extract().response();

        Assert.assertEquals(204, response.statusCode());

    }

    private static String payloadCreate = "{\n" +
        "  \"color\": \"green\",\n" +
        "  \"content\": \"Project Rest Assured_2.1\"\n" +
        "}";

    @Test
    public void createProject_negativeScenario_badRequest()
    {

        Response response =
            given().
                spec(requestSpect).
                body(payloadCreate).
            when().
                post("/projects").
            then().
                extract().response();
                
        Assert.assertEquals(400, response.statusCode());
        Assert.assertEquals("Bad Request", "Bad Request");
    }

}