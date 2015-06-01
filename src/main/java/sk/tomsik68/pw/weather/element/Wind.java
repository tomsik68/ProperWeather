package sk.tomsik68.pw.weather.element;

import java.awt.geom.Point2D;
import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import sk.tomsik68.pw.api.BaseWeatherElement;
import sk.tomsik68.pw.impl.WeatherController;

public final class Wind extends BaseWeatherElement {
	private final Point2D windSpeed;
	private final int minLayer;
	private static final Random rand = new Random();

	public Wind(WeatherController wc) {
		super(wc);
		windSpeed = new Point2D.Double(rand.nextDouble() * 0.25 - 0.125, rand.nextDouble() * 0.25 - 0.125);
		minLayer = 50 + rand.nextInt(20);
	}

	@Override
	public void activate(WeatherController controller) {
		registerEvent(PlayerMoveEvent.class, EventPriority.NORMAL);
	}

	@Override
	public void deactivate(WeatherController controller) {
		unregisterAll();
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.getPlayer().getLocation().getY() < minLayer || Math.abs(event.getPlayer().getVelocity().getX()) > 1.85d || Math.abs(event.getPlayer().getVelocity().getZ()) > 1.85d || event.getFrom().distanceSquared(event.getTo()) < 0.05d)
			return;
		event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(new Vector(windSpeed.getX(), 0d, windSpeed.getY())));
	}

}
