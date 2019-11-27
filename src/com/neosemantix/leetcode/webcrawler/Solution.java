// Copyright 2019; All rights reserved with NeoSemantix, Inc.
package com.neosemantix.leetcode.webcrawler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.neosemantix.leetcode.webcrawler.MainClass.HtmlParser;

/**
 * @author umeshpatil
 *
 */
class Solution {
    
    private static int pendingTasks;
    private static int queueSize = 30;
    private static long keepAliveTime = 1;
    private static TimeUnit unit = TimeUnit.MILLISECONDS;
    
    private static class CrawlingTask implements Runnable {
        
        private String surl;
        private Solution solution;
        private HtmlParser parser;
        private List<String> yield;
        
        private CrawlingTask(String s, Solution sol, HtmlParser hp){
            surl = s;
            solution = sol;
            parser = hp;
        }
        
        public void run() {
        	yield = parser.getUrls(surl);
            if (!yield.isEmpty()){       
                 synchronized(solution.result) {
                    for(String y: yield){
                        if (y.startsWith(solution.prefix) && !solution.result.contains(y)){
                            solution.result.add(y);
                            try {
								solution.toCrawl.put(y);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }
                        // else already crawled or different domain, we can ignore
                    }
                }
            }
            pendingTasks--;
            System.out.println("Parsing done for url: "+surl);
        }
    }
    
    private static class CrawlerExecutor extends ThreadPoolExecutor {
        
        private static int corePoolSize = 1;
        private static int maximumPoolSize = 10;
        private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(queueSize);
        private Solution solution;
        private HtmlParser parser;
        
        private CrawlerExecutor(Solution sol, HtmlParser hp){
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
            solution = sol;
            parser = hp;
            this.allowCoreThreadTimeOut(true);
        }
        
        public void submit(String url) {
            super.submit(new CrawlingTask(url, solution, parser));
            pendingTasks++;
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
    private BlockingQueue<String> toCrawl = new ArrayBlockingQueue<String>(queueSize);
    		//Collections.synchronizedList(new ArrayList<String>());
    private String prefix;
    
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        CrawlerExecutor executor = new CrawlerExecutor(this, htmlParser);
        try {
            prefix = "http://" + getDomainName(startUrl);
            result.add(startUrl);
            toCrawl.add(startUrl);
            int inQ = toCrawl.size();
            System.out.println("Starting crawling from url: " + startUrl);
            while( (inQ > 0) || (pendingTasks > 0) ){
            	for (int j=0; j<inQ && executor.hasQueueCapacity(); j++){
            		executor.submit(toCrawl.poll());
                }
            	String tu = toCrawl.poll(keepAliveTime, unit);
            	if (tu != null) {
            		if (executor.hasQueueCapacity()) {
            			executor.submit(tu);
            		} 
            	}
            	
                inQ = toCrawl.size();
                System.out.println("inQ: " + inQ);
            }
        }catch(java.net.URISyntaxException us){
            
        }catch(InterruptedException ie){
                                
        }
        return new ArrayList<String>(result);
    }
}
