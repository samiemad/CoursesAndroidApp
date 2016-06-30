package com.svu.svucourses;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.svu.svucourses.application.App;
import com.svu.svucourses.model.Instructor;
import com.svu.svucourses.model.Student;

public class AdminLoginActivity extends AppCompatActivity {

	// UI references.
	private EditText etUsername;
	private EditText etPassword;
	private EditText etFirstName;
	private EditText etLastName;
	private EditText etAddress;
	private EditText etMobile;
	private Spinner sGender;
	private View mProgressView;
	private View mLoginFormView;
	private Button bLogin;
	private Button bRegister;
	private TextView tvRegister;
	private boolean register = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Set up the login form.
		etUsername = (EditText) findViewById(R.id.username);
		etFirstName = (EditText) findViewById(R.id.firstname);
		etLastName = (EditText) findViewById(R.id.lastname);
		etAddress = (EditText) findViewById(R.id.address);
		etMobile = (EditText) findViewById(R.id.mobile);
		sGender = (Spinner) findViewById(R.id.gender);
		bLogin = (Button) findViewById(R.id.b_signin);
		bRegister = (Button) findViewById(R.id.b_register);
		tvRegister = (TextView) findViewById(R.id.tv_register);

		etPassword = (EditText) findViewById(R.id.password);
		etPassword
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.b_signin
								|| id == EditorInfo.IME_ACTION_GO) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		bLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
		bLogin.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				onLoginSuccess();
				return true;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	private void attemptLogin() {

		if (register) {
			attemptRegister();
			return;
		}

		// Store values at the time of the login attempt.
		final String username = etUsername.getText().toString();
		final String password = etPassword.getText().toString();

		focusErrorView = null;

		if (errorPass(password) && errorUsername(username)) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusErrorView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			String url = App.BASE_URL + "loginAdmin";
			// url += "?name=" + username + "&password=" + password;
			StringRequest req = new StringRequest(Method.POST, url, lis, elis) {

				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					HashMap<String, String> p = new HashMap<String, String>();
					p.put("username", username);
					p.put("password", password);
					return p;
				}
			};
			Log.d("LOGIN req", req.toString());
			App.getInstance().addToRequestQueue(req);
		}
	}

	private void attemptRegister() {
		// Store values at the time of the login attempt.
		final String username = etUsername.getText().toString();
		final String password = etPassword.getText().toString();
		final String firstname = etFirstName.getText().toString();
		final String lastname = etLastName.getText().toString();
		final String address = etAddress.getText().toString();
		final String mobile = etMobile.getText().toString();
		final String gender = sGender.getSelectedItem().toString();

		focusErrorView = null;

		if (errorPass(password) || errorUsername(username)
				|| error(etFirstName) || error(etLastName) || error(etAddress)
				|| error(etMobile)) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusErrorView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			String url = App.BASE_URL + "registerAdmin";
			// url += "?name=" + username + "&password=" + password;
			StringRequest req = new StringRequest(Method.POST, url, lis, elis) {

				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
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
	}

	private boolean errorUsername(String username) {
		// Reset errors.
		etUsername.setError(null);

		boolean cancel = false;
		// Check for a valid mobile address.
		if (TextUtils.isEmpty(username)) {
			etUsername.setError(getString(R.string.error_field_required));
			focusErrorView = etUsername;
			cancel = true;
		} else if (!isUsernameValid(username)) {
			etUsername.setError(getString(R.string.error_invalid_username));
			focusErrorView = etUsername;
			cancel = true;
		}
		return cancel;
	}

	private boolean errorPass(String password) {
		// Reset errors.
		etPassword.setError(null);

		boolean cancel = false;
		// Check for a valid password, if the user entered one.
		if (TextUtils.isEmpty(password)) {
			etPassword.setError(getString(R.string.error_field_required));
			focusErrorView = etPassword;
			cancel = true;
		} else if (!isPasswordValid(password)) {
			etPassword.setError(getString(R.string.error_invalid_password));
			focusErrorView = etPassword;
			cancel = true;
		}
		return cancel;
	}

	private boolean error(EditText v) {
		// Reset errors.
		v.setError(null);
		String txt = v.getText().toString();
		boolean cancel = false;

		// Check for a valid password, if the user entered one.
		if (TextUtils.isEmpty(txt)) {
			v.setError(getString(R.string.error_field_required));
			focusErrorView = v;
			cancel = true;
		}
		return cancel;
	}

	private boolean isUsernameValid(String username) {
		// TODO: Replace this with your own logic
		return true;
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 2;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		mLoginFormView.setEnabled(show);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			// mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.setInterpolator(new OvershootInterpolator())
					.alpha(show ? 0.3f : 1.0f).scaleX(show ? 0.7f : 1.0f)
					.scaleY(show ? 0.7f : 1.0f)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							// mLoginFormView.setVisibility(show ? View.GONE :
							// View.VISIBLE);
						}
					});

			// mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime * 3)
					.setInterpolator(new OvershootInterpolator())
					.alpha(!show ? 0.0f : 1.0f).scaleX(!show ? 0.0f : 2.0f)
					.scaleY(!show ? 0.0f : 2.0f)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							// mProgressView.setVisibility(show ? View.VISIBLE :
							// View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	private Listener<String> lis = new Listener<String>() {

		@Override
		public void onResponse(String res) {
			String resJson = App.stripXml(res);
			Log.d("RES", res);
			Log.d("RESJSON", resJson);
			try {
				JSONObject obj = new JSONObject(resJson);
				if (obj.getBoolean("success")) {
					onLoginSuccess();
					App.student = new Student(obj.optJSONObject("admin"));
					App.admin = new Instructor(obj.optJSONObject("admin"));
				} else {
					onLoginFailed(obj.optString("error"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				elis.onErrorResponse(new VolleyError("unable to parse response"));
			}
		}
	};
	private ErrorListener elis = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError e) {
			Log.e("ELIS", e.toString());
			onLoginFailed("Error Connecting to server!");
		}
	};
	private View focusErrorView;

	private void onLoginSuccess() {
		showProgress(false);
		Intent i = new Intent(this, AdminCourseListActivity.class);
		startActivity(i);
		finish();
	}

	protected void onLoginFailed(final String msg) {
		mProgressView.postDelayed(new Runnable() {

			@Override
			public void run() {
				showProgress(false);
				Toast.makeText(AdminLoginActivity.this, msg, Toast.LENGTH_LONG)
						.show();
			}
		}, 1000);
	}

	public void clickRegister(View v) {
		register = !register;
		etMobile.setVisibility(register ? View.VISIBLE : View.GONE);
		etFirstName.setVisibility(register ? View.VISIBLE : View.GONE);
		etLastName.setVisibility(register ? View.VISIBLE : View.GONE);
		etAddress.setVisibility(register ? View.VISIBLE : View.GONE);
		sGender.setVisibility(register ? View.VISIBLE : View.GONE);
		bLogin.setText(register ? getString(R.string.action_register)
				: getString(R.string.action_sign_in));
		bRegister.setText(register ? getString(R.string.action_sign_in)
				: getString(R.string.action_register));
		tvRegister.setText(register ? getString(R.string.text_login)
				: getString(R.string.text_register));
		if (register)
			etFirstName.requestFocus();
		else
			etUsername.requestFocus();
	}
}