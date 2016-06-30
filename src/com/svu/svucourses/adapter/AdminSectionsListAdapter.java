package com.svu.svucourses.adapter;

import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svu.svucourses.AdminCourseViewActivity;
import com.svu.svucourses.R;
import com.svu.svucourses.model.Section;

public class AdminSectionsListAdapter extends
		RecyclerView.Adapter<AdminSectionsListAdapter.ViewHolder> {

	class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
		public TextView sectionNo, roomNo, instructorName, time;
		public View view;

		public ViewHolder(View v) {
			super(v);
			sectionNo = (TextView) v.findViewById(R.id.sectionNo);
			roomNo = (TextView) v.findViewById(R.id.roomNo);
			instructorName = (TextView) v.findViewById(R.id.instructorName);
			time = (TextView) v.findViewById(R.id.time);
			view = v;
			v.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			Intent i = new Intent(v.getContext(), AdminCourseViewActivity.class);
			Section c = items.get(getLayoutPosition());
			i.putExtra("EXTRA_ID", c.id);
			i.putExtra("EXTRA_JSON", c.json);
			v.getContext().startActivity(i);
		}
	}

	List<Section> items;

	public AdminSectionsListAdapter(List<Section> items) {
		this.items = items;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int pos) {
		Section s = items.get(pos);
		vh.sectionNo.setText("section #" + s.sectionNo);
		vh.roomNo.setText("at room:" + s.roomNo);
		vh.instructorName.setText("instructor: " + s.instructor.firstname + " "
				+ s.instructor.lastname);
		vh.time.setText(new Date(s.time).toString());
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.row_section, parent, false);
		return new ViewHolder(v);
	}

}
