/**
 * 
 */
package at.ac.uibk.inforet.twittermood;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import twitter4j.Status;

/**
 * @author vikpek
 * 
 */
public class TweetBucket implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<Status> tweets = new ArrayList<Status>();
	public static final int MAX_TWEET_NUMBER = 100000;

	public boolean addTweet(Status tweet) {
		if (getTweets().size() < MAX_TWEET_NUMBER) {
			getTweets().add(tweet);
			return true;
		} else {
			return false;
		}
	}

	public void serialize(long ts) {
		System.out.println("Serializing full bucket.");
		try {
			final FileOutputStream fileOut = new FileOutputStream("tweets/"
					+ ts + ".tweetbucket");
			final ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		} catch (final IOException i) {
			i.printStackTrace();
		}
	}

	public ArrayList<Status> getTweets() {
		return tweets;
	}

	public void setTweets(ArrayList<Status> tweets) {
		this.tweets = tweets;
	}
}
