package projektor.io;

import cpw.mods.fml.common.registry.GameRegistry;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;
import projektor.schematic.BlockWithMetaStorage;

public class IO
{

    public static void serialize(BlockWithMetaStorage[][][] dim3, String fileName)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(fileName + ".txt");

            for (BlockWithMetaStorage[][] dim2 : dim3)
            {
                for (BlockWithMetaStorage[] dim1 : dim2)
                {
                    for (BlockWithMetaStorage block : dim1)
                    {
                        fos.write(("<" + GameRegistry.findUniqueIdentifierFor(block.getBlock()).toString() + ":" + block.getMeta() + "> ").getBytes());
                    }
                    fos.write(("\n").getBytes());
                }
                fos.write(("\n\n").getBytes());
            }
            fos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static BlockWithMetaStorage[][][] deserialize(String fileName)
    {
        BlockWithMetaStorage[][][] dim3 = null;
        try
        {
            List<String> raw = Files.readAllLines(FileSystems.getDefault().getPath(fileName + ".txt"), Charset.defaultCharset());
            int dim1s = raw.get(0).split(" ").length;
            int dim2s = raw.indexOf("");
            int dim3s = 0;
            for (int i = 0; i < raw.size() - 1; i++)
            {
                if (raw.get(i).equals("") && raw.get(i + 1).equals(""))
                {
                    dim3s++;
                }
            }
            dim3 = new BlockWithMetaStorage[dim3s][dim2s][dim1s];
            int id1, id2, id3;
            id1 = id2 = id3 = 0;
            boolean prevBlank = false;
            for (String line : raw)
            {
                String[] blocks = line.split(" ");
                for (String block : blocks)
                {
                    if (block.equals(""))
                    {
                        continue;
                    }
                    block = block.substring(1, block.length() - 1);
                    String[] parts = block.split(":");
                    dim3[id3][id2][id1] = new BlockWithMetaStorage(GameRegistry.findBlock(parts[0], parts[1]), Integer.getInteger(parts[2], 0));
                    id1++;
                }
                id1 = 0;
                id2++;
                if (prevBlank && line.equals(""))
                {
                    id3++;
                    id2 = 0;
                }
                prevBlank = line.equals("");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return dim3;
    }
}
