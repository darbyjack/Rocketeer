package me.glaremasters.rocketeer;

import co.aikar.commands.PaperCommandManager;
import me.glaremasters.rocketeer.cmds.CommandRocketeer;
import me.glaremasters.rocketeer.listener.RocketListener;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rocketeer extends JavaPlugin {

	private PaperCommandManager manager;
	private NamespacedKey       key;

	@Override
	public void onEnable() {
		this.key = new NamespacedKey(this, "rocketeer");
		this.manager = new PaperCommandManager(this);
		this.manager.registerCommand(new CommandRocketeer());
		getServer().getPluginManager().registerEvents(new RocketListener(this), this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public PaperCommandManager getManager() {
		return manager;
	}

	public NamespacedKey getKey() {
		return key;
	}

}
