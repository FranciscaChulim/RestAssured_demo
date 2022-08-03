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
            //replace "myTodoisToken" with your Todoist Token 
            addHeader("Authorization", "Bearer myTodoisToken").
            addHeader("Content-Type", "application/json").
            build();
            
    }

    @org.testng.annotations.BeforeClass
    public static void createResponseSpecification(){
        
        responseSpect = new ResponseSpecBuilder().
            expectContentType(ContentType.JSON).
            build();
    }
    
        
    @Test(priority = 1)
    public void getProjects_checkStatusCodeAndResponseSize()
    {
        Response response =
            given().
                spec(requestSpect).
            when().
                get("/projects").
            then().
                spec(responseSpect).
                log().body().
                extract().response();

        Boolean containsInboxProject = response.body().jsonPath().getList("name").contains("Inbox");
        
        Assert.assertEquals(containsInboxProject, true);
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.contentType(),"application/json");
    }

    @Test(priority = 2)
    public void createProject_checkStatusCodeAndResponseContent()
    {
        //serialization
        Project project = new Project();
        project.setName("Demo project");

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

        Assert.assertEquals(jp.get("name"),"Demo project");
        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(response.contentType(),"application/json");
    }

    @Test(priority = 3)
    public void getAProject_checkStatusCodeAndResponseContent()
    {
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

        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(jp.get("name"),"Demo project");
        Assert.assertEquals(jp.get("favorite"),true);
    }

    @Test(priority = 4)
    public void updateProject_checkStatusCode()
    {
        Project project = new Project();
        project.setName("Assured update_Project");
        
        Response response =
            given().
                spec(requestSpect).
                body(project).
            when().
                post("/projects/{projectId}",projectId).
            then().
                extract().response();

        Assert.assertEquals(response.statusCode(),204);

    }

    @Test(priority = 5)
    public void deleteProject_checkStatusCode()
    {
        Response response =
            given().
                spec(requestSpect).
            when().
                delete("/projects/{projectId}",projectId).
            then().
                extract().response();

        Assert.assertEquals(response.statusCode(),204);

    }

    @Test(priority = 6)
    public void createProject_negativeScenario_badRequest()
    {
        String body = """
                {
                    "color": "green",
                    "projectName": "Project Rest Assured_2.1" 
                }
                """;

        Response response =
            given().
                spec(requestSpect).
                body(body).
            when().
                post("/projects").
            then().
                log().body().
                extract().response();
        Assert.assertEquals(response.statusCode(),500);
        Assert.assertEquals(response.print(), "Internal Server Error");
    }

}