package org.me.gcu.equakestartercode;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class EarthquakeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.menuFragmentPlaceholder, new MenuFragment());
        fragmentTransaction.commit();
    }
}
