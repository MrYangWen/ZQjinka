package com.ximei.tiny.tools;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

public class DBFWriter extends DBFBase
{

	DBFHeader header;
	Vector v_records;
	int recordCount;
	RandomAccessFile raf;
	boolean appendMode;

	public DBFWriter()
	{
		v_records = new Vector();
		recordCount = 0;
		raf = null;
		appendMode = false;
		header = new DBFHeader();
	}

	public DBFWriter(File dbfFile)
		throws IOException
	{
		v_records = new Vector();
		recordCount = 0;
		raf = null;
		appendMode = false;
		try
		{
			raf = new RandomAccessFile(dbfFile, "rw");
			if (!dbfFile.exists() || dbfFile.length() == 0L)
			{
				header = new DBFHeader();
				return;
			}
		}
		catch (FileNotFoundException e)
		{
			throw new DBFException((new StringBuilder("Specified file is not found. ")).append(e.getMessage()).toString());
		}
		catch (IOException e)
		{
			throw new DBFException((new StringBuilder(String.valueOf(e.getMessage()))).append(" while reading header").toString());
		}
		header = new DBFHeader();
		header.read(raf);
		raf.seek(raf.length() - 1L);
		recordCount = header.numberOfRecords;
		return;
	}

	public void setFields(DBFField fields[])
		throws DBFException
	{
		if (header.fieldArray != null)
			throw new DBFException("Fields has already been set");
		if (fields == null || fields.length == 0)
			throw new DBFException("Should have at least one field");
		for (int i = 0; i < fields.length; i++)
			if (fields[i] == null)
				throw new DBFException((new StringBuilder("Field ")).append(i + 1).append(" is null").toString());

		header.fieldArray = fields;
		try
		{
			if (raf != null && raf.length() == 0L)
				header.write(raf);
		}
		catch (IOException e)
		{
			throw new DBFException("Error accesing file");
		}
	}

	public void addRecord(Object values[])
		throws DBFException
	{
		if (header.fieldArray == null)
			throw new DBFException("Fields should be set before adding records");
		if (values == null)
			throw new DBFException("Null cannot be added as row");
		if (values.length != header.fieldArray.length)
			throw new DBFException("Invalid record. Invalid number of fields in row");
		for (int i = 0; i < header.fieldArray.length; i++)
			if (values[i] != null)
				switch (header.fieldArray[i].getDataType())
				{
				case 69: // 'E'
				case 71: // 'G'
				case 72: // 'H'
				case 73: // 'I'
				case 74: // 'J'
				case 75: // 'K'
				case 77: // 'M'
				default:
					break;

				case 67: // 'C'
					if (!(values[i] instanceof String))
						throw new DBFException((new StringBuilder("Invalid value for field ")).append(i).toString());
					break;

				case 76: // 'L'
					if (!(values[i] instanceof Boolean))
						throw new DBFException((new StringBuilder("Invalid value for field ")).append(i).toString());
					break;

				case 78: // 'N'
					if (!(values[i] instanceof Double))
						throw new DBFException((new StringBuilder("Invalid value for field ")).append(i).toString());
					break;

				case 68: // 'D'
					if (!(values[i] instanceof Date))
						throw new DBFException((new StringBuilder("Invalid value for field ")).append(i).toString());
					break;

				case 70: // 'F'
					if (!(values[i] instanceof Double))
						throw new DBFException((new StringBuilder("Invalid value for field ")).append(i).toString());
					break;
				}

		if (raf == null)
			v_records.addElement(((Object) (values)));
		else
			try
			{
				writeRecord(raf, values);
				recordCount++;
			}
			catch (IOException e)
			{
				throw new DBFException((new StringBuilder("Error occured while writing record. ")).append(e.getMessage()).toString());
			}
	}

	public void write(OutputStream out)
		throws DBFException
	{
		try
		{
			if (raf == null)
			{
				DataOutputStream outStream = new DataOutputStream(out);
				header.numberOfRecords = v_records.size();
				header.write(outStream);
				int t_recCount = v_records.size();
				for (int i = 0; i < t_recCount; i++)
				{
					Object t_values[] = (Object[])v_records.elementAt(i);
					writeRecord(outStream, t_values);
				}

				outStream.write(26);
				outStream.flush();
			} else
			{
				header.numberOfRecords = recordCount;
				raf.seek(0L);
				header.write(raf);
				raf.seek(raf.length());
				raf.writeByte(26);
				raf.close();
			}
		}
		catch (IOException e)
		{
			throw new DBFException(e.getMessage());
		}
	}

	public void write()
		throws DBFException
	{
		write(null);
	}

	private void writeRecord(DataOutput dataOutput, Object objectArray[])
		throws IOException
	{
		dataOutput.write(32);
		for (int j = 0; j < header.fieldArray.length; j++)
			switch (header.fieldArray[j].getDataType())
			{
			case 67: // 'C'
				if (objectArray[j] != null)
				{
					String str_value = objectArray[j].toString();
					dataOutput.write(Utils.textPadding(str_value, characterSetName, header.fieldArray[j].getFieldLength()));
				} else
				{
					dataOutput.write(Utils.textPadding("", characterSetName, header.fieldArray[j].getFieldLength()));
				}
				break;

			case 77: // 'M'
				break;

			case 68: // 'D'
				if (objectArray[j] != null)
				{
					GregorianCalendar calendar = new GregorianCalendar();
					calendar.setTime((Date)objectArray[j]);
					StringBuffer t_sb = new StringBuffer();
					dataOutput.write(String.valueOf(calendar.get(1)).getBytes());
					dataOutput.write(Utils.textPadding(String.valueOf(calendar.get(2) + 1), characterSetName, 2, 12, (byte)48));
					dataOutput.write(Utils.textPadding(String.valueOf(calendar.get(5)), characterSetName, 2, 12, (byte)48));
				} else
				{
					dataOutput.write("        ".getBytes());
				}
				break;

			case 70: // 'F'
				if (objectArray[j] != null)
					dataOutput.write(Utils.doubleFormating((Double)objectArray[j], characterSetName, header.fieldArray[j].getFieldLength(), header.fieldArray[j].getDecimalCount()));
				else
					dataOutput.write(Utils.textPadding("?", characterSetName, header.fieldArray[j].getFieldLength(), 12));
				break;

			case 78: // 'N'
				if (objectArray[j] != null)
					dataOutput.write(Utils.doubleFormating((Double)objectArray[j], characterSetName, header.fieldArray[j].getFieldLength(), header.fieldArray[j].getDecimalCount()));
				else
					dataOutput.write(Utils.textPadding("?", characterSetName, header.fieldArray[j].getFieldLength(), 12));
				break;

			case 76: // 'L'
				if (objectArray[j] != null)
				{
					if ((Boolean)objectArray[j] == Boolean.TRUE)
						dataOutput.write(84);
					else
						dataOutput.write(70);
				} else
				{
					dataOutput.write(63);
				}
				break;

			case 69: // 'E'
			case 71: // 'G'
			case 72: // 'H'
			case 73: // 'I'
			case 74: // 'J'
			case 75: // 'K'
			default:
				throw new DBFException((new StringBuilder("Unknown field type ")).append(header.fieldArray[j].getDataType()).toString());
			}

	}
}