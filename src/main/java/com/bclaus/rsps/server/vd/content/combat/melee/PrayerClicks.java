/**
 * 
 */
package com.bclaus.rsps.server.vd.content.combat.melee;

import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author Tim http://rune-server.org/members/Someone
 *
 */
public class PrayerClicks {

	public static void handleClicks(final Player c, int actionButtonId) {
		switch (actionButtonId) {
		case 21233: // thick skin
			c.getCombat().activatePrayer(0);
			break;
		case 21234: // burst of str
			c.getCombat().activatePrayer(1);
			c.getPA().closeAllWindows();
			break;
		case 21235: // charity of thought
			c.getCombat().activatePrayer(2);
			c.getPA().closeAllWindows();
			break;
		case 70080: // range
			c.getCombat().activatePrayer(3);
			c.getPA().closeAllWindows();
			break;
		case 70082: // mage
			c.getCombat().activatePrayer(4);
			c.getPA().closeAllWindows();
			break;
		case 21236: // rockskin
			c.getCombat().activatePrayer(5);
			c.getPA().closeAllWindows();
			break;
		case 21237: // super human
			c.getCombat().activatePrayer(6);
			c.getPA().closeAllWindows();
			break;
		case 21238: // improved reflexes
			c.getCombat().activatePrayer(7);
			c.getPA().closeAllWindows();
			break;
		case 21239: // hawk eye
			c.getCombat().activatePrayer(8);
			c.getPA().closeAllWindows();
			break;
		case 21240:
			c.getCombat().activatePrayer(9);
			c.getPA().closeAllWindows();
			break;
		case 21241: // protect Item
			if (!c.isDead) {
				c.getCombat().activatePrayer(10);
			} else {
				c.sendMessage("You can't use this prayer when you're already dead!");
			}
			c.getPA().closeAllWindows();
			break;
		case 70084: // 26 range
			c.getCombat().activatePrayer(11);
			c.getPA().closeAllWindows();
			break;
		case 70086: // 27 mage
			c.getCombat().activatePrayer(12);
			c.getPA().closeAllWindows();
			break;
		case 21242: // steel skin
			c.getCombat().activatePrayer(13);
			c.getPA().closeAllWindows();
			break;
		case 21243: // ultimate str
			c.getCombat().activatePrayer(14);
			c.getPA().closeAllWindows();
			break;
		case 21244: // incredible reflex
			c.getCombat().activatePrayer(15);
			c.getPA().closeAllWindows();
			break;
		case 21245: // protect from magic
			c.getCombat().activatePrayer(16);
			c.getPA().closeAllWindows();
			break;
		case 21246: // protect from range
			c.getCombat().activatePrayer(17);
			c.getPA().closeAllWindows();
			break;
		case 21247: // protect from melee
			c.getCombat().activatePrayer(18);
			c.getPA().closeAllWindows();
			break;
		case 77109: // 44 range
			c.getCombat().activatePrayer(19);
			c.getPA().closeAllWindows();
			break;
		case 77111: // 45 mystic
			c.getCombat().activatePrayer(20);
			c.getPA().closeAllWindows();
			break;
		case 2171: // retrubution
			c.getCombat().activatePrayer(21);
			c.getPA().closeAllWindows();
			break;
		case 2172: // redem
			c.getCombat().activatePrayer(22);
			c.getPA().closeAllWindows();
			break;
		case 2173: // smite
			c.getCombat().activatePrayer(23);
			c.getPA().closeAllWindows();
			break;
		case 77113: // chiv
			c.getCombat().activatePrayer(24);
			c.getPA().closeAllWindows();
			break;
		case 77115: // piety
			c.getCombat().activatePrayer(25);
			c.getPA().closeAllWindows();
			break;

		}
	}
}
