/**
 * 
 */
package com.sjsu.cmpe283.CMPEInfoCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sjsu.cmpe283.CMPEInfoCollector.VMInitalizaler;
import com.sjsu.cmpe283.CMPEInfoCollector.common.CommonConstants;
import com.sjsu.cmpe283.CMPEInfoCollector.threads.PingMonitor;
import com.sjsu.cmpe283.CMPEInfoCollector.threads.VHostMHealthUpdateThread;
import com.sjsu.cmpe283.CMPEInfoCollector.threads.VMHealthUpdateThread;
import com.sjsu.cmpe283.CMPEInfoCollector.vo.VMDetails;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * @author Bhargav
 *
 */
public class InfoCollectorMain {

	private static ArrayList<VMDetails> vmlist = new ArrayList<VMDetails>();
	private static ArrayList<String> vmIPlist = new ArrayList<String>();
	private static HashMap<String,ServiceInstance> mapOfVHostServiceInst = new HashMap<String,ServiceInstance>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Check if the VHosts are powered On and get the list of VMs
		vmIPlist = VMInitalizaler.checkVHost();
		
//		mapOfVHostServiceInst = VMInitalizaler.getServiceInstances();

		// Ping VMs and Collect the Health InfoMation
		System.out.println("helloooo in main");
		Thread lp = new Thread(new LogProcess());
		lp.start();

		ExecutorService es = Executors.newFixedThreadPool(100);
		List<Callable<Object>> mointorJobList = new ArrayList<Callable<Object>>(100);
		try {
			
			for(String vmIps: vmIPlist){
				mointorJobList.add(Executors.callable(new VMHealthUpdateThread(vmIps)));
			}
			
//			for (String vHostLink : CommonConstants.VHostList) {
//				mointorJobList.add(Executors.callable(new VHostMHealthUpdateThread(vHostLink)));
//			}
			
			es.invokeAll(mointorJobList);
			List<Future<Object>> futureObjList = es.invokeAll(mointorJobList);


			es.shutdown();

//			Iterator futureItr = futureObjList.iterator();
//
//			while (futureItr.hasNext()) {
//				Future<Object> fu = (Future<Object>) futureItr.next();
//				fu.get();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
