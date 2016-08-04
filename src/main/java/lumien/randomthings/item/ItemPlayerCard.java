package lumien.randomthings.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemPlayerCard extends ItemBase
{

	public ItemPlayerCard()
	{
		super("playerCard");
		
		this.setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		NBTTagCompound compound;
		
		if ((compound = stack.getTagCompound())!=null)
		{
			if (compound.hasKey("player-name"))
			{
				tooltip.add(TextFormatting.GRAY.toString()+ compound.getString("player-name"));
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (!worldIn.isRemote)
		{
			NBTTagCompound compound;
			
			if ((compound = itemStackIn.getTagCompound())==null)
			{
				compound = new NBTTagCompound();
				itemStackIn.setTagCompound(compound);
			}
			
			compound.setString("player-uuid", playerIn.getGameProfile().getId().toString());
			compound.setString("player-name", playerIn.getGameProfile().getName());
		}
		
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

}
