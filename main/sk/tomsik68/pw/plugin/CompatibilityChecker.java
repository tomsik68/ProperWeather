package sk.tomsik68.pw.plugin;

import java.util.List;

import org.bukkit.entity.Entity;

import sk.tomsik68.bukkitbp.v1.ClassCriteria;
import sk.tomsik68.bukkitbp.v1.PackageResolver;
import sk.tomsik68.bukkitbp.v1.ReflectionUtils;

/**
 * Hard-coded criteria for bukkit safeguard bypassing
 * 
 * @author Tomsik68
 * 
 */
public class CompatibilityChecker {
    public static void test() throws Exception {
        // WorldServer exists
        if (PackageResolver.getMinecraftClass("WorldServer") == null)
            throw new ClassNotFoundException("WorldServer not found.");
        // WorldServer has field 'entityList' : List
        ClassCriteria crit = new ClassCriteria("WorldServer", false);
        crit.addFieldRule("entityList", List.class);
        if (!crit.matches(PackageResolver.getMinecraftClass("WorldServer")))
            throw new NoSuchFieldException("No entityList in WorldServer");
        // CraftWorld exists
        if (PackageResolver.getBukkitClass("CraftWorld") == null)
            throw new ClassNotFoundException("CraftWorld not found.");
        // CraftWorld has method getHandle() : net.minecraft.server.WorldServer
        crit = new ClassCriteria("CraftWorld", true);
        crit.addMethodRule("getHandle", PackageResolver.getMinecraftClass("WorldServer"));
        if (!crit.matches(PackageResolver.getBukkitClass("CraftWorld")))
            throw new NoSuchMethodException("No getHandle in CraftWorld");
        // Entity exists
        if (PackageResolver.getMinecraftClass("Entity") == null)
            throw new ClassNotFoundException("Entity not found.");
        // Entity has method getBukkitEntity() : org.bukkit.entities.Entity
        crit = new ClassCriteria("Entity", false);
        crit.addMethodRule("getBukkitEntity", PackageResolver.getBukkitClass("entity.CraftEntity"));
        if (!crit.matches(PackageResolver.getMinecraftClass("Entity")))
            throw new NoSuchMethodException("No getBukkitEntity in Entity");
        // EntityPlayer exists
        if (PackageResolver.getMinecraftClass("EntityPlayer") == null)
            throw new ClassNotFoundException("EntityPlayer not found.");
        // CraftPlayer exists
        if (PackageResolver.getBukkitClass("entity.CraftPlayer") == null)
            throw new ClassNotFoundException("entity.CraftPlayer not found.");
        // CraftPlayer has method getHandle() : net.minecraft.server.Player
        crit = new ClassCriteria("CraftPlayer", true);
        crit.addMethodRule("getHandle", PackageResolver.getMinecraftClass("EntityPlayer"));
        if (!crit.matches(PackageResolver.getBukkitClass("entity.CraftPlayer")))
            throw new NoSuchMethodException("No getHandle in CraftPlayer");
        // Packet exists
        if (PackageResolver.getMinecraftClass("Packet") == null)
            throw new ClassNotFoundException("Packet not found.");
        // PlayerConnection exists
        if (PackageResolver.getMinecraftClass("PlayerConnection") == null)
            throw new ClassNotFoundException("PlayerConnection not found.");
        // PlayerConnection has method sendPacket(Packet)
        crit = new ClassCriteria("PlayerConnection", false);
        crit.addMethodRule("sendPacket", null, PackageResolver.getMinecraftClass("Packet"));
        if (!crit.matches(PackageResolver.getMinecraftClass("PlayerConnection")))
            throw new NoSuchMethodException("No sendPacket in PlayerConnection");
        if (PackageResolver.getMinecraftClass("Packet70Bed") == null)
            throw new ClassNotFoundException("Packet70Bed not found.");
        // Player has field netServerHandler :
        // net.minecraft.server.NetServerHandler
        crit = new ClassCriteria("EntityPlayer", false);
        crit.addFieldRule("playerConnection", PackageResolver.getMinecraftClass("PlayerConnection"));
        if (!crit.matches(PackageResolver.getMinecraftClass("EntityPlayer")))
            throw new NoSuchFieldException("No playerConnection in EntityPlayer");
    }
}
