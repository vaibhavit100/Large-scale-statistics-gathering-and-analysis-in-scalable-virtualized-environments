/**
 * 
 */
package com.sjsu.cmpe283.CMPEInfoCollector.threads;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;
import com.sjsu.cmpe283.CMPEInfoCollector.common.CommonConstants;
import com.sjsu.cmpe283.CMPEInfoCollector.util.OperationsUtilVM;
import com.sjsu.cmpe283.CMPEInfoCollector.util.SystemCache;
import com.sjsu.cmpe283.CMPEInfoCollector.vo.VMDetails;
import com.sjsu.cmpe283.CMPEInfoCollector.vo.VMInfomartion;
import com.vmware.vim25.GuestInfo;
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
import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSnapshotInfo;
import com.vmware.vim25.VirtualMachineStorageInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * @author 
 * 
 */
public class VMHealthUpdateThread implements Runnable {

	//static final String SERVER_NAME = "130.65.132.14";
	static final String SERVER_NAME = "130.65.132.163";
	static final String USER_NAME = "administrator";
	static final String PASSWORD = "12!@qwQW";
	private String ip;
	private ServiceInstance serviceInst;
	private static final int SELECTED_COUNTER_ID = 6; // Active (mem) in KB
														// (absolute)
	static Integer[] a = { 5, 23, 124, 142, 156 };
	static String[] aName = { "cpu", "mem", "disk", "net", "sys" };
	private HashMap<String, String> infoList = new HashMap<String, String>();
	int counter = 0;
	Gson gson = new Gson();

	public VMHealthUpdateThread(String hostIP) {
		this.ip = hostIP;
	}

	@Override
	public void run() {

		try {

			while (true) {

				String url = "https://" + SERVER_NAME + "/sdk/vimService";
				try {
					ServiceInstance si = new ServiceInstance(new URL(url),
							USER_NAME, PASSWORD, true);
					VirtualMachine host = (VirtualMachine) new InventoryNavigator(
							si.getRootFolder()).searchManagedEntity(
							"VirtualMachine", ip);
					PerformanceManager perfMgr = si.getPerformanceManager();
					PerfProviderSummary summary = perfMgr
							.queryPerfProviderSummary(host);
					int perfInterval = summary.getRefreshRate();
					PerfMetricId[] queryAvailablePerfMetric = perfMgr
							.queryAvailablePerfMetric(host, null, null,
									perfInterval);
					PerfCounterInfo[] pci = perfMgr.getPerfCounter();
					ArrayList<PerfMetricId> list = new ArrayList<PerfMetricId>();

					for (int i2 = 0; i2 < queryAvailablePerfMetric.length; i2++) {
						PerfMetricId perfMetricId = queryAvailablePerfMetric[i2];
						if (SELECTED_COUNTER_ID == perfMetricId.getCounterId()) {
							list.add(perfMetricId);
						}
					}
					PerfMetricId[] pmis = list.toArray(new PerfMetricId[list
							.size()]);
					PerfQuerySpec qSpec = new PerfQuerySpec();
					qSpec.setEntity(host.getMOR());
					qSpec.setMetricId(pmis);

					qSpec.intervalId = perfInterval;
					PerfEntityMetricBase[] pembs = perfMgr
							.queryPerf(new PerfQuerySpec[] { qSpec });

					for (int i = 0; pembs != null && i < pembs.length; i++) {

						PerfEntityMetricBase val = pembs[i];
						PerfEntityMetric pem = (PerfEntityMetric) val;
						PerfMetricSeries[] vals = pem.getValue();
						PerfSampleInfo[] infos = pem.getSampleInfo();

						for (int j = 0; vals != null && j < vals.length; ++j) {
							PerfMetricIntSeries val1 = (PerfMetricIntSeries) vals[j];
							long[] longs = val1.getValue();

							for (int k : a) {
								infoList.put(aName[counter],
										String.valueOf(longs[k]));
								counter++;
							}
							counter = 0;

						}
					}
					si.getServerConnection().logout();
				} catch (InvalidProperty e) {
					e.printStackTrace();
				} catch (RuntimeFault e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				
				infoList.put("vmIP",ip);
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				infoList.put("datetime",sd.format(new Date()));
				counter = 0;
				try {
//					DBObject basicdbObject = (DBObject) JSON.parse(gson.toJson(infoList));
					System.out.println("Reached File Writing Stage...");
					
					File file = new File("/home/t04/Downloads/logstashinput");
					 
					// if file doesnt exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}
		 
					FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
					BufferedWriter bw = new BufferedWriter(fw);
//					bw.append(basicdbObject.toString());
					for(String key : infoList.keySet()){
						if(key.equals("net")){
							bw.append(key +":"+ infoList.get(key));
						}
						else{
							bw.append(key +":"+ infoList.get(key)+",");
						}
					}
					bw.append('\n');
					bw.flush();
					bw.close();
					//bw.write(basicdbObject.toString()+"\n");
					//bw.write("Testing");
					//bw.write(infoList.toString()+"\n");
//					System.out.println("infolist"+ infoList.toString());
//					System.out.println("basicdb:"+basicdbObject);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Thread.sleep(25000);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
