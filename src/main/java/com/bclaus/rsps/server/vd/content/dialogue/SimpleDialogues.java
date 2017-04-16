package com.bclaus.rsps.server.vd.content.dialogue;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class SimpleDialogues {

	public static void sendStatement(Player player, Object... strings) {
		player.dialogue().start(new Dialogue() {
			@Override
			protected void start(Object... parameters) {
				send(Type.STATEMENT, strings);
			}
		});
	}

	public static void sendMobStatement(Player player, int id, String... strings) {
		player.dialogue().start(new Dialogue() {
			@Override
			protected void start(Object... parameters) {
				// XXX: Solution to ClassCastException with multiple params
				Deque<Object> objs = new LinkedList<>(Arrays.asList(strings));
				objs.addFirst(Expression.ANGRY1);
				objs.addFirst(id);
				//TODO: check if this still works compared to the google commons library
				send(Type.NPC, objs.toArray());
			}
		});
	}

	private SimpleDialogues() {
		throw new UnsupportedOperationException();
	}

}