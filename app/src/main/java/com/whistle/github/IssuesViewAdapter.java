package com.whistle.github;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class IssuesViewAdapter  extends RecyclerView.Adapter<IssuesViewAdapter.IssueViewHolder> {

        public interface OnClickListener {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            void onClick(IssueViewHolder v);
        }

        private GitHubDataModel dataModel = GitHubDataModel.getInstance();
        private OnClickListener mListener;

        public static class IssueViewHolder extends RecyclerView.ViewHolder {
            public TextView titleView;
            public TextView bodyView;
            public TextView itemIDView;
            public TextView timeView;
            private OnClickListener mListener;

            public IssueViewHolder(View v, OnClickListener listener) {
                super(v);
                mListener = listener;
                titleView = v.findViewById(R.id.title);
                bodyView = v.findViewById(R.id.body);
                itemIDView = v.findViewById(R.id.itemid);
                timeView = v.findViewById(R.id.time);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null){
                            mListener.onClick(IssueViewHolder.this);
                        }
                    }
                });
            }
        }

        public IssuesViewAdapter(OnClickListener listener) {
            mListener = listener;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public IssuesViewAdapter.IssueViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.issue_item_layout, parent, false);
            IssueViewHolder vh = new IssueViewHolder(v,mListener);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(IssueViewHolder holder, int position) {
            Issue issue = dataModel.get(position);
            if (issue != null) {
                holder.titleView.setText(issue.title);
                holder.bodyView.setText(issue.body);
                holder.itemIDView.setText(issue.number.toString());
                holder.timeView.setText(issue.dateString());
            }
            else {
                holder.titleView.setText(null);
                holder.bodyView.setText(null);
                holder.itemIDView.setText(null);
                holder.timeView.setText(null);
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return dataModel.size();
        }

    }
