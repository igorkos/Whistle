package com.whistle.github;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CommentsViewAdapter extends RecyclerView.Adapter<CommentsViewAdapter.IssueViewHolder> {

        private Issue mIssue;
        public static class IssueViewHolder extends RecyclerView.ViewHolder {
            public TextView bodyView;
            public TextView userView;
            public TextView timeView;

            public IssueViewHolder(View v) {
                super(v);
                userView = v.findViewById(R.id.user);
                bodyView = v.findViewById(R.id.body);
                timeView = v.findViewById(R.id.time);
            }
        }

        public CommentsViewAdapter(Issue issue) {
            mIssue = issue;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public CommentsViewAdapter.IssueViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_item_layout, parent, false);
            // create a new view
            IssueViewHolder vh = new IssueViewHolder(v);

            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(IssueViewHolder holder, int position) {
            if (mIssue != null && mIssue.comments != null) {
                Comment comment = mIssue.comments.get(position);
                holder.userView.setText(comment.user);
                holder.bodyView.setText(comment.body);
                holder.timeView.setText(comment.dateString());
            }
            else {
                holder.userView.setText(null);
                holder.bodyView.setText(null);
                holder.timeView.setText(null);
            }
        }

        @Override
        public int getItemCount() {
            if (mIssue != null && mIssue.comments != null){
                return mIssue.comments.size();
            }
            return 0;
        }

    }
