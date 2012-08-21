package sk.tomsik68.pw.region;

import java.awt.Rectangle;
import java.io.Externalizable;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import sk.tomsik68.pw.struct.WeatherData;

public interface Region extends Comparable<Region>, Iterable<Block>, Externalizable {
    public Player[] getPlayers();

    public World getWorld();
    
    public UUID getWorldId();
    
    public Rectangle getBounds();

    public int getUID();

    public void setUID(int paramInt);

    public boolean contains(Location paramLocation);

    public void updateBlockState(BlockState paramBlockState);

    public void update();

    public void spawnEntity(Class<? extends Entity> paramClass, Location paramLocation, Vector paramVector);
    
    public boolean isProbabilityOn();

    public WeatherData getWeatherData();
}