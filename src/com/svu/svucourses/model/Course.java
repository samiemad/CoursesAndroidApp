package com.svu.svucourses.model;

import org.json.JSONObject;

public class Course {
	public String json;
	public String title;
	public int hours;
	public int id;

	public Course(JSONObject obj) {
		if (obj == null)
			return;
		json = obj.toString();
		title = obj.optString("title");
		hours = obj.optInt("hours");
		id = obj.optInt("Id");
	}
}
