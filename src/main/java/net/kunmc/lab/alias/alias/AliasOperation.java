package net.kunmc.lab.alias.alias;

import net.kunmc.lab.alias.Alias;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.List;

public class AliasOperation {
    public static void resetPlayerName(List<Player> players) {
        players.forEach(p -> {
            NickAPI.resetNick(p);
            NickAPI.refreshPlayer(p);
        });

        players.forEach(p -> {
            p.setDisplayName(p.getName());
        });
    }

    public static void setPlayerName(Player player, String name) {
        // ニックネームが既についているなら何もしない
        if (NickAPI.getName(player)
                   .equals(name)) {
            return;
        }
        NickAPI.nick(player, name);
        NickAPI.refreshPlayer(player);

        player.setDisplayName(name);
    }

    public static void applyConfigPlayerName(Player player) {
        if (!Alias.getPlugin().config.playerAlias.containsKey(player.getUniqueId())) {
            return;
        }

        setPlayerName(player, Alias.getPlugin().config.playerAlias.get(player.getUniqueId()));
    }
}
