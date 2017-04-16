package com.bclaus.rsps.server.util;

import java.util.Objects;

import com.bclaus.rsps.server.vd.mobile.PoisonCombatTask;
import com.bclaus.rsps.server.vd.mobile.PoisonType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The {@link JsonLoader} implementation that loads all weapons that poison
 * players.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class NPCPoisonLoader extends JsonLoader {

    /**
     * Creates a new {@link NPCPoisonLoader}.
     */
    public NPCPoisonLoader() {
		super("./Data/npc_poison.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
		int id = reader.get("npc-id").getAsInt();
        PoisonType type = Objects.requireNonNull(PoisonType.valueOf(reader.get("type").getAsString()));
		PoisonCombatTask.NPC_TYPES.put(id, type);
    }
}