package com.neosemantix.leetcode.concurrency;

/**
 * https://leetcode.com/problems/print-in-order/
 * 
 * 1114. Print in Order
 * 
 * Suppose we have a class:
 * public class Foo {
 * public void first() { print("first"); }
 * public void second() { print("second"); }
 * public void third() { print("third"); }
 * }
 * 
 * The same instance of Foo will be passed to three different threads. 
 * Thread A will call first(), thread B will call second(), and thread C 
 * will call third(). Design a mechanism and modify the program to ensure 
 * that second() is executed after first(), and third() is executed after second().
 * 
 * Example 1:
 * Input: [1,2,3]
 * Output: "firstsecondthird"
 * Explanation: There are three threads being fired asynchronously. 
 * The input [1,2,3] means thread A calls first(), thread B calls second(), 
 * and thread C calls third(). "firstsecondthird" is the correct output.
 * 
 * Example 2:
 * Input: [1,3,2]
 * Output: "firstsecondthird"
 * Explanation: The input [1,3,2] means thread A calls first(), 
 * thread B calls third(), and thread C calls second(). "firstsecondthird" 
 * is the correct output.
 * 
 * @author umeshpatil
 *
 */
class Foo {
    
    private static FooLock singleton;
    
    private static FooLock getFooLock(){
        if (singleton == null){
            synchronized(Foo.class){
                if (singleton == null){
                    singleton = new FooLock();
                }
            }
        }
        return singleton;
    }
    
    private static class FooLock {
        private boolean firstDone;
        private boolean secondDone;        
    }

    /**
     * Grab the lock, run and then notify waiting threads.
     * 
     * @param printFirst
     * @throws InterruptedException
     */
    public void first(Runnable printFirst) throws InterruptedException {
        FooLock fl = getFooLock();
        synchronized(fl){
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            fl.firstDone = true;
            fl.notifyAll();
        }
    }

    /**
     * Wait unti the first is done, then grab the lock and run the second.
     * 
     * @param printSecond
     * @throws InterruptedException
     */
    public void second(Runnable printSecond) throws InterruptedException {
        FooLock fl = getFooLock();
            while(!fl.firstDone){
                synchronized(fl){
                    if (!fl.firstDone){
                        fl.wait();
                    }
                    // else no need to wait
                }
            }
        synchronized(fl){
            // printSecond.run() outputs "second". Do not change or remove this line.   
            printSecond.run();
            fl.secondDone = true;
            fl.notifyAll();
        }
    }

    /**
     * Wait until second is done, then grab the lock and run the third.
     * 
     * @param printThird
     * @throws InterruptedException
     */
    public void third(Runnable printThird) throws InterruptedException {
        FooLock fl = getFooLock();
            while(!fl.secondDone){
                synchronized(fl){
                    if (!fl.secondDone){
                        fl.wait();
                    }
                }
            }
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
        fl.firstDone = false;
        fl.secondDone = false;
    }
    
    
    /**
     * Main for testing.
     * 
     * @param args
     */
    public static void main(String[] args) {
    	PrintRunner pr1 = new PrintRunner("first");
    	PrintRunner pr2 = new PrintRunner("second");
    	PrintRunner pr3 = new PrintRunner("third");
    	Foo foo = new Foo();
    	
    	System.out.println("Input: [1,2,3]");
    	Thread t1 = new FooThread(foo, pr1, 0);
    	Thread t2 = new FooThread(foo, pr2, 1);
    	Thread t3 = new FooThread(foo, pr3, 2);
    	t1.start();
    	t2.start();
    	t3.start();
    	
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println("\nInput: [1,3,2]");
    	Thread ta = new FooThread(foo, pr1, 0);
    	Thread tb = new FooThread(foo, pr3, 2);
    	Thread tc = new FooThread(foo, pr2, 1);
    	ta.start();
    	tb.start();
    	tc.start();
    	
    	/*
    	 * Expected output:
    	 * 
    	 * Input: [1,2,3]
    	 * firstsecondthird
    	 * Input: [1,3,2]
    	 * firstsecondthird
    	 * 
    	 */
    }
    
    /**
     * Thread class which invokes PrinterRunners.
     */
    private static class FooThread extends Thread {
    	
    	private Foo foo;
    	private Runnable pr;
    	private int which;
    	
    	private FooThread(Foo f, Runnable printRunner, int index) {
    		foo = f;
    		pr = printRunner;
    		which = index;
    	}
    	
    	public void run() {
    		try {
        		switch (which) {
        		
        		case 0: foo.first(pr);	break;
        		
        		case 1: foo.second(pr);	break;
        		
        		case 2: foo.third(pr);	break;
        		}
    		} catch(InterruptedException exp) {
    			System.out.println(exp);
    		}
    	}
    	
    }
    
    /**
     * PrintRunner which simply prints the provided order.
     *
     */
    private static class PrintRunner implements Runnable {
    	
    	private String order;
    	
    	private PrintRunner(String o) {
    		order = o;
    	}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			System.out.print(order);
		}
    	
    }
}