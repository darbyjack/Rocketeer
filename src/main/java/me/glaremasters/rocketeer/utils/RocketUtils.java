package me.glaremasters.rocketeer.utils;

import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.glaremasters.rocketeer.Rocketeer;
import me.trysam.imagerenderer.api.ParticleImageRenderingAPI;
import me.trysam.imagerenderer.util.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

/**
 * Created by Glare
 * Date: 10/23/2020
 * Time: 7:05 PM
 */
public class RocketUtils {

	/**
	 * Handles encoding an ItemStack with NBT data of the image
	 *
	 * @param url        the url of the image
	 * @param sourceItem the item to apply the NBT to
	 * @return ItemStack with applied NBT data
	 * @throws IOException occurs when can't reach the image
	 */
	public static ItemStack encodeRocket(final String url, final ItemStack sourceItem) throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final BufferedImage sourceImage = ImageIO.read(new URL(url));
		final BufferedImage tempImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
		final Graphics2D graphics = tempImage.createGraphics();

		graphics.drawImage(sourceImage, 0, 0, 64, 64, null);
		graphics.dispose();

		ImageIO.write(tempImage, "png", outputStream);

		final String encoded = Base64.getEncoder().encodeToString(outputStream.toByteArray());
		return NBTEditor.set(sourceItem, encoded, "rocket");
	}


	/**
	 * Handles launching the encoded rocket to make it seem like a real rocket
	 *
	 * @param itemStack the itemstack to check
	 * @param player    the player that launched it
	 * @param rocketeer plugin instance
	 * @throws IOException
	 */
	public static void launchRocketEncoded(final ItemStack itemStack, final Player player, final Rocketeer rocketeer) throws IOException {
		final Location start = player.getLocation().add(2.0, 3.0, 2.0);
		final Location end = player.getLocation().add(0.0, 20.0, 0.0);

		XParticle.line(start, end, 3.0, new ParticleDisplay(Particle.FIREWORKS_SPARK, start));
		player.getWorld().playSound(player.getLocation(), XSound.ENTITY_FIREWORK_ROCKET_LAUNCH.parseSound(), 1.0f, 1.0f);

		final BufferedImage image = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(NBTEditor.getString(itemStack, "rocket"))));
		Bukkit.getScheduler().runTaskLater(rocketeer, () -> {
			final ParticleImageRenderingAPI renderingAPI = new ParticleImageRenderingAPI(image, end);
			renderingAPI.rotate(Axis.Z, 180.0f);
			renderingAPI.renderImage(Bukkit.getOnlinePlayers());
			player.getWorld().playSound(player.getLocation(), XSound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST.parseSound(), 1.0f, 1.0f);
		}, 20L);
	}

}
