package net.kunmc.lab.alias.listener;

import net.kunmc.lab.alias.alias.AliasOperation;
import net.kunmc.lab.alias.config.Config;
import net.kunmc.lab.alias.util.AliasUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CommandListeners implements Listener {
    private final Config config;

    public CommandListeners(Config config) {
        this.config = config;
    }

    @EventHandler
    private void onCommandAlias(PlayerCommandPreprocessEvent e) {
        if (!e.getMessage()
              .startsWith("/alias ")) {
            return;
        }
        String[] messages = e.getMessage()
                             .split(" ");

        if (messages.length == 3 && messages[1].equals("resetname")) {
            List<Player> targetPlayer = new ArrayList<>();
            for (UUID uuid : config.playerAlias.keySet()) {
                if (messages[2].equals(config.playerAlias.get(uuid))) {
                    targetPlayer.add(AliasUtil.getPlayerFromUUID(uuid));
                    break;
                }
            }

            if (targetPlayer.size() != 0) {
                AliasOperation.resetPlayerName(targetPlayer);
                targetPlayer.forEach(p -> {
                    config.playerAlias.remove(p.getUniqueId());
                });
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onCommandTp(PlayerCommandPreprocessEvent e) {
        // tp個別対応しておく
        if (!e.getMessage()
              .startsWith("/tp ")) {
            return;
        }
        Player player = e.getPlayer();
        String[] messages = e.getMessage()
                             .split(" ");

        // エンティティへtpするパターン
        if (messages.length == 2) {
            Player distPlayer = AliasUtil.getPlayerFromAlias(messages[1]);
            if (distPlayer != null) {
                player.teleport(distPlayer.getLocation());
                e.setCancelled(true);
            }
            return;
        }

        // エンティティをエンティティへtpさせるパターン
        if (messages.length == 3) {
            Player srcPlayer = AliasUtil.getPlayerFromAlias(messages[1]);
            Player distPlayer = AliasUtil.getPlayerFromAlias(messages[2]);

            if (srcPlayer == null && distPlayer != null) {
                srcPlayer = AliasUtil.getPlayerFromName(messages[1]);
                if (srcPlayer != null) {
                    srcPlayer.teleport(distPlayer.getLocation());
                    e.setCancelled(true);
                }
                return;
            }

            if (srcPlayer != null && distPlayer == null) {
                distPlayer = AliasUtil.getPlayerFromName(messages[2]);
                if (distPlayer != null) {
                    srcPlayer.teleport(distPlayer.getLocation());
                    e.setCancelled(true);
                }
                return;
            }

            if (distPlayer != null) {
                srcPlayer.teleport(distPlayer.getLocation());
                e.setCancelled(true);
            }
            return;
        }

        // エンティティを特定の座標にtpさせるパターン
        if (messages.length == 5) {
            Player distPlayer = AliasUtil.getPlayerFromAlias(messages[1]);
            if (distPlayer != null) {
                player.teleport(parseCoordinates(e.getPlayer(), Arrays.copyOfRange(messages, 2, 5)));
                e.setCancelled(true);
            }
        }
    }

    private Location parseCoordinates(Player sender, String[] coordinateStrings) {
        if (coordinateStrings.length != 3) {
            throw new IllegalArgumentException();
        }

        Location loc = sender.getLocation();
        double[] coordinates = new double[]{loc.getX(), loc.getY(), loc.getZ()};
        for (int i = 0; i < 3; i++) {
            String s = coordinateStrings[i];
            if (!s.startsWith("~")) {
                coordinates[i] = Double.parseDouble(s);
                continue;
            }

            if (s.length() > 1) {
                coordinates[i] = coordinates[i] + Double.parseDouble(s.substring(1));
            }
        }

        return new Location(sender.getWorld(), coordinates[0], coordinates[1], coordinates[2]);
    }

    @EventHandler
    private void onCommandTell(PlayerCommandPreprocessEvent e) {
        if (!e.getMessage()
              .matches("^/(tell|w|msg)\\s.*")) {
            return;
        }
        String[] messages = e.getMessage()
                             .split(" ");

        if (messages.length == 3) {
            Player sentTargetPlayer = AliasUtil.getPlayerFromAlias(messages[1]);
            if (sentTargetPlayer != null) {
                sentTargetPlayer.sendMessage(ChatColor.GRAY + e.getPlayer()
                                                               .getName() + "にささやかれました: " + messages[2] + ChatColor.RESET);
                e.getPlayer()
                 .sendMessage(ChatColor.GRAY + messages[1] + "にささやきました: " + messages[2] + ChatColor.RESET);
                e.setCancelled(true);
            }
        }
    }
}
