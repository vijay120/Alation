import com.google.gson.Gson;

public class QueryAnalyzer {
	
	public QueryAnalyzer(String jsonTree, String query) {	
		Gson gson = new Gson();
		Trie deserializedTrie = gson.fromJson(jsonTree, Trie.class);
		
		String[] result = deserializedTrie.getBestScores(query);
		
		for( int i = 0; i < result.length; i++)
		{
			System.out.println(result[i]);
		}		
	}
}
