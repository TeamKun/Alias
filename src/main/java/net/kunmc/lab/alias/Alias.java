package net.kunmc.lab.alias;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import net.kunmc.lab.alias.command.MainCommand;
import net.kunmc.lab.alias.config.Config;
import net.kunmc.lab.alias.listener.PlayerEvent;
import net.kunmc.lab.commandlib.CommandLib;
import net.kunmc.lab.configlib.ConfigCommand;
import net.kunmc.lab.configlib.ConfigCommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Alias extends JavaPlugin {

    @Getter
    private static Alias plugin;

    public Config config;

    @Override
    public void onEnable() {
        plugin = this;
        // Config
        config = new Config(this);

        // Command
        ConfigCommand configCommand = new ConfigCommandBuilder(config).build();
        CommandLib.register(this, new MainCommand(configCommand));

        // Event
        getServer().getPluginManager()
                   .registerEvents(new PlayerEvent(), plugin);

        ProtocolLibrary.getProtocolManager()
                       .addPacketListener(new PacketAdapter(this, PacketType.Play.Server.SCOREBOARD_TEAM) {
                           @Override
                           public void onPacketSending(PacketEvent event) {
                               PacketContainer packet = event.getPacket();
                               ArrayList<String> players = ((ArrayList<String>) packet.getSpecificModifier(Collection.class)
                                                                                      .read(0));
                               List<String> newPlayers = new ArrayList<>();
                               for (String player : players) {
                                   String targetName = player;
                                   for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                                       if (offlinePlayer.getName()
                                                        .equals(player) && Alias.getPlugin().config.playerAlias.containsKey(
                                               offlinePlayer.getUniqueId())) {
                                           targetName = Alias.getPlugin().config.playerAlias.get(offlinePlayer.getUniqueId());
                                           break;
                                       }
                                   }
                                   newPlayers.add(targetName);
                               }
                               packet.getSpecificModifier(Collection.class)
                                     .write(0, newPlayers);
                           }
                       });
    }

    @Override
    public void onDisable() {
    }
}
