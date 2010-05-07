package org.vtlabs.socialnetworkbyphone;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.log.Log;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsConnectionEvent;
import org.mobicents.mscontrol.MsLinkEvent;
import org.mobicents.mscontrol.MsLinkMode;
import org.mobicents.servlet.sip.seam.entrypoint.SipFactory;
import org.mobicents.servlet.sip.seam.entrypoint.media.MediaController;
import org.mobicents.servlet.sip.seam.entrypoint.media.MediaEvent;
import org.mobicents.servlet.sip.seam.media.framework.IVRHelper;
import org.mobicents.servlet.sip.seam.media.framework.MediaSessionStore;
import org.vtlabs.socialnetworkbyphone.ivr.action.IVRAction;
import org.vtlabs.socialnetworkbyphone.ivr.action.IVRActionFactory;
import org.vtlabs.socialnetworkbyphone.ivr.action.IVRActionFactory.ACTIONS;
import org.vtlabs.socialnetworkbyphone.ivr.handler.IVRHandler;
import org.vtlabs.socialnetworkbyphone.twitter.TwitterBridge;

@Name("serviceMessageHandler")
@Scope(ScopeType.SESSION)
public class ServiceMessageHandler implements IVRHandler{
    
    @In MediaController mediaController;
    @In MediaSessionStore mediaSessionStore;
    @In IVRHelper ivrHelper;
    @In SipSession sipSession;
    @In EntityManager entityManager;
    @In(create=true)  TwitterBridge twitterBridge;
    @In(required=false,scope=ScopeType.SESSION) 
    @Out(required=false,scope=ScopeType.SESSION) 
    String mode;
    
    @Logger 
    private static Log log;
    
    private IVRAction ivrAction = null;
    
    @Observer("INVITE")
    public void doInvite(SipServletRequest request) throws Exception {
    	
    	// Guarda a requisição SIP na sessão.
        sipSession.setAttribute("inviteRequest", request);
        
        // Envia resposta provisional 180 Ringing, o chamador vai
        // escutar o telefone começar a tocar.
        request.createResponse(180).send();
        
        // Extrai o SDP (Descritor da sessão de media) do body da request. 
        String sdp = new String((byte[]) request.getContent());

        // A chamada alocará um recurso de media, após esta alocação
        // será chamado o método que está observando o evento connectionOpen. 
        mediaController.createConnection("media/trunk/PacketRelay/$").modify("$", sdp);
    }

    @Observer("connectionOpen")
    public void connectionOpenRequest(MsConnectionEvent event) throws Exception {
        /* [6]
         * Let's get the MsConnection object that connects the caller to the
         * PacketRelay Endpoint. We can take it from mediaSessionStore, where STF
         * collects objects related to the Media Topology of the current call(session).
         */
    	// Pega o objeto MsConnection, objeto que é reponsável pela conexão
    	// de media.
        MsConnection connection = mediaSessionStore.getMsConnection();

        // Extrai o SDP (Descritor da sessão de media) do MsConnection. 
        String sdp = event.getConnection().getLocalDescriptor();
        
        // Recupera a request através da Sessão
        SipServletRequest inviteRequest = (SipServletRequest) 
            sipSession.getAttribute("inviteRequest");
        
        // Cria a resposta 200OK equivalente ao atendimento da chamada
        // pelo Mobcents.
        SipServletResponse sipServletResponse = inviteRequest
                .createResponse(SipServletResponse.SC_OK);

        // Adiciona o SDP do MsConnection a resposta 200OK para rotear
        // apropriadamente a sessão de RTP
        sipServletResponse.setContent(sdp, "application/sdp");
            sipServletResponse.send();
        
        
        // Executa a conexão entre a sessão do Chamador e o MsConnection
        // Quando a sessão for estabelecida com sucesso o observador do 
        // evento linkConnected será chamado
        mediaController.createLink(MsLinkMode.FULL_DUPLEX)
            .join("media/trunk/IVR/$",
                    connection.getEndpoint().getLocalName());
    }
    
    @Observer("linkConnected")
    public void doLinkConnected(MsLinkEvent event) {
    	if(ivrAction == null){
            // Instancia a primeira action para tratamento do IVR
    		nextIVRAction(ACTIONS.START_IVR_ACTION);
    	} else{
    		ivrHelper.detectDtmf();
    	}
    }
    
    @Observer("mediaEvent")
    public void doMediaEvent(MediaEvent mediaEvent) {
        log.info("MediaEvent = " + mediaEvent);

    }
    
    @Observer("DTMF")
    public void doDtmf(String digit) throws Exception{
        log.info("DTMF = " + digit + ", calling the dtmfEvent method on action:" + IVRAction.class.getName());
    	ivrAction.dtmfEvent(digit);
        
        //mediaSessionStore.getMsLink().release();
    }
    @Observer("announcementComplete")
    public void doAnnouncementComplete() {
        log.info("announcementComplete event, calling the announcementCompletedEvent method on action:" + IVRAction.class.getName());
    	ivrAction.announcementCompletedEvent();
    	
    }

    @Observer("announcementFailed")
    public void doAnnouncementFailed() {
        log.info("announcementFailed event, calling the announcementFailedEvent method on action:" + IVRAction.class.getName());
    	ivrAction.announcementFailedEvent();
    	
    }
    
    @Observer("BYE")
    public void doBye(SipServletRequest request) throws Exception {
        request.createResponse(200).send();
    }
    
    @Observer("INFO")
    public void doInfo(SipServletRequest request) throws Exception {
        request.createResponse(200).send();
    }
    
    @Observer("REGISTER")
    public void doRegister(SipServletRequest request) throws Exception {
        request.createResponse(200).send();
    }

	public void nextIVRAction(ACTIONS actionType) {
		
		if(actionType != null){
			ivrAction = IVRActionFactory.getInstance()
							.createAction(actionType, ivrHelper, this);
			ivrAction.start();
		}
		else{
			try {
				sipSession.createRequest("BYE").send();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public SipSession getSipSession() {
		return sipSession;
	}
}