package org.cmc;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerTimeUpdate extends Event {
 private static final HandlerList handlers = new HandlerList();
    private int time;

    public ServerTimeUpdate(int currenttime) 
    {
        time = currenttime;
    }
    public int getTime() 
    {
        return time;
    }
    
    public HandlerList getHandlers() 
    {
        return handlers;
    }

    public static HandlerList getHandlerList() 
    {
        return handlers;
    }
}
