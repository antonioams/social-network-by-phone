package org.vtlabs.socialnetworkbyphone.ivr.action;

import java.util.List;

import org.jfree.util.Log;
import org.mobicents.servlet.sip.seam.media.framework.IVRHelper;
import org.vtlabs.socialnetworkbyphone.entity.TwitterStatus;
import org.vtlabs.socialnetworkbyphone.entity.User;
import org.vtlabs.socialnetworkbyphone.ivr.action.IVRActionFactory.ACTIONS;
import org.vtlabs.socialnetworkbyphone.ivr.handler.IVRHandler;

public class ListenTweetsIVRAction extends IVRAction {
	private int numberOfMessages;
	private User user;
	private List<TwitterStatus> tweets;
	
	public ListenTweetsIVRAction(IVRHelper ivrHelper, IVRHandler ivrHandler) {
		super(ivrHelper, ivrHandler);
	}

	@Override
	public void announcementCompletedEvent() {
		if(tweets.size() > numberOfMessages){
			ivrHelper.playAnnouncementWithDtmf("http://localhost/jbossinbossa/user-" + 
							user.getName() + "-tw-" + tweets.get(numberOfMessages).getTwitterId() + ".wav");
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
		tweets = entityManager.createQuery(
				"select ts from TwitterStatus ts where ts.user = ? order by ts.id desc")
				.setParameter(1, user)
				.getResultList();
		Log.info("Executando play do arquivo: http://localhost/jbossinbossa/user-tw-" + user.getName() + ".wav" );
		ivrHelper.playAnnouncementWithDtmf("http://localhost/jbossinbossa/user-tw-" + user.getName() + ".wav");
	}
}
