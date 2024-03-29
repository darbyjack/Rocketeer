/*
 * Bukkit Image Renderer - A simple plugin to display images in Minecraft via particles.
 * Copyright (c) 2020. Samuel Eichelmann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package me.trysam.imagerenderer.particle;

import com.mojang.math.Vector3f;
import me.trysam.imagerenderer.math.Quaternion;
import me.trysam.imagerenderer.math.Vec3d;
import me.trysam.imagerenderer.math.Vec3f;
import net.minecraft.core.particles.DustParticleOptions;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Uses the {@link me.trysam.imagerenderer.particle.ParticleRenderer} to render the given {@link java.awt.image.BufferedImage}.
 */
public class ImageRenderer implements Renderer {

	private final ParticleRenderer renderer;
	private final Vec3d            center;
	private       BufferedImage    image;
	private       Quaternion       rotation;

	private float width;
	private float height;


	/**
	 * @param image  The image that will be rendered. The pixel sum must not be bigger than 64^2 (e.g. 64*64 or 32*128).
	 * @param center The center location of the image. Represented as a {@link org.bukkit.Location}.
	 * @param far    Whether or not the image should be visible from far.
	 * @param size   The size/movement direction of the individual particles.
	 * @param speed  The speed of the particles.
	 * @param amount The amount of Particles displayed per pixel.
	 */
	public ImageRenderer(BufferedImage image, Location center, boolean far, Vec3f size, float speed, int amount) {
		this(image, Vec3d.fromLocation(center), far, size, speed, amount);
	}

	/**
	 * @param image  The image that will be rendered. The pixel sum must not be bigger than 64^2 (e.g. 64*64 or 32*128).
	 * @param center The center location of the image. Represented as a {@link me.trysam.imagerenderer.math.Vec3d}.
	 * @param far    Whether or not the image should be visible from far.
	 * @param size   The size/movement direction of the individual particles.
	 * @param speed  The speed of the particles.
	 * @param amount The amount of Particles displayed per pixel.
	 */
	public ImageRenderer(BufferedImage image, Vec3d center, boolean far, Vec3f size, float speed, int amount) {
		this(image, center.getX(), center.getY(), center.getZ(), far, size.getX(), size.getY(), size.getZ(), speed, amount);
	}

	/**
	 * @param image  The image that will be rendered. The pixel sum must not be bigger than 64^2 (e.g. 64*64 or 32*128).
	 * @param cx     X-Coordinate of the center location of the image.
	 * @param cy     Y-Coordinate of the center location of the image.
	 * @param cz     Z-Coordinate of the center location of the image.
	 * @param far    Whether or not the image should be visible from far.
	 * @param sx     X-Element of the size/movement direction of the individual particles.
	 * @param sy     Y-Element of the size/movement direction of the individual particles.
	 * @param sz     Z-Element of the size/movement direction of the individual particles.
	 * @param speed  The speed of the particles.
	 * @param amount The amount of Particles displayed per pixel.
	 */
	public ImageRenderer(BufferedImage image, double cx, double cy, double cz, boolean far, float sx, float sy, float sz, float speed, int amount) {
		setImage(image);

		this.center = new Vec3d(cx, cy, cz);
		renderer = new ParticleRenderer(new DustParticleOptions(new Vector3f(0, 0, 0), 4), far, 0, 0, 0, sx, sy, sz, speed, amount);
	}

	/**
	 * @param player The player for which the image will be rendered.
	 */
	@Override
	public void render(Player player) {
		//The scaling factor determines how dense the particles should be together (the higher the denominator, the less the space between the particles/pixels)
		float scalingFactor = 1f / 2.5f;

		//Iterates through all the pixels
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {

				float px = ((float) x * scalingFactor) - ((width * scalingFactor) / 2);
				float py = 0;
				float pz = ((float) y * scalingFactor) - ((height * scalingFactor) / 2);

				//If there is some rotation assigned, rotate the px, py, and pz coordinates by the given quaternion.
				if (rotation != null) {
					Quaternion point = new Quaternion(0, px, py, pz);
					Quaternion rotated = rotation.multiplied(point).multiplied(rotation.getInverse());
					px = rotated.getX();
					py = rotated.getY();
					pz = rotated.getZ();
				}

				//Set the renderer's location to the center + the calculated pixel coordinates.
				//TODO: Threadsafe location modification.
				renderer.getLocation().setX(center.getX() + px);
				renderer.getLocation().setY(center.getY() + py);
				renderer.getLocation().setZ(center.getZ() + pz);


				Color color = new Color(image.getRGB(x, y), true);

				//If there is some transparency in the pixel, go to the next pixel if there is some.
				if (color.getAlpha() < 255) {
					continue;
				}

				//Set the color of the particle param to the pixel color
				renderer.setParticleParam(new DustParticleOptions(new Vector3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f), 4.0f));
				renderer.render(player);

			}
		}
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public ParticleRenderer getRenderer() {
		return renderer;
	}

	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @param image The new image
	 * @throws IllegalArgumentException when the image does not fit the dimension requirements.
	 */
	public void setImage(BufferedImage image) {

		width = image.getWidth();
		height = image.getHeight();

		if (width * height > 64 * 64) {
			throw new IllegalArgumentException("Image dimensions too big!");
		}

		this.image = image;
	}

}
