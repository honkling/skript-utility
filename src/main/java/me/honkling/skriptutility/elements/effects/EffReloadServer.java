package me.honkling.skriptutility.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.event.Event;

import static org.bukkit.Bukkit.reload;

public class EffReloadServer extends Effect {

    static {
        Skript.registerEffect(EffReloadServer.class, "(reload|rl) [the] server");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Reload server effect";
    }

    @Override
    protected void execute(Event event) {
        reload();
    }

}
