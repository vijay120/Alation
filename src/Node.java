import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class Node {
 
        private final Character ch;
        private final String value;
        private Map<String, Node> children = new HashMap<String, Node>();
        private boolean isValidWord;
        public Integer index = -1;
        public String stringVal = "";
        public TreeMap<Integer, String> topScores = new TreeMap<Integer, String>(Collections.reverseOrder());
 
        public Node(char argChar, String argValue) {
                ch = argChar;
                value = argValue;
        }
 
        public boolean addChild(Node argChild) {
                if (children.containsKey(Character.toString(argChild.getChar()))) {
                        return false;
                }
 
                children.put(Character.toString(argChild.getChar()), argChild);
                return true;
        }
 
        public boolean containsChildValue(char c) {
                return children.containsKey(Character.toString(c));
        }
 
        public String getValue() {
                return value.toString();
        }
        
        public void insertIntoTopScore(Integer key, String value) { 
        	topScores.put(key, value);
        	if(topScores.size() > 10) {
        		//since this will be the lowest score.
        		topScores.remove(topScores.lastKey());
        	}        	
        }
              
        public char getChar() {
                return ch;
        }
 
        public Node getChild(char c) {
                return children.get(Character.toString(c));
        }
 
        public boolean isWord() {
                return isValidWord;
        }
 
        public void setIsWord(boolean argIsWord) {
                isValidWord = argIsWord;
 
        }
 
        public String toString() {
                return value;
        }
 
}

