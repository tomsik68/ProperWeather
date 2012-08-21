/*    */ package sk.tomsik68.pw.spout;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.getspout.spoutapi.player.SpoutPlayer;
/*    */ import sk.tomsik68.pw.plugin.ProperWeather;
/*    */ 
/*    */ public class SpoutModule
/*    */ {
/*    */   private ProperWeather plugin;
/* 28 */   private Map<UUID, MainGui> windows = new HashMap();
/*    */ 
/* 30 */   public SpoutModule(ProperWeather instance) { this.plugin = instance;
/*    */   }
/*    */ 
/*    */   public void init()
/*    */   {
/*    */   }
/*    */ 
/*    */   public static boolean verifyPermission(Player player, String p)
/*    */   {
/* 39 */     return ProperWeather.instance().permissions.has(player, p);
/*    */   }
/*    */ 
/*    */   public void openWindow(SpoutPlayer player) {
/* 43 */     if (this.windows.containsKey(player.getUniqueId()))
/* 44 */       ((MainGui)this.windows.get(player.getUniqueId())).setScreen(this.plugin, player.getCurrentScreen());
/*    */     else
/* 46 */       this.windows.put(player.getUniqueId(), new MainGui(player));
/*    */   }
/*    */ }

/* Location:           C:\Downloads\ProperWeather.jar
 * Qualified Name:     sk.tomsik68.pw.spout.SpoutModule
 * JD-Core Version:    0.6.0
 */