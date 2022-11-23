package net.kunmc.lab.alias.command;

import net.kunmc.lab.alias.Alias;
import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.argument.StringArgument;
import org.bukkit.entity.Player;
import xyz.haoshoku.nick.api.NickAPI;

public class SetNameCommand extends Command {
    public SetNameCommand() {
        super("setname");
        argument(builder -> {
            builder.playerArgument("target")
                    .stringArgument("name", StringArgument.Type.PHRASE, null, ctx -> {
                        Player player = (Player) ctx.getParsedArg(0);
                        String name = ctx.getParsedArg("name", String.class);
                        setPlayerName(player, name);
                        sendMessage(ctx, player, name);
                        Alias.getPlugin().config.playerAlias.put(player.getUniqueId(), name);
                        Alias.getPlugin().config.saveConfig();
                    });
        });
    }

    private void setPlayerName(Player player, String name) {
        NickAPI.nick(player, name);
        NickAPI.refreshPlayer(player);
    }

    private void sendMessage(CommandContext ctx, Player player, String name) {
        ctx.sendSuccess(player.getName() + "の名前を" + name + "に変更しました");
    }
}
