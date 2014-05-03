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


public class syslog {

	/**
	 * @author Kuntal
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

	FileReader in = new FileReader("C:/Spring workspace/experiment/CMPE283InfoCollector/src/com/sjsu/cmpe283/CMPEInfoCollector/log/sysboot.log");
	BufferedReader br = new BufferedReader(in);
	
	FileWriter out =new FileWriter("C:/Spring workspace/experiment/CMPE283InfoCollector/src/com/sjsu/cmpe283/CMPEInfoCollector/log/sysparsed.txt");
	BufferedWriter bw = new BufferedWriter(out);
	
	String line = null;
	//line = br.readLine();
	//System.out.println(line);
	//bw.write("TSC\t CPU \t Boot\t");

	bw.write("Data \t\t\t\t Message\t");
	bw.write("\n");
	
	while((line = br.readLine())!=null)
	{
		String[] s = line.split(": ");
		
		System.out.println("Before writing");
		//bw.write("TSC\t CPU \t Boot\t");
		//bw.write("\n");
		
		for(int i=0;i<s.length-1;i+=2)
		{
			bw.write(s[i] +"\t\t"+s[i+1]+"\n");
			System.out.println(s[i] +"\t"+s[i+1]);
			System.out.println("Done");
		}
		//out.close();
		//in.close();
		}
		
		
	}
	
}

