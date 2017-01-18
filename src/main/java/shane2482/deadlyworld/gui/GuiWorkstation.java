package shane2482.deadlyworld.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import shane2482.deadlyworld.blocks.storage.container.ContainerWorkstation;
import shane2482.deadlyworld.library.Reference;
import shane2482.deadlyworld.tiles.TileEntityWorkstation;

public class GuiWorkstation extends GuiContainer{

private static final ResourceLocation GUI = new ResourceLocation(Reference.MOD_ID + ":" + "textures/gui/container/workstation_gui.png");
	
	private TileEntityWorkstation Te;
   

    public GuiWorkstation(InventoryPlayer playerInv, TileEntityWorkstation Te)
    {
        super(new ContainerWorkstation(playerInv, Te));
       this.Te = Te;
        xSize = 253;
        ySize = 159;
    }

    @Override
    public void onGuiClosed() {
    	super.onGuiClosed();
    }
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(I18n.format("Workstation", new Object[0]), 13, 10, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 180, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
    	Minecraft.getMinecraft().getTextureManager().bindTexture(GUI);
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}