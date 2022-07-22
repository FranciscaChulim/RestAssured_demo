package dataentities;

import com.fasterxml.jackson.annotation.*;

@JsonPropertyOrder({"id","content", "description", "priority", "project"})
public class Task {
    
    private Long id;
    private String content;
    private String description;
    private Integer priority;

    public Task(){
        
        this.id = 6025608120L;
        this.content="New Task";
        this.description="This is a new task";
        this.priority=2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
}
