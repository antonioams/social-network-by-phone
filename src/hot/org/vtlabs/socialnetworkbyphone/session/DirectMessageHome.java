package org.vtlabs.socialnetworkbyphone.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityHome;

import org.vtlabs.socialnetworkbyphone.entity.TwitterDirectMessage;

@Name("directMessageHome")
public class DirectMessageHome extends EntityHome<TwitterDirectMessage>
{
    @RequestParameter Long directMessageId;

    @Override
    public Object getId()
    {
        if (directMessageId == null)
        {
            return super.getId();
        }
        else
        {
            return directMessageId;
        }
    }

    @Override @Begin
    public void create() {
        super.create();
    }

}
