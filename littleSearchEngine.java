package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		
		if (docFile == null) {
			throw new FileNotFoundException();
		}
		
		HashMap<String, Occurrence> hMap = new HashMap<String, Occurrence>();
		
		Scanner scan = new Scanner(new File(docFile));
		
		while (scan.hasNext()) {
			
			String tempKey = getKeyword(scan.next());
			
			if (tempKey != null) {
				
				if (hMap.containsKey(tempKey)) {
					
					Occurrence obj = hMap.get(tempKey);
					obj.frequency++;	
					
				} else {
					
					Occurrence obj = new Occurrence(docFile, 1);
					hMap.put(tempKey, obj);
					
				}
			}
			
		}

		return hMap;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		
		for (String word : kws.keySet()) {
			
			ArrayList<Occurrence> temp = new ArrayList<Occurrence>();
			
			if (keywordsIndex.containsKey(word)) {
				
				temp = keywordsIndex.get(word);
			}
			
			temp.add(kws.get(word));
			insertLastOccurrence(temp);
			keywordsIndex.put(word, temp);	
		}
		
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		String tmp = word.toLowerCase();
		
		for (int i = tmp.length() - 1; i >= 0; i--) {

			if ((i == tmp.length() - 1) && (tmp.charAt(i) == '.'|| tmp.charAt(i) == ','|| tmp.charAt(i) == '?'|| tmp.charAt(i) == ':'|| tmp.charAt(i) == ';'|| tmp.charAt(i) == '!')) {
				tmp = tmp.substring(0,i);
			} else if (!(Character.isLetter(tmp.charAt(i)))) {
				tmp = tmp.substring(0,i);
			} else {
				break;
			}
		}
		
		for (int i = 0; i < tmp.length() - 1; i++) {

			if (tmp.charAt(i) == '.'|| tmp.charAt(i) == ','|| tmp.charAt(i) == '?'|| tmp.charAt(i) == ':'|| tmp.charAt(i) == ';'|| tmp.charAt(i) == '!') {
				return null;
			} else if (!(Character.isLetter(tmp.charAt(i)))) {
				return null;
			} 
		}
		Object s = tmp.toLowerCase();
		
		if (noiseWords.contains(s) || s.equals("")) {
			return null;
		}
		
		return tmp.toLowerCase();
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		
		if (occs.size() < 2) {
			return null;
		}
		
		ArrayList<Integer> midpointList = new ArrayList<Integer>();
		
		int high = occs.size() - 2;
		int mid = 0;
		int low = 0;
		
		
		int target = occs.get(occs.size()-1).frequency;
		
		while (high >= low) {
			
			mid = (high + low) / 2;
			midpointList.add(mid);
			
			if (occs.get(mid).frequency == target) {
				break;
			} else if (occs.get(mid).frequency > target) {
				low = mid + 1;
				
				if (high <= mid) {
					mid = mid + 1;
				}
			} else {
				high = mid - 1;
			}
		}
		
	
		midpointList.add(mid);			
		occs.add(midpointList.get(midpointList.size() - 1), occs.remove(occs.size() - 1));
		
		if (midpointList.get(midpointList.size()-1) == midpointList.get(midpointList.size() - 2)) {
			midpointList.remove(midpointList.size() - 1);
		}
		return midpointList;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		
		kw1.toLowerCase();
		kw2.toLowerCase();
		
		ArrayList<String> resultingDocs = new ArrayList<String>();
		ArrayList<Occurrence> kw1Array = new ArrayList<Occurrence>();
		ArrayList<Occurrence> kw2Array = new ArrayList<Occurrence>();
		ArrayList<Occurrence> bothArray = new ArrayList<Occurrence>();
		
		if (keywordsIndex.containsKey(kw2)) {
			kw2Array = keywordsIndex.get(kw2);
		}
		
		if (keywordsIndex.containsKey(kw1)) {
			kw1Array = keywordsIndex.get(kw1);
		}
		
		
		bothArray.addAll(kw1Array);
		bothArray.addAll(kw2Array);
		
		
		if ((!(kw1Array.isEmpty())) && (!(kw2.isEmpty()))) {
			
			for (int i = 0; i < bothArray.size() - 1; i++) {
				for (int j = 1; j < bothArray.size() - i; j++) {
					
					if (bothArray.get(j-1).frequency < bothArray.get(j).frequency) {
						
						Occurrence a = bothArray.get(j-1);
						bothArray.set(j-1, bothArray.get(j));
						bothArray.set(j, a);
					}
					
				}
			}
			
			for (int a = 0; a < bothArray.size() - 1; a++) {
				for (int b = a + 1; b < bothArray.size(); b++) {
					
					if (bothArray.get(a).document == bothArray.get(b).document) {
						
						bothArray.remove(b);
					}
					
				}
				
				
				
			}
			
			
		}
		
		while (bothArray.size() > 5) {
			
			bothArray.remove(bothArray.size() - 1);
		}

		for (Occurrence obj : bothArray) {
			
			resultingDocs.add(obj.document);
		}
		
		return resultingDocs;
	
	}
}
