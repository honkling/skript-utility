package me.honkling.skriptutility.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.io.File;

import static org.bukkit.Bukkit.getServer;

public class ExprLoadedPlugins extends SimpleExpression<Plugin> {

    static {
        Skript.registerExpression(ExprLoadedPlugins.class, Plugin.class, ExpressionType.SIMPLE, "[all [of the]] loaded plugins");
    }

    private final Server server = getServer();
    private final SimpleCommandMap cMap = new SimpleCommandMap(server);
    private final SimplePluginManager manager = new SimplePluginManager(server, cMap);

    @Override
    public Class<? extends Plugin> getReturnType() {
        return Plugin.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Loaded plugins expression";
    }

    @Override
    @Nullable
    protected Plugin[] get(Event event) {
        return manager.getPlugins();
    }

    public void change(Event event, String[] delta, ChangeMode mode) {
        if(mode == ChangeMode.ADD) {
            for (String name : delta) {
                Plugin plugin = manager.getPlugin(name);
                if (plugin == null || plugin.isEnabled()) continue;
                File file = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
                try {
                    manager.loadPlugin(file);
                } catch (InvalidPluginException e) {
                    e.printStackTrace();
                }
            }
        } else if(mode == ChangeMode.REMOVE) {
            for (String name: delta) {
                Plugin plugin = manager.getPlugin(name);
                if(plugin == null || !plugin.isEnabled()) continue;
                manager.disablePlugin(plugin);
            }
        } else if(mode == ChangeMode.DELETE) {
            manager.clearPlugins();
        } else if(mode == ChangeMode.RESET || mode == ChangeMode.REMOVE_ALL) {
            manager.disablePlugins();
        } else if(mode == ChangeMode.SET) {
            manager.disablePlugins();
            for (String name : delta) {
                Plugin plugin = manager.getPlugin(name);
                if(plugin == null) continue;
                File file = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
                try {
                    manager.loadPlugin(file);
                } catch (InvalidPluginException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode) {
        return CollectionUtils.array(Plugin.class);
    }

}
