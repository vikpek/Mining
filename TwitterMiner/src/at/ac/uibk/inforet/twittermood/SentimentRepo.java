/**
 * 
 */
package at.ac.uibk.inforet.twittermood;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * @author vikpek
 * 
 */
public class SentimentRepo {


	private MongoClient mongoClient;
	private DB db;
	private DBCollection coll;

	public SentimentRepo() {

		try {
			mongoClient = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = mongoClient.getDB(MapReducer.DB);
		coll = db.getCollection("sentiments");

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					"resources/sentiment.tff")));
			String line;
			while ((line = br.readLine()) != null) {
				String[] arr = line.split(" ");
				if (arr.length == 6) {
					String[] wordArr = arr[2].split("=");
					if (wordArr.length == 2) {
						String[] popArr = arr[5].split("=");
						if (popArr.length == 2) {

							Integer popInt = 0;
							if (popArr[1].equals("neutral")) {
								popInt = 0;
							} else if (popArr[1].equals("negative")) {
								popInt = -1;
							} else if (popArr[1].equals("positive")) {
								popInt = 1;
							}

							System.out.println("putting in: " + wordArr[1]
									+ " and " + popInt);

							BasicDBObject doc = new BasicDBObject();
							
							doc.append("term", wordArr[1]);
							doc.append("sentiment", popInt);
							
							coll.createIndex(new BasicDBObject("term", 1));
							coll.insert(doc);	
						}
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
