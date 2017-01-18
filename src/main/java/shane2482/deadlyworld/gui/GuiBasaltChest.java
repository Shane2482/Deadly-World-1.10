package shane2482.deadlyworld.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import shane2482.deadlyworld.library.Reference;

public class GuiBasaltChest extends GuiContainer {

	private static final ResourceLocation Basalt_Chest = new ResourceLocation(Reference.MOD_ID + ":" + "textures/gui/container/Basalt_Chest_Gui.png");
	private final IInventory upperChestInventory;
	private final IInventory lowerChestInventory;

	private final int inventoryRows;

	public GuiBasaltChest(IInventory upperInv, IInventory lowerInv) {
		super(new ContainerChest(upperInv, lowerInv, Minecraft.getMinecraft().thePlayer));
		this.upperChestInventory = upperInv;
		this.lowerChestInventory = lowerInv;
		this.allowUserInput = false;
		int i = 222;
		int j = 114;
		this.inventoryRows = lowerInv.getSizeInventory() / 9;
		this.ySize = 114 + this.inventoryRows * 18;
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 14277081);
		this.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8,
				this.ySize - 96 + 2, 14277081);
	}

	protected void drawGuiContainerBackgroundLayer(float ticks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(Basalt_Chest);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
		this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
	}
}