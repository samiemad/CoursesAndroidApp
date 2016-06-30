package com.svu.svucourses.model;

import org.json.JSONObject;

public class Enrollment {
	public String json;
	public int id;
	public int grade;
	public Student student;
	public Section section;

	public Enrollment(JSONObject obj) {
		if (obj == null)
			return;
		json = obj.toString();
		id = obj.optInt("Id");
		grade = obj.optInt("grade");
		JSONObject stu = obj.optJSONObject("student");
		student = new Student(stu);
		JSONObject sec = obj.optJSONObject("section");
		section = new Section(sec);
	}
}
