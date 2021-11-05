package ru.restudios.industrialise.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.containers.DisenergizerContainer;
import ru.restudios.industrialise.other.REUtils;
import ru.restudios.industrialise.other.RegistryHelper;
import ru.restudios.industrialise.other.multiblock.IMultiBlockFactory;
import ru.restudios.industrialise.other.multiblock.IMultiBlockFactoryProvider;
import ru.restudios.industrialise.other.multiblock.custom.DisenergizerMultiBlock;
import ru.restudios.industrialise.tileentities.DisenergizerTile;

import javax.annotation.Nullable;

public class DisenergizerBlock extends Block implements IMultiBlockFactoryProvider<DisenergizerMultiBlock> {

    public DisenergizerBlock() {
        super(RegistryHelper.getMetalBlockProperties());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Industrialise.TileEntities.DISENERGIZER_TILE.get().create();
    }

    @Override
    public IMultiBlockFactory<DisenergizerMultiBlock> provide(World world) {
        return (state, at, computer) -> {
            DisenergizerTile tile = REUtils.castOrNull(DisenergizerTile.class,at);
            assert tile != null;
            tile.computer = computer;
            return new DisenergizerMultiBlock(state,world,tile);
        };
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos,
                                PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isClientSide) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if(!player.isCrouching()) {
                if(tileEntity instanceof DisenergizerTile) {
                    INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);

                    NetworkHooks.openGui(((ServerPlayerEntity)player), containerProvider, tileEntity.getBlockPos());
                } else {
                    throw new IllegalStateException("Our Container provider is missing!");
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen.industrialise.disenergizer");
            }

            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new DisenergizerContainer(i,playerEntity,REUtils.castOrNull(DisenergizerTile.class,worldIn.getBlockEntity(pos)));
            }
        };
    }
}
