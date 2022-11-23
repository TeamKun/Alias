package net.kunmc.lab.alias.config;

import net.kunmc.lab.configlib.BaseConfig;
import net.kunmc.lab.configlib.value.map.UUID2StringMapValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Config extends BaseConfig {
    // UUID, Player名のaliasを保持
    public net.kunmc.lab.configlib.value.map.UUID2StringMapValue playerAlias = new UUID2StringMapValue();

    public Config(@NotNull Plugin plugin) {
        super(plugin);
    }

    @Override
    public void saveConfig() {
        super.saveConfig();
    }

    @Override
    public boolean loadConfig() {
        super.loadConfig();
        return true;
    }
}
