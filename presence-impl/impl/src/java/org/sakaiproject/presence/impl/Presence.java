package org.sakaiproject.presence.impl;

import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.presence.api.PresenceService;
import org.sakaiproject.tool.api.SessionBindingEvent;
import org.sakaiproject.tool.api.SessionBindingListener;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jbush
 * Date: 11/21/13
 * Time: 10:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class Presence implements SessionBindingListener, Serializable
{
    transient protected PresenceService presenceService;

	/** The location id. */
	protected String m_locationId = null;

	/** If true, process the unbound. */
	protected boolean m_active = true;

	/** Time in seconds before expiry. */
	protected long m_presence_timeout = 0;

	/** Timestamp in milliseconds to expire. */
	protected long m_expireTime = 0;

	public Presence(String locationId, int timeout)
	{
		m_locationId = locationId;
		m_presence_timeout = timeout;
		m_expireTime = System.currentTimeMillis() + m_presence_timeout * 1000;
	}

	public void deactivate()
	{
		m_active = false;
	}

	/**
	 * Reset the timeout based on current activity
	 */
	public void setActive()
	{
		m_expireTime = System.currentTimeMillis() + m_presence_timeout * 1000;
	}

	/**
	 * Has this presence timed out?
	 *
	 * @return true if expired, false if not.
	 */
	public boolean isExpired()
	{
		return System.currentTimeMillis() > m_expireTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueBound(SessionBindingEvent event)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueUnbound(SessionBindingEvent evt)
	{
		if (m_active)
		{
            getPresenceService().removePresence(m_locationId);
		}
	}

    public PresenceService getPresenceService() {
        if (presenceService == null) {
            presenceService = (PresenceService) ComponentManager.get(PresenceService.class);
        }
        return presenceService;
    }
}
