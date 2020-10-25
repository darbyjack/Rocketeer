package me.glaremasters.rocketeer.listener;

import me.glaremasters.rocketeer.Rocketeer;
import me.glaremasters.rocketeer.utils.RocketUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

/**
 * Created by Glare
 * Date: 10/25/2020
 * Time: 12:04 PM
 */
public class RocketListener implements Listener {
	private final Rocketeer rocketeer;

	public RocketListener(final Rocketeer rocketeer) {
		this.rocketeer = rocketeer;
	}

	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}
		final Action action = event.getAction();
		if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) {
			return;
		}
		final Player player = event.getPlayer();
		final ItemStack item = player.getInventory().getItemInMainHand();
		if (item.getType() != Material.FIREWORK_ROCKET) {
			return;
		}

		try {
			RocketUtils.launchRocketEncoded(item, player, rocketeer);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
