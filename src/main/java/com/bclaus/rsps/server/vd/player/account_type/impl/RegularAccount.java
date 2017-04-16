package com.bclaus.rsps.server.vd.player.account_type.impl;

import java.util.Arrays;
import java.util.List;

import com.bclaus.rsps.server.vd.player.account_type.AccountType;
import com.bclaus.rsps.server.vd.player.account_type.Account;

/**
 * Represents a regular account, the default account type.
 * 
 * @author Jason MacKeigan
 * @date Sep 11, 2014, 8:26:36 PM
 */
public class RegularAccount extends AccountType {

	@Override
	public String alias() {
		return "Regular";
	}

	@Override
	public int getPrivilege() {
		return 0;
	}

	@Override
	public boolean unownedDropsVisible() {
		return true;
	}

	@Override
	public boolean tradingPermitted() {
		return true;
	}

	@Override
	public boolean shopAccessible(int shopId) {
		return true;
	}

	@Override
	public boolean changable() {
		return true;
	}

	@Override
	public boolean dropAnnouncementVisible() {
		return true;
	}

	@Override
	public boolean stakeItems() {
		return true;
	}

	@Override
	public List<String> attackableTypes() {
		return Arrays.asList(Account.REGULAR_TYPE.alias());
	}

}
