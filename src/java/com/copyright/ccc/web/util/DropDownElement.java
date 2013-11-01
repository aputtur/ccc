package com.copyright.ccc.web.util;
/**
 * Class that represents an <code>&lt;option&gt;</code> HTML element.
 * 
 * @author Lucas E. Alberione
 */
public class DropDownElement 
{
  private String _value;
  private String _text;
  
  public DropDownElement(String value, String text)
  {
    _value = value;
    _text = text;
  }

  public DropDownElement(int value, String text)
  {
    _value = String.valueOf(value);
    _text = text;
  }

  public void setValue(String value)
  {
    _value = value;
  }


  public String getValue()
  {
    return _value;
  }


  public void setText(String text)
  {
    _text = text;
  }


  public String getText()
  {
    return _text;
  }
}
