package com.te.UIcontroller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import services.DataAcess;





/**
 * Servlet implementation class TestController
 */
@WebServlet("/TestController")
public class TestController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		System.out.println("Reache My TestController------------------>");	
		
		
		String id = request.getParameter("menu");
		System.out.println(id);
		DataAcess da = new DataAcess();
		java.util.HashMap<String, double[]> data = da.getData(id);
		
//		Double[] tempArrayVM1 =new Double[]{0.8475, 0.8444, 0.8451,  0.8418, 0.8264,0.8258, 0.8232,0.8233, 0.8258, 0.8446,0.8446, 0.8475, 0.8444, 0.8451,  0.8418, 0.8264,0.8258, 0.8232,0.8233, 0.8258, 0.8446,0.8258, 0.8446};
		
		JSONArray list = new JSONArray();
		for(Double temp : data.get("T04-Project-VM"))
		{
			list.add(temp);
		}
		
		
		JSONArray list2 = new JSONArray();
		for(Double temp : data.get("T04-Prj-vm1"))
		{
			list2.add(temp);
		}
		
		JSONArray list3 = new JSONArray();
		for(Double temp : data.get("T04-Prj-vm2"))
		{
			list3.add(temp);
		}
		
		JSONObject obj = new JSONObject();
		obj.put("vm1", list);
		
		obj.put("vm2", list2);
		obj.put("vm3", list3);

		
		/*HttpSession session=request.getSession(true);
		session.setAttribute("ArrayVM1", obj);*/
		HttpSession session=request.getSession(true);
		session.setAttribute("ArrayVM1", obj.get("vm1").toString());
		session.setAttribute("ArrayVM2", obj.get("vm2").toString());
		session.setAttribute("ArrayVM3", obj.get("vm3").toString());
		session.setAttribute("st1",da.getSt1());
		session.setAttribute("metric",id);
		/*HttpSession session=request.getSession(true);
		session.setAttribute("ArrayVM1", obj.get("vm1").toString());*/
		response.sendRedirect("CPUutilization.jsp");
		
	}

	
}
