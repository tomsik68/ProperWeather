package sk.tomsik68.pw.command;

import org.bukkit.World;

import sk.tomsik68.autocommand.context.CommandExecutionContext;
import sk.tomsik68.autocommand.context.ContextParameterProvider;
import sk.tomsik68.autocommand.context.LocatedCommandExecutionContext;

public class WorldFromPlayerProvider implements ContextParameterProvider {

    @Override
    public boolean canProvide(CommandExecutionContext arg0) {
        return (arg0 instanceof LocatedCommandExecutionContext);
    }

    @Override
    public Object provide(CommandExecutionContext arg0, Class<?> arg1, String... arg2) {
        return ((LocatedCommandExecutionContext) arg0).getLocation().getWorld();
    }

    @Override
    public boolean provides(CommandExecutionContext arg0, Class<?> arg1, String... arg2) {
        return arg1.equals(World.class);
    }

}
