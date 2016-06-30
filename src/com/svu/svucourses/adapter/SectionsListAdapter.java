package com.svu.svucourses.adapter;

import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svu.svucourses.R;
import com.svu.svucourses.model.Section;

public class SectionsListAdapter extends
		RecyclerView.Adapter<SectionsListAdapter.ViewHolder> {

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
			int oldPos = selectedItem;
			selectedItem = getLayoutPosition();
//			Snackbar.make(v, "choice: " + items.get(selectedItem).sectionNo,
//					Snackbar.LENGTH_LONG).show();
			notifyItemChanged(oldPos);
			notifyItemChanged(selectedItem);
		}
	}

	private static final int COLOR_SEL = Color.MAGENTA;
	private static final int COLOR_NORM = Color.BLACK;
	private static final int COLOR_BG_SEL = Color.rgb(200, 225, 255);
	private static final int COLOR_BG_NORM = Color.WHITE;

	List<Section> items;
	public int selectedItem = -1;

	public SectionsListAdapter(List<Section> items) {
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
		vh.sectionNo.setTextColor(pos == selectedItem ? COLOR_SEL : COLOR_NORM);
		vh.view.setBackgroundColor(pos == selectedItem ? COLOR_BG_SEL
				: COLOR_BG_NORM);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.row_section, parent, false);
		return new ViewHolder(v);
	}

	public Section getSelectedSetion() {
		if (selectedItem >= 0)
			return items.get(selectedItem);
		else
			return null;
	}

}
