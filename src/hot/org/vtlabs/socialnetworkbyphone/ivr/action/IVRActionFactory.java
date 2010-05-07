package org.vtlabs.socialnetworkbyphone.ivr.action;

import org.mobicents.servlet.sip.seam.media.framework.IVRHelper;
import org.vtlabs.socialnetworkbyphone.ivr.handler.IVRHandler;



public class IVRActionFactory {
	private static IVRActionFactory instance;
	
	public enum ACTIONS{
		AUTHENTICATE_IVR_ACTION,
		START_IVR_ACTION,
		MAIN_MENU_IVR_ACTION,
		LISTEN_TWEETS_IVR_ACTION,
		LISTEN_DM_IVR_ACTION,
		SEE_YOU_IVR_ACTION, 
		LISTEN_RT_IVR_ACTION, 
		MESSAGES_FINISHED_IVR_ACTION
	}
	
	public IVRActionFactory(){
		
	}
	
	public IVRAction createAction(ACTIONS action, IVRHelper ivrhelper, IVRHandler ivrHandler){
		IVRAction ivrAction = null;
		switch(action){
		case AUTHENTICATE_IVR_ACTION:
			ivrAction = new AuthenticateIVRAction(ivrhelper, ivrHandler);
			break;			
		case START_IVR_ACTION:
			ivrAction = new StartIVRAction(ivrhelper, ivrHandler);
			break;
		case MAIN_MENU_IVR_ACTION:
			ivrAction = new MainMenuIVRAction(ivrhelper, ivrHandler);
			break;
		case LISTEN_TWEETS_IVR_ACTION:
			ivrAction = new ListenTweetsIVRAction(ivrhelper, ivrHandler);
			break;
		case LISTEN_DM_IVR_ACTION:
			ivrAction = new ListenDMIVRAction(ivrhelper, ivrHandler);
			break;	
		case SEE_YOU_IVR_ACTION:
			ivrAction = new SeeYouIVRAction(ivrhelper, ivrHandler);
			break;	
		case LISTEN_RT_IVR_ACTION:
			ivrAction = new ListenRTIVRAction(ivrhelper, ivrHandler);
			break;
		case MESSAGES_FINISHED_IVR_ACTION:
			ivrAction = new MessagesFinishedIVRAction(ivrhelper, ivrHandler);
			break;
		}
		
		return ivrAction;

	}
	
	public static synchronized IVRActionFactory getInstance(){
		if(instance == null)
			instance = new IVRActionFactory();
		return instance;
	}
}
