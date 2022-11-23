package net.kunmc.lab.alias;
import lombok.Getter;
import net.kunmc.lab.alias.command.MainCommand;
import net.kunmc.lab.alias.config.Config;
import net.kunmc.lab.alias.listener.PlayerEvent;
import net.kunmc.lab.commandlib.CommandLib;
import net.kunmc.lab.configlib.ConfigCommand;
import net.kunmc.lab.configlib.ConfigCommandBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public final class Alias extends JavaPlugin{

    @Getter
    private static Alias plugin;

    public Config config;

    @Override
    public void onEnable() {
        plugin = this;
        // Config
        config = new Config(this);
        config.loadConfig();

        // Command
        ConfigCommand configCommand = new ConfigCommandBuilder(config).build();
        CommandLib.register(this, new MainCommand(configCommand));

        // Event
        getServer().getPluginManager().registerEvents(new PlayerEvent(), plugin);
    }

    @Override
    public void onDisable() {
    }
}