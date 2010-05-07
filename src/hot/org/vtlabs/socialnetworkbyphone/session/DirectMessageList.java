package org.vtlabs.socialnetworkbyphone.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.vtlabs.socialnetworkbyphone.entity.TwitterDirectMessage;

@Name("directMessageList")
public class DirectMessageList extends EntityQuery<TwitterDirectMessage>
{
    public DirectMessageList()
    {
        setEjbql("select directMessage from DirectMessage directMessage");
    }
}
