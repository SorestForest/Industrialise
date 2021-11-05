package ru.restudios.industrialise.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.containers.ThunderChargerContainer;
import ru.restudios.industrialise.other.REUtils;
import ru.restudios.industrialise.other.multiblock.IMultiBlockFactory;
import ru.restudios.industrialise.other.multiblock.IMultiBlockFactoryProvider;
import ru.restudios.industrialise.other.multiblock.custom.ThunderChargerMultiBlock;
import ru.restudios.industrialise.tileentities.ThunderChargerTile;

import javax.annotation.Nullable;
import java.util.Objects;

public class ThunderChargerBlock extends Block implements IMultiBlockFactoryProvider<ThunderChargerMultiBlock> {
    public ThunderChargerBlock() {
        super(Block.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).strength(4,4));
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Industrialise.TileEntities.THUNDER_CHARGER_TILE.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos,
                                             PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isClientSide) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if(!player.isCrouching()) {
                if(tileEntity instanceof ThunderChargerTile) {
                    INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);

                    NetworkHooks.openGui(((ServerPlayerEntity)player), containerProvider, tileEntity.getBlockPos());
                } else {
                    throw new IllegalStateException("Our Container provider is missing!");
                }
            } else {
                if(tileEntity instanceof ThunderChargerTile) {
                    if(Objects.requireNonNull(REUtils.castOrNull(ThunderChargerTile.class, tileEntity)).canCraft()) {
                        EntityType.LIGHTNING_BOLT.spawn(((ServerWorld) worldIn), null, player,
                                pos, SpawnReason.TRIGGERED, true, true);
                        Objects.requireNonNull(REUtils.castOrNull(ThunderChargerTile.class, tileEntity)).startCraft();
                    }
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen.industrialise.thunder_charger");
            }

            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new ThunderChargerContainer(i,worldIn.getBlockEntity(pos),playerEntity);
            }
        };
    }

    @Override
    public IMultiBlockFactory<ThunderChargerMultiBlock> provide(World world) {
        return (state, at,computer) -> {
            assert at != null;
            Objects.requireNonNull(REUtils.castOrNull(ThunderChargerTile.class, at)).computer = computer;
            return new ThunderChargerMultiBlock(REUtils.castOrNull(ThunderChargerTile.class,at), world, state);
        };
    }
}
