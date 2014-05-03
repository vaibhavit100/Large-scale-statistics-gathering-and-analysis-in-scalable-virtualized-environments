/**
 * 
 */
package com.sjsu.cmpe283.CMPEInfoCollector.threads;

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

/**
 * @author Bhargav
 * 
 */
public class VHostMHealthUpdateThread implements Runnable {

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

	public VHostMHealthUpdateThread(String hostIP) {
		this.ip = hostIP.substring(8, 21);
	}

	@Override
	public void run() {

		try {

			while (true) {

				String url = "https://" + SERVER_NAME + "/sdk/vimService";
				try {
					ServiceInstance si = new ServiceInstance(new URL(url),
							USER_NAME, PASSWORD, true);
					HostSystem host = (HostSystem) new InventoryNavigator(
							si.getRootFolder()).searchManagedEntity(
							"HostSystem", ip);
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
//				System.out.println(gson.toJson(infoList));
				counter = 0;
				try {
					Mongo mongo = new Mongo("130.65.133.178", 27017);
					DB db = mongo.getDB("project2");
					DBCollection collection1 = db.getCollection("vmtest");
					DBObject basicdbObject = (DBObject) JSON.parse(gson.toJson(infoList));
					collection1.insert(basicdbObject);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Thread.sleep(120000);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
