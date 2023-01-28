package net.kunmc.lab.alias.command;

import net.kunmc.lab.alias.Alias;
import net.kunmc.lab.alias.alias.AliasOperation;
import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.argument.StringArgument;
import org.bukkit.entity.Player;

public class SetNameCommand extends Command {
    public SetNameCommand() {
        super("setname");
        argument(builder -> {
            builder.playerArgument("target")
                   .stringArgument("name", StringArgument.Type.PHRASE, null, ctx -> {
                       Player player = (Player) ctx.getParsedArg(0);
                       String name = ctx.getParsedArg("name", String.class);
                       AliasOperation.setPlayerName(player, name);
                       sendMessage(ctx, player, name);
                       Alias.getPlugin().config.playerAlias.put(player.getUniqueId(), name);
                   });
        });
    }

    private void sendMessage(CommandContext ctx, Player player, String name) {
        ctx.sendSuccess(player.getName() + "の名前を" + name + "に変更しました");
    }
}
