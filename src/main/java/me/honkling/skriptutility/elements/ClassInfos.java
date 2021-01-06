package me.honkling.skriptutility.elements;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.plugin.Plugin;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

public class ClassInfos {

    static {
        Classes.registerClass(new ClassInfo<>(Plugin.class, "plugin")
                .user("plugins?")
                .name("Plugin")
                .description("Represents a plugin.")
                .examples("command /contains:", "\ttrigger:", "\t\tsend \"true\" if loaded plugins contains Vault")
                .defaultExpression(new EventValueExpression<>(Plugin.class))
                .parser(new Parser<Plugin>() {

                    @Override
                    @Nullable
                    public Plugin parse(String input, ParseContext context) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return true;
                    }

                    @Override
                    public String toVariableNameString(Plugin plugin) {
                        return plugin.getName();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return "(.*)";
                    }

                    @Override
                    public String toString(Plugin plugin, int flags) {
                        return toVariableNameString(plugin);
                    }

                    public Fields serialize(Plugin plugin) throws NotSerializableException {
                        Fields fields = new Fields();
                        fields.putPrimitive("name", plugin.getName());
                        fields.putPrimitive("path", plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
                        return fields;
                    }

                    public Plugin deserialize(Fields fields) throws StreamCorruptedException {
                        return null;
                    }

                    public void deserialize(Plugin plugin, Fields fields) throws StreamCorruptedException {
                        assert false;
                    }

                }));
    }

}