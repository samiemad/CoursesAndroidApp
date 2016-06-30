package com.svu.svucourses.adapter;

import java.util.List;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svu.svucourses.CourseViewActivity;
import com.svu.svucourses.R;
import com.svu.svucourses.model.Course;

public class CoursesListAdapter extends
		RecyclerView.Adapter<CoursesListAdapter.ViewHolder> {

	class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
		public TextView title, hours;

		public ViewHolder(View v) {
			super(v);
			title = (TextView) v.findViewById(R.id.title);
			hours = (TextView) v.findViewById(R.id.hours);

			v.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			// Snackbar.make(v, items.get(getLayoutPosition()).title,
			// Snackbar.LENGTH_LONG).show();
			Intent i = new Intent(v.getContext(), CourseViewActivity.class);
			Course c = items.get(getLayoutPosition());
			i.putExtra("COURSE_ID", c.id);
			v.getContext().startActivity(i);
		}
	}

	List<Course> items;

	public CoursesListAdapter(List<Course> items) {
		this.items = items;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int pos) {
		vh.title.setText(items.get(pos).title);
		vh.hours.setText(items.get(pos).hours + " hours");
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.row_course, parent, false);
		return new ViewHolder(v);
	}

}
