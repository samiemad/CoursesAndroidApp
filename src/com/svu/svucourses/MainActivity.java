package com.svu.svucourses;

import java.util.ArrayList;

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
import android.view.View;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.svu.svucourses.adapter.CoursesListAdapter;
import com.svu.svucourses.adapter.DividerItemDecoration;
import com.svu.svucourses.application.App;
import com.svu.svucourses.model.Course;

public class MainActivity extends AppCompatActivity {

	RecyclerView rList;
	ProgressDialog progress;
	CoursesListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		// CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)
		// findViewById(R.id.collapsing_toolbar);
		// collapsingToolbar.setTitle(getString(R.string.app_name));

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

	private void loadCoursesList() {
		String url = App.BASE_URL + "getCourses";
		StringRequest req = new StringRequest(Method.POST, url, lis, elis);
		App.getInstance().addToRequestQueue(req);
	}

	private Listener<String> lis = new Listener<String>() {
		@Override
		public void onResponse(String str) {
			progress.dismiss();
			try {
				ArrayList<Course> items = new ArrayList<Course>();
				JSONArray arr = new JSONArray(App.stripXml(str));
				for (int i = 0; i < arr.length(); ++i) {
					items.add(new Course((JSONObject) arr.opt(i)));
				}
				adapter = new CoursesListAdapter(items);
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
					Snackbar.LENGTH_INDEFINITE).setAction("retry",
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							loadCoursesList();
						}
					}).show();
		}
	};

}
