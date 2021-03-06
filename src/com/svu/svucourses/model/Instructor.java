package com.svu.svucourses.model;

import org.json.JSONObject;

public class Instructor {

	public String json;
	public int id;
	public String firstname;
	public String lastname;
	public String username;
	public String address;
	public String gender;
	public String mobile;

	public Instructor(JSONObject obj) {
		if (obj == null)
			return;
		json = obj.toString();
		id = obj.optInt("Id");
		firstname = obj.optString("firstname");
		lastname = obj.optString("lastname");
		username = obj.optString("username");
		address = obj.optString("address");
		gender = obj.optString("gender");
		mobile = obj.optString("mobile");
	}
}
