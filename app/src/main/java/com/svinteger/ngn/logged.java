package com.svinteger.ngn;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;

/**
 * Created by Офис on 14.11.2016.
 */

public class logged extends Activity {

    TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_activity);

        mail = (TextView) findViewById(R.id.mail);

        try {
            mail.setText(Main.product.getString("email"));
            System.out.print(Main.product.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
