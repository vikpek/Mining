package at.ac.uibk.inforet.twittermood;

import java.util.Scanner;

public class CrawlerController {

	public static void main(String args[]) throws Exception {

		final TwitterEngine te = new TwitterEngine();
		
		if(args.length>0){
			System.out.println("Crawl only tweets containing \"" + args[0] + "\"") ;
			TweetBucketController.getInstance().setKeyword(args[0]);			
		}

		if (TokenSerializable.getInstance().tokenExists()) {
			System.out.println("Found token file - trying to load...");

            if(args.length>0){
                System.out.println("Enter search keyword!");
                Scanner s= new Scanner(System.in);
                String searchKeyword = s.next();
                System.out.println("Crawl only tweets containing \"" + searchKeyword + "\"") ;
                TweetBucketController.getInstance().setKeyword(searchKeyword);
            }

            // IMPORTANT - even though no idea what it does. removal leads to auth. failure!!!
            te.loadTokens(1);

		} else {
			System.out.println("Didnt found any token file...");
			te.recieveTokens(args);
		}

		// te.searchForTweets();
		new Runnable() {			
			@Override
			public void run() {
				te.streamTweets();		
			}
		}.run();
	}

}
