package com.sjsu.cmpe283.CMPEInfoCollector.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfEntityMetric;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfMetricIntSeries;
import com.vmware.vim25.PerfMetricSeries;
import com.vmware.vim25.PerfProviderSummary;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PerfSampleInfo;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class DisplayHostCounter {

	static final String SERVER_NAME = "130.65.157.14";
	static final String USER_NAME = "administrator";
	static final String PASSWORD = "12!@qwQW";
	private static final String HOSTNAME = "130.65.132.163";
	private static final int SELECTED_COUNTER_ID = 6; // Active (mem) in KB (absolute)
	static Integer[] a = {5,23,124,142,155};

	public static void main(String[] args) {
		String url = "https://" + SERVER_NAME + "/sdk/vimService";
		try {
			ServiceInstance si = new ServiceInstance(new URL(url), USER_NAME, PASSWORD, true);
			HostSystem host = (HostSystem) new InventoryNavigator(si.getRootFolder()).searchManagedEntity("HostSystem", HOSTNAME);
			VirtualMachine vm[] = host.getVms();
			
			//System.out.println(vm[4].getGuest().getIpAddress());
			PerformanceManager perfMgr = si.getPerformanceManager();
			System.out.println("Counter ID = " + SELECTED_COUNTER_ID);

			PerfProviderSummary summary = perfMgr.queryPerfProviderSummary(host);
			
			int perfInterval = summary.getRefreshRate();
			
		//	Integer myi = new Integer(5000);
		//	System.out.println(myi);
			
		//	System.out.println("Refresh rate = " + perfInterval);

			PerfMetricId[] queryAvailablePerfMetric = perfMgr.queryAvailablePerfMetric(host, null, null, perfInterval);
			
			PerfCounterInfo[] pci = perfMgr.getPerfCounter();
			/*
			PerfCounterInfo[] perfCounters = perfMgr.getPerfCounter();
			
			for(int l=0;l<queryAvailablePerfMetric.length;l++){
				PerfCounterInfo perfCounterInfo = perfCounters[l];
				System.out.println(queryAvailablePerfMetric[l].counterId+"is counter for: ");
				
				String perfCounterString = perfCounterInfo.getNameInfo().getLabel() 
						+ " (" + perfCounterInfo.getGroupInfo().getKey() + ") in "
						+ perfCounterInfo.getUnitInfo().getLabel() 
						+ " (" + perfCounterInfo.getStatsType().toString() + ")";
	
				System.out.println(perfCounterInfo.getKey() + " : " + perfCounterString);
				
			}
			
			*/
			
			ArrayList<PerfMetricId> list = new ArrayList<PerfMetricId>();
			
			
			for (int i2 = 0; i2 < queryAvailablePerfMetric.length; i2++) {
				PerfMetricId perfMetricId = queryAvailablePerfMetric[i2];
				if (SELECTED_COUNTER_ID == perfMetricId.getCounterId()) {
					list.add(perfMetricId);
				}
			}
			PerfMetricId[] pmis = list.toArray(new PerfMetricId[list.size()]);

			PerfQuerySpec qSpec = new PerfQuerySpec();
			
			qSpec.setEntity(host.getMOR());
			qSpec.setMetricId(pmis);
			
			qSpec.intervalId = perfInterval;

			PerfEntityMetricBase[] pembs = perfMgr.queryPerf(new PerfQuerySpec[] { qSpec });
			
			for (int i = 0; pembs != null && i < pembs.length; i++) {
				
				PerfEntityMetricBase val = pembs[i];
				PerfEntityMetric pem = (PerfEntityMetric) val;
				PerfMetricSeries[] vals = pem.getValue();
				PerfSampleInfo[] infos = pem.getSampleInfo();
 
				for (int j = 0; vals != null && j < vals.length; ++j) {
					PerfMetricIntSeries val1 = (PerfMetricIntSeries) vals[j];
					long[] longs = val1.getValue();
//					for (int k = 0; k < longs.length; k++) {
//						if(k==124)
//						System.out.println(pci[k].getNameInfo().getLabel()+"("+ pci[k].getGroupInfo().getKey()+")"+"in"+" "+pci[k].getUnitInfo().getLabel()+"(" +pci[k].getStatsType().toString()+")" + ":" + longs[k] +" --- " + pci[k].getRollupType().name());
//					}
					 
					for(int k:a)
						System.out.println(pci[k].getNameInfo().getLabel()+"("+ pci[k].getGroupInfo().getKey()+")"+"in"+" "+pci[k].getUnitInfo().getLabel()+"(" +pci[k].getStatsType().toString()+")" + ":" + longs[k] +" --- " + pci[k].getRollupType().name());

					
					
					System.out.println();
				}
			}
			si.getServerConnection().logout();
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testing(String url, String USER_NAME, String PASSWORD, String HOSTNAME) {
		try {
			ServiceInstance si = new ServiceInstance(new URL(url), USER_NAME, PASSWORD, true);
			HostSystem host = (HostSystem) new InventoryNavigator(si.getRootFolder()).searchManagedEntity("HostSystem", HOSTNAME);
			VirtualMachine vm[] = host.getVms();
			
			//System.out.println(vm[4].getGuest().getIpAddress());
			PerformanceManager perfMgr = si.getPerformanceManager();
			System.out.println("Counter ID = " + SELECTED_COUNTER_ID);

			PerfProviderSummary summary = perfMgr.queryPerfProviderSummary(host);
			
			int perfInterval = summary.getRefreshRate();
			
		//	Integer myi = new Integer(5000);
		//	System.out.println(myi);
			
		//	System.out.println("Refresh rate = " + perfInterval);

			PerfMetricId[] queryAvailablePerfMetric = perfMgr.queryAvailablePerfMetric(host, null, null, perfInterval);
			
			PerfCounterInfo[] pci = perfMgr.getPerfCounter();
			/*
			PerfCounterInfo[] perfCounters = perfMgr.getPerfCounter();
			
			for(int l=0;l<queryAvailablePerfMetric.length;l++){
				PerfCounterInfo perfCounterInfo = perfCounters[l];
				System.out.println(queryAvailablePerfMetric[l].counterId+"is counter for: ");
				
				String perfCounterString = perfCounterInfo.getNameInfo().getLabel() 
						+ " (" + perfCounterInfo.getGroupInfo().getKey() + ") in "
						+ perfCounterInfo.getUnitInfo().getLabel() 
						+ " (" + perfCounterInfo.getStatsType().toString() + ")";
	
				System.out.println(perfCounterInfo.getKey() + " : " + perfCounterString);
				
			}
			
			*/
			
			ArrayList<PerfMetricId> list = new ArrayList<PerfMetricId>();
			
			
			for (int i2 = 0; i2 < queryAvailablePerfMetric.length; i2++) {
				PerfMetricId perfMetricId = queryAvailablePerfMetric[i2];
				if (SELECTED_COUNTER_ID == perfMetricId.getCounterId()) {
					list.add(perfMetricId);
				}
			}
			PerfMetricId[] pmis = list.toArray(new PerfMetricId[list.size()]);

			PerfQuerySpec qSpec = new PerfQuerySpec();
			
			qSpec.setEntity(host.getMOR());
			qSpec.setMetricId(pmis);
			
			qSpec.intervalId = perfInterval;

			PerfEntityMetricBase[] pembs = perfMgr.queryPerf(new PerfQuerySpec[] { qSpec });
			
			for (int i = 0; pembs != null && i < pembs.length; i++) {
				
				PerfEntityMetricBase val = pembs[i];
				PerfEntityMetric pem = (PerfEntityMetric) val;
				PerfMetricSeries[] vals = pem.getValue();
				PerfSampleInfo[] infos = pem.getSampleInfo();
 
				for (int j = 0; vals != null && j < vals.length; ++j) {
					PerfMetricIntSeries val1 = (PerfMetricIntSeries) vals[j];
					long[] longs = val1.getValue();
//					for (int k = 0; k < longs.length; k++) {
//						if(k==124)
//						System.out.println(pci[k].getNameInfo().getLabel()+"("+ pci[k].getGroupInfo().getKey()+")"+"in"+" "+pci[k].getUnitInfo().getLabel()+"(" +pci[k].getStatsType().toString()+")" + ":" + longs[k] +" --- " + pci[k].getRollupType().name());
//					}
					 
					for(int k:a)
						System.out.println(pci[k].getNameInfo().getLabel()+"("+ pci[k].getGroupInfo().getKey()+")"+"in"+" "+pci[k].getUnitInfo().getLabel()+"(" +pci[k].getStatsType().toString()+")" + ":" + longs[k] +" --- " + pci[k].getRollupType().name());

					
					
					System.out.println();
				}
			}
			si.getServerConnection().logout();
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}