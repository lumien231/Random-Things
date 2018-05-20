package lumien.randomthings.asm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public class MCPNames
{
	private static Map<String, String> fields;
	private static Map<String, String> methods;

	static
	{
		if (mcp())
		{
			try
			{
				Class gradleClass = Class.forName("net.minecraftforge.gradle.GradleStartCommon");
				Field dirField = gradleClass.getDeclaredField("CSV_DIR");
				dirField.setAccessible(true);
				File mappingDir = (File) dirField.get(null);

				fields = readMappings(new File(mappingDir, "fields.csv"));
				methods = readMappings(new File(mappingDir, "methods.csv"));
			}
			catch (Exception e)
			{
				e.printStackTrace();

				System.out.println("[RT] [ERROR] Error getting mappings from Gradlew SRG, falling back to mcp folder.");
				fields = readMappings(new File("./../mcp/fields.csv"));
				methods = readMappings(new File("./../mcp/methods.csv"));
			}

		}
		else
		{
			fields = methods = null;
		}
	}

	public static boolean mcp()
	{
		return LoadingPlugin.IN_MCP;
	}

	public static String field(String srgName)
	{
		if (mcp())
		{
			return fields.get(srgName);
		}
		else
		{
			return srgName;
		}
	}

	public static String method(String srgName)
	{
		if (mcp())
		{
			return methods.get(srgName);
		}
		else
		{
			return srgName;
		}
	}

	private static Map<String, String> readMappings(File file)
	{
		if (!file.isFile())
		{
			throw new RuntimeException("Couldn't find MCP mappings.");
		}
		try
		{
			return Files.readLines(file, Charsets.UTF_8, new MCPFileParser());
		}
		catch (IOException e)
		{
			throw new RuntimeException("Couldn't read SRG->MCP mappings", e);
		}
	}

	private static class MCPFileParser implements LineProcessor<Map<String, String>>
	{
		private static final Splitter splitter = Splitter.on(',').trimResults();
		private final Map<String, String> map = Maps.newHashMap();
		private boolean foundFirst;

		@Override
		public boolean processLine(String line) throws IOException
		{
			if (!foundFirst)
			{
				foundFirst = true;
				return true;
			}

			Iterator<String> splitted = splitter.split(line).iterator();
			try
			{
				String srg = splitted.next();
				String mcp = splitted.next();
				if (!map.containsKey(srg))
				{
					map.put(srg, mcp);
				}
			}
			catch (NoSuchElementException e)
			{
				throw new IOException("Invalid Mappings file!", e);
			}

			return true;
		}

		@Override
		public Map<String, String> getResult()
		{
			return ImmutableMap.copyOf(map);
		}
	}
}
