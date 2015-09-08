# groupon_crawling
Groupon (https://www.groupon.com/) is a deal-of-the-day recommendation service for consumers.
Every 24 hours, Groupon broadcasts electronic coupons with discounts to
restaurants, services, stores, electronics etc. in your city. Groupon however does not provide a
filter based on the price of deals.
This web crawler was built with an intention to crawl the groupon website, specifically for the city
of Los Angeles and get deals in the category "Food and Drinks" and "Things to do" under $50.
The web crawler extends from the open source crawling framework
"crawler4j"(https://github.com/yasserg/crawler4j).
There are two classes that have to be implemented/extended. 
1.	Controller class – This is the class where we specify the seeds of the crawl,
    the folder in which intermediate crawl data has to be stored, and number of concurrent threads.
    The Crawl configuration has to be set in this class which includes maximum depth of crawling, number of threads,
    the politeness setting, user agent string (in order to identify the crawler).
2.	Crawler class – This has to be extended from the WebCrawler class in crawler4j. 
    This class decides which URLs have to be crawled and handles downloading of the page.
    The two main functions that have to be overridden are:
    a.	shouldVisit – This function decides whether the page with a given URL has to be crawled or not.
        We can define our own filters for example not allowing stylesheets, script files etc.
    b.	visit – This function is called after the content of the URL is downloaded successfully. 
        You can easily fetch all the text, html, links and unique id of the downloaded page.

In order to collect the pages which have a deal price below $50, I observed the DOM of the deal pages.
The price was either a fixed price or a breakout option price (several combination prices).
I decided to extract the DOM element - <span class=”price”> or <span class=”breakout-option-price”>
using JSoup(jsoup.org), which is a Java HTML Parser Library and parse the value from the span text to get the price.
In case of breakout options, I chose the first child since price is sorted in ascending order and the first price 
is the lowest. Only if these conditions are met, the page is downloaded and saved locally. 
In the GrouponCrawler class (source code), the method checkDownloadCondition() implements this logic.
