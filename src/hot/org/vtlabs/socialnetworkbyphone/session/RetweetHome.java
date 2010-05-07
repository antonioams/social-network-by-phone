package org.vtlabs.socialnetworkbyphone.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityHome;

import org.vtlabs.socialnetworkbyphone.entity.TwitterRetweet;

@Name("retweetHome")
public class RetweetHome extends EntityHome<TwitterRetweet>
{
    @RequestParameter Long retweetId;

    @Override
    public Object getId()
    {
        if (retweetId == null)
        {
            return super.getId();
        }
        else
        {
            return retweetId;
        }
    }

    @Override @Begin
    public void create() {
        super.create();
    }

}
