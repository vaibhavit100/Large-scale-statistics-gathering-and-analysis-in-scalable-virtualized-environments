
package com.sjsu.cmpe283.CMPEInfoCollector.test;


import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.sjsu.cmpe283.CMPEInfoCollector.VMInitalizaler;
import com.sjsu.cmpe283.CMPEInfoCollector.threads.VHostMHealthUpdateThread;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestCases {
   
    static Connection connection;
    static String url;
    static String URL ="jdbc:mysql://localhost:3306/cmpe283_sql";
    static String username= "root" ;
    static String password = "root";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("Before");
    }
   
   
    @Test
    public void testIfDataPresentInSql() throws SQLException, ClassNotFoundException
    {
    Class.forName("com.mysql.jdbc.Driver");
    connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/cmpe283_sql","root","root");
    Statement st = connection.createStatement();
    ResultSet rs = st.executeQuery("select * from vmstats");
    System.out.println("result set rs="+rs);
    assertNotNull(rs);
    }
   


   
    @Test
    public void testIfCPUStatsExists() throws ClassNotFoundException, SQLException
    {

    Class.forName("com.mysql.jdbc.Driver");
    connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/cmpe283_sql","root","root");
    Statement st = connection.createStatement();
    ResultSet rs = st.executeQuery("select cpu from vmstats");
    System.out.println("result set rs="+rs);
    assertNotNull(rs);
    connection.close();
    }
   
   
    @Test
    public void testIfMemoryStatsExists() throws ClassNotFoundException, SQLException
    {

    Class.forName("com.mysql.jdbc.Driver");
    connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/cmpe283_sql","root","root");
    Statement st = connection.createStatement();
    ResultSet rs = st.executeQuery("select mem from vmstats");
    System.out.println("result set rs="+rs);
    assertNotNull(rs);
    connection.close();
    }
   
   
   
    @Test
    public void testIfDiskStatsExists() throws ClassNotFoundException, SQLException
    {

    Class.forName("com.mysql.jdbc.Driver");
    connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/cmpe283_sql","root","root");
    Statement st = connection.createStatement();
    ResultSet rs = st.executeQuery("select disk from vmstats");
    System.out.println("result set rs="+rs);
    assertNotNull(rs);
    connection.close();
    }
   
    @Test
    public void testIfNetworkStatsExists() throws ClassNotFoundException, SQLException
    {

    Class.forName("com.mysql.jdbc.Driver");
    connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/cmpe283_sql","root","root");
    Statement st = connection.createStatement();
    ResultSet rs = st.executeQuery("select net from vmstats");
    System.out.println("result set rs="+rs);
    assertNotNull(rs);
    connection.close();
    }
    @Test
    public void testIfSystemStatsExists() throws ClassNotFoundException, SQLException
    {

    Class.forName("com.mysql.jdbc.Driver");
    connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/cmpe283_sql","root","root");
    Statement st = connection.createStatement();
    ResultSet rs = st.executeQuery("select sys from vmstats");
    System.out.println("result set rs="+rs);
    assertNotNull(rs);
    connection.close();
    }
   
    @Test
    public void testIfVMNameStatsExists() throws ClassNotFoundException, SQLException
    {

    Class.forName("com.mysql.jdbc.Driver");
    connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/cmpe283_sql","root","root");
    Statement st = connection.createStatement();
    ResultSet rs = st.executeQuery("select vmname from vmstats");
    System.out.println("result set rs="+rs);
    assertNotNull(rs);
    connection.close();
    }

   
   
    @Test
    public void testIfDBPresentInMongo() throws UnknownHostException
    {
    MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
    DB dbName = mongoClient.getDB("local");
    assertNotNull(dbName);
    mongoClient.close();
    }
   
   
   
    @Test
    public void testIfCollectionPresent() throws UnknownHostException
    {
    MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
    DB dbName = mongoClient.getDB("project2");
    DBCollection collection = dbName.getCollection("sample");
    assertEquals(collection.getName(),"sample");
    mongoClient.close();
    }
   
   
    @Test
    public void testIfDataExistsInMongo() throws UnknownHostException
    {
    MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
    DB dbName = mongoClient.getDB("project2");
    DBCollection collection = dbName.getCollection("sample");
    assert(collection.find().count()!=0);
    mongoClient.close();
    }
   
   
    @Test
    public void testIfArchievedDataCollectionIsPresent() throws UnknownHostException
    {
    MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
    DB dbName = mongoClient.getDB("project2");
    DBCollection collection = dbName.getCollection("sample");
    assertNotNull(collection);
    mongoClient.close();
    }
   
    @Test
    public void testIfArchievedDataIsPresent() throws UnknownHostException
    {
    MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
    DB dbName = mongoClient.getDB("project2");
    DBCollection collection = dbName.getCollection("sample1");
    assert(collection.find().count()!=0);
    mongoClient.close();
    }
   
   
   
    @Test
    public void BasicTesting(){
        String SERVER_NAME = "130.65.132.163";
        String USER_NAME = "administrator";
        String PASSWORD = "12!@qwQW";
        String HOSTNAME = "130.65.132.165";
        String url = "https://" + SERVER_NAME + "/sdk/vimService";
        DisplayHostCounter dhc = new DisplayHostCounter();
        dhc.testing(url,USER_NAME,PASSWORD,HOSTNAME);
        Assert.assertTrue(true);
    }

    @Test
    public void BasicTesting1(){
        String SERVER_NAME = "130.65.132.163";
        String USER_NAME = "administrator";
        String PASSWORD = "12!@qwQW";
        String HOSTNAME = "130.65.132.166";
        String url = "https://" + SERVER_NAME + "/sdk/vimService";
        DisplayHostCounter dhc = new DisplayHostCounter();
        dhc.testing(url,USER_NAME,PASSWORD,HOSTNAME);
        Assert.assertFalse(false);
    }

   
   
   
   
   
   
   
   
   
    @Test
    public void BasicTesting2(){
        String SERVER_NAME = "130.65.132.163";
        String USER_NAME = "administrator";
        String PASSWORD = "12!@qw";
        String HOSTNAME = "130.65.132.167";
        String url = "https://" + SERVER_NAME + "/sdk/vimService";
        DisplayHostCounter dhc = new DisplayHostCounter();
        dhc.testing(url,USER_NAME,PASSWORD,HOSTNAME);
        Assert.assertFalse(false);
    }

//    @Test
//    public void BasicTesting3(){
//        String SERVER_NAME = "130.65.132.163";
//        String USER_NAME = "administrator";
//        String PASSWORD = "12!@qwQW";
//        String HOSTNAME = "130.65.157.57";
//        String url = "https://" + SERVER_NAME + "/sdk/vimService";
//        DisplayHostCounter dhc = new DisplayHostCounter();
//        dhc.testing(url,USER_NAME,PASSWORD,HOSTNAME);
//        Assert.assertFalse(false);
//    }

    @Test
    public void BasicTesting4(){
        String SERVER_NAME = "130.65.132.163";
        String USER_NAME = "administrator";
        String PASSWORD = "12!@qwQW";
        String HOSTNAME = "130.65.157.57";
        String url = "htt://" + SERVER_NAME + "/sdk/vimService";
        DisplayHostCounter dhc = new DisplayHostCounter();
        dhc.testing(url,USER_NAME,PASSWORD,HOSTNAME);
        Assert.assertFalse(false);
    }   

    @Test
    public void PingTestPass()throws Exception{
        String ipAddress = "130.65.133.178";
        VMInitalizaler init = new VMInitalizaler();
        init.pingVM(ipAddress);
        Assert.assertTrue(true);
    }
   
   
    @Test
    public void PingTestFail()throws Exception{
        String ipAddress = "130.65.133.170";
        VMInitalizaler init = new VMInitalizaler();
        init.pingVM(ipAddress);
        Assert.assertFalse(false);
    }
   
    @Test
    public void CheckHostPass(){
        String hostIP = "https://130.65.132.166/sdk";
        VHostMHealthUpdateThread thread = new VHostMHealthUpdateThread(hostIP);
        Assert.assertTrue(true);
    }
   
    @Test
    public void CheckHostFail(){
        String hostIP = "https://130.65.132.166.00/sdk";
        VHostMHealthUpdateThread thread = new VHostMHealthUpdateThread(hostIP);
        Assert.assertFalse(false);
    }
   

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("After");
    }


}


