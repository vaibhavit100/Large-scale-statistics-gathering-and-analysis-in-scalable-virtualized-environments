/**
 * 
 */
package com.sjsu.cmpe283.CMPEInfoCollector.vo;

import com.vmware.vim25.mo.VirtualMachine;

/**
 * @author Bhargav
 *
 */
public class VMInfomartion {
	
	private VirtualMachine VMNameOne;
	private VirtualMachine VMCloneOne;
	private VirtualMachine VMNameTwo;
	private VirtualMachine VMCloneTwo;
	private VirtualMachine VHostOne;
	private VirtualMachine VHostTwo;

	/**
	 * @return the vHostOne
	 */
	public VirtualMachine getVHostOne() {
		return VHostOne;
	}
	/**
	 * @param vHostOne the vHostOne to set
	 */
	public void setVHostOne(VirtualMachine vHostOne) {
		VHostOne = vHostOne;
	}
	/**
	 * @return the vHostTwo
	 */
	public VirtualMachine getVHostTwo() {
		return VHostTwo;
	}
	/**
	 * @param vHostTwo the vHostTwo to set
	 */
	public void setVHostTwo(VirtualMachine vHostTwo) {
		VHostTwo = vHostTwo;
	}
	/**
	 * @return the vM_ONE
	 */
	public VirtualMachine getVMNameOne() {
		return VMNameOne;
	}
	/**
	 * @param vM_ONE the vM_ONE to set
	 */
	public void setVMNameOne(VirtualMachine vM_ONE) {
		VMNameOne = vM_ONE;
	}
	/**
	 * @return the vMCloneOne
	 */
	public VirtualMachine getVMCloneOne() {
		return VMCloneOne;
	}
	/**
	 * @param vMCloneOne the vMCloneOne to set
	 */
	public void setVMCloneOne(VirtualMachine vMCloneOne) {
		VMCloneOne = vMCloneOne;
	}
	/**
	 * @return the vMNameTwo
	 */
	public VirtualMachine getVMNameTwo() {
		return VMNameTwo;
	}
	/**
	 * @param vMNameTwo the vMNameTwo to set
	 */
	public void setVMNameTwo(VirtualMachine vMNameTwo) {
		VMNameTwo = vMNameTwo;
	}
	/**
	 * @return the vMCloneTwo
	 */
	public VirtualMachine getVMCloneTwo() {
		return VMCloneTwo;
	}
	/**
	 * @param vMCloneTwo the vMCloneTwo to set
	 */
	public void setVMCloneTwo(VirtualMachine vMCloneTwo) {
		VMCloneTwo = vMCloneTwo;
	}

}
