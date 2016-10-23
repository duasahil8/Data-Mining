import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

public class AprioriOnGene {
	private static HashMap<String,Integer> freqMap = new HashMap<String,Integer>(); 
	private static HashMap<ArrayList<String>,Integer> freqMapGeneral = new HashMap<ArrayList<String>,Integer>(); 
	private static ArrayList<String> transaction  ; 
	private static ArrayList<String> freqOne  ; 
	private static ArrayList<ArrayList<String>> database = new ArrayList<ArrayList<String>>(); 
	private static int minFreq = 0 ;
	private static int minConf = 1 ;
	private static int sampleSize = 0 ;
	private static ArrayList<ArrayList<String>> itemListTwo ; 
	private static HashMap<Integer, ArrayList<ArrayList<String>>> sizeMap = new HashMap<Integer, ArrayList<ArrayList<String>>>(); 
	private static ArrayList<ArrayList<String>> oneDown ; 
	private static ArrayList<ArrayList<String>> headList = new ArrayList<ArrayList<String>>();
	private static ArrayList<ArrayList<String>> bodyList = new ArrayList<ArrayList<String>>();
	private static ArrayList<ArrayList<String>> combinedList = new ArrayList<ArrayList<String>>();
	public static ArrayList<Integer> res1 = new ArrayList<Integer>(); 
	public static ArrayList<Integer> res2  = new ArrayList<Integer>(); 
	
	
	private static int k = 10 ; 
	public static void main(String[] args) {
		//give path GeneData.csv or comment manual initializations and uncomment args[i]
		String filePath = "C:\\Users\\sdua\\Documents\\GeneData.csv" ; 
		//String filePath = args[0];
		//minFreq = Integer.parseInt(args[1]); 
		//minConf = Integer.parseInt(args[2]); 
		//String query = args[3] ; 
		minFreq = 50 ;
		minConf = 60 ; 
		sampleSize = 100 ; 
		
		
		System.out.println("Support = " + minFreq + "%");
		System.out.println("Confidence = " + minConf + "%");
		//System.out.println("Confidence = " + minConf + "%");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			//System.out.println(reader.readLine().toString());
			String g = ""; int freq = 0 ; String line = "";
			while((line=reader.readLine())!=null){
				
				//printMap(); 
				//System.out.println(line);
				String[] lineSplit = line.toString().split(",");
				transaction = new ArrayList<String>(); 
				for(int i = 1 ; i < 101 ; i++){
					g = "G" + Integer.toString(i)+ "_" + lineSplit[i]; 
					g = g.trim(); 
					//System.out.print(g+ " , "); 
					transaction.add(g); 
					if(freqMap.get(g)!=null){
						//System.out.println(g + " | Exists");
						freq = freqMap.get(g); 
						freq ++ ; 
						freqMap.put(g,freq);
						
					}
					else{
						freqMap.put(g,1); 
					}
					
				}
				
				transaction.add(lineSplit[101]);
				Collections.sort(transaction);
				
				
				if(freqMap.get(lineSplit[101])!=null){
					freq = freqMap.get(lineSplit[101]); 
					freq ++ ; 
					freqMap.put(lineSplit[101],freq);
					 
				}
				
				else{
					freqMap.put(lineSplit[101],1);
				}
				
				database.add(transaction); 
				}
			buildFreqOne();
			System.out.println("Set Size = 1 || Number of sets = "  + freqOne.size());
			 
			buildListTwo(); 
			System.out.println("Set Size = 2 || Number of sets = "  + itemListTwo.size());
			oneDown = itemListTwo; 
			while(k>=1){
				buildList(oneDown);
			}
			
			 
			 Iterator it = freqMapGeneral.entrySet().iterator(); 
			 
			 ArrayList<String> listOne,listTwo,comb; 
			 ArrayList<String> h , b ; 
			 
			 ArrayList<ArrayList<String>> copyL ; 
			 while(it.hasNext()){
				 Map.Entry pair = (Map.Entry) it.next();
				 listOne = (ArrayList<String>) pair.getKey();
				 int s = listOne.size(); 
				 if(sizeMap.get(s)!=null){
					 copyL = sizeMap.get(s) ; 
					 copyL.add(listOne);
					 sizeMap.put(s, copyL); 
				 }
				 else{
					 copyL = new ArrayList<ArrayList<String>>(); 
					 copyL.add(listOne);
					 sizeMap.put(s, copyL);
				 }
			 }
			 
			/* Iterator iterator = sizeMap.entrySet().iterator(); 
			 while(iterator.hasNext()){
				 Map.Entry p = (Map.Entry) iterator.next();
				 System.out.println(p.getKey());
			 }*/
			 int c = 0 ;
			 ArrayList<ArrayList<String>> copyM ;
			 ArrayList<ArrayList<String>> copyN ;
			 ArrayList<String> body , head ; 
			 ArrayList<String> combined ; 
			 for(int m = 1 ; m <= sizeMap.size(); m++){
				 copyM = sizeMap.get(m);
				 for(int n = 1  ; n <= sizeMap.size() ; n++){
					 copyN = sizeMap.get(n); 
						 for(ArrayList<String> mlist : copyM){
							 for(ArrayList<String> nlist : copyN){
								 if(mlist.size()<nlist.size()){
									 if(nlist.containsAll(mlist)){
										 body = new ArrayList<String>();
										 body.addAll(mlist); 
										 head = new ArrayList<String>(); 
										 head.addAll(nlist);
										 head.removeAll(body);
										  
										 combined = new ArrayList<String>(); 
										 combined.addAll(body);
										 combined.addAll(head);
										 //head.removeAll(body);
										 c++; 
										 int lhs = calculateScore(body);
										  int nu =  calculateScore(combined);
										  int conf = nu * 100 ;  
										  conf = conf/lhs ; 
										  if(conf>minConf){
											  
											  //System.out.println(body + " ->  " + head);
											  headList.add(head);
											  bodyList.add(body);
											  combinedList.add(combined); 
										  }
										 
										 
									 }
								 }
								
							 }
						 }
					 
				 }
			 }
			 //System.out.println(c);
			 //System.out.println(headList.size());
			 /*for(int i = 0 ; i < headList.size() ; i++){
				 System.out.println(headList.get(i) + " -> " + bodyList.get(i));
			 }*/
				
			 //templateTwo("SIZE OF RULE >= 3");
			 //templateTwo("SIZE OF HEAD >= 2");
			 //simpleOneOf("RULE HAS 1 OF G1_UP");
			 //multiPleOneOf("RULE HAS 1 OF (G1_UP, G10_Down)");
			 //multiPleOneOf("BODY HAS 1 OF (G1_UP, G10_Down)");
			 //multiPleOneOf("HEAD HAS 1 OF (G6_UP, G8_UP)");
			 //RULE HAS 1 OF (G1_UP, G6_UP, G72_UP)
			 //multiPleOneOf("RULE HAS 1 OF (G1_UP, G6_UP, G72_UP)");
			 //simpleAny("HEAD HAS ANY OF G6_UP");
			 //multiPleAny("RULE HAS ANY OF (G1_UP, G6_UP, G72_UP)");
			  //simpleNONE("BODY HAS NONE OF G72_UP");

			// 1.	BODY HAS ANY OF G1_UP AND HEAD HAS 1 OF G59_UP
			 //res1 = simpleAny("BODY HAS ANY OF G1_UP"); 
			 //res2 = simpleOneOf("HEAD HAS 1 OF G6_UP"); 
			 
			 res1 = simpleAny("BODY HAS ANY OF G1_UP");
			 res2 = simpleOneOf("HEAD HAS 1 OF G59_UP"); 
			 
			// ArrayList<Integer> finalRes = performOR(res1,res2); 
			 
			 ArrayList<Integer> finalRes = performAnd(res1,res2);
			 
			 System.out.println("\nFinal Results  - \n" + finalRes.size() );
			 for(Integer k : finalRes){
					
					System.out.println(bodyList.get(k) + " ->  " + headList.get(k));
				}
			 
			
			 
			// multipleNONE("HEAD HAS NONE OF (G1_UP, G6_UP)");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
private static ArrayList<Integer> performOR(ArrayList<Integer> res12, ArrayList<Integer> res22) {
		HashMap<Integer, Integer> resMap = new HashMap<Integer, Integer>(); 
		ArrayList<Integer> resList = new ArrayList<Integer>(); 
		for(int i : res12 ){
			resMap.put(i, 1); 
		}
		for(int j : res22 ){
			resMap.put(j, 1); 
		}
		
		Iterator it = resMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry) it.next(); 
			resList.add((Integer) pair.getKey());
			
		}
	 
		return resList ; 
	}

private static ArrayList<Integer> performAnd(ArrayList<Integer> res12, ArrayList<Integer> res22) {
	
	ArrayList<Integer> resList = new ArrayList<Integer>(); 
	for(int i : res12 ){
		if(res22.contains(i)){
			resList.add(i);
		}
	}
	 
	 
 
	return resList ; 
}
public static ArrayList<Integer> simpleOneOf(String query){
	//RULE HAS 1 OF G1_UP
	//String variable = query.split("HAS 1 OF")[0].trim();
	String value = query.split("HAS 1 OF")[1].trim();
	ArrayList<Integer> indexList = new ArrayList<Integer>(); 
	int variableCount = 0 ; 
	System.out.println(query);
	for(int i = 0 ; i < headList.size(); i++){
		 if(query.contains("BODY")){
			 ArrayList<String> b = bodyList.get(i);
			 if(b.contains(value)){
				 indexList.add(i); 
			 }
		 }
		 else if(query.contains("HEAD")){
			 ArrayList<String> h = headList.get(i);
			 if(h.contains(value)){
				 indexList.add(i); 
			 }
		 }
		 else if(query.contains("RULE")){
			 ArrayList<String> r = combinedList.get(i);
			 if(r.contains(value)){
				 indexList.add(i);  
			 }
		 }
	}
	
	System.out.println("Results   " + indexList.size() + "\n");
	for(Integer k : indexList){
		
		System.out.println(bodyList.get(k) + " ->  " + headList.get(k));
	}

	return indexList; 
}

public static ArrayList<Integer> simpleAny(String query){
	//RULE HAS 1 OF G1_UP
	//String variable = query.split("HAS 1 OF")[0].trim();
	String value = query.split("HAS ANY OF")[1].trim();
	ArrayList<Integer> indexList = new ArrayList<Integer>(); 
	int variableCount = 0 ; 
	System.out.println(query);
	for(int i = 0 ; i < headList.size(); i++){
		 if(query.contains("BODY")){
			 ArrayList<String> b = bodyList.get(i);
			 if(b.contains(value)){
				 indexList.add(i); 
			 }
		 }
		 else if(query.contains("HEAD")){
			 ArrayList<String> h = headList.get(i);
			 if(h.contains(value)){
				 indexList.add(i); 
			 }
		 }
		 else if(query.contains("RULE")){
			 ArrayList<String> r = combinedList.get(i);
			 if(r.contains(value)){
				 indexList.add(i);  
			 }
		 }
	}
	
	System.out.println("Results   " + indexList.size() + "\n");
	for(Integer k : indexList){
		
		System.out.println(bodyList.get(k) + " ->  " + headList.get(k));
	}

	return indexList; 
}





public static void simpleNONE(String query){
	//RULE HAS 1 OF G1_UP
	//String variable = query.split("HAS 1 OF")[0].trim();
	String value = query.split("NONE OF")[1].trim();
	ArrayList<Integer> indexList = new ArrayList<Integer>(); 
	int variableCount = 0 ; 
	System.out.println(query);
	for(int i = 0 ; i < headList.size(); i++){
		 if(query.contains("BODY")){
			 ArrayList<String> b = bodyList.get(i);
			 if(!b.contains(value)){
				 indexList.add(i); 
			 }
		 }
		 else if(query.contains("HEAD")){
			 ArrayList<String> h = headList.get(i);
			 if(!h.contains(value)){
				 indexList.add(i); 
			 }
		 }
		 else if(query.contains("RULE")){
			 ArrayList<String> r = combinedList.get(i);
			 if(!r.contains(value)){
				 indexList.add(i);  
			 }
		 }
	}
	
	System.out.println("Results   " + indexList.size() + "\n");
	for(Integer k : indexList){
		
		System.out.println(bodyList.get(k) + " ->  " + headList.get(k));
	}

	
}
public static void multipleNONE(String query){
	String value = query.split("HAS NONE OF")[1].trim();
	value = value.replace("(", ""); 
	value = value.replace(")", ""); 
	String[] vars = value.split(","); 
	String var1 = vars[0].trim(); 
	String var2 = vars[1].trim(); 
	String var3 = null ; 
	if(vars.length>2){
		var3 = vars[2].trim(); 
	}
	
	ArrayList<Integer> indexList = new ArrayList<Integer>(); 
	int variableCount = 0 ; 
	int c = 0 ; 
	System.out.println(query);
	for(int i = 0 ; i < headList.size(); i++){
		 if(query.contains("BODY")){
			
			 ArrayList<String> b = bodyList.get(i);
			 if(!b.contains(var1)&&!b.contains(var2)){
				  if(vars.length>3){
					  if(!b.contains(var3)){
						  indexList.add(i);
					  }
				  }
				  else indexList.add(i);
			 }
			 
		 }
		 else if(query.contains("HEAD")){
			 
			 ArrayList<String> h = headList.get(i);
			 if(!h.contains(var1)&&!h.contains(var2)){
				  if(vars.length>3){
					  if(!h.contains(var3)){
						  indexList.add(i);
					  }
				  }
				  else indexList.add(i);
			 }
		 }
		 else if(query.contains("RULE")){
			 c = 0 ; 
			 ArrayList<String> r = combinedList.get(i);
			 if(!r.contains(var1)&&!r.contains(var2)){
				  if(vars.length>3){
					  if(!r.contains(var3)){
						  indexList.add(i);
					  }
				  }
				  else indexList.add(i);
			 }
		 }
	}
	
	System.out.println("Results   " + indexList.size() + "\n");
	for(Integer k : indexList){
		
		System.out.println(bodyList.get(k) + " ->  " + headList.get(k));
	}
}
public static void multiPleOneOf(String query){
	//RULE HAS 1 OF G1_UP
	//String variable = query.split("HAS 1 OF")[0].trim();
	String value = query.split("HAS 1 OF")[1].trim();
	value = value.replace("(", ""); 
	value = value.replace(")", ""); 
	String[] vars = value.split(","); 
	String var1 = vars[0].trim(); 
	String var2 = vars[1].trim(); 
	String var3 = null ; 
	if(vars.length>2){
		var3 = vars[2].trim(); 
	}
	System.out.println(var1 + " " + var2);
	ArrayList<Integer> indexList = new ArrayList<Integer>(); 
	int variableCount = 0 ; 
	int c = 0 ; 
	System.out.println(query);
	for(int i = 0 ; i < headList.size(); i++){
		 if(query.contains("BODY")){
			 c = 0;
			 ArrayList<String> b = bodyList.get(i);
			 if(b.contains(var1)) c++; 
			 if(b.contains(var2)) c++ ; 
			 if(vars.length>2){
				 if(b.contains(var3)) c++ ; 
			 }
			 
			 if(c==1) indexList.add(i);
			 
		 }
		 else if(query.contains("HEAD")){
			 c = 0; 
			 ArrayList<String> h = headList.get(i);
			 if(h.contains(var1)) c++; 
			 if(h.contains(var2)) c++ ; 
			 if(vars.length>2){
				 if(h.contains(var3)) c++ ; 
			 }
			 
			 if(c==1) indexList.add(i);
		 }
		 else if(query.contains("RULE")){
			 c = 0 ; 
			 ArrayList<String> r = combinedList.get(i);
			 if(r.contains(var1)) c++; 
			 if(r.contains(var2)) c++ ; 
			 if(vars.length>2){
				 if(r.contains(var3)) c++ ; 
			 }
			 
			 if(c==1) indexList.add(i);
		 }
	}
	
	System.out.println("Results   " + indexList.size() + "\n");
	for(Integer k : indexList){
		
		System.out.println(bodyList.get(k) + " ->  " + headList.get(k));
	}

	
}





public static void multiPleAny(String query){
	//RULE HAS 1 OF G1_UP
	//String variable = query.split("HAS 1 OF")[0].trim();
	String value = query.split("HAS ANY OF")[1].trim();
	value = value.replace("(", ""); 
	value = value.replace(")", ""); 
	String[] vars = value.split(","); 
	String var1 = vars[0].trim(); 
	String var2 = vars[1].trim(); 
	String var3 = null ; 
	if(vars.length>2){
		var3 = vars[2].trim(); 
	}
	System.out.println(var1 + " " + var2);
	ArrayList<Integer> indexList = new ArrayList<Integer>(); 
	int variableCount = 0 ; 
	int c = 0 ; 
	System.out.println(query);
	for(int i = 0 ; i < headList.size(); i++){
		 if(query.contains("BODY")){
			 c = 0;
			 ArrayList<String> b = bodyList.get(i);
			 if(vars.length>2){
				 if(b.contains(var1)||b.contains(var2)||b.contains(var3))
					 indexList.add(i);
			 }
			 else if(b.contains(var1)||b.contains(var2))
					 indexList.add(i);
				 
			 
		 }
		 else if(query.contains("HEAD")){
			 c = 0; 
			 ArrayList<String> h = headList.get(i);
			 if(vars.length>2){
				 if(h.contains(var1)||h.contains(var2)||h.contains(var3))
					 indexList.add(i);
			 }
			 else if(h.contains(var1)||h.contains(var2))
					 indexList.add(i);
		 }
		 else if(query.contains("RULE")){
			 c = 0 ; 
			 ArrayList<String> r = combinedList.get(i);
			 if(vars.length>2){
				 if(r.contains(var1)||r.contains(var2)||r.contains(var3))
					 indexList.add(i);
			 }
			 else if(r.contains(var1)||r.contains(var2))
					 indexList.add(i);
		 }
	}
	
	System.out.println("Results   " + indexList.size() + "\n");
	for(Integer k : indexList){
		
		System.out.println(bodyList.get(k) + " ->  " + headList.get(k));
	}

	
}
public static void templateTwo(String query ){
	
	String v = query.split("SIZE OF")[1]; 
	 
	String operator = ">=" ; 
	System.out.println(query);
	int number = Integer.parseInt(query.split(">=")[1].trim()); 
	System.out.println("Value = " + number);
	ArrayList<Integer> indexList = new ArrayList<Integer>(); 
	for(int i = 0 ; i < headList.size(); i++){
		 if(v.contains("BODY")){
			 ArrayList<String> b = bodyList.get(i);
			 if(b.size()>=number){
				 indexList.add(i); 
			 }
		 }
		 else if(v.contains("HEAD")){
			 ArrayList<String> h = headList.get(i);
			 if(h.size()>=number){
				 indexList.add(i); 
			 }
		 }
		 else if(v.contains("RULE")){
			 ArrayList<String> r = combinedList.get(i);
			 if(r.size()>=number){
				 indexList.add(i); 
			 }
		 }
	}
	System.out.println("Results   " + indexList.size() + "\n\n");
	for(Integer k : indexList){
		
		System.out.println(bodyList.get(k) + " ->  " + headList.get(k));
	}
}
	 


public static void buildListTwo(){
	 int listSize = freqOne.size(); 
	 ArrayList<ArrayList<String>> tempTwo = new ArrayList<ArrayList<String>>(); 
	 ArrayList<String> temp ; 
	 String a , b ; 
	 
	 for(int i = 0 ; i < listSize ; i++){
		 a = freqOne.get(i);
		 for(int j=i+1 ; j < listSize ; j++){
			 b = freqOne.get(j);
			 temp = new ArrayList<String>(); 
			 temp.add(a); temp.add(b) ; Collections.sort(temp);
			 tempTwo.add(temp);
		 }
	 }
	 //System.out.println("Map size " + freqMap.size());
	 //System.out.println("Freq one " + freqOne.size()); 
	 //System.out.println("Two set Size " + tempTwo.size());
	  
	 itemListTwo = new ArrayList<ArrayList<String>>(); 
	 for(ArrayList<String> list : tempTwo){
		 int sc = calculateScore(list); 
		 if(sc>=minFreq){
			//System.out.println(list + " " + sc);
			itemListTwo.add(list);
			freqMapGeneral.put(list, sc);
			
		 }
			 
	 }
	 //System.out.println("Two Final " + itemListTwo.size());
}

public static void buildList(ArrayList<ArrayList<String>> list_prev){
	 int size = list_prev.size(); 
	 int lastIndex = 0 ; 
	 int matchCount = 0 ; 
	 int c = 0 ; 
	 ArrayList<String> l1 , l2 , l1_copy,l2_copy, l_combined; 
	 ArrayList<ArrayList<String>> l3 = new ArrayList<ArrayList<String>>(); 
	 int listCount = 0 ; 
	 if(list_prev.size()>0){
		 lastIndex = list_prev.get(0).size() - 1  ;
	 }
	 
	 int secondLastIndex = lastIndex - 1 ; 
	 int batch = lastIndex + 2  ; 
	 //System.out.println("Batch of " + batch);
	 for(int i = 0 ; i < size ; i++){
		 l1 = list_prev.get(i); 
		 
		 for(int j=i+1 ; j < size ; j++){
			 l2 = list_prev.get(j);
			 matchCount = 0 ; 
			 for(int k = 0 ; k < lastIndex ; k++){
				 if(l1.get(k).equals(l2.get(k))){
					 matchCount++; 
				 }
			 }
			 if(matchCount==lastIndex){
				 if(!l1.get(lastIndex).equals(l2.get(lastIndex))){
					
					l_combined = new ArrayList<String>(); 
					/*for(String l : l2){
						l_combined.add(l);
						
					}*/
					 
					l_combined.addAll(l1);
					l_combined.add(l2.get(lastIndex));
					Collections.sort(l_combined);
					int sc = calculateScore(l_combined);
					
					if(sc>=minFreq) { 
						//System.out.println(l_combined + " " + sc);
						c++; 
						l3.add(l_combined);
						freqMapGeneral.put(l_combined, sc);
					}
					
					
				 }
			 }
		 }		 
		 
		
	}
	 System.out.println("Set Size = " + batch + " || Number of sets = " + l3.size());
	 oneDown = l3 ; 
	 k = l3.size(); 
	 //System.out.println(l3.size());
	 
}
 

public static int calculateScore(List<String> list){
		int score = 0 ; 
		int match = 0 ; 
		for(List<String> trans : database){
			if(trans.containsAll(list)){
				score++; 
			}
			/*match = 0 ; 
			for(String entry : list){
				if(trans.contains(entry)){
					match++ ;
				}
			}
			if(match==list.size()){
				score++;
			}
		}*/
			
		}
		return score ; 
	}
	
	public static void buildFreqOne(){
		//System.out.println(freqMap.size());
		ArrayList<String> mapList = new ArrayList<String>(); 
		freqOne = new ArrayList<String>(); 
		Iterator it = freqMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry) it.next(); 
			int f = (int) pair.getValue();
			
			if(f>= minFreq){
				freqOne.add(pair.getKey().toString()); 
				mapList = new ArrayList<String>(); 
				mapList.add(pair.getKey().toString()); 
				freqMapGeneral.put(mapList, (Integer) pair.getValue());
				
				//System.out.println(pair.getKey() + "  " + pair.getValue());
			}
		}
		Collections.sort(freqOne);
		
	}
	/*
	public static void printSampleList(){
		for(List<String> l : database){
			System.out.println(l);
		}
	}*/

}
