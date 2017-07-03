package lumien.randomthings;

import java.lang.reflect.Constructor;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.RegistryEntry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class RTRegistryHandler
{
	public static IForgeRegistry currentRegistry;

	Multimap<Class, IForgeRegistryEntry.Impl> classMap = HashMultimap.create();

	public void preInit()
	{
		try
		{
			Set<ASMData> asmData = RandomThings.instance.asmDataTable.getAll(RegistryEntry.class.getName());

			for (ASMData data : asmData)
			{
				Class registrant = Class.forName(data.getClassName());

				Constructor[] constructors = registrant.getConstructors();
				
				Constructor booleanCons = null;
				
				for (Constructor c : constructors)
				{
					if (c.getParameterTypes().length == 1)
					{
						if (c.getParameterTypes()[0] == boolean.class)
						{
							booleanCons = c;
							break;
						}
					}
				}
				
				if (booleanCons != null)
				{
					Object o1 = booleanCons.newInstance(false);
					Object o2 = booleanCons.newInstance(true);
					
					Class registryType = ((IForgeRegistryEntry.Impl) o1).getRegistryType();

					classMap.put(registryType, (Impl) o1);
					classMap.put(registryType, (Impl) o2);
				}
				else
				{
					Object o = registrant.newInstance();

					Class registryType = ((IForgeRegistryEntry.Impl) o).getRegistryType();

					classMap.put(registryType, (Impl) o);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new RuntimeException("Couldn't register RT Objects, can't recover from that :(.");
		}
	}

	@SubscribeEvent
	public void register(RegistryEvent.Register event)
	{
		Class type = event.getRegistry().getRegistrySuperType();

		if (classMap.containsKey(type))
		{
			for (Impl i : classMap.get(type))
			{
				event.getRegistry().register(i);

				System.out.println("Registering " + i.getRegistryName().toString());
			}
		}
	}
}
