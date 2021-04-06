// Code written by Sean Muldoon
// S1714073
// Mobile Platform Development coursework submission

package org.me.gcu.muldoonseanS1714073;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class EmptyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty, parent, false);

        return view;
    }
}
