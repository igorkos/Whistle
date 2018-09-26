package com.whistle.github;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    public static final String ROOT= "https://api.github.com/repos/ReactiveX/RxJava/";

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private RecyclerView.Adapter<IssuesViewAdapter.IssueViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GitHubDataModel dataModel = GitHubDataModel.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataModel.initialize(this,ROOT);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new IssuesViewAdapter(new IssuesViewAdapter.OnClickListener() {
            @Override
            public void onClick(IssuesViewAdapter.IssueViewHolder v) {
                Intent i = new Intent(MainActivity.this, CommentsActivity.class);
                i.putExtra("key",v.getLayoutPosition());
                startActivityForResult(i,100);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
        mRecyclerView.setLayoutAnimation(animation);

        onReload(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reload) {
            onReload(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onReload(boolean force){
        if (force || dataModel.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            dataModel.getIssues(new DataModelListener() {
                @Override
                public void onUpdated() {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(String error) {
                    showError(error);
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
        builder.setTitle("Error")
                .setMessage(error)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onReload(true);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
