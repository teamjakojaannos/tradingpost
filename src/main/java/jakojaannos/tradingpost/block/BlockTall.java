package jakojaannos.tradingpost.block;

import java.util.Random;

import jakojaannos.tradingpost.TRDBlocks;
import jakojaannos.tradingpost.TradingPostMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTall extends BlockContainer {

	public static final PropertyBool isTop = PropertyBool.create("istop");

	public BlockTall() {
		super(Material.WOOD);
		setHardness(1.2F);
		setSoundType(SoundType.WOOD);
		setUnlocalizedName("tallBlock");
		setDefaultState(this.blockState.getBaseState().withProperty(isTop, false));
	}

	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, isTop);
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
	}

	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		BlockPos blockDown = pos.down();
		BlockPos blockUp = pos.up();

		// this is the top block -> check if there is a bottom block under it
		// and destroy it
		if (player.capabilities.isCreativeMode && state.getValue(isTop)
				&& worldIn.getBlockState(blockDown).getBlock() == this) {
			worldIn.setBlockToAir(blockDown);
		} else if (worldIn.getBlockState(blockUp).getBlock() == this) {
			if (player.capabilities.isCreativeMode) {
				worldIn.setBlockToAir(pos);
			}

			worldIn.setBlockToAir(blockUp);
		}
	}

	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		BlockPos blockDown = pos.down();

		if (state.getValue(isTop)) {
			IBlockState stateBelow = worldIn.getBlockState(blockDown);

			if (stateBelow.getBlock() != this) {
				worldIn.setBlockToAir(pos);
			} else {
				stateBelow.neighborChanged(worldIn, blockDown, blockIn, fromPos);
			}
		} else {
			BlockPos blockUp = pos.up();

			// lower half
			if (worldIn.getBlockState(blockUp).getBlock() != this) {
				worldIn.setBlockToAir(pos);

				if (!worldIn.isRemote) {
					this.dropBlockAsItem(worldIn, pos, state, 0);
				}
			}
		}
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(isTop) ? Items.AIR : TRDBlocks.ITEM_TALL;
	}

	public int getMetaFromState(IBlockState state) {
		if (state.getValue(isTop))
			return 1;
		else
			return 0;
	}

	public IBlockState getStateFromMeta(int meta) {
		if (meta == 1)
			return getDefaultState().withProperty(isTop, true);
		else
			return getDefaultState().withProperty(isTop, false);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return state.getValue(isTop);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return state.getValue(isTop);
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.openGui(TradingPostMod.instance, GuiHandlerTrd.GUI_IDS.TRADING_POST.ordinal(), worldIn, pos.getX(),
					pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityTrd();
	}
}
