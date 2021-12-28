package io.github.lucunji.noitawands.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.lucunji.noitawands.NoitaWands;
import io.github.lucunji.noitawands.entity.spell.EnergySphereSpellEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class EnergySphereSpellRenderer extends EntityRenderer<EnergySphereSpellEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(NoitaWands.MODID, "textures/entity/spell/energy_sphere.png");
    private static final RenderType RENDER_TYPE = RenderType.getEntityCutoutNoCull(TEXTURE);

    public EnergySphereSpellRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    protected int getBlockLight(EnergySphereSpellEntity entityIn, float partialTicks) {
        return 15;
    }

    @Override
    public void render(EnergySphereSpellEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.scale(0.4f, 0.4f, 0.4f);
        matrixStackIn.rotate(this.renderManager.getCameraOrientation());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RENDER_TYPE);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, 0.0F, 0, 0, 1, packedLightIn);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, 1.0F, 0, 1, 1, packedLightIn);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, 1.0F, 1, 1, 0, packedLightIn);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, 0.0F, 1, 0, 0, packedLightIn);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static void func_229045_a_(IVertexBuilder vertexBuilder, Matrix4f matrix4f, Matrix3f matrix3f, float x, int y, int u, int v, int lightmapUV) {
        vertexBuilder.pos(matrix4f, x - 0.5F, (float)y - 0.25F, 0.0F).color(255, 255, 255, 255).tex((float)u, (float)v).overlay(OverlayTexture.NO_OVERLAY).lightmap(lightmapUV).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public ResourceLocation getEntityTexture(EnergySphereSpellEntity entity) {
        return TEXTURE;
    }
}
