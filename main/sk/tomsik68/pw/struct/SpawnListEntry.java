/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
package sk.tomsik68.pw.struct;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class SpawnListEntry {
	private final Location location;
	private final Class<? extends Entity> entityClass;
	private final Vector velocity;

	public SpawnListEntry(Location l, Class<? extends Entity> c, Vector v) {
		this.location = l;
		this.entityClass = c;
		this.velocity = v;
	}

	public Location getLocation() {
		return this.location;
	}

	public Vector getVelocity() {
		return this.velocity;
	}

	public Class<? extends Entity> getEntityClass() {
		return this.entityClass;
	}
}