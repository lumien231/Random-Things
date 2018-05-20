package lumien.randomthings.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.handler.floo.FlooNetworkHandler;
import lumien.randomthings.tileentity.TileEntityFlooBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFlooSign extends ItemBase
{

	public ItemFlooSign()
	{
		super("flooSign");

		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState state = worldIn.getBlockState(pos);

		if (state.getBlock() == Blocks.BRICK_BLOCK)
		{
			if (!worldIn.isRemote)
			{
				String signName = null;
				ItemStack flooSign = player.getHeldItem(hand);

				if (flooSign.hasTagCompound())
				{
					NBTTagCompound compound = flooSign.getTagCompound();

					if (compound.hasKey("display"))
					{
						NBTTagCompound displayCompound = compound.getCompoundTag("display");

						if (displayCompound.hasKey("Name", 8))
						{
							signName = displayCompound.getString("Name");
						}
					}
				}

				EnumFacing teleportFacing = facing;

				if (!teleportFacing.getAxis().isHorizontal())
				{
					teleportFacing = player.getHorizontalFacing().getOpposite();
				}

				List<BlockPos> brickList = new ArrayList<BlockPos>();

				List<BlockPos> checkStack = new ArrayList<BlockPos>();
				checkStack.add(pos);

				HashSet<BlockPos> checkedPositions = new HashSet<BlockPos>();

				while (!checkStack.isEmpty())
				{
					BlockPos take = checkStack.remove(checkStack.size() - 1);

					if (checkedPositions.contains(take))
					{
						continue;
					}

					checkedPositions.add(take);

					IBlockState takeState = worldIn.getBlockState(take);

					if (takeState.getBlock() == Blocks.BRICK_BLOCK)
					{
						if (brickList.size() > 20)
						{
							return EnumActionResult.FAIL;
						}
						brickList.add(take);

						for (EnumFacing offSetFacing : EnumFacing.HORIZONTALS)
						{
							BlockPos offPos = take.offset(offSetFacing);

							checkStack.add(offPos);
						}
					}
					else if (takeState.getBlock() == ModBlocks.flooBrick)
					{
						return EnumActionResult.FAIL;
					}
				}

				brickList.remove(pos);

				UUID uuid = UUID.randomUUID();

				boolean valid = FlooNetworkHandler.get(worldIn).createFireplace(worldIn, uuid, signName, player, pos, brickList);

				if (valid)
				{
					worldIn.setBlockState(pos, ModBlocks.flooBrick.getDefaultState());
					TileEntityFlooBrick masterTE = (TileEntityFlooBrick) worldIn.getTileEntity(pos);
					masterTE.initToMaster(brickList, teleportFacing, uuid);

					for (BlockPos brickPos : brickList)
					{
						worldIn.setBlockState(brickPos, ModBlocks.flooBrick.getDefaultState());
						TileEntityFlooBrick te = (TileEntityFlooBrick) worldIn.getTileEntity(brickPos);
						te.initToChild(uuid);
					}

					if (!player.capabilities.isCreativeMode)
					{
						flooSign.shrink(1);
					}
				}
			}

			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

}
