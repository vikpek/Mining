package at.ac.uibk.inforet.twittermood;

public class CrawlerController {

	public static void main(String args[]) throws Exception {

		final TwitterEngine te = new TwitterEngine();
		
		if(args.length>0){
			System.out.println("Crawl only tweets containing \"" + args[0] + "\"") ;
			TweetBucketController.getInstance().setKeyword(args[0]);			
		}

		if (TokenSerializable.getInstance().tokenExists()) {
			System.out.println("Found token file - trying to load...");
			// Scanner s= new Scanner(System.in);
			// System.out.println("Insert useId:");
			// int useId = s.nextInt();

			// TODO figure out what this use id is about....
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
