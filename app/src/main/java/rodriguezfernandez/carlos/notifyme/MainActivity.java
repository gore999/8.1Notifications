package rodriguezfernandez.carlos.notifyme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
private NotificationManager mNotifyManager;
private static final int NOTIFICATION_ID=0;

    private Button button_notify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_notify=findViewById(R.id.notify);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        createNotificationChannel();
    }
    public void sendNotification(){
        NotificationCompat.Builder notifyBuilder=getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID,notifyBuilder.build());
    }
    public void createNotificationChannel(){
        mNotifyManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(PRIMARY_CHANNEL_ID,"Mascot Notification",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notificacion desde Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent=new Intent(this,MainActivity.class);//Crear intent
        //envolver intent en un pendingIntent.
        PendingIntent notificationPendingIntent=PendingIntent.getActivity(this,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder=new NotificationCompat.Builder(this,PRIMARY_CHANNEL_ID);
        notifyBuilder.setContentTitle("Has sido notificado!")
                .setContentText("Esto es lo que te notifico")
                .setSmallIcon(R.drawable.ic_android)
                .setPriority(NotificationCompat.PRIORITY_HIGH)//añadir prioridad alta
                .setDefaults(NotificationCompat.DEFAULT_ALL)//
                .setContentIntent(notificationPendingIntent)//Añadir el pendingintent
                .setAutoCancel(true);
        return notifyBuilder;
    }
}
