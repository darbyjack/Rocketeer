package me.glaremasters.rocketeer.cmds;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.annotation.Values;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.glaremasters.rocketeer.Rocketeer;
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
	@Dependency Rocketeer rocketeer;

	@Subcommand("create")
	@Syntax("<url>")
	public void onCreate(final Player player, final String url) throws IOException {
		final ItemStack stack = RocketUtils.encodeRocket(url, new ItemStack(Material.FIREWORK_ROCKET));
		player.getInventory().addItem(stack);
	}

	@Subcommand("give")
	@Syntax("<player> <rocket>")
	@CommandCompletion("@players @rockets")
	public void onGive(final CommandIssuer issuer, final OnlinePlayer onlinePlayer, final String name) {
		onlinePlayer.getPlayer().getInventory().addItem(rocketeer.getRocketHandler().getRocket(name));
	}

	@Subcommand("save")
	@Syntax("<name>")
	public void onSave(final Player player, final String name) {
		final ItemStack stack = player.getInventory().getItemInMainHand();
		rocketeer.getRocketHandler().addRocket(name, stack);
	}

	@Subcommand("remove")
	@Syntax("<name>")
	@CommandCompletion("@rockets")
	public void onRemove(final CommandIssuer issuer, @Values("@rockets") final String name) {
		rocketeer.getRocketHandler().removeRocket(name);
	}

}
