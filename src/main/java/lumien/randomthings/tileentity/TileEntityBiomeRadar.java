package lumien.randomthings.tileentity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import lumien.randomthings.client.particles.EntityColoredSmokeFX;
import lumien.randomthings.item.ItemBiomeCrystal;
import lumien.randomthings.item.ItemPositionFilter;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.network.messages.sync.MessageBiomeRadarAntenna;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBiomeRadar extends TileEntityBase implements ITickable
{
	ItemStack currentCrystal;

	boolean powered;
	STATE state = STATE.IDLE;

	Biome biomeToSearch;
	int searchCounter = 0;

	int antennaCounter = 0;
	String[] antennaBiomes = new String[4];

	int testCounter = 0;

	public enum STATE
	{
		IDLE, SEARCHING, FINISHED;
	}

	@Override
	public void update()
	{
		if (!this.worldObj.isRemote)
		{
			testCounter++;

			if (testCounter % 60 == 0 && this.state != STATE.IDLE && !isValid())
			{
				this.state = STATE.IDLE;
				syncTE();
			}

			if (this.state == STATE.SEARCHING)
			{
				boolean changedColor = false;
				for (int i = 0; i < 5; i++)
				{
					BlockPos testPos = getPos(searchCounter);
					Biome testBiome = this.worldObj.getBiome(testPos);
					ResourceLocation registryName = testBiome.getRegistryName();

					boolean exist = false;
					for (int s = 0; s < 4; s++)
					{
						if (registryName.toString().equals(antennaBiomes[s]))
						{
							exist = true;
						}
					}

					if (!exist)
					{
						antennaBiomes[antennaCounter] = registryName.toString();

						changedColor = true;

						if (antennaCounter >= 3)
						{
							antennaCounter = 0;
						}
						else
						{
							antennaCounter++;
						}
					}

					if (testBiome == biomeToSearch)
					{
						this.state = STATE.FINISHED;

						this.syncTE();
						break;
					}

					searchCounter++;
				}

				if (searchCounter % 100 == 0 && this.worldObj != null)
				{
					if (changedColor)
					{
						changedColor = false;
						MessageBiomeRadarAntenna message = new MessageBiomeRadarAntenna(antennaBiomes, this.pos);
						MessageUtil.sendToAllWatchingPos(this.worldObj, this.pos, message);
					}
				}
			}
		}
		else
		{
			spawnParticles();
		}
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticles()
	{
		if (this.state == STATE.SEARCHING)
		{
			for (int i = 0; i < 4; i++)
			{
				if (antennaBiomes[i] != null)
				{
					String biomeName = antennaBiomes[i];
					Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(biomeName));
					if (biome != null)
					{
						Color color = new Color(RenderUtils.getBiomeColor(null, biome, this.pos));

						EntityColoredSmokeFX particle = new EntityColoredSmokeFX(this.worldObj, i < 2 ? this.pos.getX() + 1.5 - i * 2 : this.pos.getX() + 0.5, this.pos.getY() + 4.1, i > 1 ? this.pos.getZ() + 1.5 - (i - 2) * 2 : this.pos.getZ() + 0.5, 0, 0, 0);
						particle.setRBGColorF(1F / 255F * color.getRed(), 1F / 255F * color.getGreen(), 1F / 255F * color.getBlue());

						Minecraft.getMinecraft().effectRenderer.addEffect(particle);
					}
				}
			}
		}
		else if (this.state == STATE.FINISHED)
		{
			Biome b = ItemBiomeCrystal.getBiome(currentCrystal);

			if (b != null)
			{
				Color color = new Color(RenderUtils.getBiomeColor(null, b, this.pos));
				for (int i = 0; i < 4; i++)
				{
					EntityColoredSmokeFX particle = new EntityColoredSmokeFX(this.worldObj, i < 2 ? this.pos.getX() + 1.5 - i * 2 : this.pos.getX() + 0.5, this.pos.getY() + 4.1, i > 1 ? this.pos.getZ() + 1.5 - (i - 2) * 2 : this.pos.getZ() + 0.5, 0, 0, 0);
					particle.setRBGColorF(1F / 255F * color.getRed(), 1F / 255F * color.getGreen(), 1F / 255F * color.getBlue());

					Minecraft.getMinecraft().effectRenderer.addEffect(particle);
				}

				EntityColoredSmokeFX particle = new EntityColoredSmokeFX(this.worldObj, this.pos.getX() + 0.5, this.pos.getY() + 3.1, this.pos.getZ() + 0.5, 0, 0, 0);
				particle.setRBGColorF(1F / 255F * color.getRed(), 1F / 255F * color.getGreen(), 1F / 255F * color.getBlue());

				Minecraft.getMinecraft().effectRenderer.addEffect(particle);
			}
		}
	}

	private BlockPos getPos(int n)
	{
		double x = 0;
		double z = 0;

		// given n an index in the squared spiral
		// p the sum of point in inner square
		// a the position on the current square
		// n = p + a

		double r = Math.floor((Math.sqrt(n + 1) - 1) / 2) + 1;

		// compute radius : inverse arithmetic sum of 8+16+24+...=
		double p = (8 * r * (r - 1)) / 2;
		// compute total point on radius -1 : arithmetic sum of 8+16+24+...

		double en = r * 2;
		// points by face

		double a = (1 + n - p) % (r * 8);
		// compute de position and shift it so the first is (-r,-r) but
		// (-r+1,-r)
		// so square can connect

		switch ((int) Math.floor(a / (r * 2)))
		{
			// find the face : 0 top, 1 right, 2, bottom, 3 left
			case 0:
				x = a - r;
				z = -r;
				break;
			case 1:
				x = r;
				z = (a % en) - r;
				break;
			case 2:
				x = r - (a % en);
				z = r;
				break;
			case 3:
				x = -r;
				z = r - (a % en);
				break;
		}

		return new BlockPos(this.pos.getX() + x * 48, this.pos.getY(), this.pos.getZ() + z * 48);
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		if (currentCrystal != null)
		{
			compound.setTag("currentCrystal", currentCrystal.serializeNBT());
		}

		compound.setInteger("state", state.ordinal());
		compound.setBoolean("powered", powered);

		if (biomeToSearch != null)
		{
			compound.setString("biomeToSearch", biomeToSearch.getRegistryName().toString());
		}

		compound.setInteger("searchCounter", searchCounter);
		compound.setInteger("antennaCounter", antennaCounter);

		for (int i = 0; i < antennaBiomes.length; i++)
		{
			if (antennaBiomes[i] != null)
			{
				compound.setString("antennaBiome" + i, antennaBiomes[i]);
			}
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		this.currentCrystal = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("currentCrystal"));
		this.state = STATE.values()[compound.getInteger("state")];
		this.powered = compound.getBoolean("powered");

		if (compound.hasKey("biomeToSearch"))
		{
			this.biomeToSearch = Biome.REGISTRY.getObject(new ResourceLocation(compound.getString("biomeToSearch")));
		}

		this.searchCounter = compound.getInteger("searchCounter");
		this.antennaCounter = compound.getInteger("antennaCounter");


		for (int i = 0; i < antennaBiomes.length; i++)
		{
			if (compound.hasKey("antennaBiome" + i))
			{
				antennaBiomes[i] = compound.getString("antennaBiome" + i);
			}
		}
	}

	public ItemStack getCurrentCrystal()
	{
		return currentCrystal;
	}

	public void setCrystal(ItemStack crystal)
	{
		this.currentCrystal = crystal;
		this.syncTE();
	}

	public STATE getState()
	{
		return state;
	}

	public void neighborChanged(Block neighborBlock)
	{
		boolean newPowered = this.worldObj.isBlockIndirectlyGettingPowered(this.pos) > 0;
		boolean changed = false;

		if (!this.powered && newPowered && this.state == STATE.IDLE && this.currentCrystal != null && isValid())
		{
			Biome biome = ItemBiomeCrystal.getBiome(this.currentCrystal);

			if (biome != null)
			{
				this.state = STATE.SEARCHING;
				this.searchCounter = 0;
				this.biomeToSearch = biome;
				changed = true;
			}
		}
		else if (this.state == STATE.SEARCHING && !newPowered && powered)
		{
			this.state = STATE.IDLE;
			changed = true;
		}
		else if (this.state == STATE.FINISHED && powered && !newPowered)
		{
			this.state = STATE.IDLE;
			changed = true;
		}

		this.powered = newPowered;

		if (changed)
		{
			this.syncTE();
		}
	}

	public void setAntennaBiomes(String[] antennaBiomes)
	{
		this.antennaBiomes = antennaBiomes;
	}

	public ItemStack generatePositionFilter()
	{
		BlockPos pos = getPos(searchCounter);
		ItemStack positionFilter = new ItemStack(ModItems.positionFilter);
		ItemPositionFilter.setPosition(positionFilter, this.worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ());

		return positionFilter;
	}

	private boolean isValid()
	{
		List<BlockPos> posToCheck = new ArrayList<BlockPos>();

		posToCheck.add(this.pos.offset(EnumFacing.UP));
		posToCheck.add(this.pos.offset(EnumFacing.UP, 2));

		posToCheck.add(this.pos.add(1, 2, 0));
		posToCheck.add(this.pos.add(-1, 2, 0));
		posToCheck.add(this.pos.add(1, 3, 0));
		posToCheck.add(this.pos.add(-1, 3, 0));

		posToCheck.add(this.pos.add(0, 2, 1));
		posToCheck.add(this.pos.add(0, 2, -1));
		posToCheck.add(this.pos.add(0, 3, 1));
		posToCheck.add(this.pos.add(0, 3, -1));

		for (BlockPos pos : posToCheck)
		{
			if (this.worldObj.getBlockState(pos).getBlock() != Blocks.IRON_BARS)
			{
				return false;
			}
		}

		return true;
	}
}
