package net.kunmc.lab.alias.command;

import net.kunmc.lab.alias.Alias;
import net.kunmc.lab.alias.alias.AliasOperation;
import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandContext;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.List;


public class ResetNameCommand extends Command {
    public ResetNameCommand() {
        super("resetname");
        argument(builder -> {
            builder.playersArgument("target", null, ctx -> {
                List<Player> players = (List<Player>) ctx.getParsedArg(0);
                AliasOperation.resetPlayerName(players);
                sendMessage(ctx);
                players.forEach(p -> {
                    Alias.getPlugin().config.playerAlias.remove(p.getUniqueId());
                });
                Alias.getPlugin().config.saveConfig();
            });
        });
    }

    private void sendMessage(CommandContext ctx) {
        ctx.sendSuccess("指定されたプレイヤーの名前を元に戻しました");
    }
}