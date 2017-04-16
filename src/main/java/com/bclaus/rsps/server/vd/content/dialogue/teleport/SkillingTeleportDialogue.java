package com.bclaus.rsps.server.vd.content.dialogue.teleport;
 
import com.bclaus.rsps.server.vd.content.dialogue.Dialogue;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.content.dialogue.Type;
import com.bclaus.rsps.server.vd.world.Position;
 
public class SkillingTeleportDialogue extends Dialogue
{
       
        /**
        * An array for all the dialogue strings.
        */
        private static final String[] OPTIONS_1 =
        {
                "Farming Patches",
                "Hunting Grounds",
                "Karamja Jungle",
                "Skilling Area"
        };
       
        /**
        * An array for the corresponding dialogue strings which holds all the teleport locations.
        */
        private static final int[][] OPTIONS_1_TELEPORT =
        {
                { 2818, 3462, 0 },
                { 2956, 2997, 0 },
                { 2924, 3173, 0 },
                { 2849, 3431, 0 }
        };
       
        @Override
        protected void start(Object... parameters)
        {
                send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTIONS_1[0], OPTIONS_1[1], OPTIONS_1[2], OPTIONS_1[3]);
                phase = 0;
        }
       
        @Override
        public void select(int index)
        {
                TeleportExecutor.teleport(player, new Position(OPTIONS_1_TELEPORT[index - 1][0], OPTIONS_1_TELEPORT[index - 1][1], OPTIONS_1_TELEPORT[index - 1][2]));
        }
}