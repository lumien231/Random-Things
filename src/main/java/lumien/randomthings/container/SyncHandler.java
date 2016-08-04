package lumien.randomthings.container;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.ContainerSynced;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class SyncHandler
{
	static Map<Class, ArrayList<Field>> fieldMap = new HashMap<Class, ArrayList<Field>>();

	public static void postInit(FMLPostInitializationEvent event)
	{
		Set<ASMData> annotationSet = RandomThings.instance.getASMData().getAll(ContainerSynced.class.getName());

		for (ASMData data : annotationSet)
		{
			try
			{
				Class clazz = Class.forName(data.getClassName());
				Field f = clazz.getDeclaredField(data.getObjectName());
				f.setAccessible(true);

				ArrayList<Field> classFieldList;
				if (fieldMap.containsKey(clazz))
				{
					classFieldList = fieldMap.get(clazz);
				}
				else
				{
					fieldMap.put(clazz, classFieldList = new ArrayList<Field>());
				}

				classFieldList.add(f);

				Collections.sort(classFieldList, new Comparator<Field>()
				{
					@Override
					public int compare(Field field1, Field field2)
					{
						return field1.getName().compareTo(field2.getName());
					}
				});
			}
			catch (Exception e)
			{
				RandomThings.instance.logger.log(Level.ERROR, "Error stitching extra textures");
				e.printStackTrace();
			}
		}
	}
}
