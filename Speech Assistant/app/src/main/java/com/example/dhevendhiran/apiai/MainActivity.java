package com.example.dhevendhiran.apiai;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private final int REQ_CODE_SPEECH_INPUT = 100;
    ImageButton btnSpeak;
    TextView txtSpeechInput, outputText;
    int time;
    String s = null;
    public TextToSpeech tts;
    String userQuery = null;
    int flag = 0;
    String name;

    public String callName = null;
    public String phoneNumber = null;
    private static final int REQUEST_CALL = 1;
    ArrayList<String> contactList;
    public static final int REQUEST_READ_CONTACTS = 444;
    Cursor cursor;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        outputText = (TextView) findViewById(R.id.outputText);
        tts = new TextToSpeech(this, this);

        /*name = "Dhevendhiran";
        s = "Hello\t" + name +"\t what's up";
        outputText.setText ( s );
        speakOut ();*/


        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });


    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say Something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn\\'t support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    //******************************************************************
                    //getting user query here
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //*******************************************************************
                    //convert the user query into string
                    userQuery=result.get(0);
                    //Toast.makeText(getApplicationContext(), userQuery, Toast.LENGTH_LONG).show();
                    userQuery = userQuery.toLowerCase ();

                    txtSpeechInput.setText(userQuery);

                    if(userQuery.substring ( 0,4 ).equals ( "call" )){
                        callName = userQuery.substring(5);
                        String s = "connecting call to " + callName;
                        speakOut ();
                        getContacts ();
                        // Toast.makeText ( getApplicationContext (), callName, Toast.LENGTH_SHORT ).show ();

                    }else{
                        RetrieveFeedTask task=new RetrieveFeedTask();
                        task.execute(userQuery); // go to GetText() method api.ai function
                    }
                    /*int flag = 0;


                    if(flag == 0){
                        int i = 0, j = 0;
                        char[] charUserQuery = userQuery.toCharArray ();
                        String output = "";
                        int length = userQuery.length ();
                        for (i = 0; i < length; i++) {
                            if (charUserQuery[i] == ' ') {
                                output = output.concat ( "%20" );
                            } else {
                                output = output.concat ( Character.toString ( charUserQuery[i] ) );
                            }
                        }



                        String url;
                        url = "http://api.wolframalpha.com/v2/query?appid=KPAE5V-8JGV8549K5&input=";
                        url = url.concat ( output );
                        url = url.concat ( "&includepodid=Result&output=json" );

                        new JSONTask ().execute ( url );
                        RetrieveFeedTask task=new RetrieveFeedTask();
                        task.execute(userQuery);
                    }*/
                }
                break;
            }
        }
    }
    // for api.ai
    public String GetText(String query) throws UnsupportedEncodingException {

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("https://api.api.ai/v1/query?v=20150910");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Authorization", "Bearer c7b0f823eeb44bfeae8b9aa378661409");
            //conn.setRequestProperty("Authorization", "Bearer 497facec285d4a9f8c7eb7267c7655f9");
            conn.setRequestProperty("Content-Type", "application/json");

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            JSONArray queryArray = new JSONArray();
            queryArray.put(query);
            jsonParam.put("query", queryArray);
//            jsonParam.put("name", "order a medium pizza");
            jsonParam.put("lang", "en");
            jsonParam.put("sessionId", "1234567890");


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            // Log.d("karma", "after conversion is " + jsonParam.toString());
            wr.write(jsonParam.toString());
            wr.flush();
            //Log.d("karma", "json is " + jsonParam);

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;


            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString();

            JSONObject object1 = new JSONObject(text);
            JSONObject object = object1.getJSONObject("result");
            JSONObject fulfillment = null;
            String speech = null;
//            if (object.has("fulfillment")) {
            fulfillment = object.getJSONObject("fulfillment");
//                if (fulfillment.has("speech")) {
            speech = fulfillment.optString("speech");
//                }
//            }
            //Log.d("karma ", "response is " + text);
            return speech;

        } catch (Exception ex) {
            //Log.d("karma", "exception at last " + ex);
        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }

        return null;
    }

    // for api.ai
    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... voids) {
            //String s = null;
            try {
                //***************************************************************************************
                //the output string
                s = GetText(voids[0]);
                //*****************************************************************************************

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("karma", "Exception occurred " + e);
            }
            //speakOut();
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int getFlag = checkApiAiOutput ( s );
            if(getFlag == 0) {
                outputText.setText ( s );
                speakOut ();
            }
        }
    }

    public int checkApiAiOutput(String s){
        char[] ApiAiOutputStringToChar = s.toCharArray ();
        int len = s.length ();
        int start = 0;
        if(ApiAiOutputStringToChar[len - 1] == '?'){
            char[] charUserQuery = userQuery.toCharArray ();
            String output = "";
            int length = userQuery.length ();
            for (int i = 0; i < length; i++) {
                if (charUserQuery[i] == ' ') {
                    output = output.concat ( "%20" );
                } else {
                    output = output.concat ( Character.toString ( charUserQuery[i] ) );
                }
            }
            String url = "http://api.wolframalpha.com/v2/query?appid=KPAE5V-8JGV8549K5&input=";
            url = url.concat ( output );
            url = url.concat ( "&includepodid=Result&output=json" );

            new JSONTask ().execute ( url );
            flag = 1;
        }
        if(ApiAiOutputStringToChar[0] == 'O'){
            name = s.substring ( 17 );
        }
        if(ApiAiOutputStringToChar[0] == 'U'){
            s = "You are " + name;
            outputText.setText ( s );
            speakOut ();
            flag = 1;
        }
        return flag;
    }


    public class JSONTask extends AsyncTask<String, String, String>{

        String movieName = null;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection ();
                connection.connect ();

                InputStream stream = connection.getInputStream ();

                reader = new BufferedReader ( new InputStreamReader ( stream ) );

                String line = "";

                StringBuffer buffer = new StringBuffer (  );

                while((line = reader.readLine ()) != null){
                    buffer.append ( line );
                }

                String finalJson = buffer.toString ();

                JSONObject parentObject = new JSONObject ( finalJson );

                JSONObject queryresult = parentObject.getJSONObject ( "queryresult" );

                //JSONArray parentArray = parentObject.getJSONArray ( "movies" );

                //JSONObject finalObject = parentArray.getJSONObject ( 0 );

                JSONArray pods = queryresult.getJSONArray ( "pods" );
                JSONObject podsObject = pods.getJSONObject(0);

                JSONArray subpods = podsObject.getJSONArray ( "subpods" );
                JSONObject subPodsObject = subpods.getJSONObject(0);

                // result getting from wolfram alpha stored in the string names moviewName
                movieName = subPodsObject.getString("plaintext").toString();

                return movieName;// go to onPostExecute() method

            } catch (MalformedURLException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            } catch (JSONException e) {
                e.printStackTrace ();
            } finally{
                if(connection != null) {
                    connection.disconnect ();
                }
                try{
                    if(reader != null) {
                        reader.close ();
                    }
                }catch(IOException e){
                    e.printStackTrace ();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result1) {
            super.onPostExecute ( result1 );
            if(result1 != null) {
                s = new String ( result1 );
                outputText.setText ( s );
                speakOut ();
            }else{
                //s = "Sorry, I can't understand. Could you please rephrase it?";
                s = "ennai mannithu vidungal. neengal solvadhu enakku puriyavillai. meendum orumurai koorungal";
                outputText.setText (s);
                speakOut ();
                //RetrieveFeedTask task=new RetrieveFeedTask();
                //task.execute(userQuery); // go to GetText() method api.ai function
                /*s = "puriyala";
                outputText.setText(s);
                //Toast.makeText ( getApplicationContext (), s, Toast.LENGTH_LONG ).show ();
                speakOut ();*/
            }
        }
    }

    private void getContacts() {
        if (!mayRequestContacts ()) {
            return;
        }
        contactList = new ArrayList<String> (  );


        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        StringBuffer output;

        ContentResolver contentResolver = getContentResolver ();

        cursor = contentResolver.query ( CONTENT_URI, null, null, null, null );

        int flag;
        flag =0;
        //iterate every contact in the phone
        if(cursor.getCount () > 0){
            counter = 0;
            while(cursor.moveToNext ()){
                output = new StringBuffer (  );

                String contact_id = cursor.getString ( cursor.getColumnIndex ( _ID ) );
                String name = cursor.getString ( cursor.getColumnIndex ( DISPLAY_NAME ) );
                int hasPhoneNumber = Integer.parseInt ( cursor.getString ( cursor.getColumnIndex ( HAS_PHONE_NUMBER ) ) );

                if (hasPhoneNumber > 0) {

                    //output.append ( "\n First Name :" + name );
                    name = name.toLowerCase ();
                    //this is to read multiple phone number associated with the same contacts
                    Cursor phoneCursor = contentResolver.query ( PhoneCONTENT_URI, null, Phone_CONTACT_ID + " =?" , new String[]{contact_id}, null );
                    while(phoneCursor.moveToNext ()){
                        phoneNumber = phoneCursor.getString ( phoneCursor.getColumnIndex ( NUMBER ) );
                        //output.append ( "Phone Number:" + phoneNumber );
                        //textView.setText ( phoneNumber );

                        if(name.equals ( callName )){
                            //textView.setText ( phoneNumber );

                            //makePhoneCall();
                            if(phoneNumber.trim().length() > 0){
                                if(ContextCompat.checkSelfPermission( MainActivity.this, Manifest.permission.CALL_PHONE ) != getPackageManager().PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions( MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL );
                                }else{
                                    String dial = "tel:"+phoneNumber;
                                    startActivity( new Intent (Intent.ACTION_CALL, Uri.parse( dial )) );
                                }
                            }

                            //output.append ( "Phone Number:" + phoneNumber );
                            flag =1;
                            break;

                        }
                    }

                    phoneCursor.close ();
                }

                //add the contact to the ArrayList
                contactList.add ( output.toString () );
                if (flag == 1) {
                    break;
                }
            }
        }
    }

    private boolean mayRequestContacts() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        if(checkSelfPermission ( READ_CONTACTS ) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        if(shouldShowRequestPermissionRationale ( READ_CONTACTS )){
            requestPermissions ( new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS );
        }else{
            requestPermissions ( new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS );
        }
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int grantResults[]){
        if(requestCode == REQUEST_READ_CONTACTS){
            if(grantResults.length == 1 && grantResults[0] == getPackageManager ().PERMISSION_GRANTED){
                getContacts ();
            }
        }
    }


    /*********************************************************************************
     * text to speech
     */
    public void onDestroy(){
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.US);
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","This language not supported");
            }else{
                //btnSpeak.setEnabled(true);
                speakOut();
            }
        }
    }

    private void speakOut(){
        String text = s;
        tts.setPitch( (float) 1 );
        tts.setSpeechRate( (float) 0.5 );
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}


