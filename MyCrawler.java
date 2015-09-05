import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.common.io.Files;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp3|zip|gz))$");
	private static String[] CRAWL_DOMAIN = { "https://www.groupon.com/browse/los-angeles?category=things-to-do",
			"https://www.groupon.com/browse/los-angeles?category=food-and-drink", "https://www.groupon.com/deals" };
	private static File storageFolder = new File("E:/Fall2015/CSCI548/HW1/data/crawl/root/");

	/**
	 * This method receives two parameters. The first parameter is the page in
	 * which we have discovered this new url and the second parameter is the new
	 * url. You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic). In this example,
	 * we are instructing the crawler to ignore urls that have css, js, git, ...
	 * extensions and to only accept urls that start with
	 * "https://www.groupon.com/". In this case, we didn't need the
	 * referringPage parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && checkUrlInDomain(href);
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();

			if (checkDownloadPageCondition(html)) {
				String filename = url.contains("?")
						? storageFolder.getAbsolutePath() + "/" + url.substring(url.lastIndexOf("/"), url.indexOf("?"))
								+ ".html"
						: storageFolder.getAbsolutePath() + "/" + url.substring(url.lastIndexOf("/")) + ".html";
				try {
					Files.write(page.getContentData(), new File(filename));
					System.out.println("Stored:" + url);
				} catch (IOException iox) {
					System.out.println("Failed to write file: " + filename + iox);
				}
			}
			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
		}
	}

	private boolean checkUrlInDomain(String url) {
		for (String domainUrl : CRAWL_DOMAIN) {
			if (url.startsWith(domainUrl)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkDownloadPageCondition(String html) {
		Document doc = Jsoup.parse(html);
		Element priceSpan = doc.select("span.price").first();
		if (priceSpan != null) {
			if (Integer.parseInt(priceSpan.text().substring(1)) <= 50) {
				System.out.println("Price:" + priceSpan.text());
				return true;
			}
		}

		Element breakoutPrice = doc.select("span.breakout-option-price").first();
		if (breakoutPrice != null) {
			if (Integer.parseInt(breakoutPrice.text().substring(1)) <= 50) {
				System.out.println("Price:" + breakoutPrice.text());
				return true;
			}
		}
		return false;
	}

}
