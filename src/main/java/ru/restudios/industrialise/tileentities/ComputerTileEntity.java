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
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.other.REUtils;
import ru.restudios.industrialise.other.SidedInventory;
import ru.restudios.industrialise.other.Tags;
import ru.restudios.industrialise.other.multiblock.IMultiBlock;
import ru.restudios.industrialise.other.multiblock.IMultiBlockFactoryProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComputerTileEntity extends TileEntity implements ITickableTileEntity {

    private IMultiBlock multiBlock;

    public ComputerTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public ComputerTileEntity(){
        this(Industrialise.DeferredEvents.COMPUTER_TILE.get());
    }

    private Block lastBlock;
    private final HashMap<BlockPos,Block> connected = Maps.newHashMap();

    @Override
    public void tick() {
        assert level != null;
        if (!level.isClientSide){
            if (lastBlock == null || !lastBlock.is(level.getBlockState(worldPosition.above()).getBlock())){
                Block block = level.getBlockState(worldPosition.above()).getBlock();
                if (block.is(Tags.BlockTags.COMPUTER_ADDITION)){
                    this.multiBlock = parseBlock(level.getBlockState(worldPosition.above()),level.getBlockEntity(worldPosition.above()));
                    lastBlock = block;
                    System.out.println("Last block wasn't valid, so block above is: "+block.getRegistryName());
                }
                else {
                    if (multiBlock != null){
                        multiBlock.onStructureDestroyed();
                    }
                    multiBlock = null; lastBlock = null; }
            }
            if (multiBlock != null){
                if (multiBlock.isBuild()){
                    multiBlock.tick();
                }

                for (Direction d : SidedInventory.Settings.ALL){
                    if (d == Direction.UP) { continue; }
                    if (d == null) { continue; }
                    BlockPos relative = worldPosition.relative(d);

                    boolean stop = false;
                    for (BlockPos poses : connected.keySet()){
                        if (poses.equals(relative)){
                            stop = true;
                            break;
                        }
                    }
                    if (stop){
                        continue;
                    }

                    Block blockIn = level.getBlockState(relative).getBlock();
                    if (blockIn == Blocks.AIR) { continue; }
                    if (multiBlock.canConnect(blockIn)){
                        multiBlock.connectPart(level.getBlockState(relative));
                        connected.remove(relative);
                        connected.put(relative, blockIn);
                    }
                }
                ArrayList<BlockPos> blocksToRemove = new ArrayList<>();
                for (Map.Entry<BlockPos, Block> entry : connected.entrySet()){
                    BlockState currentState = level.getBlockState(entry.getKey());
                    if (!currentState.getBlock().is(entry.getValue())){
                        multiBlock.disconnectPart(entry.getValue()); blocksToRemove.add(entry.getKey());
                    }
                }
                for (BlockPos block : blocksToRemove) {
                    connected.remove(block);
                }
            }
        }
    }



    private IMultiBlock parseBlock(BlockState state,TileEntity at){
        Block block = state.getBlock();
        if (block instanceof IMultiBlockFactoryProvider){
            IMultiBlockFactoryProvider<?> provider = REUtils.castOrNull(IMultiBlockFactoryProvider.class,block);
            if (provider == null) { throw new RuntimeException("Unknown exception"); }
            return provider.provide(at.getLevel()).create(state,at);
        }
        return null; // It can't be
    }

}
