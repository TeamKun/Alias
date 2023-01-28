package net.kunmc.lab.alias.command;

import net.kunmc.lab.alias.Alias;
import net.kunmc.lab.alias.alias.AliasOperation;
import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.argument.PlayerArgument;
import net.kunmc.lab.commandlib.argument.StringArgument;

public class SetNameCommand extends Command {
    public SetNameCommand() {
        super("setname");
       
        argument(new PlayerArgument("target"), new StringArgument("name"), (player, name, ctx) -> {
            Alias.getPlugin().config.playerAlias.put(player.getUniqueId(), name);
            AliasOperation.setPlayerName(player, name);

            ctx.sendSuccess(player.getName() + "の名前を" + name + "に変更しました");
        });
    }
}
