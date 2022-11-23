package net.kunmc.lab.alias.listener;

import net.kunmc.lab.alias.alias.AliasOperation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerEvent implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        AliasOperation.applyConfigPlayerName(event.getPlayer());
    }
}
