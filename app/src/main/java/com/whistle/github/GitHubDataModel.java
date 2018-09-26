package com.whistle.github;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Consumer;

/*
GitHubDataModel
Provide access to GitHub Issues and Issie comments lists
 */
public class GitHubDataModel extends AbstractList<Issue> {

    //Singleton implementation
    private static GitHubDataModel instance;

    public static synchronized GitHubDataModel getInstance(){
        if(instance == null){
            instance = new GitHubDataModel();
        }
        return instance;
    }

    private GitHubDataModel(){}

    //parameters
    private String repoRoot; //GitHub repo root url
    private RequestQueue queue; //Volley request queue
    private ArrayList<Issue> issues; // List of fetched issues for provided repo


    //Initialize Data Model
    public void initialize(Context context, String repo){
        repoRoot = repo;
        queue = Volley.newRequestQueue(context);
    }

    //Request List of Issues for provided repository

    public void getIssues(DataModelListener listener){
        IssuesRequest request = new IssuesRequest(repoRoot,new Response.Listener<ArrayList<Issue>>() {
            @Override
            public void onResponse(ArrayList<Issue> response) {
                Collections.sort(response, new DateComparator<Issue>());
                issues = response;
                if (listener != null){
                    listener.onUpdated();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null){
                    listener.onError(error.getLocalizedMessage());
                }
            }
        });
        queue.add(request);
    }

    //Request List of Comments  for provided Issue
    public void getComments(Issue issue, DataModelListener listener){
        CommentsRequest request = new CommentsRequest(issue.comments_url,new Response.Listener<ArrayList<Comment>>() {
            @Override
            public void onResponse(ArrayList<Comment> response) {
                Collections.sort(response, new DateComparator<Comment>());
                issue.comments = response;
                if (listener != null){
                    listener.onUpdated();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null){
                    listener.onError(error.getLocalizedMessage());
                }
            }
        });
        queue.add(request);
    }

    //Comparator class for sorting by Date
    private class DateComparator<T extends GitHubObject> implements Comparator<T>{
        public int compare(T o1, T o2) {
            if (o1.updated_at == null || o2.updated_at == null)
                return 0;
            return o2.updated_at.compareTo(o1.updated_at);
        }
    }

    //AbstractList interface implementation
    @Override
    public int size() {
        if (issues == null)
            return 0;
        return issues.size();
    }

    @Override
    public Issue get(int index) {
        if (issues == null )
            return null;
        if (issues.size() < index )
            return null;
        return issues.get(index);
    }

}
