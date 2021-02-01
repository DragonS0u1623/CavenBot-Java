package com.dragons0u1.utils;

import org.apache.logging.log4j.*;

import com.dragons0u1.*;

public class StaticReferences {
	public static final Logger logger = LogManager.getLogger(CavenBot.class);
	public static final String[] COOWNERS = { "465000885286600704" };
	
	public static final String BOTID = "638446270469373972",
			AUTHORID = "163667745580253184",
			TENORAPI = "https://api.tenor.com/v1/random?q=%s&key=%s&limit=1&media_filter=minimal&contentfilter=medium&ar_range=standard",
			HOMESERVERID = "712065344264470548", NPEMOTEID = "739415661003931718", BOTEMOTEID = "758736183974297630";
}
