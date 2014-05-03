package com.sjsu.cmpe283.CMPEInfoCollector.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.sjsu.cmpe283.CMPEInfoCollector.common.*;
import com.vmware.vim25.Action;
import com.vmware.vim25.AlarmAction;
import com.vmware.vim25.AlarmExpression;
import com.vmware.vim25.AlarmSetting;
import com.vmware.vim25.AlarmSpec;
import com.vmware.vim25.AlarmTriggeringAction;
import com.vmware.vim25.DuplicateName;
import com.vmware.vim25.InvalidName;
import com.vmware.vim25.MethodAction;
import com.vmware.vim25.MethodActionArgument;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.StateAlarmExpression;
import com.vmware.vim25.StateAlarmOperator;
import com.vmware.vim25.mo.AlarmManager;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class AlarmOperations {
	private static String alarmName = null;
	static int i = 1;

	public static void createAlarm(VirtualMachine vm) throws RemoteException, MalformedURLException {
		System.out.println("Creating alarm for the Virtual Machine " + vm.getName());
		
		ServiceInstance vCenterSI = new ServiceInstance(new URL(CommonConstants.MY_VCENTER_URL), CommonConstants.VCENTER_USER_NAME, CommonConstants.VCENTER_PASSWORD, true);
		InventoryNavigator inv = new InventoryNavigator(vCenterSI.getRootFolder());
		VirtualMachine myVm = (VirtualMachine)inv.searchManagedEntity("VirtualMachine", vm.getName());
				
		if(myVm == null)
		{
			System.out.println("Cannot find the VM " + vm.getName() + "\nExisting...");
			vCenterSI.getServerConnection().logout();
			return;
		}

		AlarmManager alarmMgr = vCenterSI.getAlarmManager();

		AlarmSpec spec = new AlarmSpec();

		StateAlarmExpression expression = createStateAlarmExpression();
		AlarmAction methodAction = null;
		try {
			methodAction = createAlarmTriggerAction(createPowerOnAction());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		alarmName = "Alarm!! Power off Alarm for Vm " + myVm.getName();		

		//spec.setAction(methodAction);
		spec.setExpression(expression);
		spec.setName(alarmName);
		spec.setDescription("Monitor VM state...when Vm is powered off");
		spec.setEnabled(true);

		AlarmSetting as = new AlarmSetting();
		as.setReportingFrequency(0); //as often as possible
		as.setToleranceRange(0);

		spec.setSetting(as);

		try {				
			alarmMgr.createAlarm(vm, spec);
		} catch (InvalidName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DuplicateName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Alarm is created for Vm " + vm.getName());

		vCenterSI.getServerConnection().logout();

	}


	public static StateAlarmExpression createStateAlarmExpression()
	{
		StateAlarmExpression expression = new StateAlarmExpression();
		expression.setType("VirtualMachine");
		expression.setStatePath("runtime.powerState");
		expression.setOperator(StateAlarmOperator.isEqual);
		expression.setRed("poweredOff");
		return expression;
	}	

	public static MethodAction createPowerOnAction() 
	{
		MethodAction action = new MethodAction();
		action.setName("Nothing to be done!!!");
		MethodActionArgument argument = new MethodActionArgument();
		argument.setValue(null);
		action.setArgument(new MethodActionArgument[] { argument });
		return action;
	}	

	public static AlarmTriggeringAction createAlarmTriggerAction(MethodAction methodAction) throws Exception 
	{
		AlarmTriggeringAction alarmAction = new AlarmTriggeringAction();
		alarmAction.setYellow2red(true);
		alarmAction.setAction(methodAction);
		return alarmAction;
	}

	/*public static VirtualMachine Alarm(VirtualMachine vm, AlarmManager am) throws Exception {
		alarmName = vm.getName() + " is powered off by the user!!";
		StateAlarmExpression expression = createStateAlarmExpression();
		MethodAction methodAction = createPowerOnAction();
		AlarmAction alarmAction = (AlarmAction) createAlarmTriggerAction(methodAction);
		AlarmSpec alarmSpec = createAlarmSpec(alarmName, expression);

		try
		{			
			am.createAlarm(vm, alarmSpec);
			System.out.println("Alert: " + alarmName);
		}
		catch(InvalidName in) 
		{
			System.out.println("Alarm name is empty or too long");
		}
		catch(DuplicateName dn)
		{
			System.out.println("Alarm with the name already exists");
		}
		catch(RemoteException re)
		{
			re.printStackTrace();
		}
		return vm;
	}*/


}
