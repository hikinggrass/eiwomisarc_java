package de.hikinggrass.eiwomisarc;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import java.util.List;

public class TwitterDotCom {

	/**
	 * Constructs a new twitter object and performs a search
	 */
	public TwitterDotCom() {
		
		Query test = new Query();
		test.geoCode(new GeoLocation(48.8936, 8.6971), 30, Query.KILOMETERS);
		
		Twitter twitter = new TwitterFactory().getInstance();
        try {
            QueryResult result = twitter.search(test);
            List<Tweet> tweets = result.getTweets();
            for (Tweet tweet : tweets) {
                System.out.println("@" + tweet.getFromUser() + " - " + tweet.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
        }
	}

}
