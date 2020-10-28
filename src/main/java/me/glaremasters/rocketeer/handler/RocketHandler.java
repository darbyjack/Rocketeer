package me.glaremasters.rocketeer.handler;

import me.glaremasters.rocketeer.Rocketeer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Glare
 * Date: 10/27/2020
 * Time: 5:11 PM
 */
public class RocketHandler {

	private final ConfigurationSection   section;
	private final File                   rocketFile;
	private final YamlConfiguration      rocketFileConfig;
	private final Map<String, ItemStack> rockets = new HashMap<>();

	public RocketHandler(final Rocketeer rocketeer) {
		rocketeer.saveResource("rockets.yml", false);
		this.rocketFile = new File(rocketeer.getDataFolder(), "rockets.yml");
		this.rocketFileConfig = YamlConfiguration.loadConfiguration(rocketFile);
		this.section = rocketFileConfig.getConfigurationSection("rockets");
	}

	/**
	 * Helper method to add a rocket to the map
	 *
	 * @param name   the name of the rocket
	 * @param rocket the rocket ItemStack
	 */
	public void addRocket(final String name, final ItemStack rocket) {
		rockets.put(name, rocket);
	}

	/**
	 * Helper method to remove a rocket from the map
	 *
	 * @param name the name of the rocket to remove
	 */
	public void removeRocket(final String name) {
		rockets.remove(name);
		section.set(name, null);
	}

	/**
	 * Helper method to get a rocket from the map
	 *
	 * @param name the name of the rocket
	 * @return the rocket from the map
	 */
	public ItemStack getRocket(final String name) {
		return rockets.get(name);
	}

	/**
	 * Helper method to see if a rocket exists or not
	 *
	 * @param name the name of the rocket to check
	 * @return exists or not in the map
	 */
	public boolean hasRocket(final String name) {
		return rockets.containsKey(name);
	}

	/**
	 * Helper method to get all the names of the rockets
	 *
	 * @return names of all of the rockets
	 */
	public List<String> getRocketNames() {
		return new ArrayList<>(rockets.keySet());
	}

	/**
	 * Load the data from the config into the map
	 */
	public void loadData() {
		for (final String key : section.getKeys(false)) {
			this.rockets.put(key, section.getItemStack(key));
		}
	}

	/**
	 * Save the data from the map into the config
	 *
	 * @throws IOException
	 */
	public void saveData() throws IOException {
		for (Map.Entry<String, ItemStack> rocket : rockets.entrySet()) {
			section.set(rocket.getKey(), rocket.getValue());
		}
		rocketFileConfig.save(rocketFile);
	}

}
