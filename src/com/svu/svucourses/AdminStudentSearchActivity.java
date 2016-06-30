package com.svu.svucourses;

import java.net.URLEncoder;
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
import android.widget.EditText;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.svu.svucourses.adapter.AdminStudentsListAdapter;
import com.svu.svucourses.adapter.DividerItemDecoration;
import com.svu.svucourses.application.App;
import com.svu.svucourses.model.Student;

public class AdminStudentSearchActivity extends AppCompatActivity {

	EditText etQuery;
	RecyclerView rList;
	ProgressDialog progress;
	AdminStudentsListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		etQuery = (EditText) findViewById(R.id.query);
		rList = (RecyclerView) findViewById(R.id.recyclerView);
		LayoutManager lm = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		rList.setLayoutManager(lm);
		rList.addItemDecoration(new DividerItemDecoration(this,
				DividerItemDecoration.VERTICAL_LIST));

		progress = new ProgressDialog(this);
		progress.setMessage("Connecting...");
		progress.setCancelable(false);
	}

	public void clickSearch(View v) {
		final String query = etQuery.getText().toString();
		if (query.isEmpty()) {
			Snackbar.make(v, "Enter query first!", Snackbar.LENGTH_LONG).show();
			return;
		}
		progress.show();
		String url = App.BASE_URL + "searchStudent?query="
				+ URLEncoder.encode(query);
		StringRequest req = new StringRequest(url, lis, elis);
		// @Override
		// protected Map<String, String> getParams() throws AuthFailureError {
		// HashMap<String, String> p = new HashMap<String, String>();
		// p.put("query", query);
		// Log.d("HTTP POST:", p.toString());
		// return super.getParams();
		// }
		// };
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
							clickSearch(etQuery);
						}
					}).show();
		}
	};

}
