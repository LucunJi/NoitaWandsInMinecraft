package io.github.lucunji.noitacraft.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.lucunji.noitacraft.NoitaCraft;
import io.github.lucunji.noitacraft.client.renderer.entity.model.BombSpellModel;
import io.github.lucunji.noitacraft.entity.spell.BombSpellEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BombSpellRenderer extends EntityRenderer<BombSpellEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(NoitaCraft.MOD_ID, "textures/entity/spell/bomb.png");
    private final BombSpellModel bombSpellModel = new BombSpellModel();

    public BombSpellRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    protected int getBlockLight(BombSpellEntity entityIn, float partialTicks) {
        return 15;
    }

    @Override
    public void render(BombSpellEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.bombSpellModel.getRenderType(this.getEntityTexture(entityIn)));
        this.bombSpellModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(BombSpellEntity entity) {
        return TEXTURE;
    }
}
