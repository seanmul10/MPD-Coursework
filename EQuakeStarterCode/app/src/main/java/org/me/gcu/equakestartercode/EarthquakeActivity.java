package org.me.gcu.equakestartercode;

import android.content.Context;
import android.content.Intent;
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

    public void startDetailedViewActivity(int index) {
        Intent intent = new Intent(this, DetailedEarthquakeActivity.class);
        intent.putExtra("earthquakeIndex", index);
        if (intent != null)
            startActivity(intent);
    }
}
