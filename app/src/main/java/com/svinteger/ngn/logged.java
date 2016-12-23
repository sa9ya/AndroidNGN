package com.svinteger.ngn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sa9ya on 14.11.2016.
 */

public class logged extends Activity {

    public Intent i;
    private Boolean conn=false, inet=false;
    static JSONObject product;
    static String messageObj;
    static TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final ListView listView = (ListView) findViewById(R.id.list);

        TextView litrs = (TextView) findViewById(R.id.litrs);
        TextView credit = (TextView) findViewById(R.id.credit);
        TextView debt = (TextView) findViewById(R.id.debt);
        TextView money = (TextView) findViewById(R.id.money);

        try {
            if(Main.product.getDouble("credit") > 0.0) {

            } else {
                credit.setText("0");
                debt.setText("0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Double sumLitrs = 0.0;

        List<HashMap<String, String>> fillMaps = new ArrayList<>();

        try {
            for (int i = 0; i < Main.product.length()-4; i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("code", Main.product.getJSONObject(String.valueOf(i)).getString("code"));
                if (Main.product.getJSONObject(String.valueOf(i)).getInt("litr_place") > 0) {
                    map.put("litr_place", "Балансовая карта");
                    map.put("LitrBalance", String.format("%(.2f",Main.product.getDouble("total")) + " л");
                    map.put("UAHBalance", String.format("%(.2f", Main.product.getDouble("total")*Main.product.getDouble("customer_price")) + " грн"); // uah " \u20B4"
                } else {
                    map.put("litr_place", "Литровая карта");
                    map.put("LitrBalance", Main.product.getJSONObject(String.valueOf(i)).getString("litrnum") + " л");
                    map.put("UAHBalance", String.format("%(.2f", Main.product.getJSONObject(String.valueOf(i)).getDouble("litrnum")*Main.product.getDouble("customer_price")) + " грн");
                }
                fillMaps.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ListAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.card,
                new String[] {"code", "litr_place", "LitrBalance", "UAHBalance"},
                new int[] {R.id.cardCode, R.id.cardBalance, R.id.cardLitrBalance, R.id.cardUAHBalance});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Object df = parent.getItemAtPosition(position);

                HashMap<String,String> mip =(HashMap<String,String>)listView.getItemAtPosition(position);
                String code = mip.get("code");
                String litr_place = mip.get("litr_place");
                String LitrBalance = mip.get("LitrBalance");
                i = new Intent(logged.this, CardHistory.class);
                //If you wanna send any data to nextActicity.class you can use
                i.putExtra("code", code);
                i.putExtra("litr_place", litr_place);
                i.putExtra("LitrBalance", LitrBalance);
                i.putExtra("litr_place", litr_place);
                new log().execute(code);
            }
        });

        try {
            for (int i = 0; i<Main.product.length()-4; i++) {
                sumLitrs += Main.product.getJSONObject(String.valueOf(i)).getDouble("litrnum");
            }
            money.setText(String.format("%(.2f", (sumLitrs+Main.product.getDouble("total"))*Main.product.getDouble("customer_price")));
            litrs.setText(String.format("%(.2f",Main.product.getDouble("total")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    class log extends AsyncTask<String, String, String> {

        private static final String TAG_SUCCESS = "success";
        ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(logged.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            String url = "http://www.n-g-n.com/test_app.php";
            String card = args[0];

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("cardcode", card));

            if (isNetworkAvailable()) {
                JSONObject json = jsonParser.makeHttpRequest(url, "GET", params);

                try {
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        JSONArray productObj = json.getJSONArray("data");
                        System.out.println(productObj);
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
        protected void onPostExecute(String a) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if (conn) {
                startActivity(i);
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
