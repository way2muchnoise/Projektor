package projektor.helper;

import net.minecraftforge.common.util.ForgeDirection;

/**
 *
 * @author jakimfett
 */
public class RotationHelper
{

    public static final ForgeDirection[] ROTATION_DIRECTIONS =
    {
        ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.EAST
    };

    public static ForgeDirection getDirectionFromMetadata(int meta)
    {
        return ROTATION_DIRECTIONS[meta];
    }

}
