/**
 * 
 */
package com.sjsu.cmpe283.CMPEInfoCollector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import org.tempuri.Service;
import org.tempuri.ServiceSoap;

import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineSnapshotInfo;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

import com.sjsu.cmpe283.CMPEInfoCollector.common.*;
import com.sjsu.cmpe283.CMPEInfoCollector.util.SystemCache;
import com.sjsu.cmpe283.CMPEInfoCollector.vo.VMDetails;
import com.sjsu.cmpe283.CMPEInfoCollector.vo.VMInfomartion;

/**
 * @author Bhargav
 * 
 */
public class VMInitalizaler {

	// instance variables - replace the example below with your own
	private static String vmname;
	private static ServiceInstance VHostMain;
	private static VirtualMachine vm;
	private static SystemCache cache = SystemCache.getInstance();
	private static ArrayList<VMDetails> listOfVMDetails = new ArrayList<VMDetails>();
	private static ArrayList<String> listOfVMIps = new ArrayList<String>();
	private static ArrayList<VirtualMachine> listOfVMs = new ArrayList<VirtualMachine>();
	private static ArrayList<ServiceInstance> listOfVHostServiceInst = new ArrayList<ServiceInstance>();
	private static HashMap<String,ServiceInstance> mapOfVHostServiceInst = new HashMap<String,ServiceInstance>();

	/**
	 * Constructor for objects of class MyVM
	 * 
	 * @return
	 */
	public static ArrayList<String> checkVHost() {

		try {
			for (String vHostLink : CommonConstants.VHostList) {

				VHostMain = new ServiceInstance(new URL(vHostLink),
						CommonConstants.VHOST_USER_NAME,
						CommonConstants.VHOST_PASSWORD, true);

				Folder rootFolder = VHostMain.getRootFolder();
				String name = rootFolder.getName();
				System.out.println("root:" + name);
				ManagedEntity[] mes = new InventoryNavigator(rootFolder)
						.searchManagedEntities("VirtualMachine");


				if (mes == null || mes.length == 0) {
					System.out.println("Not Found!!");
					return null;
				}

				for (ManagedEntity vme : mes) {
					VirtualMachine vHost = (VirtualMachine) vme;
					VMDetails vmNew = new VMDetails();
					VirtualMachineConfigInfo vmHostInfo = vHost.getConfig();
					if (vmHostInfo != null) {
						System.out.println("Found : " + vHost.getName());
						vmNew.setVmName(vHost.getName());
						vmNew.setVmIP(vHost.getGuest().ipAddress);
						vmNew.setVmURL("https://" + vHost.getGuest().ipAddress
								+ "/sdk");

						if (!pingVM(vHost.getGuest().ipAddress)) {
							Task poweronTask = vHost.powerOnVM_Task(null);
							if (poweronTask.waitForTask() == Task.SUCCESS)
								System.out.println("powered On!!");
						}
						listOfVMDetails.add(vmNew);
					}
					listOfVMIps.add(vHost.getName());
				}

			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return listOfVMIps;
	}

	public static boolean pingVM(String ipAddress) throws UnknownHostException {
		final InetAddress inet = InetAddress.getByName(ipAddress);
		try {
			if (inet.isReachable(5000))
				System.out
						.println("VM is reachable: " + inet.isReachable(5000));
			else
				System.out.println("VM is not reachable: "
						+ inet.isReachable(5000));

			return inet.isReachable(5000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String args[]) {
		checkVHost();
	}
	
	public static HashMap<String,ServiceInstance> getServiceInstances() {
		for (String vHostLink : CommonConstants.VHostList) {

			try {
				ServiceInstance VHostNew = new ServiceInstance(new URL(vHostLink),
						CommonConstants.VHOST_USER_NAME,
						CommonConstants.VHOST_PASSWORD, true);
				mapOfVHostServiceInst.put(vHostLink.substring(8, 21), VHostNew);		
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return mapOfVHostServiceInst;
	}
	

}
