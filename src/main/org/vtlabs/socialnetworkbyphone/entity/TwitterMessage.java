package org.vtlabs.socialnetworkbyphone.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

public interface TwitterMessage {

	@Id
	@GeneratedValue
	public abstract Long getId();

	public abstract void setId(Long id);

	@Version
	public abstract Integer getVersion();

	public abstract String getSourceScreenName();

	public abstract void setSourceScreenName(String sourceScreenName);

	public abstract String getDestinationScreenName();

	public abstract void setDestinationScreenName(String destinationScreenName);

	public abstract String getText();

	public abstract void setText(String text);

	public abstract Date getDate();

	public abstract void setDate(Date date);

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "userId")
	public abstract User getUser();

	public abstract void setUser(User user);

	public abstract void setTwitterId(Long twitterId);

	public abstract Long getTwitterId();

}