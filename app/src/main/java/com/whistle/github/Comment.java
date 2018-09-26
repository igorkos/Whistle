package com.whistle.github;

import org.json.JSONException;
import org.json.JSONObject;

/*
Github Issue Comment object

 user - name of user
 body - comment body
 updated_at - time when comment was last time updated
 */

public class Comment extends GitHubObject {

    String user;

    // Parse Comment object from JSONObject
    @Override
    public void parse(JSONObject jsonObject) {
        try {
            JSONObject user = jsonObject.getJSONObject("user");
            this.user = user.getString("login");
            this.body = jsonObject.getString("body");
            this.updated_at = dateFrom(jsonObject.getString("updated_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
