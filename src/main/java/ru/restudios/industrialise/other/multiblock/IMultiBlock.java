package ru.restudios.industrialise.other.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.ArrayList;

public interface IMultiBlock {

    ArrayList<Block> parts();

    void tick();

    void connectPart(BlockState part);

    void disconnectPart(Block part);

    boolean isBuild();

    boolean canConnect(Block block);


    void onStructureBuilt();

    void onStructureDestroyed();

    void onPartConnected(BlockState part);

    int blocksNeededToBuild(Block block);
}
