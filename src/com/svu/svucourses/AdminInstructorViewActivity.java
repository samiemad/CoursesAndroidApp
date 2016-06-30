package com.svu.svucourses;

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
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.svu.svucourses.application.App;
import com.svu.svucourses.model.Instructor;

public class AdminInstructorViewActivity extends AppCompatActivity {

	private EditText etUsername;
	private EditText etPassword;
	private EditText etFirstName;
	private EditText etLastName;
	private EditText etAddress;
	private EditText etMobile;
	private Spinner sGender;

	ProgressDialog progress;

	Instructor mInstructor;
	// private int id;
	private boolean createNew = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_instructor_detail);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Set up the login form.
		etUsername = (EditText) findViewById(R.id.username);
		etPassword = (EditText) findViewById(R.id.password);
		etFirstName = (EditText) findViewById(R.id.firstname);
		etLastName = (EditText) findViewById(R.id.lastname);
		etAddress = (EditText) findViewById(R.id.address);
		etMobile = (EditText) findViewById(R.id.mobile);
		sGender = (Spinner) findViewById(R.id.gender);

		progress = new ProgressDialog(this);
		progress.setMessage("Connecting...");
		progress.setCancelable(false);

		// id = getIntent().getIntExtra("EXTRA_ID", -1);
		try {
			String json = getIntent().getStringExtra("EXTRA_JSON");
			if (json != null) {
				JSONObject obj = new JSONObject(json);
				mInstructor = new Instructor(obj);
				displayInstructorData();
			} else {
				mInstructor = new Instructor(null);
				createNew = true;
				etPassword.setVisibility(View.VISIBLE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void displayInstructorData() {
		etFirstName.setText(mInstructor.firstname);
		etLastName.setText(mInstructor.lastname);
		etUsername.setText(mInstructor.username);
		etAddress.setText(mInstructor.address);
		etMobile.setText(mInstructor.mobile);
		sGender.setSelection(mInstructor.gender.equals("Male") ? 0 : 1);
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
		// Store values at the time of the login attempt.
		final String username = etUsername.getText().toString();
		final String firstname = etFirstName.getText().toString();
		final String lastname = etLastName.getText().toString();
		final String address = etAddress.getText().toString();
		final String mobile = etMobile.getText().toString();
		final String gender = sGender.getSelectedItem().toString();

		String url = App.BASE_URL + "updateInstructor";
		StringRequest req = new StringRequest(Method.POST, url, lis, elis) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<String, String>();
				p.put("Id", "" + mInstructor.id);
				p.put("username", username);
				// p.put("password", password);
				p.put("firstname", firstname);
				p.put("lastname", lastname);
				p.put("address", address);
				p.put("mobile", mobile);
				p.put("gender", gender);
				return p;
			}
		};
		Log.d("register req", req.toString());
		App.getInstance().addToRequestQueue(req);
	}

	private void create() {
		progress.show();
		// Store values at the time of the login attempt.
		final String username = etUsername.getText().toString();
		final String password = etPassword.getText().toString();
		final String firstname = etFirstName.getText().toString();
		final String lastname = etLastName.getText().toString();
		final String address = etAddress.getText().toString();
		final String mobile = etMobile.getText().toString();
		final String gender = sGender.getSelectedItem().toString();

		String url = App.BASE_URL + "addInstructor";
		// url += "?name=" + username + "&password=" + password;
		StringRequest req = new StringRequest(Method.POST, url, lis, elis) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<String, String>();
				p.put("username", username);
				p.put("password", password);
				p.put("firstname", firstname);
				p.put("lastname", lastname);
				p.put("address", address);
				p.put("mobile", mobile);
				p.put("gender", gender);
				return p;
			}
		};
		Log.d("register req", req.toString());
		App.getInstance().addToRequestQueue(req);
	}

	private void delete() {
		progress.show();
		String url = App.BASE_URL + "deleteInstructor";
		StringRequest req = new StringRequest(Method.POST, url, lis, elis) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<String, String>();
				p.put("Id", "" + mInstructor.id);
				Log.d("POST params:", p.toString());
				return p;
			}
		};
		App.getInstance().addToRequestQueue(req);
	}

	// private void loadCourseData(final int id) {
	// progress.show();
	// String url = App.BASE_URL + "getStudent";
	// StringRequest req = new StringRequest(Method.POST, url, lis2, elis) {
	// @Override
	// protected Map<String, String> getParams() throws AuthFailureError {
	// HashMap<String, String> p = new HashMap<String, String>();
	// p.put("id", "" + id);
	// return p;
	// }
	// };
	// App.getInstance().addToRequestQueue(req);
	// }

	// private Listener<String> lis = new Listener<String>() {
	// @Override
	// public void onResponse(String str) {
	// progress.dismiss();
	// try {
	// JSONObject obj = new JSONObject(App.stripXml(str));
	// mStudent = new Student(obj);
	// etFirstName.setText(mStudent.firstname);
	// etLastName.setText(mStudent.lastname);
	// etUsername.setText(mStudent.username);
	// etAddress.setText(mStudent.address);
	// etMobile.setText(mStudent.mobile);
	// } catch (JSONException e) {
	// elis.onErrorResponse(new VolleyError("failed to parse response"));
	// e.printStackTrace();
	// }
	// }
	// };
	private Listener<String> lis = new Listener<String>() {
		@Override
		public void onResponse(String str) {
			progress.dismiss();
			try {
				JSONObject res = new JSONObject(App.stripXml(str));
				if (res.optBoolean("success")) {
					Snackbar.make(etAddress, "Success", Snackbar.LENGTH_LONG)
							.show();
				} else {
					Snackbar.make(etAddress,
							"failed, " + res.optString("error"),
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
			Snackbar.make(etAddress,
					"Error connecting to server: " + arg0.getMessage(),
					Snackbar.LENGTH_LONG).show();
		}
	};
}
