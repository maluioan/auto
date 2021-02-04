package com.home.automation.homeboard.data;

import java.time.LocalDateTime;

// TODO: put all common data stuff in a single module care sa fie folosit de toate serviciile
public class BaseData {

    private Long id;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
