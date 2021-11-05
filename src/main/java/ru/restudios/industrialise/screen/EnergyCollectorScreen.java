package ru.restudios.industrialise.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.containers.EnergyCollectorContainer;

public class EnergyCollectorScreen extends DefaultScreen<EnergyCollectorContainer>{

    public EnergyCollectorScreen(EnergyCollectorContainer p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    protected ResourceLocation getTexture() {
        return Industrialise.resource(Industrialise.ResourceType.SCREEN,"energy_collector.png");
    }
}
