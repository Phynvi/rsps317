package com.bclaus.rsps.server.vd.player;
/**
 * s
 * @author Jason MacKeigan
 * @date Aug 21, 2014, 9:32:19 AM
 */
public class GameOptionsInterface {
	/**
	 * The player associated with these options
	 */
	Player player;
	
	/**
	 * The saved states for each toggleable option 
	 */
	private boolean[] toggleOption = new boolean[Toggles.values().length];
	
	/**
	 * The current icon the player is using
	 */
	private UserSetting playerIcon = UserSetting.NO_ICON;
	
	/**
	 * The current title the player is using
	 */
	private UserSetting playerTitle = UserSetting.NO_TITLE;
	/**
	 * Constructs a new GameOptions object with a player
	 * @param player the player
	 */
	public GameOptionsInterface(Player player) {
		this.player = player;
		for(int i = 0; i < toggleOption.length; i++)
			toggleOption[i] = true;
	}
	
	/**
	 * Sets the state of one of the toggle options to either on or off
	 * @param index the index of the toggle
	 * @param state the state of the toggle
	 */
	public void setToggle(int index, boolean state) {
		if(index > toggleOption.length - 1)
			return;
		Toggles toggle = Toggles.values()[index];
		if(toggle == null)
			return;
		toggleOption[index] = state;
		player.getPA().sendFrame36(toggle.config, state == true ? 1 : 0);
	}
	
	/**
	 * Returns wether or not the option is toggled based on the index
	 * @param index the index
	 * @return if the option is toggled or not
	 */
	public boolean isToggled(int index) {
		if(index > this.toggleOption.length - 1)
			return false;
		return this.toggleOption[index] == true;
	}
	
	/**
	 * Refreshes the toggle buttons on the interface
	 */
	public void refresh() {
		for(Toggles toggle : Toggles.values()) {
			player.getPA().sendFrame36(toggle.config,
					toggleOption[toggle.ordinal()] == true ? 1 : 0);
		}
		for(UserSetting setting : UserSetting.values()) {
			if(setting.type == ToggleType.ICON) {
    			if(this.playerIcon != null && setting == this.playerIcon) {
    				player.getPA().sendFrame36(1000 + setting.listIndex, 1);
    			} else {
    				player.getPA().sendFrame36(1000 + setting.listIndex, 0);
    			}
    		} else if(setting.type == ToggleType.TITLE) {
    			if(this.playerTitle != null && setting == this.playerTitle) {
    				player.getPA().sendFrame36(875 + setting.listIndex, 1);
    			} else {
    				player.getPA().sendFrame36(875 + setting.listIndex, 0);
    			}
    		}
		}
	}
	
	public void performAction(int actionId) {
		for(Toggles toggle : Toggles.values()) {
			if(toggle.button == actionId) {
				setToggle(toggle.ordinal(), isToggled(toggle.ordinal()) ? false : true);
				return;
			}
		}
		if(actionId >= 158004 && actionId <= 158025) {
			/*if(player.isStaff()) {
				player.sendMessage("You are a member of staff, you are not permitted to switch your icon.");
				refresh();
				return;
			}*/
			if(player.playerRights == 16) {
				player.sendMessage("You are a player informative, you are not permitted to switch your icon.");
				refresh();
				return;
			}
		}
		switch(actionId) {
    		case 158004:
    			if(playerIcon != null && playerIcon == UserSetting.NO_ICON) {
    				player.sendMessage("You already have no icon.");
    				refresh();
    				return;
    			}
    			if(UserSetting.NO_ICON.visible(player)) {
    				player.playerRights = 0;
    				playerIcon = UserSetting.NO_ICON;
    				player.sendMessage("You need to logout for the icon to leave the chatbox area.");
    			}
    			player.getPA().requestUpdates();
    			refresh();
    			break;
				
			case 157210:
				if(UserSetting.NO_TITLE.visible(player)) {
					//player.playerTitle = "";
					playerTitle = UserSetting.NO_TITLE;
					player.getPA().requestUpdates();
				}
				refresh();
				break;
		}
	}
	
	/**
	 * Returns the toggleOptions variable
	 * @return the toggle options
	 */
	public boolean[] getToggleOptions() {
		return this.toggleOption;
	}
	
	/**
	 * Sets the toggle options
	 * @param options the options
	 */
	public void setToggleOption(boolean[] options) {
		this.toggleOption = options;
	}
	
	public UserSetting getPlayerTitle() {
		return playerTitle;
	}

	public void setPlayerTitle(UserSetting playerTitle) {
		this.playerTitle = playerTitle;
	}
	
	public UserSetting getPlayerIcon() {
		return playerIcon;
	}
	
	public void setPlayerIcon(String name) {
		for(UserSetting setting : UserSetting.values()) {
			if(setting.toString().equalsIgnoreCase(name)) {
				this.playerIcon = setting;
				break;
			}
		}
	}

	/**
	 * An enum containing the button id and config frame
	 * @author Jason MacKeigan
	 * @date Aug 21, 2014, 9:36:26 AM
	 */
	public enum Toggles {
		OPTION_1(157122, 954),
		OPTION_2(157125, 955),
		OPTION_3(157128, 956),
		OPTION_4(157131, 957);
		
		public int button, config;
		Toggles(int button, int config) {
			this.button = button;
			this.config = config;
		}
	}
	
	/**
	 * A basic interface that contains a single function that will return if a player
	 * meets certain requirements that are predefined
	 * @author Jason MacKeigan
	 * @date Aug 23, 2014, 8:51:13 PM
	 */
	interface ToggleRequirement { 
		boolean able(Player player);
	}
	
	enum ToggleType {
		TITLE, ICON;
	}
	
	/**
	 * An enum that contains a single object, ToggleRequirement which utilizes the able
	 * function
	 * @author Jason MacKeigan
	 * @date Aug 23, 2014, 8:51:35 PM
	 */
	public enum UserSetting {
		NO_ICON(0, ToggleType.ICON, new ToggleRequirement() {
			@Override
			public boolean able(Player player) {
				return true;
			}
		}),
		NO_TITLE(0, ToggleType.TITLE, new ToggleRequirement() {

			@Override
			public boolean able(Player player) {
				return true;
			}
		});
		
		/**
		 * The ToggleRequirement object associated with this enum element
		 */
		private ToggleRequirement requirement;
		
		/**
		 * The type of toggle wether its an icon, or title
		 */
		private ToggleType type;
		
		/**
		 * The index on the list of toggles this setting resides
		 */
		private int listIndex;
		
		/**
		 * The enum constructor that passes the requirements needed to be able
		 * to use the icon
		 * @param requirement	the requirement
		 * @param type	the type
		 */
		UserSetting(int listIndex, ToggleType type, ToggleRequirement requirement) {
			this.listIndex = listIndex;
			this.type = type;
			this.requirement = requirement;
		}
		
		/**
		 * Determines if the icon is visible to the player
		 * @param player
		 * @return
		 */
		public boolean visible(Player player) {
			return this.requirement.able(player);
		}
		
		@Override 
		public String toString() {
			return this.name().toLowerCase();
		}
		
	}

}
