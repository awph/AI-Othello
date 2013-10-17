package Participants.AntogniniPerez;

public enum Piece {
	Red(1), Blue(-1), Empty(0);

	private int		valueInt;

	private Piece(int valInt)
	{
		valueInt = valInt;
	}

	public int getValue()
	{
		return valueInt;
	}
}
