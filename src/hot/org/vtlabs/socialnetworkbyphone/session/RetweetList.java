package org.vtlabs.socialnetworkbyphone.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.vtlabs.socialnetworkbyphone.entity.TwitterRetweet;

@Name("retweetList")
public class RetweetList extends EntityQuery<TwitterRetweet>
{
    public RetweetList()
    {
        setEjbql("select retweet from Retweet retweet");
    }
}
