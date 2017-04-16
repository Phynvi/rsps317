package com.bclaus.rsps.server.vd.world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.items.GroundItem;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.items.ItemList;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.account_type.Account;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.util.Misc;

/**
 * Handles ground items
 */

public class ItemHandler {

    public static List<GroundItem> items = new ArrayList<GroundItem>();
    public static final int HIDE_TICKS = 250;

    public ItemHandler() {
        for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
            ItemList[i] = null;
        }
        loadItemList("item.cfg");
        loadItemPrices("prices.txt");
    }

    public void reloadAllItems() {
        for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
            ItemList[i] = null;
        }
        loadItemList("item.cfg");
        loadItemPrices("prices.txt");
    }

    /**
     * Adds item to list
     */
    public void addItem(GroundItem item) {
        items.add(item);
    }

    /**
     * Removes item from list
     */
    public void removeItem(GroundItem item) {
        items.remove(item);
    }

    /**
     * Item amount
     */
    public int itemAmount(String name, int itemId, int itemX, int itemY) {
        for (GroundItem i : items) {
            if (i.hideTicks >= 1 && i.getName().equalsIgnoreCase(name)) {
                if (i.getItemId() == itemId && i.getItemX() == itemX && i
                    .getItemY() == itemY) {
                    return i.getItemAmount();
                }
            } else if (i.hideTicks < 1) {
                if (i.getItemId() == itemId && i.getItemX() == itemX && i
                    .getItemY() == itemY) {
                    return i.getItemAmount();
                }
            }
        }
        return 0;
    }

    /**
     * Item exists
     */
    public boolean itemExists(int itemId, int itemX, int itemY) {
        for (GroundItem i : items) {
            if (i.getItemId() == itemId && i.getItemX() == itemX && i
                .getItemY() == itemY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Item exists
     */
    public boolean itemExists(int itemId, int itemX, int itemY, int height) {
        for (GroundItem i : items)
            if (i.getItemId() == itemId && i.getItemX() == itemX && i
                .getItemY() == itemY && height == i.getHeightLevel())
                return true;
        return false;
    }
    
    public static void reloadItems(Player c) {
        for (GroundItem i : items) {
            if (c != null) {
                if (!c.getAccount().getType().unownedDropsVisible() && !i.getName().equalsIgnoreCase(c.playerName))
                	continue;
                if (c.getItems().tradeable(i.getItemId()) || i.getName()
                    .equalsIgnoreCase(c.playerName)) {
                    if (c.distanceToPoint(i.getItemX(), i.getItemY()) <= 60 && c
                        .getHeight() == i.getHeightLevel()) {
                        if (i.hideTicks > 0 && i.getName().equalsIgnoreCase(
                            c.playerName)) {
                            c.getItems().removeGroundItem(i.getItemId(),
                                i.getItemX(), i.getItemY(), i.getHeightLevel());
                            c.getItems().createGroundItem(i.getItemId(),
                                i.getItemX(), i.getItemY(), i.getHeightLevel(),
                                i.getItemAmount());
                        }
                        if (i.hideTicks == 0) {
                            c.getItems().removeGroundItem(i.getItemId(),
                                i.getItemX(), i.getItemY(), i.getHeightLevel());
                            c.getItems().createGroundItem(i.getItemId(),
                                i.getItemX(), i.getItemY(), i.getHeightLevel(),
                                i.getItemAmount());
                        }
                    }
                }
            }
        }
    }

    public void process() {

        CopyOnWriteArrayList<GroundItem> toRemove = new CopyOnWriteArrayList<GroundItem>();
        for (GroundItem item : items) {
            try {
                if (item != null) {
                    if (item.hideTicks > 0) {
                        item.hideTicks--;
                    }
                    if (item.hideTicks == 1) { // item can now be seen by
                        item.hideTicks = 0;
                        createGlobalItem(item);
                        item.removeTicks = HIDE_TICKS; // this is stupid your doing 500 ticks = 5 minutes to remove an item
                    }
                    if (item.removeTicks > 0) {
                        item.removeTicks--;
                    }
                    if (item.removeTicks == 1) {
                        item.removeTicks = 0;
                        toRemove.add(item);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (GroundItem i : toRemove) {
            try {
                removeGlobalItem(i, i.getItemId(), i.getItemX(), i.getItemY(),
                    i.getHeightLevel(), i.getItemAmount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates the ground item
     */
    public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 },
            { 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 }, { 4720, 4896 },
            { 4718, 4890 }, { 4722, 4902 }, { 4732, 4932 }, { 4734, 4938 },
            { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 }, { 4726, 4914 },
            { 4728, 4920 }, { 4730, 4926 }, { 4745, 4956 }, { 4747, 4962 },
            { 4749, 4968 }, { 4751, 4974 }, { 4753, 4980 }, { 4755, 4986 },
            { 4757, 4992 }, { 4759, 4998 } };

    public void createGroundItem(Player c, int itemId, int itemX, int itemY,
        int heightLevel, int itemAmount) {
        if (itemId > 0) {
        	
            if (itemId > 4705 && itemId < 4760) {
                for (int[] brokenBarrow : brokenBarrows) {
                    if (brokenBarrow[0] == itemId) {
                        itemId = brokenBarrow[1];
                        break;
                    }
                }
            }
            
            if (!Item.itemStackable[itemId] && itemAmount > 0) {
                for (int j = 0; j < itemAmount; j++) {
                    c.getItems().createGroundItem(itemId, itemX, itemY,
                        heightLevel, 1);
                    GroundItem item = new GroundItem(itemId, itemX, itemY,
                        heightLevel, 1, HIDE_TICKS, c.playerName);
                    addItem(item);
                }
            } else {
                c.getItems().createGroundItem(itemId, itemX, itemY,
                    heightLevel, itemAmount);
                GroundItem item = new GroundItem(itemId, itemX, itemY,
                    heightLevel, itemAmount, HIDE_TICKS,
                    c.playerName);
                addItem(item);

            }
        }
    }

    /**
     * Shows items for everyone who is within 60 squares
     */
    public void createGlobalItem(GroundItem i) {
		for (Player person : World.PLAYERS) {
            if (person != null) {
            	String myType = person.getAccount().getType().alias();
                if (!person.playerName.equalsIgnoreCase(i.getName())) {
                	//if the player we're loading this global item for is not the controller of the item
                	//and the player is an iron man, continue to the next possible player
                	if (myType.equals(Account.IRON_MAN_TYPE.alias())) { 
                		continue;
                	}
                	//get the owner of the item by name and see if they are online.
                	Optional<Player> controller = PlayerUpdating.getPlayer(i.getName());
                	if (controller.isPresent()) {
                		String controllerType = controller.get().getAccount().getType().alias();
                		if (myType.equals(Account.REGULAR_TYPE.alias()) && controllerType.equals(Account.IRON_MAN_TYPE.alias())) {
                			continue;
                		}
                	}
                	//if the item is not tradable and the player isn't the controller, then dont show it
                    if (!person.getItems().tradeable(i.getItemId())) {
                        continue;
                    }
                    //if the distance to the point of the item is less than 60 (roughly region size) and
                    //the height of the person is the same as the item, then create the item on the ground.
                    if (person.distanceToPoint(i.getItemX(), i.getItemY()) <= 60 && person
                        .getHeight() == i.getHeightLevel()) {
                        person.getItems().createGroundItem(i.getItemId(),
                            i.getItemX(), i.getItemY(), i.getHeightLevel(),
                            i.getItemAmount());
                    }
                }
            }
        }
    }

    /**
     * Removing the ground item
     */

    public void removeGroundItem(Player c, int itemId, int itemX, int itemY,
        int itemHeight, boolean add) {
        if (!Server.itemHandler.itemExists(itemId, itemX, itemY, itemHeight))
            return;
        for (GroundItem i : items) {
            if (i.getItemId() == itemId && i.getItemX() == itemX && i
                .getItemY() == itemY && itemHeight == i.getHeightLevel()) {
                if (i.hideTicks > 0 && i.getName().equalsIgnoreCase(
                    c.playerName)) {
                    if (add) {
                        if (!c.getItems().specialCase(itemId)) {
                            if (c.getItems().addItem(i.getItemId(),
                                i.getItemAmount())) {
                                removeControllersItem(i, c, i.getItemId(), i
                                    .getItemX(), i.getItemY(), i
                                    .getHeightLevel(), i.getItemAmount());
                                break;
                            }
                        } else {
                            removeControllersItem(i, c, i.getItemId(), i
                                .getItemX(), i.getItemY(), i.getHeightLevel(),
                                i.getItemAmount());
                            break;
                        }
                    } else {
                        if (i != null)
                            removeControllersItem(i, c, i.getItemId(), i
                                .getItemX(), i.getItemY(), i.getHeightLevel(),
                                i.getItemAmount());
                        break;
                    }
                } else if (i.hideTicks <= 0) {
                    if (add) {
                        if (c.getItems().addItem(i.getItemId(),
                            i.getItemAmount())) {
                            removeGlobalItem(i, i.getItemId(), i.getItemX(), i
                                .getItemY(), i.getHeightLevel(), i
                                .getItemAmount());
                            break;
                        }
                    } else {
                        removeGlobalItem(i, i.getItemId(), i.getItemX(), i
                            .getItemY(), i.getHeightLevel(), i.getItemAmount());
                        break;
                    }
                }
            }
        }
    }

    /**
     * Remove item for just the item controller (item not global yet)
     * 
     * @param i
     *            Variable i
     * @param c
     *            Client c
     * @param itemId
     *            The item's id
     * @param itemX
     *            X
     * @param itemY
     *            Y
     * @param itemAmount
     *            Item amount
     */
    public void removeControllersItem(GroundItem i, Player c, int itemId,
        int itemX, int itemY, int itemHeight, int itemAmount) {
        if (i != null && c != null) {
            c.getItems().removeGroundItem(itemId, itemX, itemY, itemHeight);
            removeItem(i);
        }
    }

    /**
     * Remove item for everyone within 60 squares
     */

    public void removeGlobalItem(GroundItem i, int itemId, int itemX,
        int itemY, int heightLevel, int itemAmount) {
		for (Player p : World.PLAYERS) {
            if (p != null) {
                Player person = p;
                if (person != null) {
                    if (person.distanceToPoint(itemX, itemY) <= 60 && person
                        .getHeight() == heightLevel) {
                        person.getItems().removeGroundItem(itemId, itemX,
                            itemY, heightLevel);
                    }
                }
            }
        }
        removeItem(i);
    }

    /**
     * Item List
     */

    public ItemList ItemList[] = new ItemList[Constants.ITEM_LIMIT];

    public void newItemList(int ItemId, String ItemName,
        String ItemDescription, double ShopValue, double LowAlch,
        double HighAlch, int Bonuses[]) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 0; i < 11740; i++) {
            if (ItemList[i] == null) {
                slot = i;
                break;
            }
        }

        if (slot == -1)
            return; // no free slot found
        ItemList newItemList = new ItemList(ItemId);
        newItemList.itemName = ItemName;
        newItemList.itemDescription = ItemDescription;
        newItemList.ShopValue = ShopValue;
        newItemList.LowAlch = LowAlch;
        newItemList.HighAlch = HighAlch;
        newItemList.Bonuses = Bonuses;
        ItemList[slot] = newItemList;
    }

    public void loadItemPrices(String filename) {
        try {
            Scanner s = new Scanner(new File("./data/cfg/" + filename));
            while (s.hasNextLine()) {
                String[] line = s.nextLine().split(" ");
                ItemList temp = getItemList(Integer.parseInt(line[0]));
                if (temp != null)
                    temp.ShopValue = Integer.parseInt(line[1]);
            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ItemList getItemList(int i) {
        for (ItemList aItemList : ItemList) {
            if (aItemList != null) {
                if (aItemList.itemId == i) {
                    return aItemList;
                }
            }
        }
        return null;
    }

    public boolean loadItemList(String FileName) {
        String line = "";
        String token = "";
        String token2 = "";
        String token2_2 = "";
        String[] token3 = new String[10];
        boolean EndOfFile = false;
        int ReadMode = 0;
        BufferedReader characterfile = null;
        try {
            characterfile = new BufferedReader(new FileReader(
                "./data/cfg/" + FileName));
        } catch (FileNotFoundException fileex) {
            Misc.println(FileName + ": file not found.");
            fileex.printStackTrace();
            return false;
        }
        try {
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            Misc.println(FileName + ": error loading file.");
            // return false; Memory Leak
        }
        while (!EndOfFile && line != null) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot);
                token = token.trim();
                token2 = line.substring(spot + 1);
                token2 = token2.trim();
                token2_2 = token2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token3 = token2_2.split("\t");
                if (token.equals("item")) {
                    int[] Bonuses = new int[12];
                    for (int i = 0; i < 12; i++) {
                        if (token3[(6 + i)] != null) {
                            Bonuses[i] = Integer.parseInt(token3[(6 + i)]);
                        } else {
                            break;
                        }
                    }
                    newItemList(Integer.parseInt(token3[0]), token3[1]
                        .replaceAll("_", " "), token3[2].replaceAll("_", " "),
                        Double.parseDouble(token3[4]), Double
                            .parseDouble(token3[4]), Double
                            .parseDouble(token3[6]), Bonuses);
                }
            } else {
                if (line.equals("[ENDOFITEMLIST]")) {
                    try {
                        characterfile.close();
                    } catch (IOException ioexception) {}
                }
            }
            try {
                line = characterfile.readLine();
            } catch (IOException ioexception1) {
                EndOfFile = true;
            }
        }
        try {
            characterfile.close();
        } catch (IOException ioexception) {}
        return false;
    }

}
