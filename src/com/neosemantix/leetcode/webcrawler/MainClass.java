package com.neosemantix.leetcode.webcrawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eclipsesource.json.JsonArray;

/**
 * @author umeshpatil
 *
 */
public class MainClass {
	
	public static String[] urls;
	public static Map<String, Integer> urlToIndex = new HashMap<String, Integer>();
	public static Map<Integer, List<Integer>> urlIndexToYield = new HashMap<Integer, List<Integer>>();
	
    public static String[] stringToStringArray(String line) {
        JsonArray jsonArray = JsonArray.readFrom(line);
        String[] arr = new String[jsonArray.size()];
        for (int i = 0; i < arr.length; i++) {
          arr[i] = jsonArray.get(i).asString();
          urlToIndex.put(arr[i], i);
        }
        return arr;
    }
    
    public static int[] stringToIntegerArray(String input) {
        input = input.trim();
        input = input.substring(1, input.length() - 1);
        if (input.length() == 0) {
          return new int[0];
        }
    
        String[] parts = input.split(",");
        int[] output = new int[parts.length];
        for(int index = 0; index < parts.length; index++) {
            String part = parts[index].trim();
            output[index] = Integer.parseInt(part);
        }
        return output;
    }
    
    public static int[][] stringToInt2dArray(String input) {
        JsonArray jsonArray = JsonArray.readFrom(input);
        if (jsonArray.size() == 0) {
          return new int[0][0];
        }
    
        int[][] arr = new int[jsonArray.size()][];
        for (int i = 0; i < arr.length; i++) {
          JsonArray cols = jsonArray.get(i).asArray();
          arr[i] = stringToIntegerArray(cols.toString());
          addEdge(arr[i][0], arr[i][1]);
        }
        return arr;
    }
    
    private static void addEdge(int src, int dest) {
    	List<Integer> dests = urlIndexToYield.get(src);
    	if (dests == null) {
    		dests = new ArrayList<Integer>();
    		urlIndexToYield.put(src, dests);
    	}
    	dests.add(dest);
    }
    
    public static String stringToString(String input) {
        return JsonArray.readFrom("[" + input + "]").get(0).asString();
    }
    
    
    public static void main(String[] args) throws IOException {
        //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String startUrl = "http://sta.zizqt.xyz/nelq";
//        while ((line = in.readLine()) != null) {
//            //String[] 
//            		urls = stringToStringArray(line);
//            line = in.readLine();
//            int[][] edges = stringToInt2dArray(line);
//            line = in.readLine();
//            
//            //startUrl = stringToString(line);
//            
////            int ret = new SolutionOne().foobar(urls, edges, startUrl);
////            String out = String.valueOf(ret);
////            System.out.print(out);
//            
//
//        }
        BufferedReader fr = new BufferedReader(new FileReader("/Users/umeshpatil/git/coding-exercise/src/com/neosemantix/leetcode/webcrawler/Urls.json"));
        String line = fr.readLine();
        urls = stringToStringArray(line);
        
        BufferedReader fr2 = new BufferedReader(new FileReader("/Users/umeshpatil/git/coding-exercise/src/com/neosemantix/leetcode/webcrawler/edges.json"));
        String line2 = fr2.readLine();
        int[][] edges = stringToInt2dArray(line2);
        
        Solution sol = new Solution();
        HtmlParser htmlParser = new HtmlParser();
        long start = System.currentTimeMillis();
        List<String> result = sol.crawl(startUrl, htmlParser);
        long end = System.currentTimeMillis();
        System.out.println("\n Found: " + result.size() + " links in time "+ (end-start) + "ms "+result);
        System.out.println("\nDone");
    }
    
    public static class HtmlParser {
    	
    	public List<String> getUrls(String url) {
    		int index = urlToIndex.get(url);
    		List<Integer> yui = urlIndexToYield.get(index);
    		List<String> result = new ArrayList<String>();
    		if (yui != null) {
    			int sz = yui.size();
    			for(int i=0; i<sz; i++) {
    				result.add(urls[i]);
    			}
    		}
    		return result;
    	}
    }
    
//    public static void main(String[] args) {
//    	String line = "[\"http://news.yahoo.com\",\"http://news.yahoo.com/news\",\"http://news.yahoo.com/news/topics/\",\"http://news.google.com\"]";
//    	urls = stringToStringArray(line);
//    	String el = "[[0,2],[2,1],[3,2],[3,1],[3,0]]";
//    	int[][] edges = stringToInt2dArray(el);
//    	String startUrl = "http://news.google.com";
//    }
}
