package jakojaannos.tradingpost.item;

import jakojaannos.tradingpost.init.TPostBlocks;
import jakojaannos.tradingpost.init.TPostCreativeTabs;
import jakojaannos.tradingpost.block.BlockTradingPost;
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

public class ItemTradingPost extends Item {

	private final Block block;

	public ItemTradingPost() {
		this.block = TPostBlocks.BLOCK_TALL;
		setUnlocalizedName("tradingpost");
		setCreativeTab(TPostCreativeTabs.DEVNULL);
	}

	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName();
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack stack) {
		return super.getUnlocalizedNameInefficiently(stack);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack);
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

		worldIn.setBlockState(pos, iblockstate.withProperty(BlockTradingPost.isTop, false), 2);
		worldIn.setBlockState(up, iblockstate.withProperty(BlockTradingPost.isTop, true), 2);

		worldIn.notifyNeighborsOfStateChange(pos, block, false);
		worldIn.notifyNeighborsOfStateChange(up, block, false);
	}

}
