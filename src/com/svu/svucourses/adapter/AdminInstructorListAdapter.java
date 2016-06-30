package com.svu.svucourses.adapter;

import java.util.List;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svu.svucourses.AdminInstructorViewActivity;
import com.svu.svucourses.CourseViewActivity;
import com.svu.svucourses.R;
import com.svu.svucourses.model.Instructor;

public class AdminInstructorListAdapter extends
		RecyclerView.Adapter<AdminInstructorListAdapter.ViewHolder> {

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
					AdminInstructorViewActivity.class);
			Instructor c = items.get(getLayoutPosition());
			i.putExtra("EXTRA_ID", c.id);
			i.putExtra("EXTRA_JSON", c.json);
			v.getContext().startActivity(i);
		}
	}

	List<Instructor> items;

	public AdminInstructorListAdapter(List<Instructor> items) {
		this.items = items;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int pos) {
		Instructor r = items.get(pos);
		vh.title.setText(r.firstname + " " + r.lastname);
		vh.desc.setText(r.address + " , tel:" + r.mobile);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.row_other, parent, false);
		return new ViewHolder(v);
	}

}
