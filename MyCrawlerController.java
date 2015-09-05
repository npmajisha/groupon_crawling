import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class MyCrawlerController {
	public static void main(String[] args) throws Exception {
		
		
		String userAgentIdentifier = args[0];
		int numberOfCrawlers = Integer.parseInt(args[1]);
		String crawlStorageFolder = args[2];

		String[] seedUrls = { "https://www.groupon.com/browse/los-angeles?category=things-to-do",
				"https://www.groupon.com/browse/los-angeles?category=food-and-drink" };

		CrawlConfig config = new CrawlConfig();
		config.setUserAgentString(userAgentIdentifier);
		config.setCrawlStorageFolder(crawlStorageFolder);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		for (String seedUrl : seedUrls) {
			controller.addSeed(seedUrl);
		}

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(MyCrawler.class, numberOfCrawlers);
	}

}
