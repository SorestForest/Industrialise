package ru.restudios.industrialise.containers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

import java.util.Objects;

public class ContainerUtils {

    private ContainerUtils(){}

    public static boolean stillValid(PlayerEntity player, TileEntity tile, Block block){
        IWorldPosCallable posCallable = IWorldPosCallable.create(Objects.requireNonNull(tile.getLevel()),tile.getBlockPos());

        return posCallable.evaluate((p_216960_2_, p_216960_3_) ->
                p_216960_2_.getBlockState(p_216960_3_).is(block) &&
                        player.distanceToSqr(
                                (double) p_216960_3_.getX() + 0.5D,
                                (double) p_216960_3_.getY() + 0.5D,
                                (double) p_216960_3_.getZ() + 0.5D) <= 64.0D, true);
    }




}
