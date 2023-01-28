package net.kunmc.lab.alias.command;

import net.kunmc.lab.alias.Alias;
import net.kunmc.lab.alias.alias.AliasOperation;
import net.kunmc.lab.alias.config.Config;
import net.kunmc.lab.alias.util.AliasUtil;
import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.argument.PlayersArgument;
import org.bukkit.entity.Player;

import java.util.Collections;


public class ResetNameCommand extends Command {
    public ResetNameCommand() {
        super("resetname");
        Config config = Alias.getPlugin().config;

        argument(new PlayersArgument("target", option -> {
            option.suggestionAction(sb -> {
                      config.playerAlias.values()
                                        .forEach(sb::suggest);
                  })
                  .additionalParser((ctx, input) -> {
                      Player p = AliasUtil.getPlayerFromAlias(input);
                      if (p == null) {
                          return null;
                      }
                      return Collections.singletonList(p);
                  });
        }), (players, ctx) -> {
            players.forEach(p -> {
                Alias.getPlugin().config.playerAlias.remove(p.getUniqueId());
            });
            AliasOperation.resetPlayerName(players);

            ctx.sendSuccess("指定されたプレイヤーの名前を元に戻しました");
        });
    }
}
