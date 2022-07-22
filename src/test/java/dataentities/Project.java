package dataentities;
//import java.util.ArrayList;
//import java.util.List;

import com.fasterxml.jackson.annotation.*;

@JsonPropertyOrder({"id","name", "color", "favorite"})//, "tasks"
public class Project {

    private Long id;
    private String name;
    private Integer color;
    private Boolean favorite;
    //private List<Task> tasks;

    public Project(){

        this.id=2295237117L;
        this.name="New Project";
        this.color=39;
        this.favorite=true;
    /* 
        Task task = new Task();
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        this.tasks = tasks;*/
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
    /* 
    public List<Task> getProjects() {
        return tasks;
    }

    public void setProjects(List<Task> tasks) {
        this.tasks = tasks;
    }*/
}