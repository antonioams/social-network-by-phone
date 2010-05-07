package org.vtlabs.socialnetworkbyphone.ivr.handler;

import javax.persistence.EntityManager;
import javax.servlet.sip.SipSession;

import org.vtlabs.socialnetworkbyphone.ivr.action.IVRActionFactory.ACTIONS;

public interface IVRHandler {
	public void nextIVRAction(ACTIONS actionType);
	
	public SipSession getSipSession();
	
	public EntityManager getEntityManager();
}
