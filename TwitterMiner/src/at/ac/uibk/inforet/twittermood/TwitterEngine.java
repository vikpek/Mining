/**
 * 
 */
package at.ac.uibk.inforet.twittermood;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * @author vikpek
 * 
 */
public class TwitterEngine {

	private static final String CONSUMER_TOKEN = "zlQ7UTtJGMbWCGNpmDXiCg";
	private static final String CONSUMER_SECRET = "38xMHoJpw0xGrwMhWc9nY1uRkfJynTzzmB1g1kDIA";

	private Twitter twitter;
	private TwitterStream twitterStream;

	public TwitterEngine() {
		twitter = TwitterFactory.getSingleton();
		twitterStream = new TwitterStreamFactory().getInstance();
	}

	public void searchForTweets() throws TwitterException {
		Query query = new Query();
		QueryResult result = twitter.search(query);
		System.out.println("Searching for :" + query);
		for (Status tweet : result.getTweets()) {
			System.out.println(":" + tweet.getText());
		}
	}

	public void streamTweets() {
		DefaultListener listener = new DefaultListener();
		
		twitterStream.addListener(listener);
		twitterStream.sample();
	}

	public void loadTokens(int useId) throws TwitterException {
		TokenSerializable.getInstance().tryToDeserialize();
		AccessToken accessToken = loadAccessToken(useId);
		twitter.setOAuthConsumer(CONSUMER_TOKEN, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(accessToken);
		
		twitterStream.setOAuthConsumer(CONSUMER_TOKEN, CONSUMER_SECRET);
		twitterStream.setOAuthAccessToken(accessToken);
	}

	public AccessToken loadAccessToken(int useId) {

		String token = TokenSerializable.getInstance().getToken();
		String tokenSecret = TokenSerializable.getInstance().getTokenSecret();
		return new AccessToken(token, tokenSecret);
	}

	public void recieveTokens(String[] args) throws TwitterException,
			IOException {
		// The factory instance is re-useable and thread safe.
		twitter.setOAuthConsumer(CONSUMER_TOKEN, CONSUMER_SECRET);
		twitterStream.setOAuthConsumer(CONSUMER_TOKEN, CONSUMER_SECRET);
		
		RequestToken requestToken = twitter.getOAuthRequestToken();
		AccessToken accessToken = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (null == accessToken) {
			System.out
					.println("Open the following URL and grant access to your account:");
			System.out.println(requestToken.getAuthorizationURL());
			System.out
					.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
			String pin = br.readLine();
			try {
				if (pin.length() > 0) {
					accessToken = twitter
							.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = twitter.getOAuthAccessToken();
				}
			} catch (TwitterException te) {
				if (401 == te.getStatusCode()) {
					System.out.println("Unable to get the access token.");
				} else {
					te.printStackTrace();
				}
			}
		}
		storeAccessToken(twitter.verifyCredentials().getId(), accessToken);
	}

	private void storeAccessToken(long l, AccessToken accessToken) {
		TokenSerializable.getInstance().setToken(accessToken.getToken());
		TokenSerializable.getInstance().setTokenSecret(
				accessToken.getTokenSecret());
		TokenSerializable.getInstance().serialize();
	}
}
