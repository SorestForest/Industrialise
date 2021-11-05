package ru.restudios.industrialise.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class DefaultScreen<T extends Container> extends ContainerScreen<T> {

    public DefaultScreen(T p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
            RenderSystem.color4f(1f, 1f, 1f, 1f);
            assert this.minecraft != null;
            this.minecraft.getTextureManager().bind(getTexture());
            int i = this.getGuiLeft();
            int j = this.getGuiTop();
            this.blit(p_230450_1_, i, j, 0, 0, this.getXSize(), this.getYSize());
        }
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);

    }

    protected abstract ResourceLocation getTexture();
}

