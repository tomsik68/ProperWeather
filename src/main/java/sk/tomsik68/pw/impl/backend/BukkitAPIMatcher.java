package sk.tomsik68.pw.impl.backend;

import org.bukkit.Server;

import sk.tomsik68.pw.api.IServerBackend;
import sk.tomsik68.pw.api.IServerBackendMatcher;

public class BukkitAPIMatcher implements IServerBackendMatcher {

	@Override
	public boolean matches(Server server) {
		return true;
	}

	@Override
	public IServerBackend createBackend(Server server) {
		return new BukkitAPIBackend();
	}

}
