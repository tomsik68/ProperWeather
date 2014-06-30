/*    */package sk.tomsik68.pw.command;

/*    */
/*    */import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/*    */
/*    */public abstract class RunnableCommand
/*    */implements CommandExecutor, Runnable
/*    */{
    /*    */protected CommandSender sender;
    /*    */protected Command command;
    /*    */protected String label;
    /*    */protected String[] args;

    /*    */
    /*    */public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
    /*    */{
        /* 32 */this.sender = arg0;
        /* 33 */this.command = arg1;
        /* 34 */this.label = arg2;
        /* 35 */this.args = arg3;
        /* 36 */run();
        /* 37 */return true;
        /*    */}
    /*    */
}