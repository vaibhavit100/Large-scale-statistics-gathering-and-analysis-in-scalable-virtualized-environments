<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page</title>
<h1><u>VM Statistics</u></h1>

<script src="jquery-1.10.2.js"></script>
<script src="exporting.js"></script>
<script src="highcharts.js"></script>



<% 
String s =(String) session.getAttribute("ArrayVM1");
String newString=s.substring(1,s.length()-1);
 System.out.println("new  s after sub:"+newString);
 
String s1 =(String) session.getAttribute("ArrayVM2");
String newString1=s1.substring(1,s1.length()-1);
 //System.out.println("new  s after sub:"+newString);
 
 String s2 =(String) session.getAttribute("ArrayVM3");
String newString2=s2.substring(1,s2.length()-1);
 //System.out.println("new  s after sub:"+newString); 
 
 
 %>
</head>
<body bgcolor="#E6E6FA">
<input id="title" name="title" type = "hidden" value="<%=session.getAttribute("metric")%>">
<input id="startdate" name="startdate" type = "hidden" value="<%=session.getAttribute("st1")%>">
<input id="tempArray" name="tempArray" type = "hidden" value=<%=newString%>>
<input id="tempArray1" name="tempArray" type = "hidden" value=<%=newString1%>>
<input id="tempArray2" name="tempArray" type = "hidden" value=<%=newString2%>>

<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>

<script>

var title1= document.getElementById("title").value;

var startdate1 =  document.getElementById("startdate").value;

var VM1Div= document.getElementById("tempArray");
var VM1string =VM1Div.value; 
var res = new Array();
res = VM1string.split(",");
var resArray = res.join(); 

var VM2Div= document.getElementById("tempArray1");
var VM2string =VM2Div.value; 
var res = new Array();
res = VM2string.split(",");
var resArray1 = res.join();

var VM3Div= document.getElementById("tempArray2");
var VM3string =VM3Div.value; 
var res = new Array();
res = VM3string.split(",");
var resArray2 = res.join();


var obj = jQuery.parseJSON( '{ "vm1": ['+resArray+'],"vm2": ['+resArray1+'],"vm3": ['+resArray2+'] }' );
//alert('new array'+res);

$(function () {


    var options = {
            chart: {
             renderTo: 'container',
                zoomType: 'x',
                
                spacingRight: 20
            },
            title: {
                text: 'Log Analysis Graph'
            },
            subtitle: {
                text: document.ontouchstart === undefined ?
                    'Click and drag in the plot area to zoom in' :
                    'Pinch the chart to zoom in'
            },
            xAxis: {
                  type: 'datetime',
               
                maxZoom: 3000000, // (300000 is 5 mins)
                title: {
                    text: 'Time'
                }
            },
            yAxis: {
                title: {
					text: ''
                }
            },
            tooltip: {
                shared: true
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: [{

                name: 'vm1',
                pointInterval: 24 * 3600000 * 1,  // 3600000(1 hr in ms)
       			pointStart: Date.UTC(2013, 10, 30, 0, 0, 0, 0),
                data:[]             
            },
                        
             {

                name: 'vm2',                
                pointInterval: 24 * 3600000 * 1,  // 3600000(1 hr in ms)
       			 pointStart: Date.UTC(2013, 10, 30, 0, 0, 0, 0),
                data: []                 
            },
            {

                name: 'vm3',            
                pointInterval: 24 * 3600000 * 1,  // 3600000(1 hr in ms)
       			 pointStart: Date.UTC(2013, 10, 30, 0, 0, 0, 0),
                data: [ ]                 
            }            
            ]
       };
  
   
    options.yAxis.title.text = title1;

     options.series[0].data =obj.vm1  ;
      options.series[1].data =obj.vm2  ;
       options.series[2].data =obj.vm3  ;
      var chart = new Highcharts.Chart(options);
             
   });      
 
</script>
</body>
</html>