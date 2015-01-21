package com.nurseschedule.mvc.algorithm;

import java.util.HashMap;
import java.util.Map;

public class WeekSchedule {
	
	private Map<Integer, String> schedule;
	
	public WeekSchedule() {
		schedule = new HashMap<Integer, String>();
	}

	public Map<Integer, String> getSchedule() {
		return schedule;
	}

}
