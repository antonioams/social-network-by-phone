package org.vtlabs.socialnetworkbyphone.ivr.action;

import java.util.List;

import org.mobicents.servlet.sip.seam.media.framework.IVRHelper;
import org.vtlabs.socialnetworkbyphone.entity.TwitterDirectMessage;
import org.vtlabs.socialnetworkbyphone.entity.User;
import org.vtlabs.socialnetworkbyphone.ivr.action.IVRActionFactory.ACTIONS;
import org.vtlabs.socialnetworkbyphone.ivr.handler.IVRHandler;

public class ListenDMIVRAction extends IVRAction {
	private int numberOfMessages;
	private User user;
	private List<TwitterDirectMessage> directMessages;
	
	public ListenDMIVRAction(IVRHelper ivrHelper, IVRHandler ivrHandler) {
		super(ivrHelper, ivrHandler);
	}

	@Override
	public void announcementCompletedEvent() {
		if(directMessages.size() > numberOfMessages){
			ivrHelper.playAnnouncementWithDtmf("http://localhost/jbossinbossa/user-" + 
							user.getName() + "-dm-" + directMessages.get(numberOfMessages).getTwitterId() + ".wav");
			numberOfMessages++;
		} else
			ivrHandler.nextIVRAction(ACTIONS.MESSAGES_FINISHED_IVR_ACTION);
	}

	@Override
	public void announcementFailedEvent() {
		ivrHandler.nextIVRAction(null);
	}
	@Override
	public void dtmfEvent(String digits) {
		ivrHandler.nextIVRAction(ACTIONS.SEE_YOU_IVR_ACTION);
	}

	@Override
	public void start() {
		numberOfMessages = 0;
		user = (User) sipSession.getAttribute("user");
		directMessages = entityManager.createQuery(
				"select dm from TwitterDirectMessage dm where dm.user = ? order by dm.id desc")
				.setParameter(1, user)
				.getResultList();
		
		ivrHelper.playAnnouncementWithDtmf("http://localhost/jbossinbossa/user-dm-" + user.getName() + ".wav");
	}

}
