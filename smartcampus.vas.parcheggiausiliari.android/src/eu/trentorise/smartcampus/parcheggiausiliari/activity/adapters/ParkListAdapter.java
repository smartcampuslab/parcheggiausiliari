package eu.trentorise.smartcampus.parcheggiausiliari.activity.adapters;

import java.util.ArrayList;
import java.util.List;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;

/**
 * adapter for populating the ListView and performing the research
 * 
 * @author Michele Armellini
 * 
 */
public class ParkListAdapter extends ArrayAdapter<Parking> {
	private Filter filter;
	private final Context context;
	private List<Parking> items = new ArrayList<Parking>();
	private List<Parking> filtered = new ArrayList<Parking>();

	public ParkListAdapter(Context context, List<Parking> values) {
		super(context, R.layout.listrow, values);
		this.context = context;
		this.filtered.addAll(values);
		this.items.addAll(values);
		this.filter = new MyFilter();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.listrow, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.txt1);
		textView.setText(filtered.get(position).getName());

		return rowView;
	}

	public void updateCollections(List<Parking> parkings){
		this.filtered.clear();
		this.filtered.addAll(parkings);
		this.items.clear();
		this.items.addAll(parkings);
	}
	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new MyFilter();
		return filter;
	}

	private class MyFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			filtered.clear();
			filtered.addAll(items);
			constraint = constraint.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<Parking> filt = new ArrayList<Parking>();
				ArrayList<Parking> lItems = new ArrayList<Parking>();
				synchronized (this) {
					lItems.addAll(filtered);
				}
				for (int i = 0, l = lItems.size(); i < l; i++) {
					Parking m = lItems.get(i);
					if (m.getName().toLowerCase().contains(constraint))
						filt.add(m);
				}
				result.count = filt.size();
				result.values = filt;
			} else {

				filtered.clear();
				filtered.addAll(items);
				result.count = filtered.size();
				result.values = filtered;
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filtered = (ArrayList<Parking>) results.values;
			ArrayList<Parking> temp = new ArrayList<Parking>();
			temp.addAll(filtered);
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = temp.size(); i < l; i++) {
				add(temp.get(i));
			}

			notifyDataSetInvalidated();
		}

	}

}
