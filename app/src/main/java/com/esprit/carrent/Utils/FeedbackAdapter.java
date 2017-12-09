package com.esprit.carrent.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esprit.carrent.Entities.Feedback;
import com.esprit.carrent.R;

import java.util.List;

/**
 * Created by Lou_g on 06/12/2017.
 */


public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {


    private Context mCtx;
    private List<Feedback> feedbackList;

    public FeedbackAdapter(Context mCtx, List<Feedback> feedbackList) {
        this.mCtx = mCtx;
        this.feedbackList = feedbackList;

    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.feedback_item, null);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {
        Feedback feeds = feedbackList.get(position);
        holder.feedbacktxt.setText(feeds.getFeedback());
        holder.userfeedtxt.setText(feeds.getFeedUser());
        //holder.heuretxt.setText(String.valueOf(feeds.getDateFeed()));

    }


    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    class FeedbackViewHolder extends RecyclerView.ViewHolder {

        TextView feedbacktxt, userfeedtxt, heuretxt;


        public FeedbackViewHolder(View itemView) {
            super(itemView);

            feedbacktxt = itemView.findViewById(R.id.feedbacktxt);
            userfeedtxt = itemView.findViewById(R.id.userfeedtxt);
            //heuretxt = itemView.findViewById(R.id.heuretxt);
        }
    }
}
