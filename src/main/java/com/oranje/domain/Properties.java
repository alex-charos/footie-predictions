package com.oranje.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Properties.
 */

@Document(collection = "properties")
public class Properties implements Serializable {

    @Id
    private String id;

    @Field("display_standings")
    private Boolean displayStandings;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getDisplayStandings() {
        return displayStandings;
    }
    
    public void setDisplayStandings(Boolean displayStandings) {
        this.displayStandings = displayStandings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Properties properties = (Properties) o;
        if(properties.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, properties.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Properties{" +
            "id=" + id +
            ", displayStandings='" + displayStandings + "'" +
            '}';
    }
}
