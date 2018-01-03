package jakojaannos.tradingpost.client.gui.inventory;

import jakojaannos.tradingpost.ModInfo;
import jakojaannos.tradingpost.inventory.ContainerTradePost;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTradePost extends GuiContainer {

	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(ModInfo.MODID,"textures/gui/container/tradingpost.png");
	private final InventoryPlayer inventoryPlayer;
	private final IInventory tileTradePost;

	public GuiTradePost(InventoryPlayer inventoryPlayer, IInventory inventoryPost) {
		super(new ContainerTradePost(inventoryPlayer, inventoryPost));
		this.inventoryPlayer = inventoryPlayer;
		tileTradePost = inventoryPost;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = tileTradePost.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(inventoryPlayer.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		// Draw background
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURE_LOCATION);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);
	}
}
