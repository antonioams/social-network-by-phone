package org.vtlabs.socialnetworkbyphone.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityHome;

import org.vtlabs.socialnetworkbyphone.entity.TwitterStatus;

@Name("statusHome")
public class StatusHome extends EntityHome<TwitterStatus>
{
    @RequestParameter Long statusId;

    @Override
    public Object getId()
    {
        if (statusId == null)
        {
            return super.getId();
        }
        else
        {
            return statusId;
        }
    }

    @Override @Begin
    public void create() {
        super.create();
    }

}
