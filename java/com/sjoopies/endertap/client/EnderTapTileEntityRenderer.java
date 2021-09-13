package com.sjoopies.endertap.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.sjoopies.endertap.blockentity.EnderTapBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnderTapTileEntityRenderer implements BlockEntityRenderer<EnderTapBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public EnderTapTileEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(EnderTapBlockEntity tile, float partialTicks, PoseStack pos, MultiBufferSource buffers,
                       int light, int overlay) {
        if (tile.hasPlayer() && !tile.getPlayerUsername().equals("")) {

            pos.pushPose();

            renderName(tile, tile.getPlayerUsername(), pos, buffers, light);
            pos.popPose();
        }
    }


    protected void renderName(EnderTapBlockEntity blockEntity, String displayNameIn, PoseStack poseStack,
                              MultiBufferSource bufferIn, int packedLightIn) {
        double d0 = Minecraft.getInstance().getCameraEntity().distanceToSqr(blockEntity.getBlockPos().getX() + 0.5,
                blockEntity.getBlockPos().getY() + 0.5, blockEntity.getBlockPos().getZ() + 0.5);

        if (!(d0 > 4096.0D)) {
            float f = 1.35F;

            poseStack.pushPose();
            poseStack.translate(0.5D, f, 0.5D);

            poseStack.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
            poseStack.scale(-0.025F, -0.025F, 0.025F);

            Matrix4f matrix4f = poseStack.last().pose();

            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25f);
            int j = (int) (f1 * 255.0F) << 24;
            Font font = context.getFont();
            float f2 = (float) (-font.width(displayNameIn) / 2);
            font.drawInBatch(displayNameIn, f2, (float) 0, 553648127, false, matrix4f, bufferIn, true, j, packedLightIn);
            font.drawInBatch(displayNameIn, f2, (float) 0, -1, false, matrix4f, bufferIn, false, 0, packedLightIn);
            poseStack.popPose();
        }

    }
}
