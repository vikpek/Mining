/**
 * 
 */
package at.ac.uibk.inforet.twittermood;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;

/**
 * @author vikpek
 * 
 */
public class MapReducer {

	/**
	 * 
	 */
	public static final String DB = "db";
	private MongoClient mongoClient;
	private DB db;
	private DBCollection coll;

	public MapReducer() {
		try {
			mongoClient = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = mongoClient.getDB(DB);
		coll = db.getCollection("tweets");
	}

	private MapReduceOutput miscMapReduce(DBCollection coll, String map,
			String reduce) {
		try {
			MapReduceCommand cmd = new MapReduceCommand(coll, map, reduce,
					null, MapReduceCommand.OutputType.INLINE, null);
			return coll.mapReduce(cmd);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public void mapReduceTweetsPerDay() {

		String map = "function() {key = ''+this.created_at.getDay()+'_'+this.created_at.getMonth();"
				+ "emit(key, {count: 1});}";

		String reduce = "function(key, values) { " + "var count = 0; "
				+ "values.forEach(function(doc) { "
				+ "count = count + doc.count;});" + "return {count: count};} ";

		MapReduceOutput out = miscMapReduce(coll, map, reduce);

		for (DBObject o : out.results()) {
			System.out.println(o.toString());
		}
	}

	public void mapReduceTweetsPerHour() {
		String map = "function() {key = ''+this.created_at.getHours();"
				+ "emit(key, {count: 1});}";

		String reduce = "function(key, values) { " + "var count = 0; "
				+ "values.forEach(function(doc) { "
				+ "count = count + doc.count;});" + "return {count: count};} ";

		MapReduceOutput out = miscMapReduce(coll, map, reduce);

		for (DBObject o : out.results()) {
			System.out.println(o.toString());
		}
	}

	public void mapReduceRetweets() {
		String map = "function() {key = \"retweets\";"
				+ "emit(key, {count_retweets: this.retweet_count});}";

		String reduce = "function(key, values) { " + "var count = 0; "
				+ "values.forEach(function(doc) { "
				+ "count = count + doc.count_retweets;" + "});"
				+ "return {count_retweets: count};} ";

		MapReduceOutput out = miscMapReduce(coll, map, reduce);

		for (DBObject o : out.results()) {
			System.out.println(o.toString());
		}
	}

	public void mapReduceHashtags() {
		String map = "function() {key = \"user_hashtag_count\";"
				+ "emit(key, {user_hashtag_count: this.user_hashtag_count});}";

		String reduce = "function(key, values) { " + "var count = 0; "
				+ "values.forEach(function(doc) { "
				+ "count = count + doc.user_hashtag_count;" + "});"
				+ "return {user_hashtag_count: count};} ";

		MapReduceOutput out = miscMapReduce(coll, map, reduce);

		for (DBObject o : out.results()) {
			System.out.println(o.toString());
		}
	}

	public void mapReduceGeoLocations() {
		String map = "function() {key = \"place_id\";"
				+ "emit(key, {place_id: 1});}";

		String reduce = "function(key, values) { " + "var count = 0; "
				+ "values.forEach(function(doc) { "
				+ "count = count + doc.place_id;" + "});"
				+ "return {place_id: count};} ";

		MapReduceOutput out = miscMapReduce(coll, map, reduce);

		for (DBObject o : out.results()) {
			System.out.println(o.toString());
		}
	}

	public void mapReduceSentiment() {
		String map = "function() {key = \"sentiment\";" + "var sum = 0;"
				+ "var arr = this.text.split(\" \");" + "for(var v in arr){"
				+ "var v2 = db.sentiments.find({term: arr[v]});"
				+ "if(!(v2.sentiment == undefined)){"
				// + "print(arr[v] + \" - \" + v2.sentiment);"
				+ "sum += v2.sentiment;" + "}};"
				+ "emit(key, {sentiment: sum});" + "}";

		String reduce = "function(key, values) { " + "var count = 0;"
				+ "values.forEach(function(doc) { "
				+ "count = count + doc.sentiment;" + "});"
				+ "return {sentiment: count};} ";

		MapReduceOutput out = miscMapReduce(coll, map, reduce);

		for (DBObject o : out.results()) {
			System.out.println(o.toString());
		}
	}

	public int getSentimentForWord(String term) {

		BasicDBObject query = new BasicDBObject("term", term);
		coll = db.getCollection("sentiments");
		int result = 0;
		DBObject object = coll.findOne(query);
		if (object != null) {
//			String tmp = (String) object.get("sentiment");
			result = (Integer) object.get("sentiment");
		}
		return result;
	}
}
