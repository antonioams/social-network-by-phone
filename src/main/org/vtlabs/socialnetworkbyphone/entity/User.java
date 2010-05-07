package org.vtlabs.socialnetworkbyphone.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.validator.Length;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Validator;
import org.jboss.seam.annotations.security.permission.PermissionDiscriminator;

@Entity
@Name("user")
@Table(name="users")
@Scope(ScopeType.SESSION)

public class User implements Serializable
{
    // seam-gen attributes (you should probably edit these)
    private Long id;
    private Integer version;
    private String name;
	private String fullName;
    private String password;
    private String passwordConfirm;
    private String phoneNumber;
    private String twitterUsername;
    private String twitterPassword;
    private List<TwitterDirectMessage> directMessages;
    private List<TwitterStatus> friendStatuses;
    private List<TwitterRetweet> Retweests;

    // add additional entity attributes

    // seam-gen attribute getters/setters with annotations (you probably should edit)

    @Id @GeneratedValue
    public Long getId() {
        return id;
    }

    @Length(max = 20)
    public String getPasswordConfirm() {
		return passwordConfirm;
	}


	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}


	@Length(max = 40)
    public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Length(max = 20)
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(max = 20)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Length(max = 20)
	public String getTwitterUsername() {
		return twitterUsername;
	}

	public void setTwitterUsername(String twitterUsername) {
		this.twitterUsername = twitterUsername;
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

    @Length(max = 20)
    @Column(unique=true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getTwitterPassword() {
		return twitterPassword;
	}

	public void setTwitterPassword(String twitterPassword) {
		this.twitterPassword = twitterPassword;
	}

	@OneToMany(mappedBy="user")
	public List<TwitterDirectMessage> getDirectMessages() {
		return directMessages;
	}

	public void setDirectMessages(List<TwitterDirectMessage> directMessages) {
		this.directMessages = directMessages;
	}
	
	@OneToMany(mappedBy="user")
	public List<TwitterStatus> getFriendStatuses() {
		return friendStatuses;
	}

	public void setFriendStatuses(List<TwitterStatus> friendStatuses) {
		this.friendStatuses = friendStatuses;
	}

	@OneToMany(mappedBy="user")
	public List<TwitterRetweet> getRetweests() {
		return Retweests;
	}

	public void setRetweests(List<TwitterRetweet> retweests) {
		Retweests = retweests;
	}
    
}
