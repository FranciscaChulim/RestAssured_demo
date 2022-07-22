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
            log().body().
            extract().response();

        int responseLength = response.body().jsonPath().getList("id").size();
        System.out.println(" Task response size is = "+ responseLength);

        Assert.assertEquals(200, response.statusCode());
    }

    @Test(priority = 2)
    public void getAnActiveTasks_checkStatusCode()
    {
        Task task = new Task();
        taskId=task.getId();
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

        Assert.assertEquals("This is a new task", jp.get("description"));
        Assert.assertEquals(200, response.statusCode());

    }

    @Test(priority = 3)
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

        Assert.assertEquals("Assured last test", jp.get("content"));
        Assert.assertEquals(200, response.statusCode());

    }

    @Test(priority = 4)
    public void updateTasks_checkStatusCode()
    {
       
        Task task = new Task();
        taskId=task.getId();
        task.setContent("Assured update_Task");
        
        Response response =
            given().
                spec(requestSpect).
                body(task).
            when().
                post("/tasks/{taskId}",taskId).
            then().
                extract().response();

        Assert.assertEquals(204, response.statusCode());

    }

    @Test(priority = 5)
    public void deleteTasks_checkStatusCodeAndResponseContent()
    {
        Task task = new Task();
        taskId=task.getId();

        Response response =
            given().
                spec(requestSpect).
            when().
                delete("/tasks/{taskId}",taskId).
            then().
                extract().response();

        Assert.assertEquals(204, response.statusCode());
    }


    private static String payloadCreate = "{\n" +
        "  \"color\": \"39\",\n" +
        "  \"favorite\": true,\n" +
        "  \"name\": \"Project Rest Assured_2.1\"\n" +
        "}";

    @Test
    public void createTasks_negativeScenario_badRequest()
    {
        Response response =
            given().
                spec(requestSpect).
                body(payloadCreate).
            when().
                post("/tasks").
            then().
                log().body().
                extract().response();
                
        Assert.assertEquals(400, response.statusCode());
        Assert.assertEquals("Bad Request", "Bad Request");
    }

}
