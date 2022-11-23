package net.kunmc.lab.alias.listener;

import net.kunmc.lab.alias.Alias;
import net.kunmc.lab.alias.alias.AliasOperation;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

public class PlayerEvent implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        AliasOperation.applyConfigPlayerName(event.getPlayer());
    }

    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        // tp, giveはよく使うので個別対応しておく
        if (e.getMessage().contains("/tp ")) {
            String[] messages = e.getMessage().split(" ");
            if (messages.length == 2) {
                Player distPlayer = null;
                for (UUID uuid : Alias.getPlugin().config.playerAlias.keySet()) {
                    if (messages[1].equals(Alias.getPlugin().config.playerAlias.get(uuid))) {
                        distPlayer = getPlayerFromUUID(uuid);
                        break;
                    }
                }
                if (distPlayer != null) {
                    player.teleport(distPlayer.getLocation());
                    e.setCancelled(true);
                }
            } else if (messages.length == 3) {
                Player distPlayer = null;
                Player srcPlayer = null;
                for (UUID uuid : Alias.getPlugin().config.playerAlias.keySet()) {
                    if (messages[1].equals(Alias.getPlugin().config.playerAlias.get(uuid))) {
                        srcPlayer = getPlayerFromUUID(uuid);
                    }
                    if (messages[2].equals(Alias.getPlugin().config.playerAlias.get(uuid))) {
                        distPlayer = getPlayerFromUUID(uuid);
                    }
                }
                if (distPlayer == null && srcPlayer != null) {
                } else if (distPlayer != null && srcPlayer == null) {
                    distPlayer = getPlayerFromName(messages[2]);
                    if (distPlayer != null) {
                        srcPlayer.teleport(distPlayer.getLocation());
                        e.setCancelled(true);
                    }
                } else if (distPlayer != null && srcPlayer == null) {
                    srcPlayer = getPlayerFromName(messages[1]);
                    if (srcPlayer != null) {
                        srcPlayer.teleport(distPlayer.getLocation());
                        e.setCancelled(true);
                    }
                } else if (distPlayer != null && srcPlayer != null) {
                    srcPlayer.teleport(distPlayer.getLocation());
                    e.setCancelled(true);
                }
            }
        }
    }

    private Player getPlayerFromUUID(UUID uuid) {
        Player targetPlayer = null;
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getUniqueId().equals(uuid)) {
                targetPlayer = offlinePlayer.getPlayer();
            }
        }
        return targetPlayer;
    }

    private Player getPlayerFromName(String name) {
        Player targetPlayer = null;
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName().equals(name)) {
                targetPlayer = offlinePlayer.getPlayer();
            }
        }
        return targetPlayer;
    }
}
