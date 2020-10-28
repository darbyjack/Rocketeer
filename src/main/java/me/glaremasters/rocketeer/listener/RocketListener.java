package me.glaremasters.rocketeer.listener;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import me.glaremasters.rocketeer.Rocketeer;
import me.glaremasters.rocketeer.utils.RocketUtils;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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
	public void onLaunch(final PlayerLaunchProjectileEvent event) {
		final Projectile projectile = event.getProjectile();
		final ItemStack itemStack = event.getItemStack();
		if (!(projectile instanceof Firework)) {
			return;
		}
		if (!RocketUtils.isCustomRocket(itemStack)) {
			return;
		}
		projectile.getPersistentDataContainer().set(rocketeer.getRegularKey(), PersistentDataType.STRING, RocketUtils.getImage(itemStack));
		if (!RocketUtils.hasHeight(itemStack)) {
			return;
		}
		projectile.getPersistentDataContainer().set(rocketeer.getHeightKey(), PersistentDataType.INTEGER, RocketUtils.getHeight(itemStack));
	}

	@EventHandler
	public void onExplode(final FireworkExplodeEvent event) {
		final Firework entity = event.getEntity();
		if (!entity.getPersistentDataContainer().has(rocketeer.getRegularKey(), PersistentDataType.STRING)) {
			return;
		}
		try {
			RocketUtils.explode(entity, rocketeer);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
