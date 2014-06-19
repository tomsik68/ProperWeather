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
package sk.tomsik68.pw.api;

import java.util.HashMap;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public abstract interface WeatherDefaults extends ConfigurationSerializable {
    public abstract int getDefMaxDuration();

    public abstract String[] getDefCantBeAfter();

    public abstract int getDefRandomTimeProbability();

    public abstract int getDefProbability();

    public abstract String[] getDefElements();

    public HashMap<String, Object> getCustomNodes();

    public abstract int getMinDuration();
}