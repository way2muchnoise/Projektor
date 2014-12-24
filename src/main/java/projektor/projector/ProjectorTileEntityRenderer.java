package projektor.projector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import projektor.Reference;
import projektor.schematic.BasicSchematic;
import projektor.schematic.BlockWithMetaStorage;

public class ProjectorTileEntityRenderer extends TileEntitySpecialRenderer
{
    ProjectorModel model;
    BasicSchematic schematic;

    public ProjectorTileEntityRenderer()
    {
        this.model = new ProjectorModel();
        this.schematic = new BasicSchematic();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale)
    {
        if (tileEntity instanceof ProjectorTileEntity)
        {
            ProjectorTileEntity blueprintProjector = (ProjectorTileEntity) tileEntity;
            int facing = blueprintProjector.getFacing();
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
            GL11.glRotatef(180f, 0f, 0f, 1f);
            GL11.glRotatef((facing * 90.0F), 0.0F, 1.0F, 0.0F);
            if (blueprintProjector.getHasBlueprint())
            {
                bindTexture(Reference.Model.PROJECTOR_ON);
            } else
            {
                bindTexture(Reference.Model.PROJECTOR_OFF);
            }
            model.render(0.0625F);
            GL11.glPopMatrix();

            if (blueprintProjector.getHasWorldObj())
            {
                this.renderGhostBlockAt(blueprintProjector, x, y, z);
            }
        }
    }

    public void renderGhostBlockAt(ProjectorTileEntity blueprintProjector, double x, double y, double z)
    {

        /* Mount the blocks texture */
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        /* Turn off item lighting */
        RenderHelper.disableStandardItemLighting();

        /* If the player has occlusion enabled */
        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GL11.glShadeModel(GL11.GL_SMOOTH); /* Use smooth shading */

        } else
        {
            GL11.glShadeModel(GL11.GL_FLAT); /* ... or not, that's cool */

        }

        /* Store and translate to our draw position */
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslated(x, y, z);
        /* Get the global tessellator */
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.startDrawingQuads();
        /*
         * Translate the tessellator so that shapes drawn at (x, y, z) are
         * at (0, 0, 0) and so we can draw our block using real-world
         * coordinates properly.
         */
        tessellator.setTranslation((int) -x, (int) -y, (int) -z);

        BlockWithMetaStorage[][][] testArray = new BlockWithMetaStorage[3][1][3];

        testArray[0][0][0] = new BlockWithMetaStorage(Blocks.anvil, 0);
        testArray[0][0][1] = new BlockWithMetaStorage(Blocks.stone, 0);
        testArray[0][0][2] = new BlockWithMetaStorage(Blocks.bedrock, 0);
        testArray[1][0][0] = new BlockWithMetaStorage(Blocks.clay, 0);
        testArray[1][0][1] = new BlockWithMetaStorage(Blocks.brick_block, 0);
        testArray[1][0][2] = new BlockWithMetaStorage(Blocks.wool, 2);
        testArray[2][0][0] = new BlockWithMetaStorage(Blocks.cobblestone, 0);
        testArray[2][0][1] = new BlockWithMetaStorage(Blocks.soul_sand, 0);
        testArray[2][0][2] = new BlockWithMetaStorage(Blocks.bookshelf, 0);

        schematic.setSchematic(testArray);
        /* Process the schematic for rendering onto the tessellator */
        schematic.processSchematicArrayForRendering(blueprintProjector, (int) x, (int) y, (int) z);

        /* Render from the tessellator to the screen */
        tessellator.draw();
        /* Reset the tessellator translation */
        tessellator.setTranslation(0, 0, 0);
        /* Recall the previous model matrix */
        GL11.glPopMatrix();

        /* Restore normal lighting */
        RenderHelper.enableStandardItemLighting();
    }

}
