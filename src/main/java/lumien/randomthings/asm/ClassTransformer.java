package lumien.randomthings.asm;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFGT;
import static org.objectweb.asm.Opcodes.IFLT;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;
import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ClassTransformer implements IClassTransformer
{
	Logger logger = LogManager.getLogger("RandomThingsCore");

	final String asmHandler = "lumien/randomthings/handler/AsmHandler";

	public ClassTransformer()
	{
		logger.log(Level.DEBUG, "Starting Class Transformation");
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if (transformedName.equals("net.minecraft.world.World"))
		{
			return patchWorldClass(basicClass);
		}
		else if (transformedName.equals("net.minecraft.client.renderer.BlockRendererDispatcher"))
		{
			return patchBlockRendererDispatcher(basicClass);
		}
		else if (transformedName.equals("net.minecraft.block.Block"))
		{
			return patchBlock(basicClass);
		}
		else if (transformedName.equals("net.minecraft.client.renderer.entity.RenderLivingBase"))
		{
			return patchRenderLivingBase(basicClass);
		}
		else if (transformedName.equals("net.minecraft.entity.EntityLivingBase"))
		{
			return patchEntityLivingBase(basicClass);
		}
		else if (transformedName.equals("net.minecraft.client.renderer.RenderItem"))
		{
			return patchRenderItem(basicClass);
		}
		else if (transformedName.equals("net.minecraft.client.renderer.entity.layers.LayerArmorBase"))
		{
			return patchLayerArmorBase(basicClass);
		}
		else if (transformedName.equals("net.minecraft.world.gen.structure.StructureOceanMonumentPieces$MonumentCoreRoom"))
		{
			// return patchOceanCoreRoom(basicClass); TODO Reimplement Without
			// asm?
		}
		else if (transformedName.equals("net.minecraft.block.BlockLiquid"))
		{
			// return patchLiquidBlock(basicClass); TODO Ehhhh, was this fixed
			// by vanilla?
		}
		else if (transformedName.equals("net.minecraft.client.renderer.EntityRenderer"))
		{
			return patchEntityRenderer(basicClass);
		}
		else if (transformedName.equals("net.minecraft.entity.player.InventoryPlayer"))
		{
			return patchInventoryPlayer(basicClass);
		}

		return basicClass;
	}

	private byte[] patchInventoryPlayer(byte[] basicClass)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		logger.log(Level.DEBUG, "Found InventoryPlayer Class: " + classNode.name);

		MethodNode dropAllItems = null;

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(MCPNames.method("func_70436_m")))
			{
				dropAllItems = mn;
				break;
			}
		}

		if (dropAllItems != null)
		{
			logger.log(Level.DEBUG, " - Found dropAllItems (1/2)");
			for (int i = 0; i < dropAllItems.instructions.size(); i++)
			{
				AbstractInsnNode ain = dropAllItems.instructions.get(i);

				if (ain instanceof JumpInsnNode)
				{
					JumpInsnNode jin = (JumpInsnNode) ain;

					if (jin.getOpcode() == Opcodes.IFNULL)
					{
						LabelNode l0 = jin.label;

						InsnList toInsert = new InsnList();

						toInsert.add(new VarInsnNode(ALOAD, 0));
						toInsert.add(new VarInsnNode(ILOAD, 5));
						toInsert.add(new VarInsnNode(ALOAD, 4));
						toInsert.add(new VarInsnNode(ILOAD, 5));
						toInsert.add(new InsnNode(AALOAD));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, asmHandler, "shouldPlayerDrop", "(Lnet/minecraft/entity/player/InventoryPlayer;ILnet/minecraft/item/ItemStack;)Z"));
						toInsert.add(new JumpInsnNode(IFEQ, l0));

						dropAllItems.instructions.insert(jin, toInsert);

						i += 7;

						logger.log(Level.DEBUG, " - Patched dropAllItems (2/2)");
					}
				}
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchLiquidBlock(byte[] basicClass)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		logger.log(Level.DEBUG, "Found BlockLiquid Class: " + classNode.name);

		MethodNode shouldSideBeRendered = null;

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(MCPNames.method("func_176225_a")))
			{
				shouldSideBeRendered = mn;
				break;
			}
		}

		if (shouldSideBeRendered != null)
		{
			logger.log(Level.DEBUG, " - Found shouldSideBeRendered (1/1)");

			LabelNode l1 = new LabelNode(new Label());

			InsnList toInsert = new InsnList();
			toInsert.add(new VarInsnNode(ALOAD, 0));
			toInsert.add(new VarInsnNode(ALOAD, 1));
			toInsert.add(new VarInsnNode(ALOAD, 2));
			toInsert.add(new VarInsnNode(ALOAD, 3));
			toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "shouldLiquidSideBeRendered", "(Lnet/minecraft/block/BlockLiquid;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)I", false));
			toInsert.add(new InsnNode(DUP));
			toInsert.add(new JumpInsnNode(IFLT, l1));
			toInsert.add(new InsnNode(IRETURN));
			toInsert.add(l1);
			toInsert.add(new InsnNode(POP));

			shouldSideBeRendered.instructions.insert(toInsert);
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchLayerArmorBase(byte[] basicClass)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		logger.log(Level.DEBUG, "Found LayerArmorBase Class: " + classNode.name);

		MethodNode renderEnchantedGlint = null; // Where the effect is rendered
		MethodNode renderArmorLayer = null; // Getting ItemStack from here

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(MCPNames.method("func_188364_a")))
			{
				renderEnchantedGlint = mn;
			}
			else if (mn.name.equals(MCPNames.method("func_188361_a")))
			{
				renderArmorLayer = mn;
			}
		}

		if (renderEnchantedGlint != null)
		{
			logger.log(Level.DEBUG, "- Found renderEnchantedGlint (Effect Rendering 1/2)");

			for (int i = 0; i < renderEnchantedGlint.instructions.size(); i++)
			{
				AbstractInsnNode ain = renderEnchantedGlint.instructions.get(i);

				if (ain instanceof MethodInsnNode)
				{
					MethodInsnNode min = (MethodInsnNode) ain;

					if (min.owner.equals("net/minecraft/client/renderer/GlStateManager") && min.name.equals(MCPNames.method("func_179131_c")))
					{
						renderEnchantedGlint.instructions.insert(min, new MethodInsnNode(INVOKESTATIC, asmHandler, "armorEnchantmentHook", "()V", false));
					}
				}
			}
		}

		int renderCounter = 0;
		if (renderArmorLayer != null)
		{
			logger.log(Level.DEBUG, "- Found renderArmorLayer (ItemStack Information & Armor Coloring 2/2)");
			for (int i = 0; i < renderArmorLayer.instructions.size(); i++)
			{
				AbstractInsnNode ain = renderArmorLayer.instructions.get(i);

				if (ain instanceof MethodInsnNode)
				{
					MethodInsnNode min = (MethodInsnNode) ain;

					if (min.name.equals("func_188364_a"))
					{
						logger.log(Level.DEBUG, "- Set currentlyRendering");
						InsnList toInsert = new InsnList();
						toInsert.add(new VarInsnNode(ALOAD, 10));
						toInsert.add(new FieldInsnNode(PUTSTATIC, asmHandler, "currentlyRendering", "Lnet/minecraft/item/ItemStack;"));
						renderArmorLayer.instructions.insertBefore(min, toInsert);

						i += 2;
					}

					if (min.name.equals(MCPNames.method("func_78088_a")))
					{
						if (renderCounter == 1)
						{
							logger.log(Level.DEBUG, "- Found render");
							InsnList toInsert = new InsnList();

							toInsert.add(new VarInsnNode(ALOAD, 10));
							toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "armorColorHook", "(Lnet/minecraft/item/ItemStack;)V", false));

							renderArmorLayer.instructions.insertBefore(min, toInsert);
						}
						renderCounter++;
					}
				}
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchRenderItem(byte[] basicClass)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		logger.log(Level.DEBUG, "Found RenderItem Class: " + classNode.name);

		MethodNode renderEffect = null;
		MethodNode renderItem = null;
		MethodNode renderQuads = null;

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(MCPNames.method("func_180451_a")))
			{
				renderEffect = mn;
			}
			else if (mn.name.equals(MCPNames.method("func_180454_a")) && mn.desc.equals("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V"))
			{
				renderItem = mn;
			}
			else if (mn.name.equals(MCPNames.method("func_175032_a")))
			{
				renderQuads = mn;
			}
		}

		if (renderEffect != null)
		{
			logger.log(Level.DEBUG, "- Found renderEffect (1/3)");

			for (int i = 0; i < renderEffect.instructions.size(); i++)
			{
				AbstractInsnNode ain = renderEffect.instructions.get(i);

				if (ain instanceof LdcInsnNode)
				{
					LdcInsnNode lin = (LdcInsnNode) ain;

					if (lin.cst.equals(new Integer(-8372020)))
					{
						logger.log(Level.DEBUG, "- Found Texture Binding");

						renderEffect.instructions.insert(lin, new MethodInsnNode(INVOKESTATIC, asmHandler, "enchantmentColorHook", "()I", false));
						renderEffect.instructions.remove(lin);
					}
				}
			}
		}

		if (renderItem != null)
		{
			boolean found = false;
			logger.log(Level.DEBUG, "- Found renderItem (2/3) (" + renderItem.desc + ")");

			for (int i = 0; i < renderItem.instructions.size(); i++)
			{
				AbstractInsnNode ain = renderItem.instructions.get(i);

				if (ain instanceof MethodInsnNode)
				{
					MethodInsnNode min = (MethodInsnNode) ain;

					if (!found)
					{
						if (min.name.equals(MCPNames.method("func_180451_a")))
						{
							logger.log(Level.DEBUG, "- Found renderEffect calling");

							InsnList toInsert = new InsnList();
							toInsert.add(new VarInsnNode(ALOAD, 1));
							toInsert.add(new FieldInsnNode(PUTSTATIC, asmHandler, "currentlyRendering", "Lnet/minecraft/item/ItemStack;"));
							renderItem.instructions.insertBefore(min, toInsert);
							found = true;
						}
					}

					if (min.name.equals(MCPNames.method("func_179022_a")))
					{
						LabelNode l1 = new LabelNode(new Label());
						LabelNode l2 = new LabelNode(new Label());
						logger.log(Level.DEBUG, "- Inserting TE Item Renderer");
						InsnList insertBefore = new InsnList();

						insertBefore.add(new FieldInsnNode(GETSTATIC, "lumien/randomthings/client/RandomThingsTEItemRenderer", "instance", "Llumien/randomthings/client/RandomThingsTEItemRenderer;"));
						insertBefore.add(new VarInsnNode(ALOAD, 1));
						insertBefore.add(new MethodInsnNode(INVOKEVIRTUAL, "lumien/randomthings/client/RandomThingsTEItemRenderer", "renderByItem", "(Lnet/minecraft/item/ItemStack;)Z", false));
						insertBefore.add(new JumpInsnNode(IFEQ, l2));
						insertBefore.add(new InsnNode(POP));
						insertBefore.add(new InsnNode(POP));
						insertBefore.add(new JumpInsnNode(GOTO, l1));
						insertBefore.add(l2);

						InsnList insertAfter = new InsnList();
						insertAfter.add(l1);

						renderItem.instructions.insertBefore(min, insertBefore);
						renderItem.instructions.insert(min, insertAfter);

						i += 8;
					}
				}
			}
		}

		if (renderQuads != null)
		{
			logger.log(Level.DEBUG, "- Found renderQuads (3/3) (" + renderQuads.desc + ")");

			for (int i = 0; i < renderQuads.instructions.size(); i++)
			{
				AbstractInsnNode ain = renderQuads.instructions.get(i);

				if (ain instanceof MethodInsnNode)
				{
					MethodInsnNode min = (MethodInsnNode) ain;

					if (min.name.equals("renderQuadColor"))
					{
						InsnList toInsert = new InsnList();

						toInsert.add(new InsnNode(POP));
						toInsert.add(new VarInsnNode(ALOAD, 4));
						toInsert.add(new VarInsnNode(ILOAD, 9));
						toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "getColorFromItemStack", "(Lnet/minecraft/item/ItemStack;I)I", false));
						renderQuads.instructions.insertBefore(min, toInsert);
						i += 4;
					}
				}
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchEntityLivingBase(byte[] basicClass)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		logger.log(Level.DEBUG, "Found EntityLivingBase Class: " + classNode.name);

		MethodNode updatePotionEffects = null;

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(MCPNames.method("func_70679_bo")))
			{
				updatePotionEffects = mn;
			}
		}

		if (updatePotionEffects != null)
		{
			logger.log(Level.DEBUG, "- Found updatePotionEffects (1/1)");

			for (int i = 0; i < updatePotionEffects.instructions.size(); i++)
			{
				AbstractInsnNode ain = updatePotionEffects.instructions.get(i);
				if (ain instanceof FieldInsnNode)
				{
					FieldInsnNode fin = (FieldInsnNode) ain;
					if (fin.name.equals(MCPNames.field("field_70180_af")))
					{
						AbstractInsnNode aload = updatePotionEffects.instructions.get(i - 1);

						InsnList toInsert = new InsnList();

						LabelNode l1 = new LabelNode(new Label());

						toInsert.add(new VarInsnNode(ALOAD, 0));
						toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "shouldRenderPotionParticles", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false));
						toInsert.add(new JumpInsnNode(IFGT, l1));
						toInsert.add(new InsnNode(RETURN));
						toInsert.add(l1);

						updatePotionEffects.instructions.insertBefore(aload, toInsert);
						break;
					}
				}
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchRenderLivingBase(byte[] basicClass)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		logger.log(Level.DEBUG, "Found RenderLivingBase Class: " + classNode.name);

		MethodNode canRenderName = null;

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(MCPNames.method("func_177070_b")))
			{
				canRenderName = mn;
				break;
			}
		}

		if (canRenderName != null)
		{
			logger.log(Level.DEBUG, "- Found canRenderName (1/1)");
			LabelNode l1 = new LabelNode(new Label());
			InsnList toInsert = new InsnList();

			toInsert.add(new VarInsnNode(ALOAD, 1));
			toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "canRenderName", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false));
			toInsert.add(new JumpInsnNode(IFGT, l1));
			toInsert.add(new InsnNode(ICONST_0));
			toInsert.add(new InsnNode(IRETURN));
			toInsert.add(l1);

			canRenderName.instructions.insert(toInsert);
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchBlock(byte[] basicClass)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		logger.log(Level.DEBUG, "Found Block Class: " + classNode.name);

		MethodNode addCollisionBoxesToList = null;

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(MCPNames.method("func_185477_a")) && mn.desc.equals("(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;)V"))
			{
				addCollisionBoxesToList = mn;
			}
		}

		if (addCollisionBoxesToList != null)
		{
			logger.log(Level.DEBUG, "- Found addCollisionBoxesToList (1/1)");

			InsnList toInsert = new InsnList();
			LabelNode l1 = new LabelNode(new Label());

			toInsert.add(new VarInsnNode(ALOAD, 1));
			toInsert.add(new VarInsnNode(ALOAD, 2));
			toInsert.add(new VarInsnNode(ALOAD, 3));
			toInsert.add(new VarInsnNode(ALOAD, 4));
			toInsert.add(new VarInsnNode(ALOAD, 5));
			toInsert.add(new VarInsnNode(ALOAD, 6));
			toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "addCollisionBoxesToList", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;)Z", false));
			toInsert.add(new JumpInsnNode(IFEQ, l1));
			toInsert.add(new InsnNode(RETURN));
			toInsert.add(l1);

			addCollisionBoxesToList.instructions.insert(toInsert);
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchBlockRendererDispatcher(byte[] basicClass)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		logger.log(Level.DEBUG, "Found BlockRendererDispatcher Class: " + classNode.name);

		MethodNode renderBlock = null;

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(MCPNames.method("func_175018_a")))
			{
				renderBlock = mn;
			}
		}

		if (renderBlock != null)
		{
			logger.log(Level.DEBUG, "- Found renderBlock (1/1)");

			InsnList toInsert = new InsnList();
			LabelNode l1 = new LabelNode(new Label());

			toInsert.add(new VarInsnNode(ALOAD, 0));
			toInsert.add(new VarInsnNode(ALOAD, 1));
			toInsert.add(new VarInsnNode(ALOAD, 2));
			toInsert.add(new VarInsnNode(ALOAD, 3));
			toInsert.add(new VarInsnNode(ALOAD, 4));
			toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "renderBlock", "(Lnet/minecraft/client/renderer/BlockRendererDispatcher;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/VertexBuffer;)I", false));
			toInsert.add(new InsnNode(DUP));
			toInsert.add(new InsnNode(ICONST_2));
			toInsert.add(new JumpInsnNode(IF_ICMPEQ, l1));
			toInsert.add(new InsnNode(IRETURN));
			toInsert.add(l1);
			toInsert.add(new InsnNode(POP));

			renderBlock.instructions.insert(toInsert);
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchWorldClass(byte[] basicClass)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		logger.log(Level.DEBUG, "Found World Class: " + classNode.name);

		MethodNode getRedstonePower = null;
		MethodNode getStrongPower = null;
		MethodNode isRainingAt = null;

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(MCPNames.method("func_175651_c")))
			{
				getRedstonePower = mn;
			}
			else if (mn.name.equals(MCPNames.method("func_175627_a")) && mn.desc.equals("(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)I"))
			{
				getStrongPower = mn;
			}
			else if (mn.name.equals(MCPNames.method("func_175727_C")))
			{
				isRainingAt = mn;
			}
		}

		if (getRedstonePower != null)
		{
			logger.log(Level.DEBUG, "- Found getRedstonePower (1/3)");

			InsnList toInsert = new InsnList();

			LabelNode l1 = new LabelNode(new Label());

			toInsert.add(new VarInsnNode(ALOAD, 0));
			toInsert.add(new VarInsnNode(ALOAD, 1));
			toInsert.add(new VarInsnNode(ALOAD, 2));
			toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "getRedstonePower", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)I", false));
			toInsert.add(new InsnNode(DUP));
			toInsert.add(new JumpInsnNode(IFEQ, l1));
			toInsert.add(new InsnNode(IRETURN));
			toInsert.add(l1);
			toInsert.add(new InsnNode(POP));

			getRedstonePower.instructions.insert(toInsert);
		}

		if (getStrongPower != null)
		{
			logger.log(Level.DEBUG, "- Found getStrongPower (2/3)");

			InsnList toInsert = new InsnList();

			LabelNode l1 = new LabelNode(new Label());

			toInsert.add(new VarInsnNode(ALOAD, 0));
			toInsert.add(new VarInsnNode(ALOAD, 1));
			toInsert.add(new VarInsnNode(ALOAD, 2));
			toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "getStrongPower", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)I", false));
			toInsert.add(new InsnNode(DUP));
			toInsert.add(new JumpInsnNode(IFEQ, l1));
			toInsert.add(new InsnNode(IRETURN));
			toInsert.add(l1);
			toInsert.add(new InsnNode(POP));

			getStrongPower.instructions.insert(toInsert);
		}

		if (isRainingAt != null)
		{
			logger.log(Level.DEBUG, "- Found isRainingAt (3/3)");

			AbstractInsnNode returnNode = isRainingAt.instructions.get(isRainingAt.instructions.size() - 2);

			InsnList toInsert = new InsnList();
			LabelNode returnLabel = new LabelNode(new Label());

			toInsert.add(new InsnNode(Opcodes.DUP));
			toInsert.add(new JumpInsnNode(IFEQ, returnLabel));
			toInsert.add(new InsnNode(POP));
			toInsert.add(new VarInsnNode(ALOAD, 0));
			toInsert.add(new VarInsnNode(ALOAD, 1));
			toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "shouldRain", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false));
			toInsert.add(returnLabel);

			isRainingAt.instructions.insertBefore(returnNode, toInsert);
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchEntityRenderer(byte[] basicClass)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		logger.log(Level.DEBUG, "Found EntityRenderer Class: " + classNode.name);

		MethodNode renderRainSnow = null;
		MethodNode addRainParticles = null;

		for (MethodNode mn : classNode.methods)
		{
			if (mn.name.equals(MCPNames.method("func_78474_d")))
			{
				renderRainSnow = mn;
			}
			else if (mn.name.equals(MCPNames.method("func_78484_h")))
			{
				addRainParticles = mn;
			}
		}

		if (renderRainSnow != null)
		{
			logger.log(Level.DEBUG, "- Found renderRainSnow");

			VarInsnNode insnPoint = null;
			for (int i = 0; i < renderRainSnow.instructions.size(); i++)
			{
				AbstractInsnNode ain = renderRainSnow.instructions.get(i);
				if (ain instanceof MethodInsnNode)
				{
					MethodInsnNode min = (MethodInsnNode) ain;

					if (min.name.equals(MCPNames.method("func_76738_d")))
					{
						logger.log(Level.DEBUG, "- Found canRain");

						insnPoint = (VarInsnNode) renderRainSnow.instructions.get(i - 1);
					}

					if (min.name.equals(MCPNames.method("func_76746_c")))
					{
						logger.log(Level.DEBUG, "- Found getEnableSnow");
						int jumpCounter = i + 1;

						int worldIndex = 5;
						int blockPosIndex = 21;

						// Optifine Why :'(
						for (LocalVariableNode lv : renderRainSnow.localVariables)
						{
							if (lv.desc.equals("Lnet/minecraft/client/multiplayer/WorldClient;") || lv.desc.equals("Lnet/minecraft/world/World;"))
							{
								worldIndex = lv.index;
							}
							else if (lv.desc.equals("Lnet/minecraft/util/math/BlockPos$MutableBlockPos;"))
							{
								blockPosIndex = lv.index;
							}
						}

						AbstractInsnNode jumpNode;

						while (!((jumpNode = renderRainSnow.instructions.get(jumpCounter)) instanceof JumpInsnNode))
						{
							jumpCounter++;
						}

						JumpInsnNode jin = (JumpInsnNode) jumpNode;
						LabelNode labelNode = jin.label;

						InsnList toInsert = new InsnList();
						toInsert.add(new VarInsnNode(ALOAD, worldIndex));
						toInsert.add(new VarInsnNode(ALOAD, blockPosIndex));
						toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "shouldRain", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false));
						toInsert.add(new JumpInsnNode(IFEQ, labelNode));
						renderRainSnow.instructions.insertBefore(insnPoint, toInsert);
						i += 4;
					}
				}
			}
		}

		if (addRainParticles != null)
		{
			logger.log(Level.DEBUG, "- Found addRainParticles");

			for (int i = 0; i < addRainParticles.instructions.size(); i++)
			{
				AbstractInsnNode ain = addRainParticles.instructions.get(i);
				if (ain instanceof JumpInsnNode)
				{
					JumpInsnNode jin = (JumpInsnNode) ain;
					if (jin.getOpcode() == Opcodes.IF_ICMPGT)
					{
						LabelNode jumpTarget = jin.label;

						InsnList toInsert = new InsnList();
						toInsert.add(new VarInsnNode(ALOAD, 3));
						toInsert.add(new VarInsnNode(ALOAD, 15));
						toInsert.add(new MethodInsnNode(INVOKESTATIC, asmHandler, "shouldRain", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false));
						toInsert.add(new JumpInsnNode(IFEQ, jumpTarget));

						addRainParticles.instructions.insert(jin, toInsert);

						break;
					}
				}
			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}

	private byte[] patchDummyClass(byte[] basicClass)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		logger.log(Level.DEBUG, "Found Dummy Class: " + classNode.name);

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		return writer.toByteArray();
	}
}
