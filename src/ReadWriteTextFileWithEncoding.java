import java.io.*;
import java.util.Scanner;
import java.util.Hashtable;
import com.google.gson.Gson;


/** 
 Read and write a file using an explicit encoding.
 JDK 1.5.
 Removing the encoding from this code will simply cause the 
 system's default encoding to be used instead.  
*/
public final class ReadWriteTextFileWithEncoding {
	
	public String finalTrie;
	
  /** Requires two arguments - the file name, and the encoding to use.  */
  public static void main(String... aArgs) throws IOException {
    String fileName = aArgs[0];
    String encoding = aArgs[1];
    ReadWriteTextFileWithEncoding test = new ReadWriteTextFileWithEncoding(
      fileName, encoding
    );
    test.write();
    test.read();
  }
  
  public static String[] parseLineFirstStringSecondInt(String inputLine) {	  
	  String[] seperated = inputLine.split(",");  
	  return seperated;
  }
  
  
  /** Constructor. */
  ReadWriteTextFileWithEncoding(String aFileName, String aEncoding){
    fEncoding = aEncoding;
    fFileName = aFileName;
    serializableTable = new Hashtable<String, Integer>();
    serializableTrie = new Trie();
  }
  
  /** Write fixed content to the given file. */
  void write() throws IOException  {
    log("Writing to file named " + fFileName + ". Encoding: " + fEncoding);
    Writer out = new OutputStreamWriter(new FileOutputStream(fFileName), fEncoding);
    try {
      out.write(FIXED_TEXT);
    }
    finally {
      out.close();
    }
  }
  
  /** Read the contents of the given file. */
  void read() throws IOException {
    Scanner scanner = new Scanner(new FileInputStream(fFileName), fEncoding);
    try {
      while (scanner.hasNextLine()){ 
    	  String inputLine = scanner.nextLine();  	      	  
    	  String[] seperatedValue = parseLineFirstStringSecondInt(inputLine);
    	  String name = seperatedValue[0];
    	  Integer score = Integer.parseInt(seperatedValue[1]);
    	  serializableTable.put(name, score);	
    	  serializableTrie.addWord(name.toCharArray(), score, name);
      }
    }
    finally{
      scanner.close();
    }
    String json = new Gson().toJson(serializableTrie);
    finalTrie = json;
  }
    
  // PRIVATE 
  private Hashtable<String, Integer> serializableTable;
  private Trie serializableTrie;
  private final String fFileName;
  private final String fEncoding;
  private final String FIXED_TEXT = "But soft! what code in yonder program breaks?";
  
  private void log(String aMessage){
    System.out.println(aMessage);
  }
}
