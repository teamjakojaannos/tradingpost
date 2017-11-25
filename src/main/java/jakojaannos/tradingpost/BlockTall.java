package jakojaannos.tradingpost;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTall extends Block {

	public static final PropertyBool isTop = PropertyBool.create("istop");

	public BlockTall() {
		super(Material.WOOD);
		setHardness(1.2F);
		setSoundType(SoundType.WOOD);
		setUnlocalizedName("TallBlock");
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
		if (state.getValue(isTop) && worldIn.getBlockState(blockDown).getBlock() == this) {
			worldIn.setBlockToAir(blockDown);
		} else {
			if (worldIn.getBlockState(blockUp).getBlock() == this) {
				worldIn.setBlockToAir(blockUp);
			}
		}

	}

	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		BlockPos blockDown = pos.down();
		BlockPos blockUp = pos.up();

		boolean destroy = false;

		if (state.getValue(isTop) && worldIn.getBlockState(blockDown).getBlock() != this) {
			// upper half, the block below me is not BlockTall
			worldIn.setBlockToAir(pos);
			destroy = true;
		} else {
			// lower half
			if (worldIn.getBlockState(blockUp).getBlock() != this || //
			// the block above me is not BlockTall
					!worldIn.getBlockState(blockDown).isSideSolid(worldIn, blockDown, EnumFacing.UP)) {
				// the block below me is not solid
				worldIn.setBlockToAir(pos);
				destroy = true;
			}
		}

		if (destroy && !worldIn.isRemote) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
		}

	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {

		return TRDBlocks.ITEM_TALL;
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

}
