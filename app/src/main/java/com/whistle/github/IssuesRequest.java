package com.whistle.github;

import com.android.volley.Response;

import java.util.ArrayList;

/*
 GitHub Issues list Volley request
 */
public class IssuesRequest extends GitHubRequest<Issue> {
    private static final String PATH = "issues";

    public IssuesRequest(String url, Response.Listener<ArrayList<Issue>> listener, Response.ErrorListener errorListener) {
        super( Method.GET,url + PATH, Issue.class, listener, errorListener);
    }



}