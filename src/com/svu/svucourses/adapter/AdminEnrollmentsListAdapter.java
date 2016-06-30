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
import com.svu.svucourses.model.Enrollment;

public class AdminEnrollmentsListAdapter extends
		RecyclerView.Adapter<AdminEnrollmentsListAdapter.ViewHolder> {

	class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
		public TextView title, desc;

		public ViewHolder(View v) {
			super(v);
			title = (TextView) v.findViewById(R.id.tv1);
			desc = (TextView) v.findViewById(R.id.tv2);
			v.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			Intent i = new Intent(v.getContext(), CourseViewActivity.class);
			Enrollment c = items.get(getLayoutPosition());
			i.putExtra("EXTRA_ID", c.id);
			i.putExtra("EXTRA_JSON", c.json);
			v.getContext().startActivity(i);
		}
	}

	List<Enrollment> items;

	public AdminEnrollmentsListAdapter(List<Enrollment> items) {
		this.items = items;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int pos) {
		Enrollment r = items.get(pos);
		vh.title.setText(r.student.firstname + " " + r.student.lastname
				+ " at " + r.section.course.title + " #" + r.section.sectionNo);
		vh.desc.setText("grade : " + r.grade);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.row_other, parent, false);
		return new ViewHolder(v);
	}

}
