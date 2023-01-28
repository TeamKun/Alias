package net.kunmc.lab.alias.listener;

import net.kunmc.lab.alias.alias.AliasOperation;
import net.kunmc.lab.alias.config.Config;
import net.kunmc.lab.alias.util.DecolationConst;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlayerEvent implements Listener {
    private final Config config;

    public PlayerEvent(Config config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        AliasOperation.applyConfigPlayerName(event.getPlayer());
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
                    targetPlayer.add(getPlayerFromUUID(uuid));
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
            Player distPlayer = getPlayerFromAlias(messages[1]);
            if (distPlayer != null) {
                player.teleport(distPlayer.getLocation());
                e.setCancelled(true);
            }
            return;
        }

        // エンティティをエンティティへtpさせるパターン
        if (messages.length == 3) {
            Player srcPlayer = getPlayerFromAlias(messages[1]);
            Player distPlayer = getPlayerFromAlias(messages[2]);

            if (srcPlayer == null && distPlayer != null) {
                srcPlayer = getPlayerFromName(messages[1]);
                if (srcPlayer != null) {
                    srcPlayer.teleport(distPlayer.getLocation());
                    e.setCancelled(true);
                }
                return;
            }

            if (srcPlayer != null && distPlayer == null) {
                distPlayer = getPlayerFromName(messages[2]);
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
            Player distPlayer = getPlayerFromAlias(messages[1]);
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
            Player sentTargetPlayer = getPlayerFromAlias(messages[1]);
            if (sentTargetPlayer != null) {
                sentTargetPlayer.sendMessage(DecolationConst.GRAY + e.getPlayer()
                                                                     .getName() + "にささやかれました: " + messages[2] + DecolationConst.RESET);
                e.getPlayer()
                 .sendMessage(DecolationConst.GRAY + messages[1] + "にささやきました: " + messages[2] + DecolationConst.RESET);
                e.setCancelled(true);
            }
        }
    }

    @Nullable
    private Player getPlayerFromAlias(String alias) {
        return config.playerAlias.keySet()
                                 .stream()
                                 .filter(x -> alias.equals(config.playerAlias.get(x)))
                                 .findFirst()
                                 .map(this::getPlayerFromUUID)
                                 .orElse(null);
    }

    @Nullable
    private Player getPlayerFromUUID(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid)
                     .getPlayer();
    }

    @Nullable
    private Player getPlayerFromName(String name) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                     .filter(x -> Objects.equals(x.getName(), name))
                     .findFirst()
                     .map(OfflinePlayer::getPlayer)
                     .orElse(null);
    }
}
