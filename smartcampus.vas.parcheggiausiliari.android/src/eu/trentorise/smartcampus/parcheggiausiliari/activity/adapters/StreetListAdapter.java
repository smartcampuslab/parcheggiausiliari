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
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;

/**
 * adapter for populating the ListView and performing the research
 * 
 * @author Michele Armellini
 * 
 */
public class StreetListAdapter extends ArrayAdapter<Street> {
	private Filter filter;
	private final Context context;
	private List<Street> items = new ArrayList<Street>();
	private List<Street> filtered = new ArrayList<Street>();

	public StreetListAdapter(Context context, List<Street> values) {
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
				ArrayList<Street> filt = new ArrayList<Street>();
				ArrayList<Street> lItems = new ArrayList<Street>();
				synchronized (this) {
					lItems.addAll(filtered);
				}
				for (int i = 0, l = lItems.size(); i < l; i++) {
					Street m = lItems.get(i);
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
			filtered = (ArrayList<Street>) results.values;
			ArrayList<Street> temp = new ArrayList<Street>();
			temp.addAll(filtered);
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = temp.size(); i < l; i++) {
				add(temp.get(i));
			}

			notifyDataSetInvalidated();
		}

	}

	public void updateCollections(List<Street> streets) {
		this.filtered.clear();
		this.filtered.addAll(streets);
		this.items.clear();
		this.items.addAll(streets);
		
	}
}
