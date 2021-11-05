package ru.restudios.industrialise.other.multiblock.custom;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.other.REUtils;
import ru.restudios.industrialise.other.multiblock.AbstractMultiBlock;
import ru.restudios.industrialise.tileentities.BatteryTileEntity;
import ru.restudios.industrialise.tileentities.DisenergizerTile;

import java.util.ArrayList;

public class DisenergizerMultiBlock extends AbstractMultiBlock {

    private final DisenergizerTile tile;

    public DisenergizerMultiBlock(BlockState state, World world, DisenergizerTile tile) {
        super(state, world);
        this.tile = tile;
    }

    @Override
    public ArrayList<Block> parts() {
        return Lists.newArrayList(Industrialise.Blocks.BATTERY_BLOCK.get());
    }

    @Override
    public void tick() {

    }

    @Override
    public TileEntity getTileEntity() {
        return tile;
    }

    @Override
    public void onStructureBuilt() {

        tile.structureCompleted();}

    @Override
    public void onStructureDestroyed() {

        tile.structureDestroyed();}

    @Override
    public void onPartConnected(BlockState part,TileEntity server) {
        tile.batteryServer = REUtils.castOrNull(BatteryTileEntity.class, server);
    }
}
