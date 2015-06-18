package edu.psu.rest;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class is intended to provide an adaptor to convert Java Date's into
 * ISO 8601 formatted time stamps in JAXB produced documents.  
 * 
 * An example of an ISO 8601 Date is 2014-04-15
 * @author ses44
 *
 */

public class AdabasDateFormatAdapter extends XmlAdapter<String, Date>
{  
  private SimpleDateFormat dateFormat = new SimpleDateFormat(edu.psu.legacy.DateFormat.WAREHOUSE_DATE_FORMAT);

  @Override
  public String marshal(Date date) 
  {
    return dateFormat.format(date);
  }

  @Override
  public Date unmarshal(String date) throws Exception
  {
    return dateFormat.parse(date);
  }
}