package es.iespuertodelacruz.dam.gtdnow.utility;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.activity.DisplayerGroupActivity;
import es.iespuertodelacruz.dam.gtdnow.activity.DisplayerPlaceActivity;
import es.iespuertodelacruz.dam.gtdnow.activity.DisplayerProjectActivity;
import es.iespuertodelacruz.dam.gtdnow.activity.DisplayerTaskActivity;

public class MenuListener implements NavigationView.OnNavigationItemSelectedListener {
    private Activity activity;

    public MenuListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_tasks) {
            activity.startActivity(new Intent(activity.getBaseContext(), DisplayerTaskActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        } else if (id == R.id.nav_projects) {
            activity.startActivity(new Intent(activity.getBaseContext(), DisplayerProjectActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        } else if (id == R.id.nav_places) {
            activity.startActivity(new Intent(activity.getBaseContext(), DisplayerPlaceActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        } else if (id == R.id.nav_groups) {
            activity.startActivity(new Intent(activity.getBaseContext(), DisplayerGroupActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        } else if (id == R.id.nav_logout) {
            Toast.makeText(activity, "A falta de implementar login", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
