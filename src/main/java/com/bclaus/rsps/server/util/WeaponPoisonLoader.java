package com.bclaus.rsps.server.util;

import java.util.Objects;

import com.bclaus.rsps.server.vd.mobile.PoisonCombatTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.bclaus.rsps.server.vd.mobile.PoisonType;

/**
 * The {@link JsonLoader} implementation that loads all weapons that poison
 * players.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class WeaponPoisonLoader extends JsonLoader {

    /**
     * Creates a new {@link WeaponPoisonLoader}.
     */
    public WeaponPoisonLoader() {
		super("./Data/weapon_poison.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        PoisonType type = Objects.requireNonNull(PoisonType.valueOf(reader.get("type").getAsString()));
		PoisonCombatTask.WEAPON_TYPES.put(id, type);
    }
}