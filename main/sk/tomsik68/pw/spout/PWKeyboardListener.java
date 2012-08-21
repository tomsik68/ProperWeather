/*    */ package sk.tomsik68.pw.spout;
/*    */ 
/*    */ import org.getspout.spoutapi.event.input.KeyBindingEvent;
/*    */ import org.getspout.spoutapi.gui.ScreenType;
/*    */ import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
/*    */ 
/*    */ public class PWKeyboardListener
/*    */   implements BindingExecutionDelegate
/*    */ {
/*    */   private final SpoutModule sm;
/*    */ 
/*    */   public PWKeyboardListener(SpoutModule spoutModule)
/*    */   {
/* 24 */     this.sm = spoutModule;
/*    */   }
/*    */ 
/*    */   public void keyPressed(KeyBindingEvent kbe)
/*    */   {
/* 29 */     if (kbe.getScreenType() != ScreenType.GAME_SCREEN) return;
/* 30 */     this.sm.openWindow(kbe.getPlayer());
/*    */   }
/*    */ 
/*    */   public void keyReleased(KeyBindingEvent kbe)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Downloads\ProperWeather.jar
 * Qualified Name:     sk.tomsik68.pw.spout.PWKeyboardListener
 * JD-Core Version:    0.6.0
 */