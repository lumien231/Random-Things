package lumien.randomthings.asm.confirmer;

import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces.MonumentCoreRoom;

public class ServerConfirmer
{
	public void confirm()
	{
		fun(World.class);
		fun(Block.class);
		fun(EntityLivingBase.class);
		fun(MonumentCoreRoom.class);
		fun(InventoryPlayer.class);
		fun(PlayerInteractionManager.class);
		fun(WorldGenAbstractTree.class);
		fun(EntitySlime.class);
	}
	
	protected void fun(Class class1)
	{
	}
}


