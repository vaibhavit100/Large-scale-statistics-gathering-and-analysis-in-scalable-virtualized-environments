package services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;



import services.connectionHelper;

public class DataAcess {
	int st1=0,st2=0,st3=0;
	public int getSt1() {
		return st1;
	}
	public int getSt2() {
		return st2;
	}
	public int getSt3() {
		return st3;
	}
	public HashMap<String,double[]> getData(String metric){
		HashMap<String,double[]> finaldata = new HashMap<String,double[]>();

		double[] values1,values2,values3;
		int k1=0,k2=0,k3=0;
		int rcount1=0,rcount2=0,rcount3=0;
		int i1=0,i2=0,i3=0;
		connectionHelper connectionhelper= new connectionHelper();
		Connection conn=(Connection) connectionhelper.getConnection();
		try {
			Statement stmt= (Statement) conn.createStatement();
			String query1 = "select avg("+metric+"),date from vmstats where vmName='T04-Project-VM' group by date";
			String query2 = "select avg("+metric+"),date from vmstats where vmName='T04-Prj-vm2' group by date";
			String query3 = "select avg("+metric+"),date from vmstats where vmName='T04-Prj-vm3' group by date";
			//start for first vm
			ResultSet r=stmt.executeQuery(query1);
			if(r.last()){
				rcount1 = r.getRow();
				r.beforeFirst();
			}
			values1 = new double[rcount1];
			while(r.next()){
				if(k1==0){
					this.st1=Integer.parseInt(r.getString("date"));
					k1=1;
				}
				values1[i1]=Double.parseDouble(r.getString(1));
				System.out.println(values1[i1]);
				System.out.println(r.getString(1));
				i1++;
				
			}
			finaldata.put("T04-Project-VM",values1);

			//start for second vm

			ResultSet r1=stmt.executeQuery(query2);
			if(r1.last()){
				rcount2 = r1.getRow();
				r1.beforeFirst();
			}
			values2 = new double[rcount2];
			while(r1.next()){
				if(k2==0){
					this.st2=Integer.parseInt(r1.getString("date"));
					k2=1;
				}
				values2[i2]=Double.parseDouble(r1.getString(1));
				System.out.println(values2[i2]);
				System.out.println(r1.getString(1));

				i2++;
			}
			finaldata.put("T04-Prj-vm1",values2);			
			
			//start for third vm

			ResultSet r2=stmt.executeQuery(query3);
			if(r2.last()){
				rcount3 = r2.getRow();
				r2.beforeFirst();
			}
			values3 = new double[rcount3];
			while(r2.next()){
				if(k3==0){
					this.st3=Integer.parseInt(r2.getString("date"));
					k3=1;
				}
				values3[i3]=Double.parseDouble(r2.getString(1));
				System.out.println(values3[i3]);
				System.out.println(r2.getString(1));
				i3++;
			}
			finaldata.put("T04-Prj-vm2",values3);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finaldata;

	}

}
