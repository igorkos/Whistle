package com.whistle.github;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.ArrayList;

/*
 GitHub Comments Volley request
 */
public class CommentsRequest extends GitHubRequest<Comment>{
    public CommentsRequest(String url, Response.Listener<ArrayList<Comment>> listener, Response.ErrorListener errorListener) {
        super( Request.Method.GET,url, Comment.class, listener, errorListener);
    }
}
