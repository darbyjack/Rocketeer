package me.glaremasters.rocketeer.cmds;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import me.glaremasters.rocketeer.utils.RocketUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

/**
 * Created by Glare
 * Date: 10/23/2020
 * Time: 7:02 PM
 */
@CommandAlias("rocketeer")
public class CommandRocketeer extends BaseCommand {


	@Subcommand("create")
	public void onCreate(final Player player, final String url) throws IOException {
		final ItemStack stack = RocketUtils.encodeRocket(url, new ItemStack(Material.FIREWORK_ROCKET));
		player.getInventory().addItem(stack);
	}
}
