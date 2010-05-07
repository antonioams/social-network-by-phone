package org.vtlabs.socialnetworkbyphone.ivr.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.mobicents.servlet.sip.seam.media.framework.IVRHelper;
import org.vtlabs.socialnetworkbyphone.ivr.handler.IVRHandler;

public class StartIVRAction extends IVRAction {

	public StartIVRAction(IVRHelper ivrHelper, IVRHandler ivrHandler) {
		super(ivrHelper, ivrHandler);
	}

	@Override
	public void announcementCompletedEvent() {
		ivrHandler.nextIVRAction(IVRActionFactory.ACTIONS.AUTHENTICATE_IVR_ACTION);
	}

	public void announcementFailedEvent() {
		ivrHandler.nextIVRAction(null);
	}
	
	@Override
	public void dtmfEvent(String digits) {
	}

	@Override
	public void start() {
        ivrHelper.playAnnouncement("http://localhost/jbossinbossa/saudacao.wav");
	}

}
