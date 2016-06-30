package com.svu.svucourses;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.svu.svucourses.adapter.AdminStudentsListAdapter;
import com.svu.svucourses.adapter.DividerItemDecoration;
import com.svu.svucourses.application.App;
import com.svu.svucourses.model.Student;

public class AdminStudentListActivity extends AppCompatActivity {

	RecyclerView rList;
	ProgressDialog progress;
	AdminStudentsListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

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

		loadCoursesList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.nav_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnu_courses:
			startActivity(new Intent(this, AdminCourseListActivity.class));
			finish();
			break;
		case R.id.mnu_sections:
			startActivity(new Intent(this, AdminSectionListActivity.class));
			finish();
			break;
		case R.id.mnu_enrollments:
			startActivity(new Intent(this, AdminEnrollmentListActivity.class));
			finish();
			break;
		case R.id.mnu_students:
			startActivity(new Intent(this, AdminStudentListActivity.class));
			finish();
			break;
		case R.id.mnu_instructors:
			startActivity(new Intent(this, AdminInstructorListActivity.class));
			finish();
			break;
		case R.id.mnu_new:
			startActivity(new Intent(this, AdminStudentViewActivity.class));
			break;
		case R.id.mnu_search:
			startActivity(new Intent(this, AdminStudentSearchActivity.class));
			break;
		}
		return true;
	}

	private void loadCoursesList() {
		String url = App.BASE_URL + "getStudents";
		StringRequest req = new StringRequest(Method.POST, url, lis, elis);
		App.getInstance().addToRequestQueue(req);
	}

	private Listener<String> lis = new Listener<String>() {
		@Override
		public void onResponse(String str) {
			progress.dismiss();
			try {
				ArrayList<Student> items = new ArrayList<Student>();
				JSONArray arr = new JSONArray(App.stripXml(str));
				for (int i = 0; i < arr.length(); ++i) {
					items.add(new Student((JSONObject) arr.opt(i)));
				}
				adapter = new AdminStudentsListAdapter(items);
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
							loadCoursesList();
						}
					}).show();
		}
	};

}
