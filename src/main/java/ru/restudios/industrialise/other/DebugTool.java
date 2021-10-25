package ru.restudios.industrialise.other;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.items.other.BatteryItem;
import ru.restudios.industrialise.tileentities.BatteryTileEntity;

import java.util.UUID;

public class DebugTool extends Item {

    public DebugTool() {
        super(RegistryHelper.getEpicItemProperties());
    }


    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand p_77659_3_) {

            if (entity.isCrouching()){
                BatteryItem battery = REUtils.castOrNull(BatteryItem.class,entity.inventory.getItem(0).getItem());
                entity.sendMessage(new StringTextComponent(String.valueOf(battery.getEnergy(entity.inventory.getItem(0)))), UUID.randomUUID());
                battery.setEnergy(entity.inventory.getItem(0),battery.getCapacity());
            }
            else {
                BatteryTileEntity tile = REUtils.castOrNull(BatteryTileEntity.class,world.getBlockEntity(entity.blockPosition().below()));
                assert tile != null;
                tile.getCapability(CapabilityEnergy.ENERGY,null).ifPresent(iEnergyStorage -> {
                    for (int i = 0; i <10 ; i++) {
                        iEnergyStorage.receiveEnergy(90000,false);
                    }
                    if (world.isClientSide())
                        entity.sendMessage(Industrialise.string("Energy filled: "+iEnergyStorage.getEnergyStored()),UUID.randomUUID());
                });

            }

        return ActionResult.success(entity.getItemInHand(p_77659_3_));
    }


}
