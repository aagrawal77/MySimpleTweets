package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> tweets;
    Context context;

    // pass in the Tweets array into the constructor
    public TweetAdapter(List<Tweet> tweets) {
        this.tweets = tweets;

    }

    // for each row we need to inflate the layout and pass them into a viewholder class

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, viewGroup,  false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        viewHolder.context = context;
        viewHolder.tweets = tweets;
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // get the data according to the position
        Tweet tweet = tweets.get(i);

        // populate the view according to the data
        viewHolder.tvUserName.setText(tweet.user.name);
        viewHolder.tvBody.setText(tweet.body);
        viewHolder.tvDate.setText(getRelativeTimeAgo(tweet.createdAt));
        viewHolder.tvScreenName.setText("@" + tweet.user.screenName);
        Integer retweets = tweet.retweetCount;
        Integer likes = tweet.likeCount;
        viewHolder.tvRetweet.setText(retweets.toString());
        viewHolder.tvLike.setText(likes.toString());

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }


// create viewholder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvDate;
        public TextView tvScreenName;
        public ImageButton btnRespond;
        public ImageButton btnLike;
        public boolean liked;
        public Context context;
        public List<Tweet> tweets;
        public TextView tvRetweet;
        public TextView tvLike;
        public TwitterClient client;
        public Tweet tweet;
        private final String ACTIVITY = "respond";

        public ViewHolder(View itemView) {
            super(itemView);

            // perform the findviewbyid lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            btnRespond = (ImageButton) itemView.findViewById(R.id.btnRespond);
            btnLike = (ImageButton) itemView.findViewById(R.id.btnLike);
            tvRetweet = (TextView) itemView.findViewById(R.id.tvRetweet);
            tvLike = (TextView) itemView.findViewById(R.id.tvLike);
            liked = false;
            client = TwitterApp.getRestClient(context);

            this.btnRespond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ComposeActivity.class);
                    tweet = tweets.get(getAdapterPosition());
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    intent.putExtra("og", ACTIVITY);
                    context.startActivity(intent);
                }
            });

            this.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!liked) {
                        btnLike.setImageResource(R.drawable.ic_vector_heart);
                        btnLike.setColorFilter(ContextCompat.getColor(context, R.color.medium_red));
                    } else {
                        btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                        btnLike.setColorFilter(ContextCompat.getColor(context, R.color.medium_gray));
                    }
                    liked = !liked;
                }
            });
        }
    }

    // relative time
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

}
