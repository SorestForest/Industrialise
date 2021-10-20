package ru.restudios.industrialise.other;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DebugTool extends Item {

    public DebugTool() {
        super(ItemProperties.getEpic());
    }


    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand p_77659_3_) {
        if (!world.isClientSide()){

        }
        return ActionResult.success(entity.getItemInHand(p_77659_3_));
    }
}
