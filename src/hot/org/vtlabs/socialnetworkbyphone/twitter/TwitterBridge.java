package org.vtlabs.socialnetworkbyphone.twitter;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.jboss.seam.log.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.vtlabs.socialnetworkbyphone.entity.TwitterDirectMessage;
import org.vtlabs.socialnetworkbyphone.entity.TwitterMessage;
import org.vtlabs.socialnetworkbyphone.entity.TwitterRetweet;
import org.vtlabs.socialnetworkbyphone.entity.TwitterStatus;
import org.vtlabs.socialnetworkbyphone.entity.User;

import br.com.voicetechnology.vp.tts.Engine;
import br.com.voicetechnology.vp.tts.TTSObserver;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@Name("twitterBridge")
@Scope(ScopeType.APPLICATION)
@Startup
@AutoCreate
public class TwitterBridge implements TTSObserver {
    @In(create=true) EntityManager entityManager;
    @In QuartzTriggerHandle timer;
	@Logger
	private Log log;
	
	private Engine engine;

    private String cron = "0 0/1 * * * ?";
    
    private String fileDir = "/var/www/html/jbossinbossa/";
    
    private Integer activeJobs = 0;

    @Create
    public void schedule() throws Exception {
    	timer = TwitterJob.instance().execute(cron);
    }

    @Destroy
    public void unschedule() throws Exception {
          timer.cancel();
    }

	private Twitter connect(User user) throws TwitterException
	{
		Twitter twitter = new TwitterFactory().getInstance(user.getTwitterUsername(), user.getTwitterPassword());
		if(twitter.verifyCredentials() != null)
			return twitter;
		return null;
	}

	private void getTwitterStatus(Twitter twitter, User user){
		List result = entityManager.createNativeQuery(
							"SELECT twitterId FROM twitterStatuses WHERE timestamp = (" +
								"SELECT MAX(timestamp) FROM twitterStatuses where userId = ?)")
						.setParameter(1, user.getId())
						.getResultList();
		if(result != null && result.size() > 0)
			getTwitterStatus(twitter, user, ((BigInteger)result.get(0)).longValue());
		else
			getTwitterStatus(twitter, user, -1);
	}
	
	private void getTwitterStatus(Twitter twitter, User user, long lastUpdateId)
	{
		try
		{
			ResponseList<Status> statuses;
			if(lastUpdateId != -1)
				statuses = twitter.getFriendsTimeline(new Paging(lastUpdateId));
			else
				statuses = twitter.getFriendsTimeline();
			
			for (Status status : statuses)
			{
				TwitterMessage twStatus = new TwitterStatus();
				twStatus.setSourceScreenName(status.getUser().getScreenName());
				twStatus.setDestinationScreenName("*");
				twStatus.setDate(status.getCreatedAt());
				twStatus.setText(status.getText());
				twStatus.setTwitterId(status.getId());
				twStatus.setUser(user);
				createAudioFile(twStatus, user);
				entityManager.persist(twStatus);
			}
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}
	}
	
	private void createAudioFile(TwitterMessage message, User user) {
		checkUserAudioFiles(user);
		StringBuilder fileName = new StringBuilder();
		StringBuilder msgBody = new StringBuilder();
		fileName.append(fileDir);
		fileName.append("user-");
		fileName.append(user.getName()); 
		if(message instanceof TwitterStatus)
			fileName.append("-tw-");
		else if(message instanceof TwitterDirectMessage)
			fileName.append("-dm-");
		else if(message instanceof TwitterRetweet)
			fileName.append("-rt-");
		
		fileName.append(message.getTwitterId());
		fileName.append(".wav");
		
		msgBody.append("MENSAGEM RECEBIDA DE ");
		msgBody.append(message.getSourceScreenName());
		msgBody.append(", CONTEÚDO DA MENSAGEM, ");
		msgBody.append(message.getText());
		synchronized (activeJobs) {
			engine.addJob(new br.com.voicetechnology.vp.tts.Job(fileName.toString(), msgBody.toString().toUpperCase(), message));
			activeJobs++;
		}
		
	}


	private void checkUserAudioFiles(User user) {
		String fileName;
		File file;
		
		fileName = fileDir + "user-dm-" + user.getName() + ".wav";
		file = new File(fileName);
		synchronized (activeJobs) {
			if(!file.exists()){
				engine.addJob(new br.com.voicetechnology.vp.tts.Job(fileName, "IREI REPRODUZIR AS MENSAGENS DIRETAS ENVIADAS PARA " + user.getFullName().toUpperCase(), user));
				activeJobs++;
			}
				
			fileName = fileDir + "user-tw-" + user.getName() + ".wav";
			file = new File(fileName);
			if(!file.exists()){
				engine.addJob(new br.com.voicetechnology.vp.tts.Job(fileName, "IREI REPRODUZIR AS ATUALIZASSÕES DOS AMIGOS DE " + user.getFullName().toUpperCase(), user));
				activeJobs++;
			}
			
			fileName = fileDir + "user-rt-" + user.getName() + ".wav";
			file = new File(fileName);
			if(!file.exists()){
				engine.addJob(new br.com.voicetechnology.vp.tts.Job(fileName, "IREI REPRODUZIR AS RESPOSTAS DOS AMIGOS DE " + user.getFullName().toUpperCase(), user));
				activeJobs++;
			}
		}		
	}


	private void getTwitterRetweets(Twitter twitter, User user){
		List result = entityManager.createNativeQuery(
						"SELECT twitterId FROM twitterRetweets WHERE timestamp = (" +
							"SELECT MAX(timestamp) FROM twitterRetweets where userId = ?)")
						.setParameter(1, user.getId())
						.getResultList();
		if(result != null && result.size() > 0)
			getTwitterRetweets(twitter, user, ((BigInteger)result.get(0)).longValue());
		else
			getTwitterRetweets(twitter, user, -1);
	}
	
	private void getTwitterRetweets(Twitter twitter, User user, long lastUpdateId)
	{
		try
		{
			ResponseList<Status> statuses;
			if(lastUpdateId != -1)
				statuses = twitter.getRetweetedToMe(new Paging(lastUpdateId));
			else
				statuses = twitter.getRetweetedToMe();
			
			for (Status status : statuses)
			{
				TwitterRetweet twStatus = new TwitterRetweet();
				twStatus.setSourceScreenName(status.getUser().getScreenName());
				twStatus.setDestinationScreenName("*");
				twStatus.setDate(status.getCreatedAt());
				twStatus.setText(status.getText());
				twStatus.setTwitterId(status.getId());
				twStatus.setUser(user);
				createAudioFile(twStatus, user);
				entityManager.persist(twStatus);
			}
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}
	}

	private void getTwitterDirectMessages(Twitter twitter, User user){
		List result = entityManager.createNativeQuery(
					"SELECT twitterId FROM twitterDirectMessages WHERE timestamp = (" +
						"SELECT MAX(timestamp) FROM twitterDirectMessages where userId = ?)")
						.setParameter(1, user.getId())
						.getResultList();
		if(result != null && result.size() > 0)
			getTwitterDirectMessages(twitter, user, ((BigInteger)result.get(0)).longValue());
		else
			getTwitterDirectMessages(twitter, user, -1);
	}

	private void getTwitterDirectMessages(Twitter twitter, User user, long lastUpdateId)
	{
		try
		{
			ResponseList<DirectMessage> messages;
			if(lastUpdateId != -1)
				messages = twitter.getDirectMessages(new Paging(lastUpdateId));
			else
				messages = twitter.getDirectMessages();
			
			for (DirectMessage message : messages)
			{
				TwitterDirectMessage twMessage = new TwitterDirectMessage();
				
				twMessage.setSourceScreenName(message.getSenderScreenName());
				twMessage.setDestinationScreenName(message.getRecipientScreenName());
				twMessage.setDate(message.getCreatedAt());
				twMessage.setText(message.getText());
				twMessage.setTwitterId(Long.valueOf(message.getId()));
				twMessage.setUser(user);
				createAudioFile(twMessage, user);
				entityManager.persist(twMessage);
			}
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}
	}

	@Transactional(value=TransactionPropagationType.REQUIRED)
	public void updateTweets(){

		List<User> users;
		if(entityManager != null){
			log.info("Verificando se existem usuários para atualizar Tweets.");
			users = (List<User>) entityManager.createQuery("select u from User u").getResultList();
			if(users != null && users.size() > 0){
		    	try{
					log.info("Existem usuários para atualizar os Tweets, criando Engine do TTS.");
		    		engine = Engine.createEngine("ScanSoft", null, 1);
					engine.setObserver(this);
		    		
		    	} catch (Throwable e) {
					log.error("Erro na criacao do Engine de TTS", e);
				}
			}
			for (User user : users) {
				Twitter twCon;
				try {
					log.info("Atualizando Tweets do usuário: " + user.getName());
					twCon = connect(user);
					//entityManager.getTransaction().begin();
					getTwitterDirectMessages(twCon, user);
					getTwitterStatus(twCon, user);
					getTwitterRetweets(twCon, user);
					//entityManager.getTransaction().commit();
				} catch (TwitterException e) {
					log.info("Erro na atualizacao dos Tweets do usuário: " + user.getName());
					e.printStackTrace();
				}
			}
			synchronized (activeJobs) {
				log.info("Verificando se houve geracao de TTS.");
				if(activeJobs > 0){
					try {
						log.info("Aguardando finalizacaos dos TTS.");
						activeJobs.wait(50000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if(engine != null)
				log.info("Fechando Engine do TTS.");
				engine.close();
		}
	}
	
    @Name("twitterJob")
    @Scope(ScopeType.EVENT)
    @AutoCreate
	public static class TwitterJob implements Job {
    	@In TwitterBridge twitterBridge;
    	@In(create=true) EntityManager entityManager;

		@Logger
		private Log log;

		@Asynchronous
		@Transactional
		public QuartzTriggerHandle execute(@IntervalCron String cron) {
			log.info("Iniciando atualização dos tweets.");
			twitterBridge.updateTweets();
			return null;
		}

		public static TwitterJob instance() {
			return (TwitterJob) Component.getInstance(TwitterJob.class);
		}

		public void execute(JobExecutionContext arg0)
				throws JobExecutionException {
			// TODO Auto-generated method stub
		}
	}

	public void exceptionCaught(br.com.voicetechnology.vp.tts.Job arg0,
			Throwable arg1) {
		log.error("Exception na geracao do arquivo de audio:" + arg0.getFile().toString(), arg1);		
		
	}

	public void jobComplete(br.com.voicetechnology.vp.tts.Job arg0) {
		log.info("Arquivo de audio gerado com sucesso:" + arg0.getFile().toString());
		synchronized (activeJobs) {
			activeJobs--;
			if(activeJobs == 0)
				activeJobs.notify();
		}
	}

	public void jobStarted(br.com.voicetechnology.vp.tts.Job arg0) {
		log.info("Iniciando processo de geracao de arquivo de audio:" + arg0.getFile().toString());
	}

}
