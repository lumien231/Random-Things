package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockBase extends Block
{
	protected BlockBase(String name, Material materialIn)
	{
		super(materialIn);

		this.setUnlocalizedName(name);
		this.setCreativeTab(RandomThings.instance.creativeTab);

		GameRegistry.registerBlock(this, name);
	}

	protected BlockBase(String name, Material materialIn, Class<? extends ItemBlock> itemBlock)
	{
		super(materialIn);

		this.setUnlocalizedName(name);
		this.setCreativeTab(RandomThings.instance.creativeTab);
		GameRegistry.registerBlock(this, itemBlock, name);
	}
}
