package me.honkling.skriptutility.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public class ExprUUIDPlayer extends SimpleExpression<Player> {

    static {
        Skript.registerExpression(ExprUUIDPlayer.class, Player.class, ExpressionType.COMBINED, "[the] player from [UUID] %string% ");
    }

    private Expression<String> uuid;

    @Override
    public Class<? extends Player> getReturnType() {
        return Player.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        uuid = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Expression Player from UUID in skript-utility with expression string: " + uuid;
    }

    @Override
    @Nullable
    protected Player[] get(Event event) {
        String uid = uuid.getSingle(event);
        if(uid != null) {
            UUID u = UUID.fromString(uid);
            if(u != null) {
                return new Player[]{getPlayer(u)};
            }
        }
        return null;
    }

}
