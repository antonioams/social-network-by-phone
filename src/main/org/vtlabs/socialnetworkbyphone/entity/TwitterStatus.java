package org.vtlabs.socialnetworkbyphone.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.validator.Length;

@Entity
@Table(name="twitterStatuses")
public class TwitterStatus implements Serializable, TwitterMessage
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

    /* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#getId()
	 */
    @Id @GeneratedValue
    public Long getId() {
        return id;
    }

    /* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#setId(java.lang.Long)
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#getVersion()
	 */
    @Version
    public Integer getVersion() {
        return version;
    }

    private void setVersion(Integer version) {
        this.version = version;
    }

	/* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#getSourceScreenName()
	 */
	public String getSourceScreenName() {
		return sourceScreenName;
	}

	/* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#setSourceScreenName(java.lang.String)
	 */
	public void setSourceScreenName(String sourceScreenName) {
		this.sourceScreenName = sourceScreenName;
	}

	/* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#getDestinationScreenName()
	 */
	public String getDestinationScreenName() {
		return destinationScreenName;
	}

	/* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#setDestinationScreenName(java.lang.String)
	 */
	public void setDestinationScreenName(String destinationScreenName) {
		this.destinationScreenName = destinationScreenName;
	}

	/* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#getText()
	 */
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#setText(java.lang.String)
	 */
	public void setText(String text) {
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#getDate()
	 */
	public Date getDate() {
		return date;
	}

	/* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#setDate(java.util.Date)
	 */
	public void setDate(Date date) {
		this.date = date;
	}

    /* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#getUser()
	 */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="userId")	
	public User getUser() {
		return user;
	}

	/* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#setUser(org.vtlabs.socialnetworkbyphone.entity.User)
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#setTwitterId(java.lang.Long)
	 */
	public void setTwitterId(Long twitterId) {
		this.twitterId = twitterId;
	}

	/* (non-Javadoc)
	 * @see org.vtlabs.socialnetworkbyphone.entity.TwitterMessage#getTwitterId()
	 */
	public Long getTwitterId() {
		return twitterId;
	}
}
