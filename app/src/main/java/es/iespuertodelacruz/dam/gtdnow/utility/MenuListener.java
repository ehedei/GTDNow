package es.iespuertodelacruz.dam.gtdnow.utility;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.activity.DisplayerGroupActivity;
import es.iespuertodelacruz.dam.gtdnow.activity.DisplayerPlaceActivity;
import es.iespuertodelacruz.dam.gtdnow.activity.DisplayerProjectActivity;
import es.iespuertodelacruz.dam.gtdnow.activity.DisplayerTaskActivity;
import es.iespuertodelacruz.dam.gtdnow.activity.MainActivity;

public class MenuListener implements NavigationView.OnNavigationItemSelectedListener {
    private Activity activity;

    public MenuListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {

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
//            Toast.makeText(activity, "A falta de implementar login", Toast.LENGTH_SHORT).show();
            AuthUI.getInstance()
                    .signOut(activity.getApplicationContext())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {

                            new TaskAlarmScheduler(activity).cancelAllAlarms();

                            Intent i = new Intent(activity.getBaseContext(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(i);
                            activity.finish();
                        }
                    });
        }

        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
