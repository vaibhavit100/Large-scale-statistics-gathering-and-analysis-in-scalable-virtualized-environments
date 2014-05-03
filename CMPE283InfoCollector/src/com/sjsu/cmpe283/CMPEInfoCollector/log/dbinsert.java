/**
 * 
 */
package com.sjsu.cmpe283.CMPEInfoCollector.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;


public class dbinsert {


	/**
	 * @author Kuntal
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

	HashMap<String,String> infoList = new HashMap<String,String>();
	FileReader in = new FileReader("C:/Spring workspace/experiment/CMPE283InfoCollector/src/com/sjsu/cmpe283/CMPEInfoCollector/log/vmkernel.log");
	BufferedReader br = new BufferedReader(in);
	
	//FileWriter out =new FileWriter("D:/NewWorkSpace/CMPE283InfoCollector/src/parsed.txt");
	//BufferedWriter bw = new BufferedWriter(out);
	
	String line = null;
	String pattern = "succeeded";
	//line = br.readLine();
	//System.out.println(line);
	//bw.write("TSC\t CPU \t Boot\t");

	//bw.write("Data \t\t\t\t Message\t");
	//bw.write("\n");
	
	while((line = br.readLine())!=null)
	{
		String[] s = line.split("\\)");
		
		boolean b = line.contains(pattern);
		System.out.println("Before writing");
		//bw.write("TSC\t CPU \t Boot\t");
		//bw.write("\n");
		if(b == true)
		{
		String l2 = line.concat("Yes");
		}
		else
		{
		String l2 = line.concat("NO");
		}

		
		for(int i=0;i<s.length-1;i+=2)
		{
			
			//bw.write(s[i] +"\t\t"+s[i+1]+"\n");
			infoList.put(s[i], s[i+1]);
			System.out.println("Done");
		}
		//out.close();
		//in.close();
		Gson gson = new Gson();
		String data = gson.toJson(infoList);
		//System.out.println(data);
		
		try {
			Mongo mongo = new Mongo("130.65.133.178",27017);
			//Mongo mongo = new Mongo("localhost",27017);
			//DB db = mongo.getDB("dbname");
			DB db = mongo.getDB("project2");
			//DBCollection collection1 = db.getCollection("vmDetails");
            DBCollection collection1 = db.getCollection("vmtest");
			DBObject basicdbObject = (DBObject)JSON.parse(data);
			//System.out.println("After basicdbObj");

			collection1.insert(basicdbObject);
			System.out.println("After Insert");

/*					DBCursor dbCursor = collection1.find();
			if(dbCursor.hasNext()){
				DBObject dbobject = dbCursor.next();
			}
*/
			
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		

		}
		
		
	}
	
}


