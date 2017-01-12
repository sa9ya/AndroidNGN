package com.svinteger.ngn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

/**
 * Created by sa9ya on 15.12.2016.
 */

public class CardHistory extends Activity {

    CharSequence first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_history_activity);

        TextView cardType = (TextView) findViewById(R.id.cardType);
        TextView availableLitrs = (TextView) findViewById(R.id.availableLitrs);
        TextView limitDayText = (TextView) findViewById(R.id.limitDayText);
        TextView limitDay = (TextView) findViewById(R.id.limitDay);
        TextView limitLitrsText = (TextView) findViewById(R.id.limitLitrsText);
        TextView limitLitrs = (TextView) findViewById(R.id.limitLitrs);
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.listHistory);

        LayoutInflater ltInflater = getLayoutInflater();

        Intent card = getIntent();
        String cardcode = card.getStringExtra("code");
        String litr_place = card.getStringExtra("litr_place");
        String LitrBalance = card.getStringExtra("LitrBalance");
        cardType.setText(litr_place + " " + cardcode);
        if(litr_place.equals(getString(R.string.cardBalance))) {
            limitDayText.setText(R.string.debt);
            limitLitrsText.setText(R.string.credit);
            try {
                if(logged.AcceptLitrs - Main.product.getDouble("credit") > 0) {
                    limitDay.setText("0,00");
                } else {
                    LitrBalanceStyle.LitrBalanceStyle(String.format("%.2f", logged.AcceptLitrs - Main.product.getDouble("credit")),limitDay," l");
                }
                limitLitrs.setText(Main.product.getString("credit"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            limitDayText.setText(R.string.limitDays);
            limitLitrsText.setText(R.string.limitLitrs);
            try {
                limitDay.setText(logged.product.getString("limit_day"));
                limitLitrs.setText(logged.product.getString("limit_litrs"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LitrBalanceStyle.LitrBalanceStyle(LitrBalance, availableLitrs, " л");
        String fill;
        String des;
        if (logged.product.length()-2 > 1) {
            for (int i = 0; i < logged.product.length() - 2; i++) {
                View item = ltInflater.inflate(R.layout.card_history, linLayout, false);
                TextView descr = (TextView) item.findViewById(R.id.description);
                TextView val = (TextView) item.findViewById(R.id.value);
                TextView data = (TextView) item.findViewById(R.id.data);
                try {
                    des = logged.product.getJSONObject(String.valueOf(i)).getString("description");
                    des = des.substring(0, des.length()-8);
                    if(logged.product.getJSONObject(String.valueOf(i)).getDouble("litrsoff") == 0) {
                        fill = logged.product.getJSONObject(String.valueOf(i)).getString("points");
                    } else {
                        fill = logged.product.getJSONObject(String.valueOf(i)).getString("litrsoff");
                    }
                    descr.setText(des);
                    LitrBalanceStyle.LitrBalanceStyle(fill, val, " л");
                    //val.setText(fill+" л");
                    data.setText(logged.product.getJSONObject(String.valueOf(i)).getString("date_added"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
                linLayout.addView(item);
            }
        } else {
            Toast tost = null;
            try {
                tost = Toast.makeText(getApplicationContext(), logged.product.getString("message"), Toast.LENGTH_SHORT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (tost != null) {
                tost.show();
            }
        }
    }
}
