package api_demo;

import org.testng.annotations.*;
import org.testng.Assert;

import dataentities.Task;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;

public class TaskScenarios 
{
    private static RequestSpecification requestSpect;
    private static ResponseSpecification responseSpect;
    public Long taskId;

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
    public void getAllActiveTasks_checkStatusCodeAndResponseSize()
    {
        Response response =
            given(). 
                spec(requestSpect).
            when().
                get("/tasks").
            then().
                spec(responseSpect).
                extract().response();

        Assert.assertEquals(response.statusCode(),200);
    }

    @Test(priority = 2)
    public void createTasks_checkStatusCodeAndResponseContent()
    {
        Task task = new Task();
        task.setContent("Assured last test");

        Response response =
            given().
                spec(requestSpect).
                body(task).
            when().
                post("/tasks").
            then().
                spec(responseSpect).
                extract().response();

        String json = response.asString();
        JsonPath jp = new JsonPath(json);

        taskId = response.body().jsonPath().get("id");
        Assert.assertEquals(jp.get("content"),"Assured last test");
        Assert.assertEquals(response.statusCode(),200);

    }

    @Test(priority = 3)
    public void getAnActiveTasks_checkStatusCode()
    {
        Response response =
            given().
                spec(requestSpect).
            when().
                get("/tasks/{taskId}",taskId).
            then().
                spec(responseSpect).
                extract().response();

        String json = response.asString();
        JsonPath jp = new JsonPath(json);

        Assert.assertEquals(jp.get("description"),"This is a new task");
        Assert.assertEquals(response.statusCode(),200);

    }

    @Test(priority = 4)
    public void updateTasks_checkStatusCode()
    {
       
        Task task = new Task();
        task.setContent("Assured update_Task");
        
        Response response =
            given().
                spec(requestSpect).
                body(task).
            when().
                post("/tasks/{taskId}",taskId).
            then().
                extract().response();

        Assert.assertEquals(response.statusCode(),204);

    }

    @Test(priority = 5)
    public void deleteTasks_checkStatusCodeAndResponseContent()
    {
        Response response =
            given().
                spec(requestSpect).
            when().
                delete("/tasks/{taskId}",taskId).
            then().
                extract().response();

        Assert.assertEquals(response.statusCode(),204);
    }

    @Test(priority = 6)
    public void createTasks_negativeScenario_badRequest()
    {
        String body = """
                {
                    "color": 39,
                    "favorite": true,
                    "name": "Project Rest Assured_2.1"
                }
                """;

        Response response =
            given().
                spec(requestSpect).
                body(body).
            when().
                post("/tasks").
            then().
                log().body().
                extract().response();
        
        Assert.assertEquals(response.statusCode(),500);
        Assert.assertEquals(response.print(), "Internal Server Error");
    }
}
