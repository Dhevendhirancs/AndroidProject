package scope.com.emergencyhelpfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class Receiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        int volume = (Integer)intent.getExtras().get("android.media.EXTRA_VOLUME_STREAM_VALUE");
        Log.i("Tag", "Action : "+ intent.getAction() + " / volume : "+volume);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);



    }


}