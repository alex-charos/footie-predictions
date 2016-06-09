package com.oranje.security;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class DateTest {

	@Test
	public void dateTest() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		Date rawDate = sdf.parse("2016-06-10T12:00:00");

		Timestamp ts = new Timestamp(rawDate.getTime()
				+ (TimeZone.getDefault().getRawOffset() - TimeZone.getTimeZone("Europe/Athens").getRawOffset()));
		
		
		Timestamp now = new Timestamp( new Date().getTime());
		
	 
		

	}

}
