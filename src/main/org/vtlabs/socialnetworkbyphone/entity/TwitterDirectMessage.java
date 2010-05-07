package org.vtlabs.socialnetworkbyphone.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.persistence.JoinColumn;
import org.hibernate.validator.Length;

@Entity
@Table(name="twitterDirectMessages")
public class TwitterDirectMessage implements Serializable, TwitterMessage
{
    // seam-gen attributes (you should probably edit these)
    private Long id;
    private Long twitterId;
    private Integer version;
    private String sourceScreenName;
    private String destinationScreenName;
    private String text;
    private Date date;
    private User user;

	// add additional entity attributes

    // seam-gen attribute getters/setters with annotations (you probably should edit)

    @Id @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    private void setVersion(Integer version) {
        this.version = version;
    }

	public String getSourceScreenName() {
		return sourceScreenName;
	}

	public void setSourceScreenName(String sourceScreenName) {
		this.sourceScreenName = sourceScreenName;
	}

	public String getDestinationScreenName() {
		return destinationScreenName;
	}

	public void setDestinationScreenName(String destinationScreenName) {
		this.destinationScreenName = destinationScreenName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="userId")	
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setTwitterId(Long twitterId) {
		this.twitterId = twitterId;
	}

	public Long getTwitterId() {
		return twitterId;
	}
}
