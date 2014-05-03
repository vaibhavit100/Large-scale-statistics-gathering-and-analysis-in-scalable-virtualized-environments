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

<script>

$(function () {
    var options = {
            chart: {
             renderTo: 'container',
                zoomType: 'x',
                 type: 'spline',
                spacingRight: 20
            },
            title: {
                text: 'CPU Utilization Per Hour'
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
                    text: 'Time in Hours'
                }
            },
            yAxis: {
                title: {
                    text: 'CPU Utilization'
                }
            },
            tooltip: {
                shared: true
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                area: {
                    fillColor: {
                        linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    lineWidth: 1,
                    marker: {
                        enabled: false
                    },
                    shadow: false,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    },
                    threshold: null
                }
            },
    
            series: [{
                type: 'area',
                name: 'CPU Utilization',
                pointInterval: 24 * 3600000,  // 3600000(1 hr in ms)
       			 pointStart: Date.UTC(2013, 10, 30, 0, 0, 0, 0),
                data: []                 
            }]
       };
      
      
       $.getJSON('test.json', function(data) {      
       options.series[0].data = data;
       var chart = new Highcharts.Chart(options);
    });     
       
      
      
    
   });
   
    
 
</script>
</head>
<body bgcolor="#E6E6FA">

<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
</body>
</html>