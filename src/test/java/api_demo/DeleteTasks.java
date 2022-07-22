package api_demo;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.testng.Assert;

import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

public class DeleteTasks {

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
            header("Authorization", "Bearer 597d34dad7b067299872ed73e0ec280e975d03f1").
            pathParam("id", id).
        when().
            delete("https://api.todoist.com/rest/v1/tasks/{id}").
        then().
            extract().response();
        Assert.assertEquals(204, response.statusCode());
    }
}
