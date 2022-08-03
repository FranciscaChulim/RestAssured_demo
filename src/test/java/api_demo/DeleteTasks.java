package api_demo;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.testng.Assert;

import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

public class DeleteTasks {

    //Use valid task ids from Todoist 
    @DataProvider (name="taskToDelete")
    public static Object[][] taskToDelete(){
        return new Object[][] {
            { 6025637305L },
            { 6025644877L }
        };
    }

    @Test (dataProvider = "taskToDelete")
    public void deleteTask_dataProvider(Long id){

        Response response =
        given().
            header("Content-Type", "application/json").
            //replace "myTodoisToken" with your Todoist Token 
            header("Authorization", "Bearer myTodoisToken").
            pathParam("id", id).
        when().
            delete("https://api.todoist.com/rest/v1/tasks/{id}").
        then().
            extract().response();
        Assert.assertEquals(response.statusCode(),204);
    }
}
