/**
 * 
 */
package at.ac.uibk.inforet.twittermood;

import java.net.UnknownHostException;
import java.util.List;

import twitter4j.Status;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoRepo {

	/**
	 * 
	 */
	public static final String TEXT = "text";
	private MongoClient mongoClient;
	private DB db;
	private DBCollection coll;

	public MongoRepo() {
		try {
			mongoClient = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = mongoClient.getDB("db");
		coll = db.getCollection("tweets");
	}

	public void addTweet(Status tweet) {

		BasicDBObject doc = new BasicDBObject();
		doc.append("id", tweet.getId());
		doc.append("created_at", tweet.getCreatedAt());
		doc.append("current_user_retweet_id", tweet.getCurrentUserRetweetId());
		doc.append("in_reply_to_screen_name", tweet.getInReplyToScreenName());
		doc.append("in_reply_to_status_id", tweet.getInReplyToStatusId());
		doc.append("in_reply_to_user_id", tweet.getInReplyToUserId());
		doc.append("retweet_count", tweet.getRetweetCount());
		// doc.append("retweet_status", tweet.getRetweetedStatus());
		doc.append("source", tweet.getSource());
		doc.append(TEXT, tweet.getText());
		doc.append("is_favorited", tweet.isFavorited());
		doc.append("is_retweet", tweet.isRetweet());

		if (tweet.getPlace() != null) {
			doc.append("place_id", tweet.getPlace().getId());
			doc.append("place_full_name", tweet.getPlace().getFullName());
			doc.append("place_country", tweet.getPlace().getCountry());
			doc.append("place_country_code", tweet.getPlace().getCountryCode());
			doc.append("place_name", tweet.getPlace().getName());
			doc.append("place_place_type", tweet.getPlace().getPlaceType());
			doc.append("place_street_address", tweet.getPlace()
					.getStreetAddress());
			doc.append("place_url", tweet.getPlace().getURL());

			coll.createIndex(new BasicDBObject("place_id", 1));
		}
		if (tweet.getGeoLocation() != null) {
			doc.append("geolocation_latitude", tweet.getGeoLocation()
					.getLatitude());
			doc.append("geolocation_longtitude", tweet.getGeoLocation()
					.getLongitude());
		}

		doc.append("user_hashtag_count", tweet.getHashtagEntities().length);

		doc.append("user_id", tweet.getUser().getId());
		doc.append("user_name", tweet.getUser().getName());

		// System.out.println("Entering doc: " + doc.toString());

		coll.createIndex(new BasicDBObject("retweet_count", 1));
		coll.createIndex(new BasicDBObject("user_hashtag_count", 1));

		coll.insert(doc);
	}

	public void sysoAllTweets() {
		DBCursor cursor = coll.find();
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} finally {
			cursor.close();
		}
	}

	public void sysoIndex() {
		List<DBObject> list = coll.getIndexInfo();

		for (DBObject o : list) {
			System.out.println(o);
		}
	}

	public List<DBObject> getAllTweetsAsDBObjects() {
		DBCursor cursor = coll.find();
		return cursor.toArray();
	}
}
