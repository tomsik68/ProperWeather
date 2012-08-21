/*    */ package sk.tomsik68.pw.spout;
/*    */ 
/*    */ import org.getspout.spoutapi.gui.GenericButton;
/*    */ import org.getspout.spoutapi.gui.GenericLabel;
/*    */ import org.getspout.spoutapi.gui.GenericListWidget;
/*    */ import org.getspout.spoutapi.gui.GenericPopup;
/*    */ import org.getspout.spoutapi.gui.ListWidgetItem;
/*    */ import org.getspout.spoutapi.gui.WidgetAnchor;
/*    */ import org.getspout.spoutapi.player.SpoutPlayer;
/*    */ import sk.tomsik68.pw.api.WeatherSystem;
/*    */ import sk.tomsik68.pw.plugin.ProperWeather;
/*    */ import sk.tomsik68.pw.struct.WeatherData;
/*    */ 
/*    */ public class MainGui extends GenericPopup
/*    */ {
/*    */   private WeatherSystem weatherSystem;
/*    */ 
/*    */   public MainGui(WeatherSystem ws, SpoutPlayer player)
/*    */   {
/* 34 */     init();
/* 35 */     this.playerId = player.getEntityId();
/*    */   }
/*    */ 
/*    */   public MainGui(SpoutPlayer player) {
/* 39 */     this(ProperWeather.instance().getWeatherSystem(), player);
/*    */   }
/*    */ 
/*    */   private void init()
/*    */   {
/* 45 */     setWidth(this.maxWidth);
/* 46 */     setHeight(this.maxHeight);
/* 47 */     GenericLabel label = new GenericLabel();
/* 48 */     label.setAlign(WidgetAnchor.TOP_CENTER);
/* 49 */     label.setText("ProperWeather");
/* 50 */     label.setAuto(true);
/* 51 */     attachWidget(null, label);
/* 52 */     GenericButton button = new GenericButton("Manage...");
/* 53 */     button.setAuto(true);
/* 54 */     button.setAlign(WidgetAnchor.BOTTOM_RIGHT);
/* 55 */     attachWidget(null, button);
/* 56 */     GenericListWidget list = new GenericListWidget();
/* 57 */     for (String name : this.weatherSystem.getWorldList()) {
/* 58 */       WeatherData wd = null;
/* 59 */       list.addItem(new ListWidgetItem(name, wd.getCurrentWeather().getClass().getSimpleName().replace("Weather", "") + " (" + (wd.canEverChange() ? "stopped" : "running")));
/*    */     }
/* 61 */     list.setWidth(this.maxWidth / 3);
/* 62 */     list.setHeight(this.maxHeight / 3);
/* 63 */     list.setAnchor(WidgetAnchor.CENTER_CENTER);
/* 64 */     attachWidget(null, list);
/*    */   }
/*    */ 
/*    */   public void onTick()
/*    */   {
/* 72 */     super.onTick();
/*    */   }
/*    */ }

/* Location:           C:\Downloads\ProperWeather.jar
 * Qualified Name:     sk.tomsik68.pw.spout.MainGui
 * JD-Core Version:    0.6.0
 */