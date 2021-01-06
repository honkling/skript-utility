package me.honkling.skriptutility.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

public class CondPluginLoaded extends Condition {

    static {
        Skript.registerCondition(CondPluginLoaded.class, "%plugins% (1¦(is|are)|2¦(is(n't| not)|are(n't| not)) loaded");
    }

    Expression<Plugin> plugin;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.plugin = (Expression<Plugin>) exprs[0];
        setNegated(parser.mark == 1);
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Plugin is loaded- Plugin: " + plugin.toString(event, debug);
    }

    @Override
    public boolean check(Event event) {
        Plugin p = plugin.getSingle(event);
        if(p == null) return isNegated();
        return p.isEnabled() ? isNegated() : !isNegated();
    }

}
