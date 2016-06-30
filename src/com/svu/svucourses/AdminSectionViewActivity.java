package com.svu.svucourses;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.svu.svucourses.application.App;
import com.svu.svucourses.model.Section;

public class AdminSectionViewActivity extends AppCompatActivity {

	ProgressDialog progress;
	EditText roomNo, time;
	

	Section section;
	private int id;
	private boolean createNew = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_section_detail);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		roomNo = (EditText) findViewById(R.id.roomNo);
		time = (EditText) findViewById(R.id.time);

		progress = new ProgressDialog(this);
		progress.setMessage("Connecting...");
		progress.setCancelable(false);

		id = getIntent().getIntExtra("EXTRA_ID", -1);

		if (id >= 0) {
			loadCourseData(id);
		} else {
			createNew = true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnu_save:
			if (createNew)
				create();
			else
				save();
			break;
		case R.id.mnu_delete:
			if (createNew)
				finish();
			else
				delete();
			break;
		}
		return true;
	}

	private void save() {
		progress.show();
		final String r = roomNo.getText().toString();
		final String t = time.getText().toString();

		String url = App.BASE_URL + "updateCourse";
		StringRequest req = new StringRequest(Method.POST, url, lis2, elis) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<String, String>();
				p.put("id", "" + id);
				p.put("roomNo", r);
				p.put("time", t);
				Log.d("POST params:", p.toString());
				return p;
			}
		};
		App.getInstance().addToRequestQueue(req);
	}

	private void create() {
		progress.show();
		// final String t = title.getText().toString();
		// final String h = hours.getText().toString();

		String url = App.BASE_URL + "addCourse";
		StringRequest req = new StringRequest(Method.POST, url, lis2, elis) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<String, String>();
				// p.put("title", t);
				// p.put("hours", h);
				Log.d("POST params:", p.toString());
				return p;
			}
		};
		App.getInstance().addToRequestQueue(req);
	}

	private void delete() {
		progress.show();
		String url = App.BASE_URL + "deleteCourse";
		StringRequest req = new StringRequest(Method.POST, url, lis2, elis) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<String, String>();
				p.put("id", "" + id);
				Log.d("POST params:", p.toString());
				return p;
			}
		};
		App.getInstance().addToRequestQueue(req);
	}

	private void loadCourseData(final int id) {
		progress.show();
		String url = App.BASE_URL + "getCourse";
		StringRequest req = new StringRequest(Method.POST, url, lis, elis) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<String, String>();
				p.put("id", "" + id);
				return p;
			}
		};
		App.getInstance().addToRequestQueue(req);
	}

	private Listener<String> lis = new Listener<String>() {
		@Override
		public void onResponse(String str) {
			progress.dismiss();
			try {
				JSONObject obj = new JSONObject(App.stripXml(str));
				section = new Section(obj);
				roomNo.setText(section.roomNo);
				time.setText((new Date(section.time)).toString());
			} catch (JSONException e) {
				elis.onErrorResponse(new VolleyError("failed to parse response"));
				e.printStackTrace();
			}
		}
	};
	private Listener<String> lis2 = new Listener<String>() {
		@Override
		public void onResponse(String str) {
			progress.dismiss();
			try {
				JSONObject res = new JSONObject(App.stripXml(str));
				if (res.optBoolean("success")) {
					Snackbar.make(roomNo, "Success", Snackbar.LENGTH_LONG)
							.show();
				} else {
					Snackbar.make(roomNo, "failed, try again later",
							Snackbar.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				elis.onErrorResponse(new VolleyError("failed to parse response"));
				e.printStackTrace();
			}
		}
	};
	private ErrorListener elis = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			progress.dismiss();
			Snackbar.make(roomNo,
					"Error connecting to server: " + arg0.getMessage(),
					Snackbar.LENGTH_LONG).show();
		}
	};
}
