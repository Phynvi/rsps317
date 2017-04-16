package com.bclaus.rsps.server.vd.player;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class InterfaceManager {
	
    private final Player player;
    
    private List<Player> interfaces;
    
    public InterfaceManager(Player player) {
        this.player = player;
        if (interfaces == null)
            this.interfaces = new ArrayList<>();
    }
    
    public void sendInterface(int id) {
       player.getPlayerAssistant().showInterface(id);
    }
    
    public void sendString(String s, int id) {
        player.getPlayerAssistant().sendString(s, id);
    }
    
    public void sendChatInterface(int id) {
        player.getPlayerAssistant().sendChatBoxInterface(id);
    }
    
    public void closeInterfaces() {
        player.getPlayerAssistant().closeAllWindows();
    }
    
    public void sendItemOnInterface(int interfaceChild, int zoom, int itemId) {
        player.getPlayerAssistant().itemOnInterface(interfaceChild, zoom, itemId);
    }
}
