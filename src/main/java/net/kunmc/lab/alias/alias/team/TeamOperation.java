package net.kunmc.lab.alias.alias.team;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamOperation {
    public static TextColor getPlayerTeamColor(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        TextColor teamColor = null;
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                try {
                    // colorが設定されていないチームを呼ぶとIllegalStateException
                    teamColor = team.color();
                } catch (IllegalStateException e) {
                }
            }
        }
        return teamColor;
    }
}
