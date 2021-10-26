package ru.restudios.industrialise.other.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public interface IMultiBlock {

    ArrayList<Block> parts();

    void tick();

    void connectPart(BlockState part,TileEntity server);

    void disconnectPart(Block part);

    boolean isBuild();

    boolean canConnect(Block block);

    TileEntity getTileEntity();

    void onStructureBuilt();

    void onStructureDestroyed();

    void onPartConnected(BlockState part,TileEntity server);

    int blocksNeededToBuild(Block block);
}
