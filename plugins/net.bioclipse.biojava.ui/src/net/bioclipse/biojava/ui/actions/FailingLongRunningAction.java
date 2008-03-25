/*******************************************************************************
 * Copyright (c) 2008 The Bioclipse Project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org/legal/epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 * 
 * Contributors:
 *     Ola Spjuth
 *     
 ******************************************************************************/
package net.bioclipse.biojava.ui.actions;

import java.lang.reflect.InvocationTargetException;

import net.bioclipse.core.jobs.AbstractJob;
import net.bioclipse.core.jobs.ActionJobRunner;
import net.bioclipse.core.util.LogUtils;

import org.apache.log4j.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;


public class FailingLongRunningAction extends ActionDelegate{

    private static final Logger logger = 
        Logger.getLogger(FailingLongRunningAction.class);
    
    
	/**
	 * This action is to demonstrate long running operations in Bioclipse.
	 */
	@Override
	public void run(IAction action) {

		LongJob job = new LongJob();
		job.setDelay(0);
		ImageDescriptor icon = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT);
		ActionJobRunner.getInstance().runSingleAction(job, true, false, icon);

	}

	class LongJob extends AbstractJob{


		public String getJobDescription() {
			return getJobName();
		}

		public String getJobName() {
			return "Long-running job that fails";
		}


		public int getTotalTime() {
			return 5;
		}

		public void run(IProgressMonitor monitor) throws InvocationTargetException,
		InterruptedException {
			monitor.subTask("Init");
			sleep(500);
			monitor.subTask("Processing");
			if (monitor.isCanceled()) {
				return;
			}
			monitor.worked(1);
			sleep(500);
			monitor.worked(1);
			if (monitor.isCanceled()) {
				return;
			}
			sleep(300);
			monitor.subTask("Validating");
			if (monitor.isCanceled()) {
				return;
			}
			sleep(300);
			monitor.worked(1);
			monitor.subTask("Validating");
			if (monitor.isCanceled()) {
				return;
			}
			sleep(300);
			monitor.worked(1);
			monitor.subTask("Failing");
			if (monitor.isCanceled()) {
				return;
			}
			
			//FAIL!!
			setFail(true);
			String failMessage="The protein could not be translated into anything at all.";

			sleep(500);
			monitor.worked(1);

			if (this.fail) {
				throw new InvocationTargetException(new IllegalArgumentException(failMessage),"Error executing job");
			}

		}

		public void sleep(int ms) {
			try {
				Thread.sleep(ms * 1);
			} catch (InterruptedException e) {
				LogUtils.debugTrace(logger, e);
			}
		}
	}

}
