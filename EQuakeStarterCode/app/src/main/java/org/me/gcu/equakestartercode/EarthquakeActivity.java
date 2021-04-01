package org.me.gcu.equakestartercode;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

    // Checks if the device is currently in dark mode
    public static boolean isDarkMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }
}
