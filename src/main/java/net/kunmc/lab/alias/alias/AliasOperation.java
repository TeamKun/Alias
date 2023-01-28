package net.kunmc.lab.alias.alias;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.injector.PacketConstructor;
import net.kunmc.lab.alias.Alias;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

public class AliasOperation {
    public static void resetPlayerName(List<Player> players) {
        players.forEach(p -> {
            NickAPI.resetNick(p);
            NickAPI.refreshPlayer(p);
        });

        players.forEach(p -> {
            p.setDisplayName(p.getName());
        });
        updateScoreboardTeams();
    }

    public static void setPlayerName(Player player, String name) {
        if (NickAPI.getName(player)
                   .equals(name)) {
            return;
        }
        NickAPI.nick(player, name);
        NickAPI.refreshPlayer(player);

        player.setDisplayName(name);
        updateScoreboardTeams();
    }

    public static void applyConfigPlayerName(Player player) {
        if (!Alias.getPlugin().config.playerAlias.containsKey(player.getUniqueId())) {
            return;
        }

        setPlayerName(player, Alias.getPlugin().config.playerAlias.get(player.getUniqueId()));
    }

    /**
     * @implNote Aliasクラスのパケット編集に依存している
     */
    private static void updateScoreboardTeams() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        Bukkit.getScoreboardManager()
              .getMainScoreboard()
              .getTeams()
              .forEach(x -> {
                  try {
                      Collection<String> entries = x.getEntries();
                      Field teamField = x.getClass()
                                         .getDeclaredField("team");
                      teamField.setAccessible(true);
                      Object nmsTeam = teamField.get(x);
                      PacketConstructor constructor = protocolManager.createPacketConstructor(PacketType.Play.Server.SCOREBOARD_TEAM,
                                                                                              nmsTeam,
                                                                                              entries,
                                                                                              3);

                      for (Player p : Bukkit.getOnlinePlayers()) {
                          protocolManager.sendServerPacket(p, constructor.createPacket(nmsTeam, entries, 4));
                          protocolManager.sendServerPacket(p, constructor.createPacket(nmsTeam, entries, 3));
                      }
                  } catch (InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
                      throw new RuntimeException(e);
                  }
              });
    }
}
