package com.sjsu.cmpe283.CMPEInfoCollector.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Date;

import com.vmware.vim25.HostVMotionCompatibility;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineMovePriority;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.VirtualMachineSnapshotTree;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

import com.sjsu.cmpe283.CMPEInfoCollector.common.CommonConstants;

public class OperationsUtilVM {
	private String vmname ;
	private static ServiceInstance si ;
	private static VirtualMachine vm ;
	private static VirtualMachineSnapshotTree[] snapTree;
	private static SystemCache cache = SystemCache.getInstance();

	/**
	 * Constructor for objects of class MyVM
	 * @return 
	 */
	public static VirtualMachine createVm( String vmname ) 
	{

		try {
			vmname = vmname ;
			si = new ServiceInstance(new URL(CommonConstants.CUMULUS_TWO_URL), CommonConstants.VHOST_USER_NAME, CommonConstants.VHOST_PASSWORD, true);
			Folder rootFolder = si.getRootFolder();
			vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", vmname);
		} catch ( Exception e ) 
		{ System.out.println( e.toString() ) ; }

		if( vm==null)
		{
			System.out.println("No VM " + vmname + " found");
			if ( si != null)
				si.getServerConnection().logout();
		}
		return vm;
	}

	/**
	 * Destructor for objects of class MyVM
	 */
	protected void finalize() throws Throwable
	{
		this.si.getServerConnection().logout(); //do finalization here
		super.finalize(); //not necessary if extending Object.
	} 

	/**
	 * Power On the Virtual Machine
	 */
	public static boolean vmPowerOn(VirtualMachine myVm) {
		String powerState = "Off";
		try {
			System.out.println("command: powered on");
			Task task = myVm.powerOnVM_Task(null);
			if(task.waitForTask()== Task.SUCCESS)
			{
				System.out.println(myVm.getName() + " powered on");
				powerState = "On";
			} 
		} catch ( Exception e ) 
		{ System.out.println( e.toString() ) ; }

		if(powerState.equals("On")) {
			return true;
		}
		else {
			return false;
		}		
	}

	/**
	 * Power Off the Virtual Machine
	 */
	public static void powerOff(VirtualMachine myVm) 
	{
		try {
			System.out.println("command: powered off");
			Task task = myVm.powerOffVM_Task();
			if(task.waitForMe()==Task.SUCCESS)
			{
				System.out.println(myVm.getName() + " powered off");
			}
		} catch ( Exception e ) 
		{ System.out.println( e.toString() ) ; }
	}

	/**
	 * Reset the Virtual Machine
	 */

	public static void reset(VirtualMachine myVm) 
	{
		try {
			System.out.println("Reset task");
			Task task = vm.resetVM_Task();
			
			if(task.waitForMe()==Task.SUCCESS)
			{
				System.out.println(myVm.getName() + " reset");
			}
		} catch ( Exception e ) 
		{ System.out.println( e.toString() ) ; }
	}

	/**
	 * Reset the Virtual Machine
	 */

	public static void destory(VirtualMachine myVm) 
	{
		try {
			System.out.println("VM Destroy Task");
			Task task = vm.destroy_Task();
			
			if(task.waitForMe()==Task.SUCCESS)
			{
				System.out.println(myVm.getName() + " destoryed");
			}
		} catch ( Exception e ) 
		{ System.out.println( e.toString() ) ; }
	}

	/**
	 * Suspend the Virtual Machine
	 */

	public void suspend(VirtualMachine myVm) 
	{
		try {
			System.out.println("command: suspend");
			Task task = vm.suspendVM_Task();
			if(task.waitForMe()==Task.SUCCESS)
			{
				System.out.println(myVm.getName() + " suspended");
			}
		} catch ( Exception e ) 
		{ System.out.println( e.toString() ) ; }
	}



	/**
	 *  Put VM & Guest OS on Standby
	 */
	public void standBy(VirtualMachine myVm) 
	{
		try {
			System.out.println("command: stand by");
			vm.standbyGuest();
			System.out.println(myVm.getName() + " guest OS standby");
		} catch ( Exception e ) 
		{ System.out.println( e.toString() ) ; }
	}

	/**
	 *  Get Vm Ip Address
	 */
	public String getVmIpAddress(VirtualMachine myVm) {
		return myVm.getGuest().getIpAddress();
	}

	/**
	 *  To get the state of the Vm
	 */
	public static boolean getVmState(VirtualMachine myVm) {
		String vmState = myVm.getGuest().getGuestState();
		System.out.println(myVm.getName() + " is " + vmState);
		if (vmState.equalsIgnoreCase("running")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *  Get the power status of the VM
	 */
	public static VirtualMachinePowerState getVmPowerState(VirtualMachine myVm) {
		return myVm.getRuntime().getPowerState();
	}

	/**
	 * Ping Vm
	 */
	public static Boolean pingVM(String ipAddress) throws IOException {
		final InetAddress inet = InetAddress.getByName(ipAddress);
		
		try {
			WriteFromFile.writeToFileMethod((new Date() + " -- VM is reachable "+ipAddress+" : " + inet.isReachable(5000) +" \n"),CommonConstants.PING_FILE_NAME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inet.isReachable(5000);
	}

	public static boolean monitorVmByPing(String ipAddress) throws IOException {
		if(pingVM(ipAddress)) {
			return true;
		} else {
			return false;
		}
	}

	public static void cloneVM(VirtualMachine myVm, String cloneName)
	{
		System.out.println("........................................................");
		snapTree = myVm.getSnapshot().getRootSnapshotList();

		try {
			VirtualMachineCloneSpec cloneSpec = 
					new VirtualMachineCloneSpec();
			cloneSpec.setLocation(new VirtualMachineRelocateSpec());
			cloneSpec.setPowerOn(false);
			cloneSpec.setTemplate(false);
			//Cloning Vm from the Snapshot
			System.out.println("Cloning from snapshot " );
			SnapshotUtil.printSnapshots(snapTree);
			cloneSpec.snapshot = myVm.getCurrentSnapShot().getMOR();


			Task task = myVm.cloneVM_Task((Folder) myVm.getParent(), cloneName, cloneSpec);
			System.out.println("Launching the VM clone task for " + myVm.getName());
			System.out.println(	"Please wait ..........................................");

			String status = task.waitForTask();
			if(status==Task.SUCCESS)
			{
				System.out.println("VM got cloned successfully.");
			}
			else
			{
				System.out.println("Failure -: VM cannot be cloned");
			}
			System.out.println("........................................................");

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e){
			System.out.println("Expception");
			e.printStackTrace();
		}
	}

	public static void migrateVM(String vmname, String newHostName, String url, String username, String password) throws InterruptedException, IOException
	{		    
		System.out.println("........................................................");		

		System.out.println("Host2 is alive.......");
		ServiceInstance si = null;
		try {
			si = new ServiceInstance(
					new URL(url), username, password, true);

			Folder rootFolder = si.getRootFolder();
			VirtualMachine vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", vmname);
			HostSystem newHost = (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity("HostSystem", newHostName);
			ComputeResource cr = (ComputeResource) newHost.getParent();

			String[] checks = new String[] {"cpu", "software"};
			HostVMotionCompatibility[] vmcs =
					si.queryVMotionCompatibility(vm, new HostSystem[] 
							{newHost},checks );

			String[] comps = vmcs[0].getCompatibility();
			if(checks.length != comps.length)
			{
				System.out.println("CPU/software NOT compatible. Exit.");
				si.getServerConnection().logout();
				return;
			}

			Task task = vm.migrateVM_Task(cr.getResourcePool(), newHost,
					VirtualMachineMovePriority.highPriority, 
					VirtualMachinePowerState.poweredOff);
			System.out.println("Launching the VM migrate task for " + vmname);
			System.out.println(	"Please wait ..........................................");

			if(task.waitForTask()==Task.SUCCESS)
			{
				System.out.println("VM is migrated successfully!");
			}
			else
			{
				System.out.println("VMotion failed!");
				TaskInfo info = task.getTaskInfo();
				System.out.println(info.getError().getFault());
			}
			si.getServerConnection().logout();

			OperationsUtilVM.vmPowerOn(vm);
			AlarmOperations.createAlarm(vm);
			cache.getCache().put("clonedVM", vm);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
