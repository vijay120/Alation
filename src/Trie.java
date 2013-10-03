import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Trie {
 
        private Node root = new Node('\0', "");
 
        public Trie() {}
 
        public Trie(List<String> argInitialWords) {
                for (String word:argInitialWords) {
                        addWord(word.toCharArray(), -1, word);
                }
        }
                
        public void addWord(char[] argWord, Integer value, String stringVal) {
                Node currentNode = root;
 
                for (int i = 0; i < argWord.length; i++) {
                	
                		if(argWord[i] == '_') {
                			char[] newCharArray = Arrays.copyOfRange(argWord, i+1, argWord.length); 
                			addWord(newCharArray, value, stringVal);
                		}         	
                	
                        if (!currentNode.containsChildValue(argWord[i])) {
                                currentNode.addChild(new Node(argWord[i], currentNode.getValue() + argWord[i]));
                        }
 
                        currentNode = currentNode.getChild(argWord[i]);
                        currentNode.insertIntoTopScore(value, stringVal);
                }
 
                currentNode.setIsWord(true);
                currentNode.index = value;
                currentNode.stringVal = stringVal;
        }
         
        public boolean containsPrefix(String argPrefix) {
                return contains(argPrefix.toCharArray(), false);
        }
 
        public boolean containsWord(String argWord) {
                return contains(argWord.toCharArray(), true);
        }
 
        public Node getWord(String argString) {
                Node node = getNode(argString.toCharArray());
                return node != null && node.isWord() ? node : null;
        }
        
        public String[] getBestScores(String argString) {
        	Node node = getNode(argString.toCharArray());
        	        	
        	if(node != null) {
        		int size = node.topScores.size();
        		String[] result = new String[size];
        		int counter = size-1;
        		for (Map.Entry<Integer, String> entry : node.topScores.entrySet())
        		{
        			result[counter] = entry.getValue();
        			counter--;
        		}
        		return result;
        	}
        	
        	else {
        		return null;
        	}
        	
        }
 
        public Node getPrefix(String argString) {
                return getNode(argString.toCharArray());
        }
 
        @Override
        public String toString() {
                return root.toString();
        }
 
        private boolean contains(char[] argString, boolean argIsWord) {
                Node node = getNode(argString);
                return (node != null && node.isWord() && argIsWord) || (!argIsWord && node != null);
        }
 
        private Node getNode(char[] argString) {
                Node currentNode = root;
 
                for (int i = 0; i < argString.length && currentNode != null; i++) {
                        currentNode = currentNode.getChild(argString[i]);
 
                        if (currentNode == null) {
                                return null;
                        }
                }
 
                return currentNode;
        }
}