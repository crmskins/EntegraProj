/*
 * Chad Milburn
 * April 1, 2018
 * 
 * Please read through my code below as I also do my best to explain what
 * my code is doing to parse the text from the business card.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class BusinessCardParser {

	/*
	 * In the main method, I create an example String, "test",
	 * which models an text from a sample business card.
	 * Change it to whatever you like when testing my code, but make sure
	 * to use the '\n' (newline character) for wherever there would be a new line on
	 * the business card.
	 * 
	 *  Below I print out the sample string and then also run "getContactInfo" on the 
	 *  sample string and format the output and input with labels to make it more readable
	 *  
	 *  Also below I have the 3 given examples in the specification, labeled accordingly.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String test = "Software Engineer\nJim John\njimJohn@g.com\nTel: 1-443-223-3945\nFax: +1 410-757-7373";
		String example1 = "Entegra Systems\nJohn Doe\nSenior Software Engineer\n(410)555-1234\njohn.doe@entegrasystems.com";
		String example2 = "Acme Technologies\nAnalytic Developer\nJane Doe\n1234 Roadrunner Way\nColumbia, MD 12345\nPhone: 410-555-1234\nFax: 410-555-4321\nJane.doe@acmetech.com";
		String example3 = "Bob Smith\nSoftware Engineer\nDivision & Security Technologies\nABC Technologies\n123 North 11th Street\nSuite 229\nArlington, VA 22209\nTel: +1 (703)555-1259\nFax: +1 (703)555-1200\nbsmith@abctech.com";
		System.out.println("Input: \n");
		System.out.println(example3);
		System.out.println("\n");
		System.out.println("Output: \n");
		//I included a toString method for the data ContactInfo and include it here
		//so the output automatically prints out
		ContactInfo c = getContactInfo(example3);
		System.out.println(c.toString());
	}
	
	
	/*
	 * Here is the main parsing method.
	 * I use a scanner to parse through the string and break on newlines and 
	 * also whitespaces. Making two different arrayLists, one just broken on 
	 * new lines, one broken on new lines and whitespaces.
	 */
	static ContactInfo getContactInfo(String document){
		String name = null, email = null, phone =null;
		Scanner sc = new Scanner(document);
		int lineCounter =0;
		//build an arrayList for each line of text, each index is a new line of text
		//using a scanner to break on new lines
		//i will use this to search for name
		ArrayList<String> lines = new ArrayList<String>();
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			line = line.trim();
			lines.add(line);
			lineCounter++;
		}
		//this arraylist simply breaks on whitespace so each piece of text from the 
		//document is in its own index array in order, this is what ill use to 
		//search for email and phone number
		ArrayList<String> text = new ArrayList <String>();
		for(int i=0; i<lineCounter;i++){
			String work = lines.get(i);
			if(work.contains(" ")){
				String[] words = work.split("\\s+");
				for(int j=0;j<words.length;j++){
					text.add(words[j]);
				}
			}
			else{
				text.add(work);
			}
		}
		//I made separate methods below in this class that individually deal with
		//finding the 3 pieces of information being looked for
		name = findName(lines);
		phone  = findPhone(text);
		email = findEmail(text);
		ContactInfo c = new ContactInfo(name, phone, email);
		sc.close();
		return c;
	}
	
	
	/*
	 * I used this method to test my code as I worked on it,
	 * not much different from "getContactInfo" but if it interests you
	 * go ahead and look
	 */
	static void test(String x){
		Scanner sc = new Scanner(x);
		int lineCounter =0;
		ArrayList<String> lines = new ArrayList<String>();
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			line = line.trim();
		//	boolean prevWasSpace = false;
			//char spaceCount[] = line.toCharArray();
		/*	for(int i=0; i< spaceCount.length; i++){
				if(spaceCount[i] == ' ' && prevWasSpace == false){
					prevWasSpace = true;
					maxWords++;
				}
				else if(spaceCount[i] != ' ' && prevWasSpace == true){
					prevWasSpace = false;
				}
			}*/
			lines.add(line);
			//System.out.println(lines.get(lineCounter));
			lineCounter++;
		}
		ArrayList<String> text = new ArrayList <String>();
		for(int i=0; i<lineCounter;i++){
			String work = lines.get(i);
			if(work.contains(" ")){
				String[] words = work.split("\\s+");
				for(int j=0;j<words.length;j++){
				//	System.out.println(words[j]);
					text.add(words[j]);
				}
			}
			else{
				//System.out.println(work);
				text.add(work);
			}
		}
		String name = findName(lines);
		String phone  = findPhone(text);
		String email = findEmail(text);
		System.out.println("Name: " + name);
		System.out.println("Phone: " + phone);
		System.out.println("Email: " + email);
		sc.close();
	}

	/*
	 * Here I deal with finding the name portion of the contact info.
	 * First each line of text (breaking on '\n') is compared with a regex
	 * statement I created on my own and allows for many names more than just 2,
	 * since you cannot assume just 2 names will be on a business card.
	 * Also it allows for numbers, periods, letters at the end so it also
	 * catches those who have "jr." or "iii" or "4" as they are named after a relative.
	 * 
	 *  Each line of text that matches my regex expression is thrown into a String 
	 *  array which holds every line of text that matches the regex expression.
	 *  
	 *  If there are no matches, a name is not present.
	 *  
	 *  If 1 match, that is the name.
	 *  
	 *  If more than 1 match, I put these possible names through another test to
	 *  hopefully find a single name. The method is called nameCheck, which is further
	 *  explained below.
	 */
	static String findName(ArrayList<String> lines){
		int maxNameMatches =10;
		String possible_names[] = new String[maxNameMatches];
		String name = null;
		int matchCount =0;
		for(int i=0; i<lines.size();i++){
			String regex = "([a-zA-z]+\\s+[a-zA-z]+\\s*(?:[a-zA-z]+\\s+)*[a-zA-z.0-9]*)";
			if(lines.get(i).matches(regex)){
				possible_names[matchCount] = lines.get(i);
				matchCount++;
			}
		}
		if(matchCount ==0){
			name = "N/A";
		}
		else if(matchCount ==1){
			name = possible_names[0];
		}
		else{
			//more name finding algorithms
			name = nameCheck(possible_names, matchCount);
		}
		return name;
	}
	/*
	 * Find phone is a tad bit easier than finding the name
	 * The only thing that will make it through the phone number regex
	 * that is not a phone number would be a fax number.
	 * So below I give in an arrayList that takes the input String
	 * and breaks on space and new lines. Therefore, I have to check if 
	 * the number that matches the regex is labeled as fax and also I have to check 
	 * if there is a "+1" that needs to be put at the beginning of the number also.
	 */
	static String findPhone(ArrayList<String> text){
		//the only thing that could be confused as a phone number on 
		//a business card is a fax number, that is why there are only 2 possible 
		//phone number matches maximum
		int maxPhoneMatches =2;
		String possible_phoneNum[] = new String[maxPhoneMatches];
		String phone = null;
		int matchCount =0;
		for(int i=0; i<text.size();i++){
			String regex = "(?:\\+\\s*)?(?:(1-)\\s*)?(?:\\()?(\\d\\d\\d)(?:-|\\s*|\\))?\\s*(\\d\\d\\d)(?:-|\\s*)?(\\d\\d\\d\\d)";
			if(text.get(i).matches(regex)){
				possible_phoneNum[matchCount] = text.get(i);
				matchCount++;
			}

		}
		//no phone number
		if(matchCount ==0){
			phone = "N/A";
		}

		else{
			for(int j =0; j<maxPhoneMatches;j++){
				int h=0;
				while(possible_phoneNum[j] != null && !(possible_phoneNum[j].equals(text.get(h))) ){
					h++;
				}
				//if 1 match, that is the phone number, must check if there is 
				//a 1 that needs to be put at the beginning
				if(matchCount == 1){
					if(h>0 && text.get(h-1).toLowerCase().endsWith("1")){
					phone = "1" + possible_phoneNum[0];
					}
					else{
						phone = possible_phoneNum[0];
					}
				}
				/*
				 * This is if there are multiple matches with the regular expression,
				 * Where I check if something is labeled with "fax" to determine what is
				 * the fax and the actual phone number.
				 */
				else{
					if(text.get(h-1).toLowerCase().endsWith("1") && !(text.get(h-2).toLowerCase().contains("fax"))){
						phone = "1" + text.get(h);
						break;
					}
					else if(!(text.get(h-1).toLowerCase().startsWith("fax")) || text.get(h-1).toLowerCase().startsWith("tel")){
						phone = text.get(h);
						break;
					}
				}
			}
		}
		/*
		 * Here is simply am removing all the non digit values in the 
		 * string to make it equivalent to the specification in result
		 */
		phone = phone.replace("-", "");
		phone = phone.replace("(", "");
		phone = phone.replace(")", "");
		phone = phone.replace(" ", "");
		phone = phone.replace("+", "");
		return phone;
	}
	
	/*
	 * The easiest find function of them all was findEmail, as there 
	 * is nothing that can get past this regular expression other than a 
	 * standard email address, so that is all I do to find the email.
	 */
	static String findEmail(ArrayList<String> text){
		String email = null;
		int matchCount =0;
		for(int i=0; i<text.size();i++){
			String regex = "([_.0-9a-zA-Z]+@[0-9a-zA-Z]+\\.[0-9a-zA-Z]+)";
			if(text.get(i).matches(regex)){
				email = text.get(i);
				matchCount++;
			}
		}
		if(matchCount ==0) email = "N/A";
		return email;
	}
	/*
	 * Here i import the text file named "first_names.txt" which holds 
	 * roughly 22,000 common first and last names. I put each name into 
	 * a hashSet to make it easy to find as Sets are not ordered.
	 * 
	 * Also note: I clearly did not make a 22,000 line text file of common first
	 * and last names, I got it from this link on github, and credit goes to them:
	 * 
	 * https://github.com/dominictarr/random-name/blob/master/names.txt
	 */
	static HashSet<String> importNames(){
		HashSet<String> firstNames = new HashSet<String>();
		File first = new File("first_names.txt");
		Scanner sc = null;
		try {
			sc = new Scanner(first);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(sc.hasNextLine()){
			firstNames.add(sc.nextLine().toLowerCase());
		}
		sc.close();
		return firstNames;
	}

	/*
	 * Here I utilize my 22,000 various first and last names HashSet by comparing 
	 * the different lines of text that could be a name. I go through and break
	 * on spaces to compare each individual word and find which line has the maximum
	 * number of "matches" with my hashSet.
	 */
	static String nameCheck (String [] possible_names, int matchCount){
		HashSet<String> names = importNames();
		int maxMatches = 0;
		int pointer =0;
		String name ="";
		for(int i=0;i<matchCount;i++){
			String poss_name = possible_names[i];
			String text [] = poss_name.split("\\s+");
			int matchCounter =0;
			for(int j=0;j<text.length;j++){
				String lower = text[j].toLowerCase();
				if(names.contains(lower)){
					matchCounter++;
				}
				if(j==text.length-1 && matchCounter > maxMatches){
					maxMatches = matchCounter;
					pointer = i;
				}
			}
		}
		name = possible_names[pointer];
		return name;
	}
}
