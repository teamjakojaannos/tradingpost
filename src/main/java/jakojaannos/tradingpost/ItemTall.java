package jakojaannos.tradingpost;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTall extends Item {

	private final Block block;

	public ItemTall() {
		this.block = TRDBlocks.BLOCK_TALL;
		setUnlocalizedName("Tallitem");
		setCreativeTab(TRDCreativeTabs.DEVNULL);
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing != EnumFacing.UP) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (!block.isReplaceable(worldIn, pos)) {
				pos = pos.offset(facing);
			}

			ItemStack itemstack = player.getHeldItem(hand);

			if (player.canPlayerEdit(pos, facing, itemstack) && this.block.canPlaceBlockAt(worldIn, pos)) {
				EnumFacing enumfacing = EnumFacing.fromAngle((double) player.rotationYaw);
				// int i = enumfacing.getFrontOffsetX();
				// int j = enumfacing.getFrontOffsetZ();
				// boolean flag = i < 0 && hitZ < 0.5F || i > 0 && hitZ > 0.5F
				// || j < 0 && hitX > 0.5F
				// || j > 0 && hitX < 0.5F;
				placeBlock(worldIn, pos, enumfacing, this.block);
				SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos),
						worldIn, pos, player);
				worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
						(soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				itemstack.shrink(1);
				return EnumActionResult.SUCCESS;
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}

	public static void placeBlock(World worldIn, BlockPos pos, EnumFacing facing, Block block) {

		BlockPos up = pos.up();
		IBlockState iblockstate = block.getDefaultState();

		worldIn.setBlockState(pos, iblockstate.withProperty(BlockTall.isTop, false), 2);
		worldIn.setBlockState(up, iblockstate.withProperty(BlockTall.isTop, true), 2);

		worldIn.notifyNeighborsOfStateChange(pos, block, false);
		worldIn.notifyNeighborsOfStateChange(up, block, false);
	}

}
