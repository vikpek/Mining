/**
 * 
 */
package at.ac.uibk.inforet.twittermood;

import java.util.ArrayList;

import twitter4j.Status;

/**
 * @author vikpek
 * 
 */
public class ParserController {

	private static MongoRepo mr = new MongoRepo();

	public static void main(String args[]) {

		parseAllTweetArchives();
		new SentimentRepo();

	}

	private static void parseAllTweetArchives() {

		ArrayList<TweetBucket> tbList = (ArrayList<TweetBucket>) TweetBucketController
				.getInstance().getAllTweetbuckets();
		
		for (TweetBucket tb : tbList) {
			parseAllTweets(tb);
		}
	}

	/**
	 * @param tb
	 */
	private static void parseAllTweets(TweetBucket tb) {
		for (Status s : tb.getTweets()) {
			mr.addTweet(s);
		}
	}
}
