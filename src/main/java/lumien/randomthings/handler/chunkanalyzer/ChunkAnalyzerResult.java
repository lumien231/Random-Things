package lumien.randomthings.handler.chunkanalyzer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ChunkAnalyzerResult
{
	public List<ItemStack> displayStacks;
	public List<String> blockDescriptions;
	public List<Integer> blockCounts;

	public ChunkAnalyzerResult()
	{
		displayStacks = new ArrayList<ItemStack>();
		blockDescriptions = new ArrayList<String>();
		blockCounts = new ArrayList<Integer>();
	}

	public void addBlock(ItemStack displayStack, String blockDescription, int count)
	{
		displayStacks.add(displayStack);
		blockDescriptions.add(blockDescription);
		blockCounts.add(count);
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		NBTTagList tagList = new NBTTagList();

		for (int i = 0; i < displayStacks.size(); i++)
		{
			ItemStack stack = displayStacks.get(i);

			NBTTagCompound entryCompound = new NBTTagCompound();

			NBTTagCompound stackCompound = new NBTTagCompound();
			stack.writeToNBT(stackCompound);

			entryCompound.setTag("stack", stackCompound);

			entryCompound.setString("description", blockDescriptions.get(i));
			entryCompound.setInteger("count", blockCounts.get(i));
			
			tagList.appendTag(entryCompound);
		}

		compound.setTag("entries", tagList);
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		NBTTagList entryList = compound.getTagList("entries", 10);

		for (int i = 0; i < entryList.tagCount(); i++)
		{
			NBTTagCompound entryCompound = entryList.getCompoundTagAt(i);

			ItemStack stack = new ItemStack(entryCompound.getCompoundTag("stack"));

			String description = entryCompound.getString("description");
			int count = entryCompound.getInteger("count");

			displayStacks.add(stack);
			blockDescriptions.add(description);
			blockCounts.add(count);
		}
	}
}
