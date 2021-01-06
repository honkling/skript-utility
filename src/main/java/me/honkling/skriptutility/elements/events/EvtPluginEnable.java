package me.honkling.skriptutility.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Checker;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.event.Event;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class EvtPluginEnable extends SkriptEvent {

    static {
        Skript.registerEvent("On Plugin Enable", EvtPluginEnable.class, PluginEnableEvent.class, "[on] [plugin] enable [of %plugin%]");
    }

    Literal<Plugin> plugins;

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parser) {
        plugins = (Literal<Plugin>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if(plugins != null) {
            Plugin plugin = ((PluginEnableEvent)e).getPlugin();
            return plugins.check(e, new Checker<Plugin>() {
                @Override
                public boolean check(Plugin data) {
                    return data == plugin;
                }
            });
        }
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Plugin enable event " + plugins.toString(e, debug);
    }

}
