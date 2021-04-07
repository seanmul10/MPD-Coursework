// Code written by Sean Muldoon
// S1714073
// Mobile Platform Development coursework submission

package org.me.gcu.muldoonseanS1714073;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

// Adapter used in the list view (main) activity
public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder> {

    private static RecyclerClickListener clickListener;

    private Context context;

    private boolean darkMode;

    public ListRecyclerAdapter(Context context, RecyclerClickListener clickListener)
    {
        this.context = context;
        this.clickListener = clickListener;
        darkMode = EarthquakeActivity.isDarkMode(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private FrameLayout eqFrameLayout;
        private TextView eqLocation;
        private TextView eqMagnitude;
        private TextView eqDateTime;
        private TextView eqDepth;

        public ViewHolder(View view, Context context) {
            super(view);

            // Define click listener for the ViewHolder's View
            eqFrameLayout = (FrameLayout)view.findViewById(R.id.eqFrameLayout);
            eqLocation = (TextView) view.findViewById(R.id.eqLocation);
            eqMagnitude = (TextView) view.findViewById(R.id.eqMagnitude);
            eqDateTime = (TextView) view.findViewById(R.id.eqDateTime);

            // Depth text view only exists when device is in landscape mode
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                eqDepth = (TextView) view.findViewById(R.id.eqDepth);
            else
                eqDepth = null;

            view.setOnClickListener(this);
        }

        public FrameLayout getEqFrameLayout() {
            return eqFrameLayout;
        }

        public TextView getEqLocation() {
            return eqLocation;
        }

        public TextView getEqMagnitude() {
            return eqMagnitude;
        }

        public TextView getEqDateTime() { return eqDateTime; }

        public TextView getEqDepth()
        {
            if (eqDepth == null)
                return null;
            return eqDepth;
        }

        @Override
        public void onClick(View view) {
            clickListener.onRecyclerItemClicked(view, this.getAdapterPosition());
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup,false);

        return new ViewHolder(view, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Earthquake earthquake = EarthquakeData.getEarthquakes().get(position);
        viewHolder.getEqLocation().setText(earthquake.getLocation());
        viewHolder.getEqDateTime().setText(earthquake.getDate());
        viewHolder.getEqMagnitude().setText(Float.toString(earthquake.getMagnitude()));

        // Only set the depth text view if the device is in landscape mode
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            while (viewHolder.getEqDepth() == null) {
                // Wait until the layout has finished switching once the orientation has changed
                // This ensures there is no attempt to access the depth TextView while the TextView is null
            }
            viewHolder.getEqDepth().setText(earthquake.getDepthString());
        }

        // Alternate each items background colour
        if (darkMode)
            viewHolder.getEqFrameLayout().setBackgroundColor(context.getColor(position % 2 == 0 ? R.color.item_dark_1 : R.color.item_dark_2));
        else
            viewHolder.getEqFrameLayout().setBackgroundColor(context.getColor(position % 2 == 0 ? R.color.item_light_1 : R.color.item_light_2));
        viewHolder.getEqMagnitude().setBackgroundColor(Color.parseColor(MagnitudeColourCoding.getColour(earthquake.getMagnitude(), context)));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return EarthquakeData.getEarthquakes().size();
    }
}