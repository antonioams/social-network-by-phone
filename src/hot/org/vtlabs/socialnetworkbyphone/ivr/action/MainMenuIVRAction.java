package org.vtlabs.socialnetworkbyphone.ivr.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.mobicents.servlet.sip.seam.media.framework.IVRHelper;
import org.vtlabs.socialnetworkbyphone.ivr.action.IVRActionFactory.ACTIONS;
import org.vtlabs.socialnetworkbyphone.ivr.handler.IVRHandler;

public class MainMenuIVRAction extends IVRAction {

	public MainMenuIVRAction(IVRHelper ivrHelper, IVRHandler ivrHandler) {
		super(ivrHelper, ivrHandler);
	}

	@Override
	public void announcementCompletedEvent() {
	}

	public void announcementFailedEvent() {
		ivrHandler.nextIVRAction(null);
	}
	
	@Override
	public void dtmfEvent(String digits) {
		switch(digits.charAt(0))
		{
		case '2':
			ivrHandler.nextIVRAction(IVRActionFactory.ACTIONS.LISTEN_TWEETS_IVR_ACTION);
			break;
		case '6':
			ivrHandler.nextIVRAction(IVRActionFactory.ACTIONS.LISTEN_DM_IVR_ACTION);
			break;
		case '4':
			ivrHandler.nextIVRAction(ACTIONS.LISTEN_RT_IVR_ACTION);
			break;
		default :
			ivrHandler.nextIVRAction(ACTIONS.SEE_YOU_IVR_ACTION);
			break;
		}
	}

	@Override
	public void start() {
        ivrHelper.playAnnouncementWithDtmf("http://localhost/jbossinbossa/mainmenu.wav");
	}

}
