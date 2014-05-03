/**
 * 
 */
package com.sjsu.cmpe283.CMPEInfoCollector.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Bhargav
 *
 */
public class WriteFromFile {

    private static BufferedWriter out = null;
    
    /**
     * Method used to write data to file
     * @param data
     */
    public static boolean writeToFileMethod(String data,String fileName){
        boolean flag = false;
        
        try{
        out = new BufferedWriter(new FileWriter(fileName,false));
        out.write(data.toString());
        out.close();
        flag =  true;
        }catch(IOException e){
            System.out.println("Issue with writing file "+e.getMessage());
        }finally{
            if(out != null)
                try {
                    out.close();
                } catch (IOException e) {
                	System.out.println("Issue with writing file "+e.getMessage());
                }
        }
        
        return flag;
    }
	
}
