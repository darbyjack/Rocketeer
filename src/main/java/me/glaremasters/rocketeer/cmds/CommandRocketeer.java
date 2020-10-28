package me.glaremasters.rocketeer.cmds;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.annotation.Values;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.glaremasters.rocketeer.Rocketeer;
import me.glaremasters.rocketeer.messages.Messages;
import me.glaremasters.rocketeer.utils.RocketUtils;
import org.apache.commons.lang.StringUtils;
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
@CommandPermission("rocketeer.admin")
public class CommandRocketeer extends BaseCommand {

	@Dependency
	Rocketeer rocketeer;

	@Subcommand("create")
	@Syntax("<url>")
	public void onCreate(final Player player, @Single final String url) {
		try {
			final ItemStack stack = RocketUtils.encodeRocket(url, new ItemStack(Material.FIREWORK_ROCKET));
			player.getInventory().addItem(stack);
			getCurrentCommandIssuer().sendInfo(Messages.CREATE__SUCCESS, "{url}", url);
		}
		catch (IOException ex) {
			ex.printStackTrace();
			getCurrentCommandIssuer().sendInfo(Messages.CREATE__ERROR);
		}
	}

	@Subcommand("height")
	@Syntax("<height>")
	public void onSetHeight(final Player player, final int height) {
		ItemStack stack = player.getInventory().getItemInMainHand();
		stack = RocketUtils.setHeight(stack, height);
		player.getInventory().setItemInMainHand(stack);
		getCurrentCommandIssuer().sendInfo(Messages.HEIGHT_SUCCESS, "{height}", String.valueOf(height));
	}

	@Subcommand("give")
	@Syntax("<player> <rocket>")
	@CommandCompletion("@players @rockets")
	public void onGive(final CommandIssuer issuer, final OnlinePlayer onlinePlayer, @Single @Values("@rockets") final String name) {
		final Player player = onlinePlayer.getPlayer();
		player.getInventory().addItem(rocketeer.getRocketHandler().getRocket(name));
		getCurrentCommandIssuer().sendInfo(Messages.GIVE__SUCCESS, "{player}", player.getName(), "{rocket}", name);
	}

	@Subcommand("save")
	@Syntax("<name>")
	public void onSave(final Player player, @Single final String name) {
		if (rocketeer.getRocketHandler().hasRocket(name)) {
			getCurrentCommandIssuer().sendInfo(Messages.SAVE__ALREADY_EXISTS);
			return;
		}
		final ItemStack stack = player.getInventory().getItemInMainHand();
		rocketeer.getRocketHandler().addRocket(name, stack);
		getCurrentCommandIssuer().sendInfo(Messages.SAVE__SUCCESS, "{rocket}", name);
	}

	@Subcommand("list")
	public void onList(CommandIssuer issuer) {
		getCurrentCommandIssuer().sendInfo(Messages.LIST__DISPLAY, "{rockets}", StringUtils.join(rocketeer.getRocketHandler().getRocketNames(), ", "));
	}

	@Subcommand("remove")
	@Syntax("<name>")
	@CommandCompletion("@rockets")
	public void onRemove(final CommandIssuer issuer, @Single @Values("@rockets") final String name) {
		rocketeer.getRocketHandler().removeRocket(name);
		getCurrentCommandIssuer().sendInfo(Messages.REMOVE__SUCCESS, "{rocket}", name);
	}

}
