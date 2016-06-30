package com.svu.svucourses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.svu.svucourses.adapter.DividerItemDecoration;
import com.svu.svucourses.adapter.SectionsListAdapter;
import com.svu.svucourses.application.App;
import com.svu.svucourses.model.Course;
import com.svu.svucourses.model.Section;

public class CourseViewActivity extends AppCompatActivity implements
		OnClickListener {

	RecyclerView rList;
	ProgressDialog progress;
	SectionsListAdapter adapter;
	TextView title, hours;
	Button enroll;

	Course mCourse;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_detail);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		title = (TextView) findViewById(R.id.title);
		hours = (TextView) findViewById(R.id.hours);
		enroll = (Button) findViewById(R.id.enroll);
		enroll.setOnClickListener(this);

		rList = (RecyclerView) findViewById(R.id.recyclerView);
		LayoutManager lm = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		rList.setLayoutManager(lm);
		rList.addItemDecoration(new DividerItemDecoration(this,
				DividerItemDecoration.VERTICAL_LIST));

		progress = new ProgressDialog(this);
		progress.setMessage("Connecting...");
		progress.setCancelable(false);
		progress.show();

		id = getIntent().getIntExtra("COURSE_ID", 0);

		loadCourseData(id);
	}

	private void loadCourseData(final int id) {
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
				List<Section> items = new ArrayList<Section>();
				mCourse = new Course(obj);
				title.setText(mCourse.title);
				setTitle(mCourse.title);
				hours.setText(mCourse.hours + " hours");
				JSONArray arr = obj.optJSONArray("sections");
				for (int i = 0; i < arr.length(); ++i) {
					items.add(new Section(arr.getJSONObject(i)));
				}
				adapter = new SectionsListAdapter(items);
				rList.setAdapter(adapter);
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
			Snackbar.make(rList,
					"Error connecting to server: " + arg0.getMessage(),
					Snackbar.LENGTH_INDEFINITE)
					.setAction("retry", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							loadCourseData(id);
						}
					}).show();
		}
	};

	@Override
	public void onClick(View v) {
		if (adapter.getSelectedSetion() == null) {
			Snackbar.make(v, "Please select a section first!",
					Snackbar.LENGTH_SHORT).show();
			return;
		}
		Snackbar.make(v, "Processing...", Snackbar.LENGTH_LONG).show();
		String url = App.BASE_URL + "enroll";
		final int id = adapter.getSelectedSetion().id;
		StringRequest req = new StringRequest(Method.POST, url, enrolllis,
				enrollelis) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<String, String>();
				p.put("studentId", "" + App.student.id);
				p.put("sectionId", "" + id);
				return p;
			}
		};
		App.getInstance().addToRequestQueue(req);
	}

	private Listener<String> enrolllis = new Listener<String>() {
		@Override
		public void onResponse(String str) {
			progress.dismiss();
			try {
				JSONObject obj = new JSONObject(App.stripXml(str));
				if (obj.optBoolean("success")) {
					Snackbar.make(rList, "Enrolled Successfully",
							Snackbar.LENGTH_LONG).show();
				} else {
					Snackbar.make(rList, "Error: " + obj.optString("error"),
							Snackbar.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				enrollelis.onErrorResponse(new VolleyError(
						"failed to parse response"));
				e.printStackTrace();
			}
		}
	};
	private ErrorListener enrollelis = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			progress.dismiss();
			Snackbar.make(
					rList,
					"Error connecting to server: " + arg0.getMessage()
							+ "\ntry again later...", Snackbar.LENGTH_LONG)
					.show();
		}
	};

}
