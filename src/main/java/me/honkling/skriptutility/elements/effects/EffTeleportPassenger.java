package me.honkling.skriptutility.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import java.util.Objects;

public class EffTeleportPassenger extends Effect {

    static {
        Skript.registerEffect(EffTeleportPassenger.class, "(teleport|tp) %entities% with passengers to [loc[ation]] %location%");
    }

    private Expression<Entity> entities;
    private Expression<Location> loc;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.entities = (Expression<Entity>) exprs[0];
        this.loc = (Expression<Location>) exprs[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Teleport with passengers effect with expression entities: " + entities.toString(event, debug) + " and location: " + loc.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        if (entities == null) return;
        for (Entity user : entities.getAll(event)) {
            if (loc != null && loc.getSingle(event) != null) {
                Entity[] passengers = user.getPassengers().toArray(new Entity[0]);
                for(Entity passenger : passengers) {
                    passenger.leaveVehicle();
                }
                user.teleport(Objects.requireNonNull(loc.getSingle(event)));
                for(Entity passenger : passengers) {
                    user.addPassenger(passenger);
                }
            }
        }
    }

}
