package com.sjsu.cmpe283.CMPEInfoCollector.util;

import java.rmi.RemoteException;

import com.vmware.vim25.FileFault;
import com.vmware.vim25.InvalidName;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.SnapshotFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.VirtualMachineSnapshotTree;
import com.vmware.vim25.VmConfigFault;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.VirtualMachineSnapshot;

public class SnapshotUtil {

	private static String snapshotname = "myVm_Snapshot";
	private static String desc = "A description for sample snapshot";
	private static VirtualMachineSnapshotTree[] snapTree;


	public static void createSnapshot(VirtualMachine vm) throws InvalidName, VmConfigFault, SnapshotFault, TaskInProgress, FileFault, InvalidState, RuntimeFault, RemoteException {
		Task task = vm.createSnapshot_Task(
				snapshotname, desc, false, false);
		try {
			if(task.waitForTask()== Task.SUCCESS)
			{
				System.out.println("Snapshot was created.");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static VirtualMachineSnapshot getSnapshotInTree(
			VirtualMachine vm, String snapName)
	{
		if (vm == null || snapName == null) 
		{
			return null;
		}

		snapTree = vm.getSnapshot().getRootSnapshotList();
		if(snapTree!=null)
		{
			ManagedObjectReference mor = findSnapshotInTree(
					snapTree, snapName);
			if(mor!=null)
			{
				return new VirtualMachineSnapshot(
						vm.getServerConnection(), mor);
			}
		}
		return null;
	}

	public static ManagedObjectReference findSnapshotInTree(VirtualMachineSnapshotTree[] snapTree, String snapName)
	{
		for(int i=0; i <snapTree.length; i++) 
		{
			VirtualMachineSnapshotTree node = snapTree[i];
			if(snapName.equals(node.getName()))
			{
				return node.getSnapshot();
			} 
			else 
			{
				VirtualMachineSnapshotTree[] childTree = 
						node.getChildSnapshotList();
				if(childTree!=null)
				{
					ManagedObjectReference mor = findSnapshotInTree(
							childTree, snapName);
					if(mor!=null)
					{
						return mor;
					}
				}
			}
		}
		return null;
	}

	public static void printSnapshots(VirtualMachineSnapshotTree[] snapTree)
	{
		for (int i = 0; snapTree!=null && i < snapTree.length; i++) 
		{
			VirtualMachineSnapshotTree node = snapTree[i];
			System.out.println("Snapshot Name : " + node.getName());           
			VirtualMachineSnapshotTree[] childTree = 
					node.getChildSnapshotList();
			if(childTree!=null)
			{
				printSnapshots(childTree);
			}
		}
	}
}
