/**
 * 
 */
package at.ac.uibk.inforet.twittermood;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

/**
 * @author vikpek
 *
 */
public class DefaultListener implements StatusListener{

	/* (non-Javadoc)
	 * @see twitter4j.StreamListener#onException(java.lang.Exception)
	 */
	@Override
	public void onException(java.lang.Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see twitter4j.StatusListener#onDeletionNotice(twitter4j.StatusDeletionNotice)
	 */
	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see twitter4j.StatusListener#onScrubGeo(long, long)
	 */
	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see twitter4j.StatusListener#onStallWarning(twitter4j.StallWarning)
	 */
	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub
		
	}
	
    public void onStatus(Status status) {
        TweetBucketController.getInstance().storeTweet(status);
    }


	/* (non-Javadoc)
	 * @see twitter4j.StatusListener#onTrackLimitationNotice(int)
	 */
	@Override
	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub
		
	}


}
