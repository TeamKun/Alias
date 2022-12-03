package net.kunmc.lab.alias.listener;

import net.kunmc.lab.alias.Alias;
import net.kunmc.lab.alias.alias.AliasOperation;
import net.kunmc.lab.alias.util.DecolationConst;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerEvent implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        AliasOperation.applyConfigPlayerName(event.getPlayer());
    }

    @EventHandler
    private void onCommandAlias(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().contains("/alias ")) {
            String[] messages = e.getMessage().split(" ");
            if (messages.length == 3 && messages[1].equals("resetname")) {
                List<Player> targetPlayer = new ArrayList<>();
                for (UUID uuid : Alias.getPlugin().config.playerAlias.keySet()) {
                    if (messages[2].equals(Alias.getPlugin().config.playerAlias.get(uuid))) {
                        targetPlayer.add(getPlayerFromUUID(uuid));
                        break;
                    }
                }
                if (targetPlayer.size() != 0) {
                    AliasOperation.resetPlayerName(targetPlayer);
                    targetPlayer.forEach(p -> {
                        Alias.getPlugin().config.playerAlias.remove(p.getUniqueId());
                    });
                    Alias.getPlugin().config.saveConfig();
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onCommandTp(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        // tp個別対応しておく
        if (e.getMessage().contains("/tp ")) {
            String[] messages = e.getMessage().split(" ");
            if (messages.length == 2) {
                Player distPlayer = getPlayerFromAlias(messages[1]);
                if (distPlayer != null) {
                    player.teleport(distPlayer.getLocation());
                    e.setCancelled(true);
                }
            } else if (messages.length == 3) {
                Player srcPlayer = getPlayerFromAlias(messages[1]);
                Player distPlayer = getPlayerFromAlias(messages[2]);
                if (distPlayer == null && srcPlayer != null) {
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

    @EventHandler
    private void onCommandTell(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().contains("/tell ") || e.getMessage().contains("/w ") || e.getMessage().contains("/msg ")) {
            String[] messages = e.getMessage().split(" ");
            if (messages.length == 3) {
                Player sentTargetPlayer = getPlayerFromAlias(messages[1]);
                if (sentTargetPlayer != null) {
                    sentTargetPlayer.sendMessage(DecolationConst.GRAY + e.getPlayer().getName() + "にささやかれました: " + messages[2] + DecolationConst.RESET);
                    e.getPlayer().sendMessage(DecolationConst.GRAY + messages[1] + "にささやきました: " + messages[2] + DecolationConst.RESET);
                    e.setCancelled(true);
                }
            }
        }
    }

    private Player getPlayerFromAlias(String alias) {
        Player player = null;
        for (UUID uuid : Alias.getPlugin().config.playerAlias.keySet()) {
            if (alias.equals(Alias.getPlugin().config.playerAlias.get(uuid))) {
                player = getPlayerFromUUID(uuid);
                break;
            }
        }
        return player;
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
