package com.ximei.tiny.tools;

public class MacToSerial
{

	public MacToSerial()
	{
	}

	public String mactoserial(String s, int i)
	{
		StringBuffer stringbuffer = new StringBuffer();
		int j = 0;
		do
		{
			if (j >= s.length())
				return stringbuffer.toString().toUpperCase();
			stringbuffer.append(Integer.toHexString(Integer.parseInt(s.substring(j, j + 1), 16) ^ j + i));
			j++;
		} while (true);
	}
}
