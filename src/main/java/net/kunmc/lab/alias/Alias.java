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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Alias extends JavaPlugin implements Listener {
    @Getter
    private static Alias plugin;
    private static final Map<UUID, String> playerUUIDToNameMap = new HashMap<>();

    public Config config;

    @Override
    public void onEnable() {
        plugin = this;
        // Config
        config = new Config(this);

        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            playerUUIDToNameMap.put(offlinePlayer.getUniqueId(), offlinePlayer.getName());
        }

        // Command
        ConfigCommand configCommand = new ConfigCommandBuilder(config).build();
        CommandLib.register(this, new MainCommand(configCommand));

        // Event
        getServer().getPluginManager()
                   .registerEvents(new PlayerEvent(), plugin);
        getServer().getPluginManager()
                   .registerEvents(this, plugin);

        ProtocolLibrary.getProtocolManager()
                       .addPacketListener(new PacketAdapter(this, PacketType.Play.Server.SCOREBOARD_TEAM) {
                           @Override
                           public void onPacketSending(PacketEvent event) {
                               PacketContainer packet = event.getPacket();
                               ArrayList<String> names = ((ArrayList<String>) packet.getSpecificModifier(Collection.class)
                                                                                    .read(0));

                               List<String> newNames = new ArrayList<>();
                               for (String name : names) {
                                   String targetName = name;
                                   for (Map.Entry<UUID, String> entry : playerUUIDToNameMap.entrySet()) {
                                       UUID uuid = entry.getKey();
                                       String offlinePlayerName = entry.getValue();

                                       if (Objects.equals(offlinePlayerName,
                                                          name) && Alias.getPlugin().config.playerAlias.containsKey(uuid)) {
                                           targetName = Alias.getPlugin().config.playerAlias.get(uuid);
                                           break;
                                       }
                                   }
                                   newNames.add(targetName);
                               }
                               packet.getSpecificModifier(Collection.class)
                                     .write(0, newNames);
                           }
                       });
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    private void onPlayerLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if (playerUUIDToNameMap.get(p.getUniqueId()) == null) {
            playerUUIDToNameMap.put(e.getPlayer()
                                     .getUniqueId(),
                                    e.getPlayer()
                                     .getName());
        }
    }
}
