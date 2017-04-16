package com.bclaus.rsps.server.vd.player.account_type;

import java.util.HashMap;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.account_type.impl.IronManAccount;
import com.bclaus.rsps.server.vd.player.account_type.impl.RegularAccount;

/**
 * The use of this class is fairly simple, it distinguishes what type of account
 * the player is currently assigned to. By default, the account type is regular.
 * 
 * @author Jason MacKeigan
 * @date Sep 11, 2014, 8:26:22 PM
 */
public class Account {

	/**
	 * A Map containing all of the possible AccountTypes available. The key is
	 * the alias of the account type.
	 */
	static HashMap<String, AccountType> types;

	/**
	 * A subclass of AccountType that stores basic information about the regular
	 * account.
	 */
	public static final RegularAccount REGULAR_TYPE = new RegularAccount();

	/**
	 * A subclass of AccountType that stores basic information about the iron
	 * man account.
	 */
	public static final IronManAccount IRON_MAN_TYPE = new IronManAccount();

	/**
	 * The type of account, being regular, iron man, etcetera.
	 */
	AccountType type;

	/**
	 * The player associated with this account. All of the member of this class
	 * are specific for the player, and the player only.
	 */
	Player player;

	/**
	 * Constructs a new Account object for the player object.
	 * 
	 * @param player
	 *            the player
	 */
	public Account(Player player) {
		this.player = player;
	}

	/**
	 * Assigns an account type to this player
	 * 
	 * @param type
	 */
	public void setType(AccountType type) {
		this.type = type;
	}

	/**
	 * Determines what type of account this is.
	 * 
	 * @return the type
	 */
	public AccountType getType() {
		if (type == null)
			type = new RegularAccount();
		return type;
	}

	/**
	 * Returns an AccountType object with the same alias as the one passed to
	 * the function. If the alias does not exist, it will return the regular
	 * account type, by default.
	 * 
	 * @param alias
	 *            the alias of the account type
	 * @return the account type
	 */
	public static AccountType get(String alias) {
		for (String key : types.keySet()) {
			if (key.equalsIgnoreCase(alias))
				return types.get(key);
		}
		return null;
	}

	/**
	 * The static constructor that will assist us in storing some basic
	 * information into a hashmap when the class if first referenced. Creating a
	 * new AccountType for each entry may seem a bit odd but it ensures that the
	 * alias used in the creation of the subclass is the same as the key. It
	 * also avoids creating 2 instances instead of 1.
	 */
	static {
		types = new HashMap<>();
		types.put(REGULAR_TYPE.alias(), REGULAR_TYPE);
		types.put(IRON_MAN_TYPE.alias(), IRON_MAN_TYPE);
	}

}
