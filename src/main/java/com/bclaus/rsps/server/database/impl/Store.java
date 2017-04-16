package com.bclaus.rsps.server.database.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import com.bclaus.rsps.server.vd.player.Player;

public class Store {

	private static final String SECRET = "utiih76PmkycPC"; //YOUR SECRET KEY!
	
	@SuppressWarnings("deprecation")
	public void claim(Player player){
		if(player.getItems().freeSlots() < 10) {
			player.sendMessage("You need at least 10 free inventory slots to claim purchased items.");
			return;
		}
		if(player.getSqlTimer().elapsed() <= 30000) {
			player.sendMessage("You can only do this once every 30 seconds.");
			return;
		}
		try{
			player.getSqlTimer().reset();
			URL url = new URL("http://DemonRsps.net/donate/callback.php?secret="+SECRET+"&username="+URLEncoder.encode(player.getName().toLowerCase().replaceAll(" ", "_")));
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String string = null;
			loop: while((string = reader.readLine()) != null) {
				switch(string.toUpperCase()) {
				case "ERROR":
					player.sendMessage("An error occured while claiming your items.");
					break loop;
				case "NO_RESULTS":
					player.sendMessage("There were no results found while claiming items.");
					break loop;
				default:
					String[] split = string.split(",");
					int quantity = Integer.parseInt(split[1]);
					switch(split[0]) {
					/** DONATOR RANKS **/
					case "Regular":
						player.getItems().addItem(6830, quantity);
						break;
					case "Extreme":
						player.getItems().addItem(6831, quantity);
						break;
					case "VIP":
						player.getItems().addItem(6833, quantity);
						break;
					case "Premium":
						player.getItems().addItem(6832, quantity);
						break;

						/** SPIRIT SHIELDS **/
					case "divine_spirit_shield":
						player.getItems().addItem(15000, quantity);
						break;
					case "elysian_spirit_shield":
						player.getItems().addItem(15002, quantity);
						break;
					case "arcane_spirit_shield":
						player.getItems().addItem(14999, quantity);
						break;
					case "spectral_spirit_shield":
						player.getItems().addItem(15003, quantity);
						break;

		

					case "amulet_of_fury":
						player.getItems().addItem(6585, quantity);
						break;
						
						/** Third age melee set**/
						
					case "third_age_melee_set":
						player.getItems().addItem(10346, quantity);
						player.getItems().addItem(10348, quantity);
						player.getItems().addItem(10350, quantity);
						player.getItems().addItem(10352, quantity);
						break;

						/** 3RD AGE MAGE SET **/
					case "third_age_mage_set":
						player.getItems().addItem(10338, quantity);
						player.getItems().addItem(10340, quantity);
						player.getItems().addItem(10342, quantity);
						player.getItems().addItem(10344, quantity);
						break;
						
					case "bandos_armour":
						player.getItems().addItem(11724, quantity);
						player.getItems().addItem(11726, quantity);
						player.getItems().addItem(11728, quantity);
						break;
						
					case "armadyl_armour":
						player.getItems().addItem(11718, quantity);
						player.getItems().addItem(11720, quantity);
						player.getItems().addItem(11722, quantity);						break;

						/** 3RD AGE RANGE SET **/
					case "third_age_range_set":
						player.getItems().addItem(10330, quantity);
						player.getItems().addItem(10332, quantity);		
						player.getItems().addItem(10334, quantity);	
						player.getItems().addItem(10336, quantity);
						break;
						
						/** GUILDED ARMOUR**/
						case "gilded_armour":
						player.getItems().addItem(3481, quantity);
						player.getItems().addItem(3483, quantity);		
						player.getItems().addItem(3488, quantity);	
						player.getItems().addItem(3486, quantity);
						break;
						
						/**WEAPONS**/

					case "toxic_blowpipe":
						player.getItems().addItem(14614, quantity);
						break;
						
						case "tentacle_whip":
						player.getItems().addItem(15052, quantity);
						break;
						
						case "toxic_staff_of_the_dead":
						player.getItems().addItem(12904, quantity);
						break;
						
						case "staff_of_the_dead":
						player.getItems().addItem(12460, quantity);
						break;
						
					case "trident_of_the_seas":
						player.getItems().addItem(12345, quantity);
						break;
						
						case "abyssal_dagger":
						player.getItems().addItem(13047, quantity);
						break;
						
						case "lime_whip":
						player.getItems().addItem(15343, quantity);
						break;
						
						
						/** SUPPLIES**/
						
						case "raw_manta_ray":
						player.getItems().addItem(392, 20000);
						break;
						
						case "manta_ray":
						player.getItems().addItem(391, 20000);
						break;
						
						case "shark":
						player.getItems().addItem(386, 20000);
						break;
						
						case "raw_shark":
						player.getItems().addItem(384, 20000);
						break;
						
						case "pure_essence":
						player.getItems().addItem(7937, 20000);
						break;
						
						case "magic_logs":
						player.getItems().addItem(1514, 20000);
						break;
						
						case "rune_dart":
						player.getItems().addItem(811, 20000);
						break;
						
						case "cannon_ball":
						player.getItems().addItem(2, 20000);
						break;
						

						
					case "ring_of_wealth":
						player.getItems().addItem(2572, quantity);
						break;
						
						case "mystery_box":
						player.getItems().addItem(6199, 50);
						break;
						
						case "ring_of__charos_(a)":
						player.getItems().addItem(2572, quantity);
						break;
						
						case "rainbow_parthat":
						player.getItems().addItem(15598, quantity);
						break;
						
						case "black_partyhat":
						player.getItems().addItem(15997, quantity);
						break;
						

						/** CHRISTMAS CRACKER **/
					case "christmas_cracker":
						player.getItems().addItem(962, quantity);
						break;

						/** BLUE PHAT **/
					case "blue_partyhat":
						player.getItems().addItem(1042, quantity);
						break;

						/** WHITE PHAT **/
					case "white_partyhat":
						player.getItems().addItem(1048, quantity);
						break;

						/** RED PHAT **/
					case "red_partyhat":
						player.getItems().addItem(1038, quantity);
						break;

						/** GREEN PARTYHAT **/
					case "green_partyhat":
						player.getItems().addItem(1044, quantity);
						break;

						/** YELLOW PARTYHAT **/
					case "yellow_partyhat":
						player.getItems().addItem(1040, quantity);
						break;

						/** PURPLE PARTYHAT **/
					case "purple_partyhat":
						player.getItems().addItem(1046, quantity);
						break;

						/** SANTA HAT **/
					case "santa_hat":
						player.getItems().addItem(1050, quantity);
						break;

						/** RED HWEEN MASK **/
					case "red_halloween_mask":
						player.getItems().addItem(1057, quantity);
						break;

						/** BLUE HWEEN MASK **/
					case "blue_halloween_mask":
						player.getItems().addItem(1055, quantity);
						break;

						/** GREEN HWEEN MASK **/
					case "green_halloween_mask":
						player.getItems().addItem(1053, quantity);
						break;


						/** PARTYHAT SET **/
					case "partyhat_set":
						for(int phat : new int[]{1048, 1038, 1044, 1040, 1046, 1042}) {
							player.getItems().addItem(phat, quantity);
						}
						break;

						/** THIRD AGE SET **/

						/** SPIRIT SHIELD SET **/
					case "spirit_shield_set":
						int[] shields = new int[]{13740, 13742, 13738, 13744};
						for(int s : shields)
							player.getItems().addItem(s, quantity);
						break;


					}
					continue loop;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			player.sendMessage("Currently not available.");
		}
	}					
}
