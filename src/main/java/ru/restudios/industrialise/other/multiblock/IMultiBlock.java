package ru.restudios.industrialise.other.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public interface IMultiBlock {

    ArrayList<Block> parts();

    void tick();

    void connectPart(BlockState part,TileEntity server,BlockPos relativePosition);

    void disconnectPart(Block part, BlockPos at);

    boolean isBuild();

    boolean canConnect(Block block);

    TileEntity getTileEntity();

    void onStructureBuilt();

    void onStructureDestroyed();

    void onPartConnected(BlockState part,TileEntity server);

    int blocksNeededToBuild(Block block);
}
