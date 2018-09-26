package com.whistle.github;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
Github Issue Comment object

 title - issue Title
 comments_url - url to fetch comments for this issue
 commentsCount - count of comments
 number - issue number on GitHub
 body - issue body
 updated_at - time when issue was last time updated
 */

public class Issue extends GitHubObject {

    String comments_url;
    Integer commentsCount;
    Integer number;
    String title;

    ArrayList<Comment> comments;

    @Override
    public void parse(JSONObject jsonObject) {
        try {
            this.comments_url = jsonObject.getString("comments_url");
            this.title = jsonObject.getString("title");
            this.body = jsonObject.getString("body");
            this.number = jsonObject.getInt("number");
            this.commentsCount = jsonObject.getInt("comments");
            this.updated_at = dateFrom(jsonObject.getString("updated_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
