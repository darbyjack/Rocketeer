package me.glaremasters.rocketeer;

import co.aikar.commands.PaperCommandManager;
import me.glaremasters.rocketeer.cmds.CommandRocketeer;
import me.glaremasters.rocketeer.handler.RocketHandler;
import me.glaremasters.rocketeer.listener.RocketListener;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Rocketeer extends JavaPlugin {

	private PaperCommandManager manager;
	private NamespacedKey       key;
	private RocketHandler rocketHandler;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		this.key = new NamespacedKey(this, "rocketeer");
		this.rocketHandler = new RocketHandler(this);
		this.rocketHandler.loadData();
		this.manager = new PaperCommandManager(this);
		this.manager.getCommandCompletions().registerCompletion("rockets", context -> this.rocketHandler.getRocketNames());
		this.manager.registerCommand(new CommandRocketeer());
		getServer().getPluginManager().registerEvents(new RocketListener(this), this);
	}

	@Override
	public void onDisable() {
		try {
			this.rocketHandler.saveData();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PaperCommandManager getManager() {
		return manager;
	}

	public NamespacedKey getKey() {
		return key;
	}

	public RocketHandler getRocketHandler() {
		return rocketHandler;
	}

}
