package com.whistle.github;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
GitHubObject
Root object for GitHub data (Issue, Comment) objects
 */
abstract class GitHubObject {

    String body;
    Date updated_at;

    public abstract void parse(JSONObject jsonObject);

    //Convert string Date representation to Date
    protected Date dateFrom(String date){
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                return format.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Get Readable Date string
    public String dateString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        return dateFormat.format(updated_at);
    }
}
