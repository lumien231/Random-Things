package lumien.randomthings.handler.magicavoxel;

import net.minecraft.network.NetHandlerPlayServer;

public class ServerModelRequest
{
	String modelName;
	NetHandlerPlayServer netHandler;

	int bytesSend;

	public ServerModelRequest(String modelName, NetHandlerPlayServer netHandler)
	{
		this.modelName = modelName;
		this.netHandler = netHandler;
	}
}
