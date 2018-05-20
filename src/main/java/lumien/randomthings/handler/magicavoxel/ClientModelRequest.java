package lumien.randomthings.handler.magicavoxel;

public class ClientModelRequest
{
	enum STATE
	{
		SEND_REQUEST, NOT_AVAILABLE, RECEIVING, FINISHED
	}

	STATE state;

	int modelSize;
	int paletteSize;

	byte[] modelData;
	byte[] paletteData;

	int bytesReceived;

	public void setState(STATE state)
	{
		this.state = state;
	}

	public STATE getState()
	{
		return state;
	}

	public void updateRequest(int modelSize, int paletteSize)
	{
		if (modelSize < 0)
		{
			this.state = STATE.NOT_AVAILABLE;
		}
		else
		{
			this.modelSize = modelSize;
			this.paletteSize = paletteSize;
			this.modelData = new byte[modelSize];
			this.paletteData = new byte[paletteSize];

			this.state = STATE.RECEIVING;
		}
	}
}
