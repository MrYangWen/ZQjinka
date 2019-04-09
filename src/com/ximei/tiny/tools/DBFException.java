package com.ximei.tiny.tools;

import java.io.IOException;

public class DBFException extends IOException
{
  public DBFException()
  {
  }

  public DBFException(String msg)
  {
    super(msg);
  }
}