package servlet;
//package servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import com.google.gson.Gson;

import servlet.RedBlackBST;
@WebServlet(
        name = "MyServlet", 
        urlPatterns = {"/didyoumean2"}
    )
public class HelloServlet2 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	Gson gson = new Gson();
		List<String> matched = findingLeastEditDistance(req.getParameter("q"));
		
        ServletOutputStream out = resp.getOutputStream();
        out.write(gson.toJson(matched).getBytes());
        out.flush();
        out.close();
    }
    
    public List<String> findingLeastEditDistance(String word) {
		List<String> matchedWords = new ArrayList<String>();
		List<String> matchedWordsToShow = new ArrayList<String>();

		File dictionary = new File("src/main/java/servlet/Dictionary.txt");
		boolean isExist = dictionary.exists();
		String line = "";
		StringTokenizer stringTokenizer = null;
		BufferedReader br = null;

		int editDistance = 0;
		int keywordScore = 0;

		List<Integer> editDistances = new ArrayList<Integer>();
		List<Integer> minDistances = new ArrayList<Integer>();
		RedBlackBST<Integer, String> rbst = new RedBlackBST<Integer, String>();
		MultiMap multiMap = new MultiValueMap();
        
		try {
			br = new BufferedReader(new FileReader(dictionary));

			while ((line = br.readLine()) != null) {

				stringTokenizer = new StringTokenizer(line);

				while (stringTokenizer.hasMoreElements()) {

					String token = stringTokenizer.nextElement().toString();

					StringBuilder sb = new StringBuilder();
					sb.append(token);
					String aSingleToken = sb.toString();

					// Calculating edit distance of given keyword with each
					// token
					editDistance = editDistance(word, aSingleToken);
					if(editDistance == 0){
						System.out.println("booom");
					}
					// editDistances.add(editDistance);
					int rbstMin = -1;

					if (!rbst.isEmpty())
						rbstMin = rbst.min();

//						if (rbstMin - editDistance <= 3) {							
							// put values into map for A
					        multiMap.put(editDistance + "", aSingleToken);
					        
//						}
						if (rbstMin == editDistance) {
	
							String wordMatched = rbst.get(rbstMin);
	//						System.out.println("Word: " + wordMatched
	//								+ " edit distance: " + editDistance);
	
							if (rbstMin <= 3)
								matchedWords.add(wordMatched);
	//						rbst.delete(editDistance);
							rbst.put(editDistance, aSingleToken);
						} else {
							rbst.put(editDistance, aSingleToken);
						}
					}
			}

			System.out.println("Edit Distances computed.\n");

//			rbst.printTree();
			
			if (!matchedWords.isEmpty()) {
				System.out.println("Did you mean?");
			}

			for (String words : matchedWords) {
				System.out.print(words + "\t");
			}
			
			// get all the set of keys
	        Set<String> keys = multiMap.keySet();
	        // iterate through the key set and display key and values
	        
	        Set<String> treeSet = new TreeSet<String>();
	        for (String key : keys) {
	        	if(key.length()<2)
		        treeSet.add(key);	
	        }
	        	
	        String firstMatch = "";
	        String secondMatch = "";
	        String thirdMatch = "";
	        String fourthMatch = "";
	        String fifthMatch = "";
	        	
	        for (String key : treeSet) {
	        	if(!key.equals("0")){
		            String[] parts = multiMap.get(key).toString().substring(1, multiMap.get(key).toString().length()-1).split(",");
		            for (int it=0; it<parts.length; it++) {
		            	if(firstMatch.length() == 0){
		            		firstMatch = parts[it];
		            	}else if(secondMatch.length() == 0){
		            		secondMatch = parts[it];
		            	}else if(thirdMatch.length() == 0){
		            		thirdMatch = parts[it];
		            	}else if(fourthMatch.length() == 0){
		            		fourthMatch = parts[it];
		            	}else if(fifthMatch.length() == 0){
		            		fifthMatch = parts[it];
		            	}
		            }
	        	}else{
	        		firstMatch = "";
	     	        secondMatch = "";
	     	        thirdMatch = "";
	     	        fourthMatch = "";
	     	        fifthMatch = "";
	     	        break;
	        	}
	        }
	        System.out.println(firstMatch + secondMatch +thirdMatch + fourthMatch + fifthMatch);

	        matchedWordsToShow.add(firstMatch);
	        matchedWordsToShow.add(secondMatch);
	        matchedWordsToShow.add(thirdMatch);
	        matchedWordsToShow.add(fourthMatch);
	        matchedWordsToShow.add(fifthMatch);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
//		return matchedWords;
		return matchedWordsToShow;
	}

	public static int editDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();

		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}

		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}

		// iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);

				// if last two chars equal
				if (c1 == c2) {
					// update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;

					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}

		return dp[len1][len2];
	}

}
