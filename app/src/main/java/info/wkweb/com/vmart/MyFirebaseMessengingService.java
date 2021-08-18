package info.wkweb.com.vmart;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

/**
 * Created by spielmohitp on 1/10/2018.
 */

public class MyFirebaseMessengingService extends FirebaseMessagingService {

    public SharedPreferences pref;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onMessageReceived(RemoteMessage remoteMessage) {


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        Intent intent = new Intent(MyFirebaseMessengingService.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.FLAG_AUTO_CANCEL);

        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
     //   builder.setSound(Uri.parse("android.resource://edu.umass.casa.casaalerts/" + R.raw.chime));
        builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        // parentActivity.finish();
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        builder.setContentTitle(remoteMessage.getData().get("title"));
      //  builder.setSmallIcon(R.drawable.defaultboy);
        builder.setContentText(remoteMessage.getData().get("body"));



    }

//    public void onBackPressed() {
//
//    }


}