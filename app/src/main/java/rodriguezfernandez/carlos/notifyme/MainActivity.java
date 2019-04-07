package rodriguezfernandez.carlos.notifyme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final String ACTION_UPDATE_NOTIFICATION ="com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";

    private NotificationReceiver mReceiver = new NotificationReceiver();// Crear una instancia de la clase interna.
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID=0;

    private Button button_notify;
    private Button button_cancel;
    private Button button_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //crear botones y añadir listeners.
        button_notify=findViewById(R.id.notify);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        button_update=findViewById(R.id.update);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Llamar a updateNotification
                updateNotification();
            }
        });
        button_cancel=findViewById(R.id.cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });
        setNotificationButtonState(true, false, false);

        //task 3: Registrar el receptor.
        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));

        createNotificationChannel();
    }
    public void sendNotification(){
        Intent updateIntent=new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent=PendingIntent.getBroadcast(this,NOTIFICATION_ID,updateIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder=getNotificationBuilder();
        notifyBuilder.addAction(R.drawable.ic_update,"Actualizar Notificacion",updatePendingIntent);
        mNotifyManager.notify(NOTIFICATION_ID,notifyBuilder.build());
        setNotificationButtonState(false, true, true);
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
    ///METODOS DE TASK 2:
    //Update notification
    public void updateNotification(){
        Bitmap androidImage=BitmapFactory.decodeResource(getResources(),R.drawable.mascot_1);
        NotificationCompat.Builder notifyBuilder=getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage).setBigContentTitle("Notificacion actualizada!"));
        mNotifyManager.notify(NOTIFICATION_ID,notifyBuilder.build());
        setNotificationButtonState(false, false, true);

    }
    public void cancelNotification(){
        mNotifyManager.cancel(NOTIFICATION_ID);
        setNotificationButtonState(true, false, false);

    }
    //Pone los botones en el estado que se indique en los parametros.
    void setNotificationButtonState(Boolean isNotifyEnabled,Boolean isUpdateEnabled,Boolean isCancelEnabled) {
        button_notify.setEnabled(isNotifyEnabled);
        button_update.setEnabled(isUpdateEnabled);
        button_cancel.setEnabled(isCancelEnabled);
    }
    //Para destruir el registro del receptor, reescribir onDestroy.
    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
    public class NotificationReceiver extends BroadcastReceiver{
        public NotificationReceiver(){}
        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }
    }

}
