package io.github.lucunji.noitacraft.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.entity.projectile.SparkProjectileEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class SparkProjectileRenderer extends EntityRenderer<SparkProjectileEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(NoitaCraft.MOD_ID, "textures/entity/spell/spell_spark_bolt.png");

    private static final float pixelWidth = 1 / 32f;

    public SparkProjectileRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(SparkProjectileEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 90.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)));

        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(45.0F));
        matrixStackIn.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStackIn.translate(-4.0D, 0.0D, 0.0D);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEyes(this.getEntityTexture(entityIn)));
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();

        for(int j = 0; j < 4; ++j) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
            this.drawTexture(matrix4f, matrix3f, ivertexbuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLightIn);
            this.drawTexture(matrix4f, matrix3f, ivertexbuilder, 8, -2, 0, pixelWidth * 13, 0.0F, 0, 1, 0, packedLightIn);
            this.drawTexture(matrix4f, matrix3f, ivertexbuilder, 8, 2, 0, pixelWidth * 13, pixelWidth * 5, 0, 1, 0, packedLightIn);
            this.drawTexture(matrix4f, matrix3f, ivertexbuilder, -8, 2, 0, 0.0F, pixelWidth * 5, 0, 1, 0, packedLightIn);
        }
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public void drawTexture(Matrix4f matrix4f, Matrix3f matrix3f, IVertexBuilder vertexBuilder, int x, int y, int z, float u, float v, int normalX, int normalZ, int normalY, int p_229039_12_) {
        vertexBuilder.pos(matrix4f, (float)x, (float)y, (float)z).color(255, 255, 255, 255).tex(u, v).overlay(OverlayTexture.NO_OVERLAY).lightmap(p_229039_12_).normal(matrix3f, (float)normalX, (float)normalY, (float)normalZ).endVertex();
    }

    @Override
    public ResourceLocation getEntityTexture(SparkProjectileEntity entity) {
        return TEXTURE;
    }
}
