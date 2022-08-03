package dataentities;

import com.fasterxml.jackson.annotation.*;

@JsonPropertyOrder({"id","name", "color", "favorite"})
public class Project {

    private Long id;
    private String name;
    private Integer color;
    private Boolean favorite;

    public Project(){

        this.name="New Project";
        this.color=39;
        this.favorite=true;
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
}