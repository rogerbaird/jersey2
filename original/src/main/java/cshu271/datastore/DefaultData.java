package cshu271.datastore;

import cshu271.value.Article;
import cshu271.value.Comment;
import cshu271.value.User;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultData
{
	public static void populateUsers()
	{
		try
		{
			Users.add(new User("abigail", "abc"));
			Users.add(new User("blanche", "abc"));
			Users.add(new User("clementine", "abc"));
			Users.add(new User("daisy", "abc"));
			Users.add(new User("elwood", "abc"));
		} catch (UnavailableException ex)
		{
			Logger.getLogger(DefaultData.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static void populateArticles()
	{
		Articles.add( new Article(
			"abigail",
			"https://motherboard.vice.com/en_us/article/qvqzd5/your-4k-netflix-streaming-is-on-a-collision-course-with-your-isp-data-caps",
		    "Your 4K Netflix Streaming Is on a Collision Course With Your ISP's Data Caps"));
		Articles.add( new Article(
			"blanche",
			"https://www.buzzfeed.com/chuphanginichandrakanthan/send-help-pls-thnx",
		    "19 Jokes You Can Only Laugh At If You're Not That Fond Of Maths"));
		Articles.add( new Article(
			"clementine",
			"https://www.zdnet.com/google-amp/article/anonymous-hacker-gets-10-years-in-prison-for-ddos-attacks-on-childrens-hospitals/",
		    "Aaron Swartz's Federal Judge Gives Anonymous Hacker 10 Years In Prison For DDoS Attacks On Children's Hospitals"));
		Articles.add( new Article(
			"daisy",
			"https://www.latimes.com/local/lanow/la-me-ln-ada-suit-scooters-san-diego-20190112-story.html",
		    "Pedestrians, E-Scooters Are Clashing In the Struggle For Sidewalk Space"));
		Articles.add( new Article(
			"elwood",
			"https://gizmodo.com/the-u-s-government-has-amassed-terabytes-of-internal-w-1831640212",
		    "The US Government Has Amassed Terabytes of Internal WikiLeaks Data"));
		Articles.add( new Article(
			"abigail",
			"https://www.vice.com/en_us/article/43k3xb/amazon-employees-are-walking-out-over-the-companys-huge-carbon-footprint",
		    "Amazon Employees Are Walking Out Over the Company's Huge Carbon Footprint "));
		Articles.add( new Article(
			"blanche",
			"https://arstechnica.com/tech-policy/2019/09/web-scraping-doesnt-violate-anti-hacking-law-appeals-court-rules/",
		    "Web Scraping Doesn't Violate Anti-Hacking Law, Appeal Court Rules"));
		Articles.add( new Article(
			"clementine",
			"https://www.buzzfeednews.com/article/nicolenguyen/google-antitrust-investigation",
		    "Nearly Every State Is Launching An Antitrust Investigation Of Google"));
		Articles.add( new Article(
			"daisy",
			"https://www.nytimes.com/interactive/2019/09/09/technology/apple-app-store-competition.html",
		    "How Apple Stacked the App Store With Its Own Products"));
		Articles.add( new Article(
			"elwood",
			"https://electrek.co/2019/09/07/tesla-battery-cell-last-1-million-miles-robot-taxis/",
		    "Tesla Battery Researcher Unveils New Cell That Could Last 1 Million Miles"));
		
	}
	
	public static void populateComments()
	{
		Comments.add(new Comment(0L, 1L, "This article stinks!", 1L));
		Comments.add(new Comment(0L, 1L, "I LOVE this article!", 2L));
	}
	
	public static void populateVotes()
	{
		try
		{
			Articles.getById(1L).vote("blanche", 1L);
			Articles.getById(2L).vote("abigail", 1L);
			Articles.getById(2L).vote("clementine", 1L);
			Articles.getById(3L).vote("abigail", -1L);
			Articles.getById(3L).vote("blanche", -1L);
		}
		catch(NotFoundException e)
		{
			
		}
	}
}

