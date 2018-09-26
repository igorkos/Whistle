package com.whistle.github;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

/*
CommentsActivity
Show list of all posted comments for GitHub Issue report
 */
public class CommentsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private RecyclerView.Adapter<CommentsViewAdapter.IssueViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GitHubDataModel dataModel = GitHubDataModel.getInstance();

    private Issue mIssue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        //Get Issue index from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

              Integer index = extras.getInt("key");
              mIssue = dataModel.get(index);
        }
        else{
            finish();
            return;
        }
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Set drop down animation
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
        mRecyclerView.setLayoutAnimation(animation);

        // specify an adapter
        mAdapter = new CommentsViewAdapter(mIssue);
        mRecyclerView.setAdapter(mAdapter);

        //Set title
        setTitle( getResources().getString(R.string.title_activity_comments) + " " + mIssue.number);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //Request comments from data source
        onReload();
    }


    private void onReload(){
        //Show spinner

        if (mIssue.comments == null ) {
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            //Request GitHub for comments data
            dataModel.getComments(mIssue, new DataModelListener() {
                @Override
                public void onUpdated() {
                    //Show list
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    //Update data
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(String error) {

                }
            });
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showError(String error){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle(getResources().getString(R.string.alert_error))
                .setMessage(error)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onReload();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
