package org.vtlabs.socialnetworkbyphone.ivr.action;

import javax.persistence.EntityManager;
import javax.servlet.sip.SipSession;

import org.mobicents.servlet.sip.seam.media.framework.IVRHelper;
import org.vtlabs.socialnetworkbyphone.ivr.handler.IVRHandler;

public abstract class IVRAction {
    protected EntityManager entityManager;
    protected SipSession sipSession;

	
	protected IVRHelper ivrHelper;
	protected IVRHandler ivrHandler;

	public IVRAction(IVRHelper ivrHelper, IVRHandler ivrHandler){
		this.ivrHelper = ivrHelper; 
		this.ivrHandler = ivrHandler;
		this.entityManager = ivrHandler.getEntityManager();
		this.sipSession = ivrHandler.getSipSession();
	}
	
	public abstract void start();
	
	public abstract void dtmfEvent(String digits);
	
	public abstract void announcementCompletedEvent();

	public abstract void announcementFailedEvent();
	
}
