package com.bclaus.rsps.server.vd.content.dialogue.teleport;
 
import com.bclaus.rsps.server.vd.content.dialogue.Dialogue;
import com.bclaus.rsps.server.vd.content.dialogue.Type;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.world.Position;
 
public class MinigameTeleportDialogue extends Dialogue
{
       
        /**
        * An array for all the dialogue strings.
        */
        private static final String[] OPTION_1 =
        {
                "Barrows",
                "Castle Wars",
                "Duel Arena",
                "Pest Control",
                "[More]"
        };
       
        private static final String[] OPTION_2 =
        {
                "Barbarian Assault",
                "Jad & TzHaar Caves",
                "Warriors Guild",
                "[Back]"
        };
       
        /**
        * An array for all corresponding dialogue strings which holds all of the teleport locations.
        */
        private static final int[][] OPTION_1_TELEPORT =
        {
                { 3565, 3313, 0 }, //Barrows
                { 2441 , 3091, 0 }, //Castle Wars
                { 3366, 3266, 0 }, //Duel Arena
                { 2662, 2650, 0 }, //Pest Control
                { 0, 0, 0 } //More
        };
       
        private static final int[][] OPTION_2_TELEPORT =
        {
                { 2605, 3153, 0 }, //Barbarian Assault
                { 2444, 5170, 0 }, //TzHaar Minigames
                { 2846, 3543, 0 }, //Warriors Guild
                { 0, 0, 0 } //Back
        };
       
        @Override
        protected void start(Object... parameters)
        {
                send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTION_1[0], OPTION_1[1], OPTION_1[2], OPTION_1[3], OPTION_1[4]);
                phase = 0;
        }
       
        @Override
        public void select(int index){ 
                if (phase == 0)
                {
                        if (index == 5)
                        {
                                phase = 1;
                                send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTION_2[0], OPTION_2[1], OPTION_2[2], OPTION_2[3]);
                        } else
                        {
                                TeleportExecutor.teleport(player, new Position(OPTION_1_TELEPORT[index - 1][0], OPTION_1_TELEPORT[index - 1][1], OPTION_1_TELEPORT[index - 1][2]));
                        }
                } else if (phase == 1)
                {
                        if (index == 4)
                        {
                                phase = 0;
                                send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTION_1[0], OPTION_1[1], OPTION_1[2], OPTION_1[3],  OPTION_1[4]);
                        } else {
                                TeleportExecutor.teleport(player, new Position(OPTION_2_TELEPORT[index - 1][0], OPTION_2_TELEPORT[index - 1][1], OPTION_2_TELEPORT[index - 1][2]));
                        }
                }
        }
}