package it.hurts.metallurgy_reforged.fluid;

import it.hurts.metallurgy_reforged.Metallurgy;
import it.hurts.metallurgy_reforged.block.fluid.FluidBlockBaseTar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*************************************************
 * Author: PierKnight
 * Date / Hour: 22/02/2019 / 21:48
 * Class: FluidEvents
 * Project: Metallurgy 4 Reforged
 **************************************************/

public class FluidEvents {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void fogLiquid(RenderBlockOverlayEvent ev)
    {
        if (ev.getPlayer().world.getBlockState(new BlockPos(ev.getPlayer().posX, ev.getPlayer().posY + ev.getPlayer().eyeHeight, ev.getPlayer().posZ)).getBlock() instanceof FluidBlockBaseTar) {
            renderTarOverlayTexture(ev.getPlayer());
            ev.setCanceled(true);
        }
    }
    @SideOnly(Side.CLIENT)
    private void renderTarOverlayTexture(EntityPlayer pl)
    {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Metallurgy.MODID + ":textures/effects/fluid_tar.png"));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        float f7 = -pl.rotationYaw / 64.0F;
        float f8 = pl.rotationPitch / 64.0F;
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1F);
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(-1.0D, -1.0D, -0.5D).tex((double) (4.0F + f7), (double) (4.0F + f8)).endVertex();
        vertexbuffer.pos(1.0D, -1.0D, -0.5D).tex((double) (0.0F + f7), (double) (4.0F + f8)).endVertex();
        vertexbuffer.pos(1.0D, 1.0D, -0.5D).tex((double) (0.0F + f7), (double) (0.0F + f8)).endVertex();
        vertexbuffer.pos(-1.0D, 1.0D, -0.5D).tex((double) (4.0F + f7), (double) (0.0F + f8)).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
        GlStateManager.popMatrix();
    }
}
