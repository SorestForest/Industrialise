package ru.restudios.industrialise.other.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public interface IMultiBlockFactory<M extends IMultiBlock> {

    M create(BlockState state,@Nullable TileEntity at);
}
