package jakojaannos.tradingpost;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
		// setCreativeTab(TRDCreativeTabs.DEVNULL);
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
		
		

		// this is the top block -> check if there is a bottom block under it and destroy it
//		if (state.getValue(isTop) && worldIn.getBlockState(blockDown).getBlock() == this) {
//			worldIn.setBlockToAir(blockDown);
//		} else {
//			if(worldIn.getBlockState(blockUp).getBlock()==this){
//				worldIn.setBlockToAir(blockUp);
//			}
//		}
		
		
		

//		if (player.capabilities.isCreativeMode && state.getValue(isTop)
//				&& worldIn.getBlockState(blockDown).getBlock() == this) {
//			worldIn.setBlockToAir(blockDown);
//		}
//
//		if (!state.getValue(isTop) && worldIn.getBlockState(blockUp).getBlock() == this) {
//			if (player.capabilities.isCreativeMode) {
//				worldIn.setBlockToAir(pos);
//			}
//
//			worldIn.setBlockToAir(blockUp);
//		}
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {

		return state.getValue(isTop) ? Items.AIR : TRDBlocks.ITEM_TALL;
	}
	
//	  /**
//     * Called when the block is right clicked by a player.
//     */
//    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
//    {
//        if (this.blockMaterial == Material.IRON)
//        {
//            return false;
//        }
//        else
//        {
//            BlockPos blockpos = state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
//            IBlockState iblockstate = pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);
//
//            if (iblockstate.getBlock() != this)
//            {
//                return false;
//            }
//            else
//            {
//                state = iblockstate.cycleProperty(OPEN);
//                worldIn.setBlockState(blockpos, state, 10);
//                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
//                worldIn.playEvent(playerIn, ((Boolean)state.getValue(OPEN)).booleanValue() ? this.getOpenSound() : this.getCloseSound(), pos, 0);
//                return true;
//            }
//        }
//    }

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
