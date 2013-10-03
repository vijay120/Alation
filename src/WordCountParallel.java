import java.util.StringTokenizer;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordCountParallel implements Runnable {
    private final String buffer;
    
    //We use concurrent map since it gives use guarantee that if we have a write before a read 
    //in a program execution, the events are sequentially reflected in concurrent threads. This
    //does not mean we have atomic updates into the map. We will implement that.
    //Source: http://www.javamex.com/tutorials/synchronization_concurrency_8_hashmap2.shtml
    private final ConcurrentMap<String,Integer> counts;

    public WordCountParallel(String buffer, 
                             ConcurrentMap<String,Integer> counts) {
        this.counts = counts;
        this.buffer = buffer;
    }

    private final static String DELIMS = " :;,.{}()\t\n";

    /**
     * Looks for the last delimiter in the string, and returns its
     * index.
     */
    private static int findDelim(String buf) {
        for (int i = buf.length() - 1; i>=0; i--) {
            for (int j = 0; j < DELIMS.length(); j++) {
                char d = DELIMS.charAt(j);
                if (d == buf.charAt(i)) return i;
            }
        }
        return 0;
    }

    /** 
     * Reads in a chunk of the file into a string.  
     */
    private static String readFileAsString(BufferedReader reader, int size)
        throws java.io.IOException 
    {
        StringBuffer fileData = new StringBuffer(size);
        int numRead=0;

        while(size > 0) {
            int bufsz = 1024 > size ? size : 1024;
            char[] buf = new char[bufsz];
            numRead = reader.read(buf,0,bufsz);
            if (numRead == -1)
                break;
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            size -= numRead;
        }
        return fileData.toString();
    }

    /**
     * Compare and swap implementation
     */
    private void updateCount(String q) {
        Integer oldVal, newVal;
        Integer cnt = counts.get(q);
        if (cnt == null) {
            oldVal = counts.put(q, 1);
            if (oldVal == null) return;
        }
        do {
            oldVal = counts.get(q);
            newVal = (oldVal == null) ? 1 : (oldVal + 1);
        } while (!counts.replace(q, oldVal, newVal));
    } 

    /**
     * Main task: tokenizes the given buffer and counts words. 
     */
    public void run() {
        StringTokenizer st = new StringTokenizer(buffer,DELIMS);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            updateCount(token);
        }
    } 

    public static void main(String args[]) throws java.io.IOException {
    	   	
        long startTime = System.currentTimeMillis();
        
        //three cores for parallel processing
        int numThreads = 3;
        
        // Char size is 2 bytes. Assuming that most of the 4GB in main memory is used by the thread's
        // stack space, so the amount of characters that can be processed is 2*10^9. This also assumes
        // that a string has not overhead compared to a char array. Therefore, the chunk size can be
        // increased to 2*10^9. It is 1000 for testing purposes.
        int chunksize = 1000;

        //creates a thread pool of 3 parallel threads.       
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
        BufferedReader reader = new BufferedReader(new FileReader("src/test2.txt"));
        
        //The 4th processor, which is the main program, reads from input and writes to output.
        ConcurrentMap<String,Integer> m = 
            new ConcurrentHashMap<String,Integer>();
                
        String leftover = ""; // in case a string broken in half
        
        while (true) {
        	
        	//reads a chunk from the input file and stringy-fies it.
            String res = readFileAsString(reader,chunksize);
                 
            //only the left overs are left, process that
            if (res.equals("")) {
                if (!leftover.equals("")) 
                    new WordCountParallel(leftover,m).run();
                break;
            }
            
            //Find the last delimited index in the chunk of string. This is needed since
            //we need to seperate the chunk of data on a delimited spacing, and not on a word,
            //which would be bad.
            int idx = findDelim(res);
            String taskstr = leftover + res.substring(0,idx);
            
            //The left overs from the split will be processed on the next core.
            leftover = res.substring(idx,res.length());
            
            //Submit the current chunk for processing.
            pool.submit(new WordCountParallel(taskstr,m));                              
        }
        pool.shutdown();
        try {
            pool.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            System.out.println("Pool interrupted!");
            System.exit(1);
        }
        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        int total = 0;
        
        File file = new File("src/output.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter(file); 
        
        for (Map.Entry<String,Integer> entry : m.entrySet()) {
            int count = entry.getValue();
            String outputWord = String.format("%s,%d\n",entry.getKey(),count);
            writer.write(outputWord);
            total += count;
        }
        writer.flush();
        writer.close();
        System.out.println("Total words = "+total);
        System.out.println("Total time = "+elapsed+" ms");
    }
}
