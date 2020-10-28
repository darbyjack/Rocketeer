package me.glaremasters.rocketeer;

import co.aikar.commands.PaperCommandManager;
import me.glaremasters.rocketeer.cmds.CommandRocketeer;
import me.glaremasters.rocketeer.handler.RocketHandler;
import me.glaremasters.rocketeer.listener.RocketListener;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public final class Rocketeer extends JavaPlugin {

	private PaperCommandManager manager;
	private NamespacedKey       regularKey;
	private NamespacedKey		heightKey;
	private RocketHandler       rocketHandler;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		this.regularKey = new NamespacedKey(this, "rocketeer");
		this.heightKey = new NamespacedKey(this, "rocketHeight");
		this.rocketHandler = new RocketHandler(this);
		this.rocketHandler.loadData();
		this.manager = new PaperCommandManager(this);
		loadLangFile();
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

	public NamespacedKey getRegularKey() {
		return regularKey;
	}

	public RocketHandler getRocketHandler() {
		return rocketHandler;
	}

	private void loadLangFile() {
		final File langFile = new File(this.getDataFolder(), "lang.yml");
		if (!langFile.exists()) {
			this.saveResource("lang.yml", false);
		}
		this.manager.addSupportedLanguage(Locale.ENGLISH);

		try {
			this.manager.getLocales().loadYamlLanguageFile(langFile, Locale.ENGLISH);
		} catch (IOException | InvalidConfigurationException exception) {
			exception.printStackTrace();
		}

		this.manager.getLocales().setDefaultLocale(Locale.ENGLISH);
	}

	public NamespacedKey getHeightKey() {
		return heightKey;
	}

}
