package net.kunmc.lab.alias.command;

import net.kunmc.lab.alias.Alias;
import net.kunmc.lab.alias.alias.AliasOperation;
import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.argument.PlayersArgument;


public class ResetNameCommand extends Command {
    public ResetNameCommand() {
        super("resetname");
       
        argument(new PlayersArgument("target"), (players, ctx) -> {
            players.forEach(p -> {
                Alias.getPlugin().config.playerAlias.remove(p.getUniqueId());
            });
            AliasOperation.resetPlayerName(players);

            ctx.sendSuccess("指定されたプレイヤーの名前を元に戻しました");
        });
    }
}
