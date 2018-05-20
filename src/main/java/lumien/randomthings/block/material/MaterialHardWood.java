package lumien.randomthings.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialHardWood extends Material
{
	public static MaterialHardWood HARD_WOOD = new MaterialHardWood();

	public MaterialHardWood()
	{
		super(MapColor.WOOD);

		this.setRequiresTool();
	}

}
