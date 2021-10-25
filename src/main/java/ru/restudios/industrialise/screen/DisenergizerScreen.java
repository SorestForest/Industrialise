package ru.restudios.industrialise.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.containers.DisenergizerContainer;

public class DisenergizerScreen extends DefaultScreen<DisenergizerContainer> {
    public DisenergizerScreen(DisenergizerContainer p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    protected ResourceLocation getTexture() {
        return Industrialise.resource(Industrialise.ResourceType.SCREEN,"disenergizer.png");
    }



}
