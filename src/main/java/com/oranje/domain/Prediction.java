package com.oranje.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Prediction.
 */

@Document(collection = "prediction")
public class Prediction implements Serializable {

    @Id
    private String id;

    @Field("username")
    private String username;

    @Field("points")
    private Integer points;

    @Field("correct_scores")
    private Integer correctScores;

    @Field("correct_results")
    private Integer correctResults;

    @Field("avatar")
    private String avatar;

    private Map<String,HomeAwayScore> resultPerEvent;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPoints() {
    	if (points ==null){
    		points = 0;
    	}
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getCorrectScores() {
    	if (correctScores == null) {
    		correctScores = 0;
    	}
        return correctScores;
    }

    public void setCorrectScores(Integer correctScores) {
        this.correctScores = correctScores;
    }

    public Integer getCorrectResults() {
    	if (correctResults == null) {
    		correctResults = 0;
    	}
        return correctResults;
    }

    public void setCorrectResults(Integer correctResults) {
        this.correctResults = correctResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prediction prediction = (Prediction) o;
        if(prediction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, prediction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Prediction{" +
            "id=" + id +
            ", username='" + username + "'" +
            ", points='" + points + "'" +
            ", correctScores='" + correctScores + "'" +
            ", correctResults='" + correctResults + "'" +
            '}';
    }

	public Map<String,HomeAwayScore> getResultPerEvent() {
		return resultPerEvent;
	}

	public void setResultPerEvent(Map<String,HomeAwayScore> resultPerEvent) {
		this.resultPerEvent = resultPerEvent;
	}

	public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
