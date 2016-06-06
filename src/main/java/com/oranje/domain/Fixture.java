package com.oranje.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Fixture.
 */

@Document(collection = "fixture")
public class Fixture implements Serializable {

    @Id
    private String id;

    @Field("home")
    private String home;
    
    @Field("away")
    private String away;
    
    @Field("home_goals")
    private Integer homeGoals;
    
    @Field("away_goals")
    private Integer awayGoals;
    
    @Field("has_result")
    private Boolean hasResult;
    
    @Field("result")
    private String result;
    
    @Field("group")
    private String group;
    
    @Field("home_flag")
    private String homeFlag;
    
    @Field("away_flag")
    private String awayFlag;
    
    private Integer order;
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHome() {
        return home;
    }
    
    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }
    
    public void setAway(String away) {
        this.away = away;
    }

    public Integer getHomeGoals() {
        return homeGoals;
    }
    
    public void setHomeGoals(Integer homeGoals) {
        this.homeGoals = homeGoals;
    }

    public Integer getAwayGoals() {
        return awayGoals;
    }
    
    public void setAwayGoals(Integer awayGoals) {
        this.awayGoals = awayGoals;
    }

    public Boolean getHasResult() {
        return hasResult;
    }
    
    public void setHasResult(Boolean hasResult) {
        this.hasResult = hasResult;
    }

    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }

    public String getGroup() {
        return group;
    }
    
    public void setGroup(String group) {
        this.group = group;
    }

    public String getHomeFlag() {
        return homeFlag;
    }
    
    public void setHomeFlag(String homeFlag) {
        this.homeFlag = homeFlag;
    }

    public String getAwayFlag() {
        return awayFlag;
    }
    
    public void setAwayFlag(String awayFlag) {
        this.awayFlag = awayFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fixture fixture = (Fixture) o;
        if(fixture.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fixture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Fixture{" +
            "id=" + id +
            ", home='" + home + "'" +
            ", away='" + away + "'" +
            ", homeGoals='" + homeGoals + "'" +
            ", awayGoals='" + awayGoals + "'" +
            ", hasResult='" + hasResult + "'" +
            ", result='" + result + "'" +
            ", group='" + group + "'" +
            ", homeFlag='" + homeFlag + "'" +
            ", awayFlag='" + awayFlag + "'" +
            '}';
    }

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
}
