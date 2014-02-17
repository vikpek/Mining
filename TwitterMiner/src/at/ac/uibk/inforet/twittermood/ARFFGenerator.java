/**
 * 
 */
package at.ac.uibk.inforet.twittermood;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

import twitter4j.Status;

/**
 * 
 * take tweetbuckets one by one and append to arff file
 * 
 * @author vikpek
 * 
 */
public class ARFFGenerator {

	/**
	 * 
	 */
	private static final String SENTIMENT_KEYWORD = "InfoRet2012SentimentKey";
	private static final String TWEET_ARFF = "tweet.arff";

	public void export2ARFF() throws IOException {

		MapReducer mr = new MapReducer();

		final HashMap<String, Integer> blankDic = (HashMap<String, Integer>) getBlankDic();

		Stack<int[]> dataStack = new Stack<int[]>();
		ArrayList<TweetBucket> tbList = (ArrayList<TweetBucket>) TweetBucketController
				.getInstance().getAllTweetbuckets();

		int[] tweetVector;
		Integer v;
		int c = 0;

		int tweetSentiment = 0, tmp = 0;

		for (TweetBucket tb : tbList) {
			for (Status s : tb.getTweets()) {
				tweetVector = new int[blankDic.size()];

				if ((c % TweetBucket.MAX_TWEET_NUMBER) == 0) {
					System.out.println(c + " / " + tbList.size()
							* TweetBucket.MAX_TWEET_NUMBER);
				}
				c++;

				String text = s.getText();
				for (String term : text.split("\\s+")) {
					if ((v = blankDic.get(term)) != null) {
						tmp = mr.getSentimentForWord(term);
						if (tmp > 0) {
							tweetSentiment++;
						} else if (tmp < 0) {
							tweetSentiment--;
						}
						tweetVector[v] = 1;
					}
				}

				if (tweetSentiment > 0) {
					tweetVector[blankDic.get(SENTIMENT_KEYWORD)] = 1;
				} else if (tweetSentiment < 0) {
					tweetVector[blankDic.get(SENTIMENT_KEYWORD)] = -1;
				}

				tweetSentiment = 0;
				dataStack.push(tweetVector);
			}
		}

		System.out.println(dataStack.size());

		String[] primitiveBlankDic = createStringBlankPrimitiveDic(blankDic);

		writeFile(primitiveBlankDic, dataStack);
	}

	/**
	 * @param blankDic
	 * @return
	 */
	private String[] createStringBlankPrimitiveDic(
			HashMap<String, Integer> blankDic) {
		String[] result = new String[blankDic.size()];
		for (String word : blankDic.keySet()) {
			result[blankDic.get(word)] = word;
		}
		return result;
	}

	/**
	 * @return
	 */
	private HashMap<String, Integer> getBlankDic() {
		ArrayList<TweetBucket> tbList = (ArrayList<TweetBucket>) TweetBucketController
				.getInstance().getAllTweetbuckets();

		HashMap<String, Integer> dic = new HashMap<String, Integer>();
		int counter = 0;
		String bufferWord = "";
		for (TweetBucket tb : tbList) {
			for (Status s : tb.getTweets()) {
				String text = s.getText();

				for (String word : text.split("\\s+")) {

					bufferWord = word.replaceAll("\\W", "");
					if (bufferWord.length() > 3) {
						if (!dic.containsKey(bufferWord)) {
							dic.put(bufferWord, Integer.valueOf(counter));
							counter++;
						}
					}

				}
			}
		}

		dic.put(SENTIMENT_KEYWORD, Integer.valueOf(counter));
		counter++;

		System.out.println("Dic size: " + dic.size() + " counter value: "
				+ counter);
		return dic;
	}

	/**
	 * @param primitiveBlankDic
	 * @param dataStack
	 * @throws IOException
	 */
	private void writeFile(String[] primitiveBlankDic, Stack<int[]> dataStack)
			throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
				TWEET_ARFF)));

		bw.write("% InfoRet 08 Viktor Pekar " + new Date() + "\n");
		bw.write("% Approx. number of exported tweets: " + dataStack.size()
				+ "\n");

		bw.write("@RELATION tweets\n");
		for (String word : primitiveBlankDic) {
			bw.write("@ATTRIBUTE \"" + word + "\" NUMERIC\n");
		}

		bw.write("\n\n" + "@DATA" + "\n");

		boolean firstTime;
		for (int[] tweetVector : dataStack) {
			firstTime = true;
			for (int v : tweetVector) {
				if (firstTime) {
					firstTime = false;
				} else {
					bw.write(",");
				}
				bw.write(v + "");
			}
			bw.write("\n");
		}
		bw.close();
	}
}
