package com.sjsu.cmpe283.CMPEInfoCollector;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class LogProcess implements Runnable {

	static double VM1sumDisk;
	static double VM1sumNet;
	static double VM1sumCpu;
	static double VM1sumMem;
	static double VM1sumSys;
	static double VM1count;

	static double VM2sumDisk;
	static double VM2sumNet;
	static double VM2sumCpu;
	static double VM2sumMem;
	static double VM2sumSys;
	static double VM2count;
	
	static double VM3sumDisk;
	static double VM3sumNet;
	static double VM3sumCpu;
	static double VM3sumMem;
	static double VM3sumSys;
	static double VM3count;

	@Override
	public void run() {
		while(true){
		try{
			VM1sumDisk = VM1sumNet = VM1sumCpu = VM1sumMem = VM1sumSys = VM1count =0;
			VM2sumDisk = VM2sumNet = VM2sumCpu = VM2sumMem = VM2sumSys = VM2count =0;
			VM3sumDisk = VM3sumNet = VM3sumCpu = VM3sumMem = VM3sumSys = VM3count =0;
			System.out.println("hiiiiiiiiiiiiiiiiiiiiiiii in logprocess");
			MongoClient mongo = new MongoClient("localhost", 27017);
            DB db = mongo.getDB("project2");
			DBCollection collection1 = db.getCollection("sample");

			DBCursor firstDoc = collection1.find();
			
			while(firstDoc.hasNext()){
				DBObject o = firstDoc.next();
				System.out.println(o);
				String msg = (String)o.get("message");
				String[] values = msg.split(",");

				HashMap<String,String> info = new HashMap<String,String>();
				for(int i=0;i<values.length;i++){
					String key = values[i].split(":")[0];
					String value = values[i].split(":")[1];
					info.put(key,value);
				}
				
				String name = info.get("vmIP");

				if(name == null){
					continue;
				}
				
					switch(name){

					case "T04-Project-VM":
						VM1count += 1;
						for(String key: info.keySet()){
							if(!key.equals("datetime") && !key.equals("vmIP") && !key.equals("_id")){
								long value = Integer.parseInt(info.get(key));
								switch(key){
								case "disk":
										VM1sumDisk += value;
									break;
								case "net":
									VM1sumNet += value;
									break;
								case "mem":
									VM1sumMem += value;
									break;
								case "cpu":
									VM1sumCpu += value;
									break;
								case "sys":
									VM1sumSys += value;
									break;
								}
							}
						}

						break;
					case "T04-Prj-vm2":
						VM2count += 1;
						for(String key: info.keySet()){
							if(!key.equals("datetime") && !key.equals("vmIP") && !key.equals("_id")){
								long value = Integer.parseInt(info.get(key));
								switch(key){
								case "disk":
										VM2sumDisk += value;
									break;
								case "net":
									VM2sumNet += value;
									break;
								case "mem":
									VM2sumMem += value;
									break;
								case "cpu":
									VM2sumCpu += value;
									break;
								case "sys":
									VM2sumSys += value;
									break;
								}
							}
						}						
						break;
					case "T04-Prj-vm3":
						VM3count += 1;
						for(String key: info.keySet()){
							if(!key.equals("datetime") && !key.equals("vmIP") && !key.equals("_id")){
								long value = Integer.parseInt(info.get(key));
								switch(key){
								case "disk":
										VM3sumDisk += value;
									break;
								case "net":
									VM3sumNet += value;
									break;
								case "mem":
									VM3sumMem += value;
									break;
								case "cpu":
									VM3sumCpu += value;
									break;
								case "sys":
									VM3sumSys += value;
									break;
								}
							}
						}
						break;
						default : 
						break;
					}
					collection1.remove(o);
			}
			
			System.out.println(VM1count+"\t"+VM1sumCpu + "\t" +VM1sumDisk +"\t" + VM1sumMem + "\t" + VM1sumNet);
			System.out.println(VM2count+"\t"+VM2sumCpu + "\t" +VM2sumDisk +"\t" + VM2sumMem + "\t" + VM2sumNet);
			System.out.println(VM3count+"\t"+VM3sumCpu + "\t" +VM3sumDisk +"\t" + VM3sumMem + "\t" + VM3sumNet);

			connectionHelper connectionhelper= new connectionHelper();
			Connection conn=(Connection) connectionhelper.getConnection();
			
			Statement stmt= (Statement) conn.createStatement();
			
			Date sd = new Date();

			int d = sd.getDate();
			int h = sd.getHours();
			String vsql1="INSERT into vmstats(vmName,mem,cpu,net,disk,sys,date,hour) VALUES('T04-Project-VM',"+ (double)VM1sumMem/VM1count +","+(double)VM1sumCpu/VM1count+","+(double)VM1sumNet/VM1count+","+(double)VM1sumDisk/VM1count+","+(double)VM1sumSys/VM1count+","+d+","+h+")";
			String vsql2="INSERT into vmstats(vmName,mem,cpu,net,disk,sys,date,hour) VALUES('T04-Prj-vm2',"+ (double)VM2sumMem/VM2count +","+(double)VM2sumCpu/VM2count+","+(double)VM2sumNet/VM2count+","+(double)VM2sumDisk/VM2count+","+(double)VM2sumSys/VM2count+","+d+","+h+")";
			String vsql3="INSERT into vmstats(vmName,mem,cpu,net,disk,sys,date,hour) VALUES('T04-Prj-vm3',"+ (double)VM3sumMem/VM3count +","+(double)VM3sumCpu/VM3count+","+(double)VM3sumNet/VM3count+","+(double)VM3sumDisk/VM3count+","+(double)VM3sumSys/VM3count+","+d+","+h+")";			
			stmt.executeUpdate(vsql1);
			stmt.executeUpdate(vsql2);
			stmt.executeUpdate(vsql3);
			Thread.sleep(300000);
		}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
}
