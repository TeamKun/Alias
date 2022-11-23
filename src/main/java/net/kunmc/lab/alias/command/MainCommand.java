package net.kunmc.lab.alias.command;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.configlib.ConfigCommand;

public class MainCommand extends Command {
    public MainCommand(ConfigCommand configCommand){
        super("alias");
        addChildren(new SetNameCommand(), new ResetNameCommand(), configCommand);
    }
}
