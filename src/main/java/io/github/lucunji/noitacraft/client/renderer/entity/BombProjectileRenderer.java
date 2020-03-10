package io.github.lucunji.noitacraft.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.client.renderer.entity.model.BombProjectileModel;
import io.github.lucunji.noitacraft.entity.projectile.BombProjectileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BombProjectileRenderer extends EntityRenderer<BombProjectileEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/wither/wither.png");
    private final BombProjectileModel bombProjectileModel = new BombProjectileModel();

    public BombProjectileRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    protected int getBlockLight(BombProjectileEntity entityIn, float partialTicks) {
        return 15;
    }

    @Override
    public void render(BombProjectileEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.bombProjectileModel.getRenderType(this.getEntityTexture(entityIn)));
        this.bombProjectileModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(BombProjectileEntity entity) {
        return new ResourceLocation(NoitaCraft.MOD_ID, "textures/entity/spell/spell_bomb.png");
    }
}
