package me.honkling.skriptutility.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.google.inject.Inject;
import jdk.internal.jline.internal.Nullable;
import me.honkling.skriptutility.SkriptUtility;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.HashSet;
import java.util.Set;

public class ExprPermissions extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprPermissions.class, String.class, ExpressionType.COMBINED, "[the] perm[ission]s of %player%");
    }

    private Expression<Player> player;
    private Permission perms;
    @Inject
    private SkriptUtility plugin;

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        player = (Expression<Player>) exprs[0];
        perms = plugin.getPermissions();
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Permissions expression with player: " + player.toString(event, debug);
    }

    @Override
    @Nullable
    protected String[] get(Event event) {
        Player p = player.getSingle(event);
        if(p != null) {
            final Set<String> permissions = new HashSet<>();
                for (final PermissionAttachmentInfo permission : p.getEffectivePermissions())
                    permissions.add(permission.getPermission());
            return permissions.toArray(new String[permissions.size()]);
        }
        return null;
    }

    public void change(Event event, String[] delta, ChangeMode mode ) {
        Player p = player.getSingle(event);
        if(p != null) {
            if(mode == ChangeMode.SET) {
                perms.playerRemove(p, "*");
                for(String perm : delta) {
                    perms.playerAdd(p, perm);
                }
            } else if(mode == ChangeMode.ADD) {
                for(String perm : delta) {
                    perms.playerAdd(p, perm);
                }
            } else if(mode == ChangeMode.REMOVE) {
                for(String perm : delta) {
                    perms.playerRemove(p, perm);
                }
            } else if(mode == ChangeMode.REMOVE_ALL) {
                perms.playerRemove(p, "*")
                ;
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode) {
        if(mode == ChangeMode.DELETE || mode == ChangeMode.RESET) return null;
        return CollectionUtils.array(String.class);
    }

}
