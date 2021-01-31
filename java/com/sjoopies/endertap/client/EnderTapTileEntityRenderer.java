package com.sjoopies.endertap.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sjoopies.endertap.tileentity.EnderTapTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnderTapTileEntityRenderer extends TileEntityRenderer<EnderTapTileEntity> {

	public EnderTapTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);

	}

	@Override
	public void render(EnderTapTileEntity tile, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers,
			int light, int overlay) {
		if (tile.hasPlayer() && !tile.getPlayerUsername().equals("")) {
			ms.push();
			renderName(tile, tile.getPlayerUsername(), ms, buffers, light);
			ms.pop();
		}
	}
	
	
	protected void renderName(EnderTapTileEntity tile, String displayNameIn, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int packedLightIn) {
		double d0 = Minecraft.getInstance().getRenderManager().getDistanceToCamera(tile.getPos().getX() + 0.5,
				tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5);
		if (!(d0 > 4096.0D)) {
			float f = 1.35F;
			int i = "deadmau5".equals(displayNameIn) ? -10 : 0;
			matrixStackIn.push();
			matrixStackIn.translate(0.5D, (double) f, 0.5D);
			matrixStackIn.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
			matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
			
			Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
			
			float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
			int j = (int) (f1 * 255.0F) << 24;
			FontRenderer fontrenderer = Minecraft.getInstance().getRenderManager().getFontRenderer();
			float f2 = (float) (-fontrenderer.getStringWidth(displayNameIn) / 2);
			fontrenderer.renderString(displayNameIn, f2, (float) i, 553648127, false, matrix4f, bufferIn, false, j,
					packedLightIn);

			matrixStackIn.pop();
		}
	}

}
