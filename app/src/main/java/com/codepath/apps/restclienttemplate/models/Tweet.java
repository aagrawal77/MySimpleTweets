package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {

    // list out the attributes
    public String body;
    public long uid; // database ID for the tweet
    public String createdAt;
    public User user;
    public int retweetCount;
    public int likeCount;
    public boolean liked;
    public boolean retweeted;

    public Tweet() {}

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject object) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = object.getString("full_text");
        tweet.uid = object.getLong("id");
        tweet.createdAt = object.getString("created_at");
        tweet.user = User.fromJSON(object.getJSONObject("user"));
        tweet.retweetCount = object.getInt("retweet_count");
        tweet.likeCount = object.getInt("favorite_count");
        tweet.liked = object.getBoolean("favorited");
        tweet.retweeted = object.getBoolean("retweeted");
        return tweet;

    }
}
