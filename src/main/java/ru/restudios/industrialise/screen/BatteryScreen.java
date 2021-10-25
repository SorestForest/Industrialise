package ru.restudios.industrialise.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.containers.BatteryContainer;

public class BatteryScreen extends ContainerScreen<BatteryContainer> {

    public static final ResourceLocation GUI = Industrialise.resource("textures/gui/battery_block.png");

    private static final int EMPTY_BAR_X_START = 177;
    private static final int FULL_BAR_X_START = 193;
    private static final int BAR_Y_END = 60;
    private static final int BAR_WIDTH = 15;


    public BatteryScreen(BatteryContainer p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(GUI);
        int i = this.getGuiLeft();
        int j = this.getGuiTop();
        //           stack     wherePlaceX wherePlaceY pictureStart pictureEnd pictureXSize pictureYSize
        this.blit(p_230450_1_, i, j, 0, 0, this.getXSize(), this.getYSize());

        int stored = menu.getEnergyStored(); // предположим 150000

        final float maxStored = menu.getCapacity();  // 300000
        double percents = stored/maxStored*100;
        double pixels = (60d/100d*percents);// 0.6
        int toPixels = (int) Math.floor(pixels);
        this.blit(p_230450_1_,i+70,j+7,FULL_BAR_X_START,0,BAR_WIDTH,BAR_Y_END);
        this.blit(p_230450_1_,i+70,j+7,EMPTY_BAR_X_START,0,BAR_WIDTH,toPixels);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}
