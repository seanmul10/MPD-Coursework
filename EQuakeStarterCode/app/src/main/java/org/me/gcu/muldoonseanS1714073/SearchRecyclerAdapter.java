// Code written by Sean Muldoon
// S1714073
// Mobile Platform Development coursework submission

package org.me.gcu.muldoonseanS1714073;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private static RecyclerClickListener clickListener;

    private Context context;

    private List<Earthquake> earthquakeRange;

    public SearchRecyclerAdapter(Context context, RecyclerClickListener clickListener, List<Earthquake> earthquakeRange)
    {
        this.context = context;
        this.clickListener = clickListener;
        this.earthquakeRange = earthquakeRange;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public int index;
        private TextView eqHeader;
        private TextView eqLocation;
        private TextView eqDateTime;
        private TextView eqData;

        public ViewHolder(View view, Context context) {
            super(view);

            // Define click listener for the ViewHolder's View
            eqHeader = (TextView) view.findViewById(R.id.eqHeader);
            eqLocation = (TextView) view.findViewById(R.id.eqLocation);
            eqDateTime = (TextView) view.findViewById(R.id.eqDateTime);
            eqData = (TextView) view.findViewById(R.id.eqData);

            view.setOnClickListener(this);
        }

        public TextView getEqHeader() { return eqHeader; }

        public TextView getEqLocation() { return eqLocation; }

        public TextView getEqDateTime() { return eqDateTime; }

        public TextView getEqData() { return eqData; }


        @Override
        public void onClick(View view) {
            clickListener.onRecyclerItemClicked(view, index);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, viewGroup,false);

        return new ViewHolder(view, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Earthquake earthquake = new Earthquake();
        switch (position) {
            case 0:
                earthquake = EarthquakeData.getBiggestEarthquake(earthquakeRange);
                viewHolder.getEqHeader().setText(context.getResources().getString(R.string.search_largest));
                viewHolder.getEqData().setText(Float.toString(earthquake.getMagnitude()));
                break;
            case 1:
                earthquake = EarthquakeData.getMostNorthernEarthquake(earthquakeRange);
                viewHolder.getEqHeader().setText(context.getResources().getString(R.string.search_most_northerly));
                viewHolder.getEqData().setText(earthquake.getLatLngString());
                break;
            case 2:
                earthquake = EarthquakeData.getMostEasterlyEarthquake(earthquakeRange);
                viewHolder.getEqHeader().setText(context.getResources().getString(R.string.search_most_easterly));
                viewHolder.getEqData().setText(earthquake.getLatLngString());
                break;
            case 3:
                earthquake = EarthquakeData.getMostSoutherlyEarthquake(earthquakeRange);
                viewHolder.getEqHeader().setText(context.getResources().getString(R.string.search_most_southerly));
                viewHolder.getEqData().setText(earthquake.getLatLngString());
                break;
            case 4:
                earthquake = EarthquakeData.getMostWesterlyEarthquake(earthquakeRange);
                viewHolder.getEqHeader().setText(context.getResources().getString(R.string.search_most_westerly));
                viewHolder.getEqData().setText(earthquake.getLatLngString());
                break;
            case 5:
                earthquake = EarthquakeData.getShallowestEarthquake(earthquakeRange);
                viewHolder.getEqHeader().setText(context.getResources().getString(R.string.search_shallowest));
                viewHolder.getEqData().setText(earthquake.getDepthString());
                break;
            case 6:
                earthquake = EarthquakeData.getDeepestEarthquake(earthquakeRange);
                viewHolder.getEqHeader().setText(context.getResources().getString(R.string.search_deepest));
                viewHolder.getEqData().setText(earthquake.getDepthString());
                break;
        }
        viewHolder.index = EarthquakeData.getEarthquakeIndex(earthquake);
        viewHolder.getEqLocation().setText(earthquake.getLocation());
        viewHolder.getEqDateTime().setText(earthquake.getDate());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 7;
    }
}