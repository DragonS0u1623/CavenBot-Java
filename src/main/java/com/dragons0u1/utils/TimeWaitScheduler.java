package com.dragons0u1.utils;

import java.util.*;

public class TimeWaitScheduler {
	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Z"));
	int dayOfMonth = cal.get(Calendar.DATE), month = cal.get(Calendar.MONTH), yesterday;
	
	public int checkDay() {
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public void checkDate() {
		
	}
}
