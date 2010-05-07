package org.vtlabs.socialnetworkbyphone.ivr.action;

import org.mobicents.servlet.sip.seam.media.framework.IVRHelper;
import org.vtlabs.socialnetworkbyphone.ivr.handler.IVRHandler;

public class SeeYouIVRAction extends IVRAction {

	public SeeYouIVRAction(IVRHelper ivrHelper, IVRHandler ivrHandler) {
		super(ivrHelper, ivrHandler);
	}

	@Override
	public void announcementCompletedEvent() {
		ivrHandler.nextIVRAction(null);
	}

	@Override
	public void announcementFailedEvent() {
		ivrHandler.nextIVRAction(null);
	}

	@Override
	public void dtmfEvent(String digits) {

	}

	@Override
	public void start() {
        ivrHelper.playAnnouncement("http://localhost/jbossinbossa/seeyou.wav");

	}

}
