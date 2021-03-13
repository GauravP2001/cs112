package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		TrieNode tree = new TrieNode(null, null, null);
		
		for (int i = 0; i < allWords.length; i++) {
			
			String temp = allWords[i];
			
			if (tree.firstChild == null) {	// for the first child when the tree is empty

				tree.firstChild = new TrieNode(new Indexes(i, (short) 0, (short) (allWords[i].length() - 1)), null, null);
				continue;
				
			} else {
				
				TrieNode ptr = tree.firstChild;

				while (ptr != null) {
					
					if (allWords[ptr.substr.wordIndex].charAt(ptr.substr.startIndex) == temp.charAt(0)) { // if the first letters match
			
						String match = matchCount(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, 1 + (ptr.substr.endIndex)), temp);
						

						
						if (match.equals(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, 1 + ptr.substr.endIndex))) { //if it equals the whole substring (prefix)
							
							int end = temp.indexOf(match.charAt(match.length() - 1));
							temp = temp.substring(end + 1);
							
							
							ptr = ptr.firstChild;
							
						} else {
							
							if (ptr.firstChild != null) {
								
								int end = temp.indexOf(match.charAt(match.length() - 1));
								temp = temp.substring(end + 1);

								int e = allWords[i].indexOf(match.charAt(match.length()-1));

								
								TrieNode tmp = ptr.firstChild;
								
								TrieNode newNode = new TrieNode(new Indexes(ptr.substr.wordIndex, (short) ((allWords[ptr.substr.wordIndex].indexOf(match.charAt(match.length()-1))) + 1), (short) ptr.substr.endIndex), null, new TrieNode(new Indexes(i, (short) (e + 1), (short) (allWords[i].length() - 1)), null, null));
								
								ptr.substr.startIndex = (short) allWords[ptr.substr.wordIndex].indexOf(match.charAt(0));
								ptr.substr.endIndex = (short) allWords[ptr.substr.wordIndex].indexOf(match.charAt(match.length()-1));
								
								ptr.firstChild = newNode;
								newNode.firstChild = tmp;
								
								break;
							} else {
								
								int endIndex = getEndIndex(allWords[ptr.substr.wordIndex], allWords[i], match.charAt(match.length() - 1), ptr.substr.startIndex);
								
								
								ptr.substr.endIndex = (short) endIndex;
								
								ptr.firstChild = new TrieNode(new Indexes(ptr.substr.wordIndex, (short) ((ptr.substr.endIndex) + 1), (short) (allWords[ptr.substr.wordIndex].length() - 1)), null, new TrieNode(new Indexes(i, (short) ((ptr.substr.endIndex) + 1), (short) (allWords[i].length() - 1)), null, null));
				
								break;	
							}
						}
												
					} else {
						
						if (ptr.firstChild == null && ptr.sibling == null) {			// if there is only one node so far
							
							ptr.sibling = new TrieNode(new Indexes(i, (short) allWords[i].indexOf(temp.charAt(0)), (short) (allWords[i].length() - 1)), null, null);
							break;
							
						} else if (ptr.sibling != null) {		// if there are multiple childs for the parent node 
							ptr = ptr.sibling;
							
						} else {
							
							ptr.sibling = new TrieNode(new Indexes(i, (short) allWords[i].indexOf(temp.charAt(0)), (short) (allWords[i].length() - 1)), null, null);
							break;
							
							
							
							
						}	
					}	
				}	
			}
		}
		
		return tree;
	}
		
	private static String matchCount(String a, String b) {
		
		String temp = "";
		
		for (int i = 0; i <= a.length() - 1; i++) {
			
			if (a.charAt(i) == b.charAt(i)) {
				temp += a.charAt(i);
			} else {
				break;
			}
		}
		
		return temp;
	}
	
	private static int getEndIndex(String s, String d, char lastCharacter, int st) {
		
		int index = 0;
		
		for (int i = st; i < s.length() - 1; i++) {
			
			if (s.charAt(i) == lastCharacter && s.charAt(i + 1) == lastCharacter && i != s.length() - 1 && d.charAt(i + 1) == lastCharacter) {
				index = i + 1;
			} else if (s.charAt(i) == lastCharacter) {
				index = i;
				break;
			}		
		}
		
		return index;
	}
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		ArrayList<TrieNode> list = new ArrayList<TrieNode>();
		TrieNode ptr = root.firstChild;
		
		while (ptr != null) {
						
			if (allWords[ptr.substr.wordIndex].startsWith(prefix) && ptr.firstChild == null) { 

				list.add(ptr);
				ptr = ptr.sibling;
				
			} else if (prefix.startsWith(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex)) || allWords[ptr.substr.wordIndex].startsWith(prefix)) {  // if it ONLY contains the prefix
				
				ArrayList<TrieNode> a = completionList(ptr, allWords, prefix);
				
				if (a == null) {
					ptr = ptr.sibling;
					continue;
				} 
				
				list.addAll(a);
				ptr = ptr.sibling;
				
			} else {
				ptr = ptr.sibling;
			}	
		}
		
		if (list.isEmpty()) {
			return null;
		}
		
		return list;
		
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
