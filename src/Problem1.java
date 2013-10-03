import java.io.*;
import java.util.Scanner;

public class Problem1 {
		
    public static void main(String[] args) {
    	
    	ReadWriteTextFileWithEncoding a = 
    			new ReadWriteTextFileWithEncoding("src/test.txt", "UTF-8");  	
    	try {
    		a.read();
    	} catch (IOException e) {
    		System.err.println("IOException: " + e.getMessage());
    	}
    	
    	String result = a.finalTrie; 	
    	Scanner scanner = new Scanner (System.in);
    	System.out.println("Enter your query:");  
    	String query = scanner.next();
    	QueryAnalyzer b = new QueryAnalyzer(result, query);    	

    }
}
