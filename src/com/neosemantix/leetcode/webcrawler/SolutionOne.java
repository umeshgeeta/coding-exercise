package com.neosemantix.leetcode.webcrawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * // This is the HtmlParser's API interface.
 * // You should not implement it, or speculate about its implementation
 * interface HtmlParser {
 *     public List<String> getUrls(String url) {}
 * }
 */
class SolutionOne {
	
	private static Map<String, List<String>> urlMap;
	
	static {
		urlMap = new HashMap<String, List<String>>();
		
		List<String> list0 = Arrays.asList("http://news.yahoo.com/us");
		urlMap.put("http://news.yahoo.com", list0);
		
		List<String> list1 = new ArrayList<String>();
		urlMap.put("http://news.yahoo.com/news", list1);
		
		List<String> list2 = Arrays.asList("http://news.yahoo.com", "http://news.yahoo.com/news");
		urlMap.put("http://news.yahoo.com/news/topics", list2);
		
		List<String> list3 = Arrays.asList("http://news.yahoo.com/news", "http://news.yahoo.com/news/topics");
		urlMap.put("http://news.google.com", list3);
		
		List<String> list4 = new ArrayList<String>();
		urlMap.put("http://news.yahoo.com/us", list4);
	}
	
	private static class HtmlParser {
		public List<String> getUrls(String url) {
			return urlMap.get(url);
		}
	}
	
	public static void main(String[] args) {
		HtmlParser htmlParser = new HtmlParser();
		SolutionOne sol = new SolutionOne();
		String startUrl = "http://news.yahoo.com/news/topics";
		List<String> result = sol.crawl(startUrl, htmlParser);
		System.out.println("\nRESULT:"+result);
	}
    
    private static class CrawlingTask implements Runnable {
        
        private String surl;
        private SolutionOne solution;
        private HtmlParser parser;
        private List<String> yield;
        
        private CrawlingTask(String s, SolutionOne sol, HtmlParser hp){
            surl = s;
            solution = sol;
            parser = hp;
        }
        
        public void run() {
        	System.out.println("Parsing: " + surl);
            yield = parser.getUrls(surl);
            if (yield != null && !yield.isEmpty()){
                synchronized(solution.result){
                    for(String y: yield){
                        if (y.startsWith(solution.prefix) && !solution.result.contains(y)){
                            solution.result.add(y);
                            try {
                                solution.toCrawl.put(y);
                            }catch(InterruptedException ie){
                                
                            }
                        }
                        // else already crawled or different domain, we can ignore
                    }
                    solution.result.notifyAll();
                }
            }
            System.out.println("Yield: "+yield);
        }
    }
    
    private static class CrawlerExecutor extends ThreadPoolExecutor {
        
        private static int corePoolSize = 2;
        private static int maximumPoolSize = 10;
        private static long keepAliveTime = 5;
        private static TimeUnit unit = TimeUnit.MILLISECONDS;
        private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100);
        private SolutionOne solution;
        private HtmlParser parser;
        
        private CrawlerExecutor(SolutionOne sol, HtmlParser hp){
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
            solution = sol;
            parser = hp;
        }
        
        public void submit(String url) {
            super.submit(new CrawlingTask(url, solution, parser));
        }
        
        private boolean hasQueueCapacity(){
            if (workQueue.remainingCapacity() > 0){
                return true;
            } else {
                return false;
            }
        }
    }
    
    public static String getDomainName(String url) throws java.net.URISyntaxException {
        java.net.URI uri = new java.net.URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
    
    private Set<String> result = new HashSet<String>();
    private BlockingQueue<String> toCrawl = new LinkedBlockingQueue<String>();
    private String prefix;
    
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        try {
            prefix = "http://" + getDomainName(startUrl);
            System.out.println("prefix: "+prefix);
            result.add(startUrl);
            toCrawl.put(startUrl);
            CrawlerExecutor executor = new CrawlerExecutor(this, htmlParser);
            while(!toCrawl.isEmpty()){
            	System.out.println("toCrawl is not empty");
                while (executor.hasQueueCapacity() && !toCrawl.isEmpty()){
                	System.out.println("Submitting urls");
                    executor.submit(toCrawl.take());
                }
                if (toCrawl.isEmpty() && executor.getActiveCount() > 0){
                    synchronized(result){
                    	System.out.println("Waiting");
                        result.wait(executor.keepAliveTime+1);
                    }
                }
                System.out.println("One while loop complete");
            }
            executor.shutdown();
        }catch(java.net.URISyntaxException us){
            
        }catch(InterruptedException ie){
                                
        }
        return new ArrayList<String>(result);
    }
}