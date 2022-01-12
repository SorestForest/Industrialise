package ru.restudios.industrialise.tileentities;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.other.REUtils;
import ru.restudios.industrialise.other.SidedInventory;
import ru.restudios.industrialise.other.Tags;
import ru.restudios.industrialise.other.multiblock.IMultiBlock;
import ru.restudios.industrialise.other.multiblock.IMultiBlockFactoryProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class ComputerTileEntity extends TileEntity implements ITickableTileEntity {



    public ComputerTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public ComputerTileEntity(){
        this(Industrialise.TileEntities.COMPUTER_TILE.get());
    }


    private Block controller;
    private IMultiBlock multiBlock;
    private final HashMap<Direction,Block> placedNear = Maps.newHashMap();

    @Override
    public void tick() {
        assert level != null;
        //TileEntity serverTile;

        if (!level.isClientSide){
            // Check if controller block has changed
            if (this.controller == null || !this.controller.is(level.getBlockState(worldPosition.above()).getBlock())){
                Block above = level.getBlockState(worldPosition.above()).getBlock(); // it changed, getting new block
                if (above.is(Blocks.AIR)){ // oh, its air, skip it
                    this.controller = above; // but set as controller to avoid empty checks
                    return;
                }
                if (above.is(Tags.BlockTags.COMPUTER_ADDITION)){ // oh, its controller block, parse it
                    this.multiBlock = parseBlock(level.getBlockState(worldPosition.above()),level.getBlockEntity(worldPosition.above()));
                }
                this.controller = above; // and set any block to avoid empty checks
                return;  // We can't do anything new on tick
            }
            if (multiBlock == null){ // make sure that we have controller
                return;
            }
            if (multiBlock.isBuild()){ // oh, its build nice, tick it
                multiBlock.tick();
            }
            for (Direction d : SidedInventory.Settings.ALL_NON_NULL){ // walk for all directions, except UP, it controlled by other lines
                if (d == Direction.UP){
                    continue;
                }
                BlockPos relativePosition = worldPosition.relative(d);
                BlockState relative = level.getBlockState(relativePosition);
                Block type = relative.getBlock();
                Block cache = placedNear.getOrDefault(d,Blocks.AIR);
                if (type.is(cache)){ // If block in cache == placed block, no actions required
                    continue;
                }
                if (multiBlock.canConnect(type)){ // if actions required, check that new block can be connected to multiblock
                    multiBlock.connectPart(relative,level.getBlockEntity(relativePosition),relativePosition); // connecting it
                }
                else {
                    multiBlock.disconnectPart(cache,relativePosition); // otherwise, disconnect it
                }
                placedNear.put(d,type); // and put block in cache to avoid empty checks
            }
        }


    }

    private IMultiBlock parseBlock(BlockState state,TileEntity at){ // simple parse blockstate to multiblock
        Block block = state.getBlock();
        if (block instanceof IMultiBlockFactoryProvider){ // validate for some reasons
            IMultiBlockFactoryProvider<?> provider = REUtils.castOrNull(IMultiBlockFactoryProvider.class,block);
            if (provider == null) { throw new RuntimeException("Unknown exception"); }
            return provider.provide(at.getLevel()).create(state,at,this); // just cast block to provider, get factory and use it!
        }
        return null; // It can't be
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (multiBlock == null){
            return super.getCapability(cap,side);
        }
        if (multiBlock.getTileEntity() == null){
            return super.getCapability(cap, side);
        }
        return multiBlock.getTileEntity().getCapability(cap,side);
    }

    @SuppressWarnings("unused")
    public boolean isStructureBuild(){
        if (multiBlock == null){ return false; }
        return multiBlock.isBuild();
    }


}
