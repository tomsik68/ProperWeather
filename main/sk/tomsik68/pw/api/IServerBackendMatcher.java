package sk.tomsik68.pw.api;

import org.bukkit.Server;

public interface IServerBackendMatcher {
    public boolean matches(Server server);

    public IServerBackend createBackend(Server server);
}
