package com.svu.svucourses.application;

import java.util.List;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.svu.svucourses.model.Course;
import com.svu.svucourses.model.Enrollment;
import com.svu.svucourses.model.Instructor;
import com.svu.svucourses.model.Section;
import com.svu.svucourses.model.Student;

public class App extends Application {
	private static final String TAG = App.class.getSimpleName();

	public static final String BASE_URL = "http://www.svu-regsite.somee.com/WebService.asmx/";

	private static App mInstance;
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	private SharedPreferences prefs;
	public static Student student;
	public static Instructor admin;
	public static List<Course> coursesList;
	public static List<Student> studentsList;
	public static List<Instructor> instructorsList;
	public static List<Section> sectionsList;
	public static List<Enrollment> enrollmentsList;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	public static synchronized App getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return requestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (imageLoader == null) {
			imageLoader = new ImageLoader(requestQueue, new LruBitmapCache());
		}
		return imageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (tag == null)
			tag = TAG;
		if (requestQueue != null) {
			requestQueue.cancelAll(tag);
		}
	}

	public static String stripXml(String str) {
		return str.substring(str.indexOf(">", str.indexOf(">") + 1) + 1,
				str.lastIndexOf("<"));
	}

	public SharedPreferences getPrefs() {
		if (prefs == null)
			prefs = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
		return prefs;
	}
}
