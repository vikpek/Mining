/**
 * 
 */
package at.ac.uibk.inforet.twittermood;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.Status;

/**
 * @author vikpek
 * 
 */
public class TweetBucketController {
	
	private static final String FILEPATH = "tweets/";
	private static final String FILE_SUFFIX = ".tweetbucket";
	
	private static TweetBucketController instance = new TweetBucketController();
	private TweetBucket tb;
	

	private String keyword = null;

	private TweetBucketController() {
		tb = new TweetBucket();
	}

	public static TweetBucketController getInstance() {
		return instance;
	}

	public void storeTweet(Status tweet) {

		boolean store = false;
		if (keyword != null) {
			if (tweet.getText().contains(keyword)) {
				store = true;
			}
		} else {
			store = true;
		}

		if (store) {
			System.out.println(tweet.getText());
			if (!tb.addTweet(tweet)) {
				tb.serialize(new Date().getTime());
				tb = new TweetBucket();
				tb.addTweet(tweet);
			}
		}
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public TweetBucket tryToDeserialize(String path) {
		final File f = new File(path);
		TweetBucket tb = null;
		if (f.exists()) {
			try {
				final FileInputStream fileIn = new FileInputStream(path);
				final ObjectInputStream in = new ObjectInputStream(fileIn);
				tb = (TweetBucket) in.readObject();
				in.close();
				fileIn.close();

			} catch (final IOException i) {
				i.printStackTrace();
			} catch (final ClassNotFoundException c) {
				System.out.println("No token class found");
				c.printStackTrace();
			}
		}
		return tb;
	}
	
	public List<TweetBucket> getAllTweetbuckets(){
		
		ArrayList<TweetBucket> tbList = new ArrayList<TweetBucket>();
		
		String path = FILEPATH;

		
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				if (files.endsWith(FILE_SUFFIX)) {
					TweetBucket tb = TweetBucketController.getInstance()
							.tryToDeserialize(path + files);
					if (tb != null) {
						tbList.add(tb);
					} else {
						try {
							throw new Exception(
									"Error while deserializing tweetbucket.");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return tbList;
	}
}
