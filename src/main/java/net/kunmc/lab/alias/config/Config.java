package net.kunmc.lab.alias.config;

import net.kunmc.lab.alias.alias.AliasOperation;
import net.kunmc.lab.configlib.BaseConfig;
import net.kunmc.lab.configlib.value.map.UUID2StringMapValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Config extends BaseConfig {
    // UUID, Player名のaliasを保持
    public UUID2StringMapValue playerAlias = new UUID2StringMapValue().onModify(x -> {
        x.forEach((k, v) -> {
            Player p = Bukkit.getPlayer(k);
            if (p != null) {
                AliasOperation.setPlayerName(p, v);
            }
        });
    });

    public Config(@NotNull Plugin plugin) {
        super(plugin);
    }
}
