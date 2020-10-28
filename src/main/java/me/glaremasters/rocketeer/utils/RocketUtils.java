package me.glaremasters.rocketeer.utils;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.glaremasters.rocketeer.Rocketeer;
import me.trysam.imagerenderer.api.ParticleImageRenderingAPI;
import me.trysam.imagerenderer.util.Axis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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
		final BufferedImage tempImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D graphics = tempImage.createGraphics();

		graphics.drawImage(sourceImage, 0, 0, 64, 64, null);
		graphics.dispose();

		ImageIO.write(tempImage, "png", outputStream);

		final String encoded = Base64.getEncoder().encodeToString(outputStream.toByteArray());
		return NBTEditor.set(sourceItem, encoded, "rocketeer");
	}

	/**
	 * Helper method to check if an ItemStack is a custom rocket or not
	 *
	 * @param itemStack the ItemStack to check
	 * @return custom rocket or not
	 */
	public static boolean isCustomRocket(final ItemStack itemStack) {
		return NBTEditor.contains(itemStack, "rocketeer");
	}

	/**
	 * Explode a firework with an image
	 *
	 * @param firework  the firework to explode
	 * @param rocketeer plugin instance
	 * @throws IOException
	 */
	public static void explode(final Firework firework, final Rocketeer rocketeer) throws IOException {
		final String data = firework.getPersistentDataContainer().get(rocketeer.getKey(), PersistentDataType.STRING);
		final BufferedImage image = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(data)));
		Bukkit.getScheduler().runTaskLater(rocketeer, () -> {
			final ParticleImageRenderingAPI renderingAPI = new ParticleImageRenderingAPI(image, firework.getLocation().add(0.0, 20.0, 0.0));
			renderingAPI.rotate(Axis.Z, 180.0f);
			renderingAPI.renderImage(Bukkit.getOnlinePlayers());
		}, 5L);
	}

	/**
	 * Helper method to get an encoded image from an ItemStack
	 *
	 * @param itemStack the ItemStack to get the image from
	 * @return encoded image as string
	 */
	public static String getImage(final ItemStack itemStack) {
		return NBTEditor.getString(itemStack, "rocketeer");
	}

}
