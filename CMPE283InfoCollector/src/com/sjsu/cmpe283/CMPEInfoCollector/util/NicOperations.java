package com.sjsu.cmpe283.CMPEInfoCollector.util;

import com.vmware.vim25.ConfigTarget;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NetworkSummary;
import com.vmware.vim25.VirtualDevice;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualEthernetCard;
import com.vmware.vim25.VirtualEthernetCardNetworkBackingInfo;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineNetworkInfo;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualPCNet32;
import com.vmware.vim25.mo.EnvironmentBrowser;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class NicOperations {
	private static VirtualDeviceConfigSpec nicSpec = new VirtualDeviceConfigSpec();
	private static VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();


	static VirtualDeviceConfigSpec addNIC(VirtualMachine vm, String nicName)throws Exception {
		VirtualMachineConfigInfo vmConfigInfo = vm.getConfig();
		if(doesNetworkNameExist(vm, nicName)) 
		{
			nicSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
			VirtualEthernetCard nic =  new VirtualPCNet32();
			VirtualEthernetCardNetworkBackingInfo nicBacking = 
					new VirtualEthernetCardNetworkBackingInfo();
			nicBacking.setDeviceName(nicName);
			nic.setAddressType("generated");
			nic.setBacking(nicBacking);
			nic.setKey(4);
			nicSpec.setDevice(nic);
			return nicSpec;
		}
		return null;

	}

	public static VirtualDeviceConfigSpec removeNIC(VirtualMachine vm, String nicName)throws Exception {
		VirtualMachineConfigInfo vmConfigInfo = vm.getConfig();
		VirtualDevice [] vds = 
				vmConfigInfo.getHardware().getDevice();
		nicSpec.setOperation(
				VirtualDeviceConfigSpecOperation.remove);
		for(int i=0;i<vds.length;i++)
		{
			if((vds[i] instanceof VirtualEthernetCard) &&
					(vds[i].getDeviceInfo().getLabel().equalsIgnoreCase(
							nicName)))
			{                             
				nicSpec.setDevice(vds[i]);
				return nicSpec;
			}
		}

		return null;

	}

	static boolean doesNetworkNameExist(VirtualMachine vm,String netName) throws Exception {
		VirtualMachineRuntimeInfo vmRuntimeInfo = vm.getRuntime();
		EnvironmentBrowser envBrowser = vm.getEnvironmentBrowser();
		ManagedObjectReference hmor = vmRuntimeInfo.getHost();

		HostSystem host = new HostSystem(
				vm.getServerConnection(), hmor);
		ConfigTarget cfg = envBrowser.queryConfigTarget(host);
		VirtualMachineNetworkInfo[] nets = cfg.getNetwork();
		for (int i = 0; nets!=null && i < nets.length; i++) 
		{
			NetworkSummary netSummary = nets[i].getNetwork();
			if (netSummary.isAccessible() && 
					netSummary.getName().equalsIgnoreCase(netName)) 
			{
				return true;
			}
		}
		return false;
	}
}
