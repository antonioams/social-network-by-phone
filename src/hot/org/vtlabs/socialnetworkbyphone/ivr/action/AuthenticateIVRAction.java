package org.vtlabs.socialnetworkbyphone.ivr.action;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.sip.SipServletRequest;

import org.mobicents.servlet.sip.seam.media.framework.IVRHelper;
import org.vtlabs.socialnetworkbyphone.ivr.action.IVRActionFactory.ACTIONS;
import org.vtlabs.socialnetworkbyphone.ivr.handler.IVRHandler;

public class AuthenticateIVRAction extends IVRAction {

	public AuthenticateIVRAction(IVRHelper ivrHelper, IVRHandler ivrHandler) {
		super(ivrHelper, ivrHandler);
	}

	@Override
	public void announcementCompletedEvent() {
        ivrHandler.nextIVRAction(ACTIONS.SEE_YOU_IVR_ACTION);
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
        SipServletRequest inviteRequest = (SipServletRequest) 
        sipSession.getAttribute("inviteRequest");
        String fromHeader = inviteRequest.getHeader("From");
        if(!authenticateUser(fromHeader)){
            ivrHelper.playAnnouncement("http://localhost/jbossinbossa/telefone-nao-cadastrado.wav");
        }
        else
        	ivrHandler.nextIVRAction(ACTIONS.MAIN_MENU_IVR_ACTION);
	}

    private boolean authenticateUser(String from) {
		String username;
    	Pattern p = Pattern.compile(".*sip:(.*)@.*");
    	Matcher m = p.matcher(from);
    	if(m.matches()){
    		username = m.group(1);
			List userList = entityManager.createQuery("select u from User u where u.phoneNumber=:phone")
				.setParameter("phone", username)
				.getResultList();
			if(userList.size() > 0){
				sipSession.setAttribute("user", userList.get(0));
				return true;
			}
    	}
    	return false;
	}
}
