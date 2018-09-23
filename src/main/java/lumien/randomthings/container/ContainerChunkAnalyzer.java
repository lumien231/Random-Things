package lumien.randomthings.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lumien.randomthings.client.gui.GuiChunkAnalyzer;
import lumien.randomthings.handler.chunkanalyzer.ChunkAnalyzerResult;
import lumien.randomthings.item.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerChunkAnalyzer extends Container
{
	EntityPlayer player;

	public boolean scanning;

	int chunkX;
	int chunkZ;

	int nextX;
	int nextZ;

	Map<IBlockState, Integer> countMap;
	
	static class BlockResult
	{
		IBlockState state;
		int count;
		String name;
		ItemStack stack;
	}
	
	@SideOnly(Side.CLIENT)
	GuiChunkAnalyzer parent;

	public ContainerChunkAnalyzer(EntityPlayer player, World world, int x, int y, int z)
	{
		this.player = player;
	}
	
	@SideOnly(Side.CLIENT)
	public void setParent(GuiChunkAnalyzer parent)
	{
		this.parent = parent;
	}

	public void startScanning()
	{
		this.scanning = true;
		this.countMap = new HashMap<IBlockState, Integer>();

		Chunk targetChunk = player.world.getChunkFromBlockCoords(player.getPosition());

		this.chunkX = targetChunk.x;
		this.chunkZ = targetChunk.z;

		sendScanning();
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		player.inventoryContainer.detectAndSendChanges();

		for (int q = 0; q < 10 && scanning; q++)
		{
			if (scanning)
			{
				Chunk c = player.world.getChunkFromChunkCoords(chunkX, chunkZ);

				for (int y = 0; y < c.getHeightValue(nextX, nextZ); y++)
				{
					IBlockState state = c.getBlockState(nextX, y, nextZ);

					if (!state.getBlock().isAir(state, player.world, new BlockPos(c.x + nextX, y, c.z + nextZ)))
					{
						if (countMap.containsKey(state))
						{
							countMap.put(state, countMap.get(state) + 1);
						}
						else
						{
							countMap.put(state, 1);
						}
					}
				}

				if (nextX == 15)
				{
					nextZ++;

					nextX = 0;

					if (nextZ == 16)
					{
						// Finished;
						nextX = nextZ = 0;

						List<BlockResult> resultList = new ArrayList<>();

						Map<String, BlockResult> nameMap = new HashMap<String, BlockResult>();

						for (Entry<IBlockState, Integer> entry : countMap.entrySet())
						{
							IBlockState state = entry.getKey();

							BlockResult br = new BlockResult();
							br.state = state;
							br.count = entry.getValue();

							int meta = state.getBlock().getMetaFromState(state);
							Item i = Item.getItemFromBlock(br.state.getBlock());

							String name;

							if (i == null)
							{
								name = state.getBlock().getLocalizedName();
								br.stack = ItemStack.EMPTY;
							}
							else
							{
								br.stack = new ItemStack(i, 1, meta);
								name = br.stack.getDisplayName();
								if (name.equals("Air"))
								{
									name = state.getBlock().getLocalizedName();
								}
							}

							if (name != null)
							{
								br.name = name;
								if (nameMap.containsKey(name))
								{
									nameMap.get(name).count += br.count;
								}
								else
								{
									nameMap.put(name, br);
									resultList.add(br);
								}
							}
						}

						resultList.sort((a, b) -> {
							return b.count - a.count;
						});

						ChunkAnalyzerResult result = new ChunkAnalyzerResult();

						for (BlockResult br : resultList)
						{
							result.addBlock(br.stack, br.name, br.count);
						}

						ItemStack analyzer = player.getHeldItemMainhand();

						if (!analyzer.isEmpty() && analyzer.getItem() == ModItems.chunkAnalyzer)
						{
							NBTTagCompound cmp = analyzer.getOrCreateSubCompound("result");

							result.writeToNBT(cmp);

							player.inventoryContainer.detectAndSendChanges();

							scanning = false;
							countMap = null;

							sendScanning();
						}
					}
				}
				else
				{
					nextX++;
				}
			}
		}
	}

	private void sendScanning()
	{
		for (int i = 0; i < this.listeners.size(); ++i)
		{
			IContainerListener icrafting = this.listeners.get(i);

			icrafting.sendWindowProperty(this, 0, this.scanning ? 1 : 0);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		super.updateProgressBar(id, data);

		if (id == 0)
		{
			this.scanning = data == 0 ? false : true;
			
			this.parent.setScanning(this.scanning);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}
}
