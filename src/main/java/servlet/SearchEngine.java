package servlet;

import java.util.ArrayList; 
import java.util.List;
import java.util.Scanner;

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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.google.gson.Gson;

import servlet.EditDistance;
import servlet.RedBlackBST;

@WebServlet(
      name = "MyServlet2", 
      urlPatterns = {"/search"}
  )
public class SearchEngine extends HttpServlet {
	List<InvertedIndex> invertedObjectsMain = new ArrayList<InvertedIndex>();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
  	Gson gson = new Gson();
  	String msg = req.getParameter("q");
  		Long startTime = System.currentTimeMillis();
		String[] splited = msg.split("\\s+");
		String toPass = "(";
		for(int i=0; i<splited.length; i++){
			toPass += "\'" + splited[i] + "\',";			
		}
		toPass = toPass.substring(0, toPass.length()-1) + ")";
		System.out.println(toPass);
		
		
		
		
//		if(splited.length > 0){
//			return Response.status(200).entity(gson.toJson(splited.toString()))
//					.header("Access-Control-Allow-Origin", "*")
//					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
//					.build();
//		}
		
		SearchEngine searchEngine = new SearchEngine();
		MatchFound matchFound;
		List<MatchFound> FileNames = new ArrayList<MatchFound>();
		List<String> matchedWords = new ArrayList<String>();
		List<InvertedIndex> invertedObjects = new ArrayList<InvertedIndex>();
		List<Integer> frequency = new ArrayList<Integer>();
		QuickSelect q = new QuickSelect();
//		String selectedWord = null;

		String aKeyword;
		aKeyword = msg;

		EditDistance ed = new EditDistance();
		System.out.println();
		matchedWords = ed.findingLeastEditDistance(aKeyword);

		
		boolean correctWord = false;
		
		
//		if (matchedWords.get(0).length() == 0) {
//			System.out.println("Exact Match" + matchedWords.get(0).length());
//			selectedWord = aKeyword;			
//		}else{ 
//			System.out.println("Show first result from did you mean");
//			selectedWord = matchedWords.get(0);
//		}

		invertedObjects = searchEngine.getInvertedIndex(toPass);
		
		int[] frequencyArray = new int[invertedObjects.size()];
		int[] sortedFrequency = new int[invertedObjects.size()];
		for (int i = 0; i < invertedObjects.size(); i++) {
			InvertedIndex invIndex = invertedObjects.get(i);
			String fileName = searchEngine.getWebFile((long) invIndex
					.getIndexId());

			matchFound = new MatchFound();
			matchFound.setFileId(invIndex.getIndexId());
			matchFound.setFileName(fileName);
			matchFound.setFrequency(invIndex.getFrequency());
			matchFound.setWord(invIndex.getWord());
			FileNames.add(matchFound);
			frequencyArray[i] = invIndex.getFrequency();
		}

		for (int i = 0; i < frequencyArray.length; i++) {
			int ind = q.quickSelect(frequencyArray, 0, frequencyArray.length - 1,
					frequencyArray.length - (i + 1));
			System.out.println("highest = " + frequencyArray[ind]);
			sortedFrequency[i] = frequencyArray[ind];
		}

		// for(int i=0; i<FileNames.size(); i++){
		// System.out.println(FileNames.get(i).getFrequency());
		// }
		List<String> finalFileNames = new ArrayList<String>();
		List<String> freqArray = new ArrayList<String>();
		int yo = 0;
		for (int i = 0; i < sortedFrequency.length; i++) {
			int freq = sortedFrequency[i];
			for (int index = 0; index < FileNames.size(); index++) {
				if (FileNames.get(index).getFrequency() == freq)
					if (!finalFileNames.contains(FileNames.get(index)
							.getFileName())) {
						finalFileNames.add(yo, FileNames.get(index)
								.getFileName());
						freqArray.add(yo, freq + "");
						yo++;
					}
			}
		}

		System.out.println(frequencyArray.length);
		System.out.println(sortedFrequency.length);
		System.out.println(finalFileNames.size());
		List<ReturnValue> returnValues = new ArrayList<ReturnValue>();
		
		Long endTime = System.currentTimeMillis();
		
		ReturnValue returnValue1 = new ReturnValue();
		returnValue1.name = "numberOfValues";
		returnValue1.value = finalFileNames.size() + "";
		returnValues.add(returnValue1);
		
		ReturnValue returnValue2 = new ReturnValue();
		returnValue2.name = "timeTaken";
		returnValue2.value = (endTime-startTime) + "";
		returnValues.add(returnValue2);
		
		for (int index = 0; index < finalFileNames.size(); index++) {
//			System.out.println(finalFileNames.get(index));
			ReturnValue returnValue = new ReturnValue();
			returnValue.name = finalFileNames.get(index);
			returnValue.value = freqArray.get(index);
			returnValues.add(returnValue);
		}
		
		ServletOutputStream out = resp.getOutputStream();
        out.write(gson.toJson(returnValues).getBytes());
        out.flush();
        out.close();
        
	}
	
	class ReturnValue{
		public String name;
		public String value;
	}
//	HibernateUtil hu = new HibernateUtil();
//	Session sessionMain = HibernateUtil.getSessionFactory().openSession();
	private List<InvertedIndex> getInvertedIndex(String word) {
		System.out.println("sql call is made");
		boolean isExist = false;

		List<InvertedIndex> invertedIndexObject = new ArrayList<InvertedIndex>();
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			StringBuilder queryString = new StringBuilder();
			System.out.println(" SELECT res FROM InvertedIndex AS res WHERE res.word in " + word);
			queryString.append(" SELECT res ").append(
					" FROM InvertedIndex AS res WHERE res.word in " + word + "");

			Query queryObject = session.createQuery(queryString.toString());

			invertedIndexObject = (List<InvertedIndex>) queryObject.list();

			session.getTransaction().commit();
		} catch (HibernateException e) {
			// System.out.println("exception");
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		System.out.println(invertedIndexObject.size() + "");
		return invertedIndexObject;
	}

	private String getWebFile(Long indexId) {
		boolean isExist = false;

		List<String> webFile = new ArrayList<String>();
		String fileName = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			StringBuilder queryString = new StringBuilder();

			queryString.append(" SELECT res.fileName ").append(
					" FROM WebFile AS res WHERE res.indexId=" + indexId);

			Query queryObject = session.createQuery(queryString.toString());

			webFile = (List<String>) queryObject.list();
			fileName = webFile.get(0);

			session.getTransaction().commit();
		} catch (HibernateException e) {
			// System.out.println("exception");
			e.printStackTrace();
			session.getTransaction().rollback();
		}

		return fileName;
	}

	public static void main(String[] args) {

		/*
		 * Dictionary dictionary = new Dictionary();
		 * dictionary.dictionaryConstrutor();
		 * 
		 * TstImplementation tst = new TstImplementation(); tst.makingTST();
		 */
		SearchEngine searchEngine = new SearchEngine();
		MatchFound matchFound;
		List<MatchFound> FileNames = new ArrayList<MatchFound>();
		List<String> matchedWords = new ArrayList<String>();
		List<InvertedIndex> invertedObjects = new ArrayList<InvertedIndex>();
		List<Integer> frequency = new ArrayList<Integer>();
		QuickSelect q = new QuickSelect();
		String selectedWord = null;

		System.out.println("Enter Word: ");
		String aKeyword;
		Scanner scanIn = new Scanner(System.in);
		aKeyword = scanIn.nextLine();

//		EditDistance ed = new EditDistance();
//		System.out.println();
//		matchedWords = ed.findingLeastEditDistance(aKeyword);
//
//		
//		boolean correctWord = false;
//		
//		
//		if (!matchedWords.isEmpty()) {
////			System.out.println("Did you mean?\n");
//			
//			selectedWord = scanIn.nextLine();
//			
//			for (String mWord : matchedWords) {
//				if (selectedWord.equals(mWord)) {
//					correctWord = true;
//					// System.out.println("Typed Word was not in the match list. Please try again later.\n");
//					// System.exit(0);
//				}
//			}
//			if (!correctWord) {
//				System.out
//						.println("Typed Word was not in the match list. Please try again later.\n");
//				System.exit(0);
//			}
//		}else{ 
//			System.out.println("Exact Match");
//			selectedWord = aKeyword;
//		}

		selectedWord = aKeyword;

		invertedObjects = searchEngine.getInvertedIndex(selectedWord);

		int[] frequecyArray = new int[invertedObjects.size()];
		int[] sortedFrequency = new int[invertedObjects.size()];
		for (int i = 0; i < invertedObjects.size(); i++) {
			InvertedIndex invIndex = invertedObjects.get(i);
			String fileName = searchEngine.getWebFile((long) invIndex
					.getIndexId());

			matchFound = new MatchFound();
			matchFound.setFileId(invIndex.getIndexId());
			matchFound.setFileName(fileName);
			matchFound.setFrequency(invIndex.getFrequency());
			matchFound.setWord(invIndex.getWord());
			FileNames.add(matchFound);
			frequecyArray[i] = invIndex.getFrequency();
		}
		for (int i = 0; i < frequecyArray.length - 1; i++) {
			int ind = q.quickSelect(frequecyArray, 0, frequecyArray.length - 1,
					frequecyArray.length - (i + 1));
			System.out.println("highest = " + frequecyArray[ind]);
			sortedFrequency[i] = frequecyArray[ind];
		}

		// for(int i=0; i<FileNames.size(); i++){
		// System.out.println(FileNames.get(i).getFrequency());
		// }
		List<String> finalFileNames = new ArrayList<String>();
		int yo = 0;
		for (int i = 0; i < sortedFrequency.length - 1; i++) {
			int freq = sortedFrequency[i];
			for (int index = 0; index < FileNames.size(); index++) {
				if (FileNames.get(index).getFrequency() == freq)
					if (!finalFileNames.contains(FileNames.get(index)
							.getFileName())) {
						finalFileNames.add(yo, FileNames.get(index)
								.getFileName());
						yo++;
					}
			}
		}

		for (int index = 0; index < finalFileNames.size(); index++) {
			System.out.println(finalFileNames.get(index));
		}
	}

}
