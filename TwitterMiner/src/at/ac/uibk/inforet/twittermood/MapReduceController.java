/**
 * 
 */
package at.ac.uibk.inforet.twittermood;

import java.util.Scanner;

/**
 * @author vikpek
 * 
 */
public class MapReduceController {
	public static void main(String args[]) {
		System.out.println("Welcome to the fancy map reduce interface:");

		MapReducer mr = new MapReducer();

		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("[0] Tweets per day");
			System.out.println("[1] Tweets per hour");
			System.out.println("[2] Retweet count");
			System.out.println("[3] Hashtag count");
			System.out.println("[4] Geolocations count");
			System.out.println("[5] Sentiment count (grab a coffee...)");
			System.out.println("[6] Compute all - not recommended");

			System.out.println("Enter integer:");
			int decision = sc.nextInt();

			switch (decision) {
			case 0:
				mr.mapReduceTweetsPerDay();
				break;
			case 1:
				mr.mapReduceTweetsPerHour();
				break;
			case 2:
				mr.mapReduceRetweets();
				break;
			case 3:
				mr.mapReduceHashtags();
				break;
			case 4:
				mr.mapReduceGeoLocations();
				break;
			case 5:
				mr.mapReduceSentiment();
				break;
			case 6:
				System.out.println("Day");
				mr.mapReduceTweetsPerDay();
				System.out.println("Hour");
				mr.mapReduceTweetsPerHour();
				System.out.println("Retweet");
				mr.mapReduceRetweets();
				System.out.println("Hashtag");
				mr.mapReduceHashtags();
				System.out.println("Geolocation");
				mr.mapReduceGeoLocations();
				System.out.println("Sentiment");
				mr.mapReduceSentiment();
				break;

			default:
				break;
			}
			System.out.println();
			System.out.println();
			System.out.println();
		}
	}
}
