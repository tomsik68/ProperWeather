package sk.tomsik68.pw.api;

import org.bukkit.Server;

/**
 * IServerBackendMathcher matches server against a backend.
 * It uses various strategies to recognize vanilla CB, MCPC, Spigot etc.
 * and then creates backend which is the best for that server.
 */
public interface IServerBackendMatcher {
    public boolean matches(Server server);

    public IServerBackend createBackend(Server server);
}
