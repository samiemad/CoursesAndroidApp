package com.svu.svucourses.adapter;

import java.util.List;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svu.svucourses.AdminStudentViewActivity;
import com.svu.svucourses.CourseViewActivity;
import com.svu.svucourses.R;
import com.svu.svucourses.model.Student;

public class AdminStudentsListAdapter extends
		RecyclerView.Adapter<AdminStudentsListAdapter.ViewHolder> {

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
			Intent i = new Intent(v.getContext(),
					AdminStudentViewActivity.class);
			Student c = items.get(getLayoutPosition());
			i.putExtra("EXTRA_ID", c.id);
			i.putExtra("EXTRA_JSON", c.json);
			v.getContext().startActivity(i);
		}
	}

	List<Student> items;

	public AdminStudentsListAdapter(List<Student> items) {
		this.items = items;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int pos) {
		Student r = items.get(pos);
		vh.title.setText(r.firstname + " " + r.lastname);
		vh.desc.setText(r.address + " , tel:" + r.mobile + "\nreg year: "
				+ r.regYear);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.row_other, parent, false);
		return new ViewHolder(v);
	}
}
