package org.vtlabs.socialnetworkbyphone.ivr.action;

import java.util.List;

import org.mobicents.servlet.sip.seam.media.framework.IVRHelper;
import org.vtlabs.socialnetworkbyphone.entity.TwitterRetweet;
import org.vtlabs.socialnetworkbyphone.entity.TwitterStatus;
import org.vtlabs.socialnetworkbyphone.entity.User;
import org.vtlabs.socialnetworkbyphone.ivr.action.IVRActionFactory.ACTIONS;
import org.vtlabs.socialnetworkbyphone.ivr.handler.IVRHandler;

public class ListenRTIVRAction extends IVRAction {
	private int numberOfMessages;
	private User user;
	private List<TwitterRetweet> rts;
	
	public ListenRTIVRAction(IVRHelper ivrHelper, IVRHandler ivrHandler) {
		super(ivrHelper, ivrHandler);
	}

	@Override
	public void announcementCompletedEvent() {
		if(rts.size() > numberOfMessages){
			ivrHelper.playAnnouncementWithDtmf("http://localhost/jbossinbossa/user-" + 
							user.getName() + "-rt-" + rts.get(numberOfMessages).getTwitterId() + ".wav");
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
		rts = entityManager.createQuery(
				"select rt from TwitterRetweet rt where rt.user = ? order by rt.id desc")
				.setParameter(1, user)
				.getResultList();
		
		ivrHelper.playAnnouncementWithDtmf("http://localhost/jbossinbossa/user-rt-" + user.getName() + ".wav");
	}

}
