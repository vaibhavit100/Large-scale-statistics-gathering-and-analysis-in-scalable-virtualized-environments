/**
 * 
 */
package com.sjsu.cmpe283.CMPEInfoCollector.threads;

import java.io.IOException;

import com.sjsu.cmpe283.CMPEInfoCollector.common.CommonConstants;
import com.sjsu.cmpe283.CMPEInfoCollector.util.OperationsUtilVM;
import com.sjsu.cmpe283.CMPEInfoCollector.util.SystemCache;
import com.sjsu.cmpe283.CMPEInfoCollector.vo.VMInfomartion;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * @author Bhargav
 *
 */
public class PingMonitor implements Runnable {

	private String ipAddress = "127.0.0.0";
	
	private SystemCache cache = SystemCache.getInstance();
	
	/**
	 * @return the vMNameOne
	 */
	public VirtualMachine getVMNameOne() {
		return VMNameOne;
	}



	/**
	 * @param vMNameOne the vMNameOne to set
	 */
	public void setVMNameOne(VirtualMachine vMNameOne) {
		VMNameOne = vMNameOne;
	}


	private VirtualMachine VMNameOne; 
	
	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}



	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

//	public PingMonitor(String ipAddress){
//		setIpAddress(ipAddress);
//	}

	public PingMonitor(VirtualMachine VMNameOne){
		setVMNameOne(VMNameOne);
	}
	

	@Override
	public void run() {
		int counter = 0;
		String cloneNameOne = "VM1_Bhargav_CMPE283_Linux2_Lab2_Clone1";
		String vHostName = "130.65.157.247";
		
		try {
			
			while(true){
				
				if(!OperationsUtilVM.pingVM(VMNameOne.getGuest().ipAddress)){
					counter++;
					VMInfomartion myVMInfo = (VMInfomartion)cache.getCache().get("vcenter");
					//Check if the VM is powered Off
					//OperationsUtilVM.vmPowerOn(VMNameOne);
				}
				if(counter >= 1){
					VMInfomartion myVMInfo = (VMInfomartion)cache.getCache().get("vcenter");
					//clone and migrate
					//OperationsUtilVM.cloneVM(VMNameOne, cloneNameOne);
					
					//OperationsUtilVM.destory(VMNameOne);
					
					OperationsUtilVM.migrateVM(cloneNameOne, vHostName, CommonConstants.MY_VCENTER_URL, CommonConstants.VCENTER_USER_NAME, CommonConstants.VCENTER_PASSWORD);
					
					
					continue;
				}
				System.out.println(counter);
				Thread.sleep(3000);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
