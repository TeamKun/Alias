package net.kunmc.lab.alias.util;

import net.kunmc.lab.alias.Alias;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class AliasUtil {
    private AliasUtil() {
    }

    @Nullable
    public static Player getPlayer(String nameOrAlias) {
        Player p = getPlayerFromName(nameOrAlias);
        if (p != null) {
            return p;
        }
        return getPlayerFromAlias(nameOrAlias);
    }

    @Nullable
    public static Player getPlayerFromAlias(String alias) {
        return Alias.getPlugin().config.playerAlias.keySet()
                                                   .stream()
                                                   .filter(x -> alias.equals(Alias.getPlugin().config.playerAlias.get(x)))
                                                   .findFirst()
                                                   .map(AliasUtil::getPlayerFromUUID)
                                                   .orElse(null);
    }

    @Nullable
    public static Player getPlayerFromUUID(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid)
                     .getPlayer();
    }

    @Nullable
    public static Player getPlayerFromName(String name) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                     .filter(x -> Objects.equals(x.getName(), name))
                     .findFirst()
                     .map(OfflinePlayer::getPlayer)
                     .orElse(null);
    }
}
