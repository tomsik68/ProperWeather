/*     */package sk.tomsik68.pw.spout;

/*     */
/*     */import org.bukkit.entity.Player;
/*     */
import org.getspout.spoutapi.SpoutManager;
/*     */
import org.getspout.spoutapi.player.SpoutPlayer;
/*     */
import sk.tomsik68.pw.api.WeatherController;
/*     */
import sk.tomsik68.pw.impl.DefaultWeatherController;
/*     */
import sk.tomsik68.pw.plugin.ProperWeather;
/*     */
import sk.tomsik68.pw.region.Region;

/*     */
/*     */public class SpoutWeatherController extends DefaultWeatherController
/*     */
/*     */{
    /*     */private java.awt.Color sky;
    /*     */private java.awt.Color clouds;
    /*     */private java.awt.Color fog;
    /*     */private int sunSize;
    /*     */private int moonSize;
    /*     */private int starFrequency;
    /*     */private boolean moon;
    /*     */private boolean cloudsVisible;
    /*     */private boolean sun;
    /*     */private boolean stars;
    /*     */private int cloudsHeight;

    /*     */
    /*     */public SpoutWeatherController(Region region1)
    /*     */{
        /* 32 */super(region1);
        /* 33 */sky = ProperWeather.defaultSkyColor;
        /* 34 */clouds = (fog = java.awt.Color.white);
        /* 35 */sunSize = 100;
        /* 36 */moonSize = 100;
        /* 37 */starFrequency = 35;
        /* 38 */moon = true;
        /* 39 */cloudsVisible = true;
        /* 40 */sun = true;
        /* 41 */stars = true;
        /* 42 */cloudsHeight = 110;
        /*     */}

    /*     */
    /*     */public SpoutWeatherController(DefaultWeatherController defaultWeatherController)
    /*     */{
        /* 47 */this(defaultWeatherController.getRegion());
        /*     */
        /* 49 */setRaining(defaultWeatherController.isRaining());
        /*     */}

    /*     */
    /*     */public void setSkyColor(java.awt.Color color)
    /*     */{
        /* 54 */sky = color;
        /* 55 */Player[] players = region.getPlayers();
        /* 56 */for (Player p : players) {
            /* 57 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 58 */if (player.isSpoutCraftEnabled())
                /* 59 */SpoutManager.getSkyManager().setSkyColor(player, convert(sky));
            /*     */}
        /*     */}

    /*     */
    /*     */public java.awt.Color getSkyColor()
    /*     */{
        /* 65 */return sky;
        /*     */}

    /*     */
    /*     */public void setFogColor(java.awt.Color color)
    /*     */{
        /* 70 */fog = color;
        /* 71 */Player[] players = region.getPlayers();
        /* 72 */for (Player p : players) {
            /* 73 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 74 */if (player.isSpoutCraftEnabled())
                /* 75 */SpoutManager.getSkyManager().setFogColor(player, convert(fog));
            /*     */}
        /*     */}

    /*     */
    /*     */public java.awt.Color getFogColor()
    /*     */{
        /* 81 */return fog;
        /*     */}

    /*     */
    /*     */public int getSunSize()
    /*     */{
        /* 86 */return sunSize;
        /*     */}

    /*     */
    /*     */public void setSunSize(int pcent)
    /*     */{
        /* 91 */sunSize = pcent;
        /* 92 */Player[] players = region.getPlayers();
        /* 93 */for (Player p : players) {
            /* 94 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 95 */if (player.isSpoutCraftEnabled())
                /* 96 */SpoutManager.getSkyManager().setSunSizePercent(player, sunSize);
            /*     */}
        /*     */}

    /*     */
    /*     */public void setStarFrequency(int pcent)
    /*     */{
        /* 102 */starFrequency = pcent;
        /* 103 */Player[] players = region.getPlayers();
        /* 104 */for (Player p : players) {
            /* 105 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 106 */if (player.isSpoutCraftEnabled())
                /* 107 */SpoutManager.getSkyManager().setStarFrequency(player, starFrequency);
            /*     */}
        /*     */}

    /*     */
    /*     */public void setMoonSize(int pcent)
    /*     */{
        /* 113 */moonSize = pcent;
        /* 114 */Player[] players = region.getPlayers();
        /* 115 */for (Player p : players) {
            /* 116 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 117 */if (player.isSpoutCraftEnabled())
                /* 118 */SpoutManager.getSkyManager().setMoonSizePercent(player, pcent);
            /*     */}
        /*     */}

    /*     */
    /*     */public int getMoonSize()
    /*     */{
        /* 124 */return moonSize;
        /*     */}

    /*     */
    /*     */public int getStarFrequency()
    /*     */{
        /* 129 */return starFrequency;
        /*     */}

    /*     */
    /*     */public void hideMoon()
    /*     */{
        /* 134 */setMoon(false);
        /*     */}

    /*     */
    /*     */public void showMoon()
    /*     */{
        /* 139 */setMoon(true);
        /*     */}

    /*     */
    /*     */public void hideSun()
    /*     */{
        /* 144 */setSun(false);
        /*     */}

    /*     */
    /*     */public void showSun()
    /*     */{
        /* 149 */setSun(true);
        /*     */}

    /*     */
    /*     */public void hideStars()
    /*     */{
        /* 154 */stars = false;
        /* 155 */Player[] players = region.getPlayers();
        /* 156 */for (Player p : players) {
            /* 157 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 158 */if (player.isSpoutCraftEnabled())
                /* 159 */SpoutManager.getSkyManager().setStarsVisible(player, stars);
            /*     */}
        /*     */}

    /*     */
    /*     */public void showStars()
    /*     */{
        /* 165 */stars = true;
        /* 166 */Player[] players = region.getPlayers();
        /* 167 */for (Player p : players) {
            /* 168 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 169 */if (player.isSpoutCraftEnabled())
                /* 170 */SpoutManager.getSkyManager().setStarsVisible(player, stars);
            /*     */}
        /*     */}

    /*     */
    /*     */public boolean isStars()
    /*     */{
        /* 176 */return stars;
        /*     */}

    /*     */
    /*     */public boolean isClouds()
    /*     */{
        /* 181 */return cloudsVisible;
        /*     */}

    /*     */
    /*     */public void showClouds()
    /*     */{
        /* 186 */cloudsVisible = true;
        /* 187 */Player[] players = region.getPlayers();
        /* 188 */for (Player p : players) {
            /* 189 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 190 */if (player.isSpoutCraftEnabled())
                /* 191 */SpoutManager.getSkyManager().setCloudsVisible(player, cloudsVisible);
            /*     */}
        /*     */}

    /*     */
    /*     */public void hideClouds()
    /*     */{
        /* 197 */cloudsVisible = false;
        /* 198 */Player[] players = region.getPlayers();
        /* 199 */for (Player p : players) {
            /* 200 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 201 */if (player.isSpoutCraftEnabled())
                /* 202 */SpoutManager.getSkyManager().setCloudsVisible(player, cloudsVisible);
            /*     */}
        /*     */}

    /*     */
    /*     */public java.awt.Color getCloudsColor()
    /*     */{
        /* 208 */return clouds;
        /*     */}

    /*     */
    /*     */public void setCloudsColor(java.awt.Color color)
    /*     */{
        /* 213 */clouds = color;
        /* 214 */Player[] players = region.getPlayers();
        /* 215 */for (Player p : players) {
            /* 216 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 217 */if (player.isSpoutCraftEnabled())
                /* 218 */SpoutManager.getSkyManager().setCloudColor(player, convert(color));
            /*     */}
        /*     */}

    /*     */
    /*     */public void setCloudsHeight(int h)
    /*     */{
        /* 224 */cloudsHeight = h;
        /* 225 */Player[] players = region.getPlayers();
        /* 226 */for (Player p : players) {
            /* 227 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 228 */if (player.isSpoutCraftEnabled())
                /* 229 */SpoutManager.getSkyManager().setCloudHeight(player, h);
            /*     */}
        /*     */}

    /*     */
    /*     */public int getCloudsHeight()
    /*     */{
        /* 235 */return cloudsHeight;
        /*     */}

    /*     */
    /*     */public void setMoon(boolean moon1)
    /*     */{
        /* 243 */moon1 = moon1;
        /* 244 */Player[] players = region.getPlayers();
        /* 245 */for (Player p : players) {
            /* 246 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 247 */if (player.isSpoutCraftEnabled())
                /* 248 */SpoutManager.getSkyManager().setMoonVisible(player, moon1);
            /*     */}
        /*     */}

    /*     */
    /*     */public boolean isMoon()
    /*     */{
        /* 256 */return moon;
        /*     */}

    /*     */
    /*     */public void setSun(boolean sun1)
    /*     */{
        /* 264 */sun1 = sun1;
        /* 265 */Player[] players = region.getPlayers();
        /* 266 */for (Player p : players) {
            /* 267 */SpoutPlayer player = SpoutManager.getPlayer(p);
            /* 268 */if (player.isSpoutCraftEnabled())
                /* 269 */SpoutManager.getSkyManager().setSunVisible(player, sun1);
            /*     */}
        /*     */}

    /*     */
    /*     */public boolean isSun()
    /*     */{
        /* 278 */return sun;
        /*     */}

    /*     */
    /*     */private static org.getspout.spoutapi.gui.Color convert(java.awt.Color color) {
        /* 282 */return new org.getspout.spoutapi.gui.Color(color.getRed(), color.getGreen(), color.getBlue());
        /*     */}

    /*     */
    /*     */public void clear()
    /*     */{
        /* 288 */super.clear();
        /* 289 */setCloudsColor(java.awt.Color.white);
        /* 290 */setFogColor(java.awt.Color.white);
        /* 291 */setSunSize(100);
        /* 292 */setMoonSize(100);
        /* 293 */setSkyColor(ProperWeather.defaultSkyColor);
        /* 294 */showMoon();
        /* 295 */showSun();
        /* 296 */showClouds();
        /*     */}

    /*     */
    /*     */public boolean isMoonVisible()
    /*     */{
        /* 301 */return moon;
        /*     */}

    /*     */
    /*     */public void setFogDistance(int d)
    /*     */{
        /*     */}

    /*     */
    /*     */public int getFogDistance()
    /*     */{
        /* 310 */return 0;
        /*     */}

    /*     */
    /*     */public Region getRegion()
    /*     */{
        /* 315 */return region;
        /*     */}

    /*     */
    /*     */public void update()
    /*     */{
        /* 320 */super.update();
        /* 321 */setCloudsColor(clouds);
        /* 322 */setCloudsHeight(cloudsHeight);
        /* 323 */setFogColor(fog);
        /* 324 */setMoon(moon);
        /* 325 */setMoonSize(moonSize);
        /* 326 */setSkyColor(sky);
        /* 327 */setStarFrequency(starFrequency);
        /* 328 */setSun(sun);
        /* 329 */setSunSize(sunSize);
        /*     */}
    /*     */
}

/*
 * Location: C:\Downloads\ProperWeather.jar Qualified Name:
 * sk.tomsik68.pw.spout.SpoutWeatherController JD-Core Version: 0.6.0
 */