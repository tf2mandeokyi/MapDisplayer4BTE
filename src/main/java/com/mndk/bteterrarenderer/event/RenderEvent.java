package com.mndk.bteterrarenderer.event;

import com.mndk.bteterrarenderer.BTETerraRenderer;
import com.mndk.bteterrarenderer.BTETerraRendererConfig;
import com.mndk.bteterrarenderer.projection.Projections;
import com.mndk.bteterrarenderer.storage.TileMapCache;
import com.mndk.bteterrarenderer.tms.TileMapService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.BiConsumer;

@Mod.EventBusSubscriber(modid = BTETerraRenderer.MODID, value = Side.CLIENT)
public class RenderEvent {


	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onRenderEvent(final RenderWorldLastEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;

		// "Smooth" player position
		final float partialTicks = event.getPartialTicks();
		final double px = player.lastTickPosX + ((player.posX - player.lastTickPosX) * partialTicks);
		final double py = player.lastTickPosY + ((player.posY - player.lastTickPosY) * partialTicks);
		final double pz = player.lastTickPosZ + ((player.posZ - player.lastTickPosZ) * partialTicks);

		if(BTETerraRendererConfig.doRender) {
			try {
				renderTiles(BTETerraRendererConfig.getTileMapService(), px, py, pz);
			} catch(IllegalArgumentException exception) {
				exception.printStackTrace();
			}
		}
	}



	/**
	 * Renders tile map.
	 * @param tms TileMapService class
	 * @param px Player's x position
	 * @param py Player's y position
	 * @param pz Player's z position
	 */
	public static void renderTiles(TileMapService tms, double px, double py, double pz) {

		if(tms == null) return;
		if(Projections.getServerProjection() == null) return;

		BTETerraRendererConfig.RenderSettings settings = BTETerraRendererConfig.RENDER_SETTINGS;

		if(settings.yAxis <= py - settings.yDiffLimit || settings.yAxis >=py + settings.yDiffLimit) {
			return;
		}

		Tessellator t = Tessellator.getInstance();
		BufferBuilder builder = t.getBuffer();

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		GlStateManager.scale(1, 1, 1);

		int size = settings.radius - 1;

		BiConsumer<Integer, Integer> drawTile = (dx, dy) -> {
			if(Math.abs(dx) > size || Math.abs(dy) > size) return;
			tms.renderTile(
					t, builder,
					-settings.zoom,
					settings.yAxis + 0.1, // Adding .1 to y because of texture-overlapping issue
					(float) settings.opacity,
					px + settings.align_x, py, pz + settings.align_z,
					dx, dy
			);
		};

		// Draw tiles around player with diamond-priority
		for(int i = 0; i < 2 * size + 1; ++i) {
			if(i == 0) {
				drawTile.accept(0, 0);
			}
			for(int j = 0; j < i; ++j) {
				drawTile.accept(-j, j - i);
				drawTile.accept(j - i, j);
				drawTile.accept(j, i - j);
				drawTile.accept(i - j, -j);
			}
		}

		TileMapCache.getInstance().cleanup();

		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}
}
