package sk.tomsik68.pw.impl.backend;

import org.bukkit.entity.Player;

import sk.tomsik68.bukkitbp.v1.PackageResolver;
import sk.tomsik68.bukkitbp.v1.ReflectionUtils;
import sk.tomsik68.pw.api.IServerBackend;
import sk.tomsik68.pw.plugin.ProperWeather;

public class CB16Backend implements IServerBackend {

    @Override
    public void setRaining(Player p, boolean isRaining) {
        try {
            Object mcPlayer = ReflectionUtils.call(PackageResolver.getBukkitClass("entity.CraftPlayer").cast(p), "getHandle");
            Object connection = ReflectionUtils.get(mcPlayer, "playerConnection");
            Object packet = PackageResolver.getMinecraftClass("Packet70Bed").getConstructor(Integer.TYPE, Integer.TYPE)
                    .newInstance(isRaining ? 1 : 2, 0);
            ReflectionUtils.call(connection, "sendPacket", packet);
        } catch (Exception e) {
            ProperWeather.log.severe("Raining set failed.");
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "CraftBukkit-pre1.7";
    }

    @Override
    public void reset(Player player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setClouds(Player p, boolean clouds) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setCloudsColor(Player p, int cloudsColorRGB) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setCloudsHeight(Player p, int cloudsHeight) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFogColor(Player p, int fogColorRGB) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMoonSize(Player p, int moonSize) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSkyColor(Player p, int skyColorRGB) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setStarFrequency(Player p, int starFrequency) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSunSize(Player p, int sunSize) {
        // TODO Auto-generated method stub
        
    }



}