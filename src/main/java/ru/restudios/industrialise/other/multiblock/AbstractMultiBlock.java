package ru.restudios.industrialise.other.multiblock;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.restudios.industrialise.other.REUtils;

import java.util.HashMap;

public abstract class AbstractMultiBlock implements IMultiBlock {

    private final HashMap<Block,Integer> blockCountsNeeded = Maps.newHashMap();
    private final HashMap<Block,Integer> blockCountsPlaced = Maps.newHashMap();
    private final HashMap<BlockPos,TileEntity> placed = new HashMap<>();

    protected BlockState state;
    protected World world;

    public AbstractMultiBlock(BlockState state, World world){
        this.state = state;
        this.world = world;
        for (Block part : parts()){
            if (blockCountsNeeded.containsKey(part)){
                int i = blockCountsNeeded.get(part)+1;
                blockCountsNeeded.remove(part);
                blockCountsNeeded.put(part,i);
            }
            else {
                blockCountsNeeded.put(part,1);
            }
        }
    }

    @Override
    public void connectPart(BlockState part, TileEntity server,BlockPos relativePosition) {
        if (canConnect(part.getBlock())){
            int placed = blockCountsPlaced.getOrDefault(part.getBlock(),0);
            placed += 1;
            blockCountsPlaced.remove(part.getBlock());
            blockCountsPlaced.put(part.getBlock(),placed);
            onPartConnected(part,server);
            this.placed.put(relativePosition,server);
            if (isBuild()){
                onStructureBuilt();
            }
        }
    }

    @Override
    public void disconnectPart(Block part,BlockPos at) {
        if (blockCountsPlaced.containsKey(part)){
            boolean isBuild = isBuild();
            int i = blockCountsPlaced.get(part);
            i -= 1;
            i = REUtils.keepInRange(i,0,Integer.MAX_VALUE);
            blockCountsPlaced.remove(part);
            blockCountsPlaced.put(part,i);
            placed.remove(at);
            if (isBuild){
                onStructureDestroyed();
            }
        }
    }

    @Override
    public boolean isBuild() {
        boolean blockTypes = blockCountsNeeded.size() <= blockCountsPlaced.size();
        boolean flag = true;
        for (Block block : blockCountsPlaced.keySet()){
            int needed = blocksNeededToBuild(block);
            if (needed > 0){
                flag = false;
                break;
            }
        }
        for (TileEntity tileEntity : placed.values()){
            if (tileEntity instanceof IMultiBlockPart){
                if (!((IMultiBlockPart) tileEntity).isReady()){
                    flag = false;
                    break;
                }
            }
        }
        return blockTypes && flag;
    }

    @Override
    public boolean canConnect(Block block) {
        if (!blockCountsNeeded.containsKey(block)) {
            return false;
        }
        int neededToBuild = blocksNeededToBuild(block);
        return neededToBuild > 0;
    }

    @Override
    public int blocksNeededToBuild(Block block){
        int neededAnyway = blockCountsNeeded.getOrDefault(block,0);
        int placed = blockCountsPlaced.getOrDefault(block,0);
        if (neededAnyway < placed){ throw new IllegalArgumentException("Needed can't be smaller than placed!"); }
        return neededAnyway - placed;
    }


}
