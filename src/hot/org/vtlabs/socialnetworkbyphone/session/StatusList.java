package org.vtlabs.socialnetworkbyphone.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.vtlabs.socialnetworkbyphone.entity.TwitterStatus;

@Name("statusList")
public class StatusList extends EntityQuery<TwitterStatus>
{
    public StatusList()
    {
        setEjbql("select status from Status status");
    }
}
