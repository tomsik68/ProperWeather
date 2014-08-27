package sk.tomsik68.pw.impl.registry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NameAlreadyBoundException;

import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;

import sk.tomsik68.pw.Util;
import sk.tomsik68.pw.api.IServerBackend;
import sk.tomsik68.pw.api.IServerBackendMatcher;
import sk.tomsik68.pw.api.registry.BaseRegistry;
import sk.tomsik68.pw.impl.backend.*;
import sk.tomsik68.pw.plugin.ProperWeather;

public class ServerBackendMatcherRegistry extends BaseRegistry<IServerBackendMatcher> {

    private String backendName = "";

    @Override
    public void load(File pluginFolder) throws IOException {
        try {
            register("BukkitAPI", new BukkitAPIMatcher());
            //register("CraftBukkit-pre1.7", new CB16BackendMatcher());
        } catch (NameAlreadyBoundException ignore) {
        }
        File cfgFile = new File(pluginFolder, "backend.yml");
        YamlConfiguration cfg;
        if (!cfgFile.exists()) {
            cfg = new YamlConfiguration();
            cfg.set("preferred-backend", "auto");
            cfg.save(cfgFile);
        } else {
            cfg = YamlConfiguration.loadConfiguration(cfgFile);
            backendName = cfg.getString("preferred-backend");
            if (backendName.equalsIgnoreCase("auto")) {
                backendName = "";
            }
        }
    }

    public IServerBackend getBackend(Server server) {
        if (backendName.length() > 0) {
            return get(backendName).createBackend(server);
        }
        ArrayList<String> matching = new ArrayList<String>();
        Collection<String> keys = getRegistered();
        for (String key : keys) {
            if (get(key).matches(server)) {
                matching.add(key);
            }
        }
        if (matching.size() < 1) {
            return null;
        } else {
            if (matching.size() > 1) {
                ProperWeather.log
                        .warning("Multiple backends for your server were found. Please check if the correct one was chosen. If not, please change your configuration. Available backends: "
                                + Util.dumpArray(matching.toArray()));
            }
            return get(matching.get(0)).createBackend(server);
        }

    }

}
