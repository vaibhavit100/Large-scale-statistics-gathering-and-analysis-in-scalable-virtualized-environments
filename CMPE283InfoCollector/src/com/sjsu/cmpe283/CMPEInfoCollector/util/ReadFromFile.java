/**
 * 
 */
package com.sjsu.cmpe283.CMPEInfoCollector.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author Bhargav
 *
 */
public class ReadFromFile {
    private static StringBuffer   data   = null;
    private static DataInputStream in     = null;
    private static BufferedReader  br     = null;

    /**
     * Method used to read data from file
     * 
     * @param data
     */
    public static StringBuffer readFromFileMethod(String fileName) {
    	int counter = 0;
    	HashMap<String, String> pingDetails = new HashMap<String, String>();
        try {
            // Initialize the file input stream
            FileInputStream fstream = new FileInputStream(fileName);
            data = new StringBuffer();

            // Get DataInputStream object
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            String strLine = null;

            // Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // append the content to StringBulder
            	pingDetails.put(String.valueOf(counter), strLine);
                data.append(strLine);
                data.append("\n\r");
                counter++;
                if(counter == 10)
                	break;
            }

            // Close the input stream
            in.close();

        } catch (Exception e) {
            System.out.println("Issue with reading file " + e.getMessage());
        } finally {
        	
        	counter = 0;
        	
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("Issue with reading file " + e.getMessage());
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                	System.out.println("Issue with reading file " + e.getMessage());
                }
            }
        }

        return data;
    }
}
