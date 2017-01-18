package shane2482.deadlyworld.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import shane2482.deadlyworld.blocks.storage.container.ContainerPlywoodCrate;
import shane2482.deadlyworld.library.Reference;
import shane2482.deadlyworld.tiles.TileEntityPlywoodCrate;

public class GuiPlywoodCrate extends GuiContainer{
	private static final ResourceLocation PlywoodInv = new ResourceLocation(Reference.MOD_ID + ":" + "textures/gui/container/Plywood_Crate_Gui.png");
	
	private TileEntityPlywoodCrate Te;
   

    public GuiPlywoodCrate(InventoryPlayer playerInv, TileEntityPlywoodCrate Te)
    {
        super(new ContainerPlywoodCrate(playerInv, Te));
       this.Te = Te;
        xSize = 230;
        ySize = 222;
    }

    @Override
    public void onGuiClosed() {
    	super.onGuiClosed();
    }
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(I18n.format("Plywood Crate", new Object[0]), 8, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 35, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
    	Minecraft.getMinecraft().getTextureManager().bindTexture(PlywoodInv);
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}