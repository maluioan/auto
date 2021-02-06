package com.home.automation.homeboard.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
// https://thorben-janssen.com/complete-guide-inheritance-strategies-jpa-hibernate/
public class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private Boolean active = Boolean.TRUE;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
