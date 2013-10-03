Problem 1 (Estimated time: 4.5 hours)

Related files: Problem1, QueryAnalyzer, ReadWriteTextFile, Trie, Node.
Test file: test.txt (you need to programatically change the test file name if you want to use your own set)
External library: I used gson for serialization and de-serialization of data.
External source: I adapted code from wikipedia's version of a Trie. (Source: http://en.wikipedia.org/wiki/Trie)

Approach and Basic Implementation:

I started out with a hash-table were the word was a key and the score was a value.
I immediately realized that this would not give we sub-linear query processing!

So I knew I had to use a Trie for sub-linear query processing. By having most of the
compute time in the construction of the tree, one can get very fast query processing.

Within the Trie, I knew that I had keep track of the top 10 word scores for each node.
To get optimal performance for this, I had to use a TreeMap since it's internal implementation
uses a red-black tree, which gives us O(log(n)) worst case insertion time.

I had to use some tricks to make the weird case of "blah_target" to propagate to the leaf of the 
"target" node. The trick involved keeping the name of the word persistent through the tree traversal.

I used gson for serialization and de-serialization of data. This proved that my trie data structure
was serializable!

Runtime for query processing: O(log(n)), where n is the total number of words.
Theoretically, each node can branch out to a maximum of 26 alphabets in the next level, the height of
the tree will be O(log_26(n)), which approximates to O(log(n)) anyways.

How to run it?

1) Run Problem1.java
2) On the "Enter your query:" command prompt, type: ho

------------------------------------------------------------------------------------------------------------

Problem 2 (Estimated time: 2 hours)

Related files: WordCountParallel
Test file: test2.txt (you need to programatically change the test file name if you want to use your own set)
Adapted from a programming assignment I did in college.
Output file: src/output.txt

I knew I had to take advantage of parallel processing in java in order to do this problem. Since I have 4 cores,
I made 1 processor to be the i/o program, and the other three would do the heavy lifting of tokenizing, counting
and inserting the word counts.

I knew I had to use a concurrent hashmap for parallel write and reads synchornization, along with the use of 
compare and swap to make sure I have atomic writes.

The runtime of this program is O(n) since all the operations are linear.

How to run it?

1) Run WordCountParallel.java
2) Read src/output.txt

