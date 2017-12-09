package com.esprit.carrent.Entities;

import java.util.Date;

/**
 * Created by Lou_g on 06/12/2017.
 */

public class Feedback {

    private int id ;
    private int idUser ;
    private String feedUser ;
    private String feedback ;
    private String dateFeed ;

    public Feedback(int id, int idUser, String feedUser, String feedback, String dateFeed) {
        this.id = id;
        this.idUser = idUser;
        this.feedUser = feedUser;
        this.feedback = feedback;
        this.dateFeed = dateFeed;
    }

    public Feedback(String feedUser, String feedback, String dateFeed) {
        this.feedUser = feedUser;
        this.feedback = feedback;
        this.dateFeed = dateFeed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFeedUser() {
        return feedUser;
    }

    public void setFeedUser(String feedUser) {
        this.feedUser = feedUser;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDateFeed() {
        return dateFeed;
    }

    public void setDateFeed(String dateFeed) {
        this.dateFeed = dateFeed;
    }
}
