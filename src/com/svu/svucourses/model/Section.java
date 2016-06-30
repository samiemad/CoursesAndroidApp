package com.svu.svucourses.model;

import org.json.JSONObject;

public class Section {
	public String json;
	public int id;
	public int courseId;
	public int sectionNo;
	public int roomNo;
	public long time;
	public Instructor instructor;
	public Course course;

	public Section(JSONObject obj) {
		if (obj == null)
			return;
		json = obj.toString();
		id = obj.optInt("Id");
		courseId = obj.optInt("courseId");
		sectionNo = obj.optInt("SectionNo");
		roomNo = obj.optInt("RoomNo");
		time = obj.optLong("Time");
		JSONObject ins = obj.optJSONObject("instructor");
		if (ins != null)
			instructor = new Instructor(ins);
		JSONObject crs = obj.optJSONObject("course");
		if (crs != null)
			course = new Course(crs);
	}
}
