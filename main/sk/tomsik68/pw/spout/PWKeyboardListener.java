/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
/*    */ package sk.tomsik68.pw.spout;
/*    */ 
/*    */ import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
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