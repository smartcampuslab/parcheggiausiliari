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
import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;

/**
	 * 
	 * Class which populates the suggestion dropdown and performs the filtering
	 * 
	 * @author Michele Armellini
	 */
	public class GeoObjectAdapter extends ArrayAdapter<GeoObject> {
		private Filter filter;
		private final Context context;
		private List<GeoObject> items = new ArrayList<GeoObject>();
		private List<GeoObject> filtered = new ArrayList<GeoObject>();

		public GeoObjectAdapter(Context context, List<GeoObject> values) {
			super(context, R.layout.search_item, values);
			this.context = context;
			this.filtered.addAll(values);
			this.items.addAll(values);
			this.filter = new MyFilter();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater
					.inflate(R.layout.search_item, parent, false);
			TextView textView = (TextView) rowView
					.findViewById(R.id.search_item);
			if (position < filtered.size())
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
				FilterResults result = new FilterResults();
				if (constraint != null && constraint.toString().length() > 0) {
					constraint = constraint.toString().toLowerCase();
					ArrayList<GeoObject> filt = new ArrayList<GeoObject>();
					ArrayList<GeoObject> lItems = new ArrayList<GeoObject>();
					synchronized (this) {
						lItems.addAll(items);
					}
					for (int i = 0, l = lItems.size(); i < l; i++) {
						GeoObject m = lItems.get(i);
						if (m.getName().toLowerCase().contains(constraint))
							filt.add(m);
					}
					result.count = filt.size();
					result.values = filt;
				} else {

					filtered.clear();
					result.count = filtered.size();
					result.values = filtered;
				}
				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// NOTE: this function is *always* called from the UI thread.
				filtered = (ArrayList<GeoObject>) results.values;
				ArrayList<GeoObject> temp = new ArrayList<GeoObject>();
				temp.addAll(filtered);
				clear();
				for (int i = 0, l = temp.size(); i < l; i++) {
					add(temp.get(i));
				}
				notifyDataSetChanged();
				notifyDataSetInvalidated();
			}

		}
	}