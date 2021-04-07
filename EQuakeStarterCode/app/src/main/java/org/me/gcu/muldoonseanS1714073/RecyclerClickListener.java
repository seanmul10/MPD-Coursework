// Code written by Sean Muldoon
// S1714073
// Mobile Platform Development coursework submission

package org.me.gcu.muldoonseanS1714073;

import android.view.View;

// Interface created to make sure recycler adapters can use the view and position of a clicked item
public interface RecyclerClickListener
{
    void onRecyclerItemClicked(View view, int position);
}