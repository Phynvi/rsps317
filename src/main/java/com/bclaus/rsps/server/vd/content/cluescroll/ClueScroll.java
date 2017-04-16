package com.bclaus.rsps.server.vd.content.cluescroll;

import com.bclaus.rsps.server.vd.world.Location;
import com.bclaus.rsps.server.vd.world.Position;

/**
 * @author lare96 <http://github.com/lare96>
 */
public enum ClueScroll {
	VARROCK_MINE(7045, new Position(3289, 3372)),
	DRAYNOR(7113, new Position(3093, 3226)),
	RANGING_GUILD(7162, new Position(2702, 3429)), 
	FALADOR(7271, new Position(3043, 3398)), 
	YANILLE(9043, new Position(2616, 3077)), 
	WIZARDS_TOWER(9275, new Position(3109, 3153)), 
	WEST_ARDOUGNE(9359, new Position(2488, 3308)), 
	EAST_ARDOUGNE(9632, new Position(2651, 3233)), 
	CLOCK_TOWER(9720, new Position(2565, 3248)), 
	FALADOR_STATURE(17537, new Position(2969, 3414)), 
	WILDY_AGILITY_COURSE(17620, new Position(3021, 3912)),
	LEGENDS_GUILD(17634, new Position(2723, 3338)),
	RIMMINGTON(9839, new Position(2924, 3209));

	private final int interfaceId;
	private final Position position;

	private ClueScroll(int interfaceId, Position position) {
		this.interfaceId = interfaceId;
		this.position = position;
	}

	public final Location getLocation() {
		return new Location(position, ClueScrollHandler.DIG_RADIUS);
	}

	public final int interfaceId() {
		return interfaceId;
	}
}