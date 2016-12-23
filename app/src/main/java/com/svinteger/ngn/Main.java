package com.svinteger.ngn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sa9ya on 08.11.2016.
 */

public class Main extends Activity {

    private Boolean conn, inet;
    static JSONObject product;
    static String messageObj;
    TextView textLogin, textPass;
    static TextView textError;
    Button btnLogin;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textLogin = (EditText) findViewById(R.id.input_login);
        textPass = (EditText) findViewById(R.id.input_password);
        textError = (TextView) findViewById(R.id.errorText);
        btnLogin = (Button) findViewById(R.id.enter);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textLogin.getText().length() == 10 && textPass.getText().length() > 3) {
                    String login = textLogin.getText().toString();
                    String pass = textPass.getText().toString();
                    new log().execute(login, pass);
                } else {
                    /*vert fh = new vert();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.asd, fh);
                    ft.commit();*/

                    textError.setText("Не верно введен логин или пароль!");
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class log extends AsyncTask<String, String, String> {

        private static final String TAG_SUCCESS = "success";
        ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(Main.this);
            pDialog.setMessage("Авторизация...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            String url = "http://www.n-g-n.com/getcustomerdetails.php";
            String login;
            String password;
            login = args[0];
            password = args[1];

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("NgnLogin", login));
            params.add(new BasicNameValuePair("NgnPassword", password));

            if (isNetworkAvailable()) {
                JSONObject json = jsonParser.makeHttpRequest(url, "GET", params);

                try {
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        JSONArray productObj = json.getJSONArray("product");
                        product = productObj.getJSONObject(0);
                        conn = true;
                    } else {
                        messageObj = json.getString("message");
                        conn = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                inet = false;
                conn = false;
                messageObj = "Нет интернет соединения!";
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String u) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if (conn) {
                    Intent logged = new Intent(Main.this, logged.class);
                    startActivity(logged);
                    finish();
            } else if(!inet){
                Toast tost = Toast.makeText(getApplicationContext(), messageObj, Toast.LENGTH_SHORT);
                tost.show();
            } else {
                textError.setText(messageObj);
            }
        }
    }
    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
