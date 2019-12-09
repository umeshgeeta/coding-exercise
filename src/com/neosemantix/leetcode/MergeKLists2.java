// Copyright 2019; All rights reserved with NeoSemantix, Inc.
package com.neosemantix.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author umeshpatil
 *
 */
public class MergeKLists2 {
    
    private static class ListNodeIndex  {
        ListNode node;
        int index;	// in which list number we find this node
        private ListNodeIndex(ListNode ln, int i){
            node = ln;
            index = i;
        }
    }
    
    private List<ListNodeIndex> currentMins;
    
    public ListNode mergeKLists(ListNode[] lists) {
        ListNode mergedList = null;
        if (lists != null && lists.length > 0){
            currentMins = new ArrayList<ListNodeIndex>();
            ListNode[] whereInList = new ListNode[lists.length];
            // add first node
            if (lists[0] != null) {
            	currentMins.add(new ListNodeIndex(lists[0], 0));
                whereInList[0] = lists[0];
            }
            
            // add / insert rests of the nodes
            for(int k=1; k<lists.length; k++){
            	if (lists[k] != null) {
            		whereInList[k] = lists[k];
                    insert(lists[k], k);
            	}
            }
            printCurrentMins();
            ListNode whereInMergedList = null;	
            while (currentMins.size() > 0){
            	// minimum of the first elements of all list is the minimum of the merged list
            	ListNodeIndex wrapperOfMinNode = currentMins.remove(0);
            	if (mergedList == null) {
            		// first time, add the head
            		mergedList = wrapperOfMinNode.node;
            		whereInMergedList = mergedList;
            	} else {
                    whereInMergedList.next = wrapperOfMinNode.node;
                	whereInMergedList = whereInMergedList.next;
            	}
                int minIndex = wrapperOfMinNode.index;
                ListNode minAdded = whereInList[minIndex];   // already merged 
                
                int nextVal = Integer.MAX_VALUE;
                if (currentMins.size() > 0){
                    nextVal = currentMins.get(0).node.val;
                }
                
                while (minAdded.next != null && minAdded.next.val <= nextVal){
                	System.out.println("minAdded.next: " + minAdded.next + " minAdded.next.val: " + minAdded.next.val);
                	whereInMergedList.next = minAdded.next;
                	whereInMergedList = whereInMergedList.next;
                    minAdded = minAdded.next;
                }
                if (minAdded.next != null){
                    insert(minAdded.next, minIndex);
                }
                
                whereInList[minIndex] = minAdded.next;
                
                printCurrentMins();
            }
            System.out.println("outside of while loop");
        }
        return mergedList;
    }
    
    private void insert(ListNode ls, int index){
        int minsFoundSoFar = currentMins.size();
        boolean inserted = false;
        int incomingValue = ls.val;
        ListNodeIndex wrapper = new ListNodeIndex(ls, index);
        int j = 0;
        while (!inserted && j < minsFoundSoFar){
            int valToCompare = currentMins.get(j).node.val;
            if (incomingValue < valToCompare){
                currentMins.add(j, wrapper);
                inserted = true;
            }
            j++;
        }
        if (!inserted){
            //we need to add it to the end
            currentMins.add(wrapper);
        }
    }
    
    public static void main(String[] args) {
    	MergeKLists2 mkl = new MergeKLists2();
    	ListNode[] lists = new ListNode[3];
    	lists[0] = mkl.createAList(1,4,5);
    	lists[1] = mkl.createAList(1,3,5);
    	lists[2] = mkl.createAList(2,6);
    	System.out.println("lists created");
    	ListNode mergedList = mkl.mergeKLists(lists);
    	mkl.printList(mergedList);
    }
    
    private ListNode createAList(int...args) {
    	ListNode head = null;
    	ListNode end = null;
    	for(int i: args) {
    		ListNode ln = new ListNode(i);
    		if (head != null) {
    			end.next = ln;
    		} else {
    			head = ln;
    		}
    		end = ln;
    	}
    	return head;
    }
    
    private void printList(ListNode head) {
    	StringBuffer sb = new StringBuffer();
    	sb.append("{");
    	while (head != null) {
    		sb.append(head.val + ",");
    		head = head.next;
    	}
    	sb.append("}");
    	System.out.println(sb.toString());
    }
    
    private void printCurrentMins() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("{");
    	for(ListNodeIndex lni: currentMins) {
    		sb.append("(val="+lni.node.val+", index="+lni.index+"), ");
    	}
    	sb.append("}");
    	System.out.println(sb.toString());
    }
}
