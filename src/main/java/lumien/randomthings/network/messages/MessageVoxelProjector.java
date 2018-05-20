package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.container.ContainerVoxelProjector;
import lumien.randomthings.network.IRTMessage;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.tileentity.TileEntityVoxelProjector;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageVoxelProjector implements IRTMessage
{
	enum VALUE
	{
		MODEL_ROTATION, MODEL, SCALE, ROTATION_SPEED, AMBIENT_LIGHT, RANDOMIZE;
	}

	BlockPos pos;
	VALUE value;

	// Model Rotation
	int newModelRotation;

	// Model
	String newModel;

	// Scale
	int newScale;

	// Rotation Speed
	int newRotationSpeed;

	// Ambient Light
	boolean newAmbientLight;

	// Randomize
	boolean newRandomize;

	public MessageVoxelProjector()
	{

	}

	public MessageVoxelProjector(BlockPos pos)
	{
		this.pos = pos;
	}

	public void setModel(String newModel)
	{
		this.value = VALUE.MODEL;
		this.newModel = newModel;
	}

	public void setModelRotation(int newModelRotation)
	{
		this.value = VALUE.MODEL_ROTATION;
		this.newModelRotation = newModelRotation;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.value = VALUE.values()[buf.readInt()];
		this.pos = MessageUtil.readBlockPos(buf);

		switch (value)
		{
		case MODEL_ROTATION:
			this.newModelRotation = buf.readInt();
			break;
		case MODEL:
			this.newModel = ByteBufUtils.readUTF8String(buf);
			break;
		case SCALE:
			this.newScale = buf.readInt();
			break;
		case ROTATION_SPEED:
			this.newRotationSpeed = buf.readInt();
			break;
		case AMBIENT_LIGHT:
			this.newAmbientLight = buf.readBoolean();
			break;
		case RANDOMIZE:
			this.newRandomize = buf.readBoolean();
			break;
		default:
			break;
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(value.ordinal());
		MessageUtil.writeBlockPos(pos, buf);

		switch (value)
		{
		case MODEL_ROTATION:
			buf.writeInt(newModelRotation);
			break;
		case MODEL:
			ByteBufUtils.writeUTF8String(buf, newModel);
			break;
		case SCALE:
			buf.writeInt(newScale);
			break;
		case ROTATION_SPEED:
			buf.writeInt(newRotationSpeed);
			break;
		case AMBIENT_LIGHT:
			buf.writeBoolean(newAmbientLight);
			break;
		case RANDOMIZE:
			buf.writeBoolean(newRandomize);
			break;
		default:
			break;
		}
	}

	@Override
	public void onMessage(final MessageContext context)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				if (context.netHandler instanceof NetHandlerPlayServer)
				{
					NetHandlerPlayServer handler = (NetHandlerPlayServer) context.netHandler;

					EntityPlayerMP player = handler.player;

					if (player != null && player.openContainer != null && player.openContainer instanceof ContainerVoxelProjector)
					{
						TileEntity te = player.world.getTileEntity(pos);

						if (te != null && te instanceof TileEntityVoxelProjector)
						{
							switch (value)
							{
							case MODEL:
								if (newModel != null)
								{
									((TileEntityVoxelProjector) te).setModel(newModel);
								}
								break;
							case MODEL_ROTATION:
								if (newModelRotation >= 0 && newModelRotation < 361)
								{
									((TileEntityVoxelProjector) te).setModelRotation(newModelRotation);
								}
								break;
							case SCALE:
								if (newScale >= 1 && newScale < 21)
								{
									((TileEntityVoxelProjector) te).setScale(newScale);
								}
								break;
							case ROTATION_SPEED:
								if (newRotationSpeed >= 0 && newRotationSpeed < 41)
								{
									((TileEntityVoxelProjector) te).setRotationSpeed(newRotationSpeed);
								}
								break;
							case AMBIENT_LIGHT:
								((TileEntityVoxelProjector) te).setAmbientLight(newAmbientLight);
								break;
							case RANDOMIZE:
								((TileEntityVoxelProjector) te).setRandomize(newRandomize);
								break;
							default:
								break;
							}
						}
					}
				}
			}
		});
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.SERVER;
	}

	public void setScale(int scale)
	{
		this.newScale = scale;
		this.value = VALUE.SCALE;
	}

	public void setRotationSpeed(int rotationSpeed)
	{
		this.newRotationSpeed = rotationSpeed;
		this.value = VALUE.ROTATION_SPEED;
	}

	public void setRandomize(boolean randomize)
	{
		this.newRandomize = randomize;
		this.value = VALUE.RANDOMIZE;
	}

	public void setAmbientLight(boolean ambientLight)
	{
		this.newAmbientLight = ambientLight;
		this.value = VALUE.AMBIENT_LIGHT;
	}
}
