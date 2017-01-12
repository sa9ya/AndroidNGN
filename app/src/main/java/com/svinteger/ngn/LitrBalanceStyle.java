package com.svinteger.ngn;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

/**
 * Created by sa9ya on 28.12.2016.
 */

public class LitrBalanceStyle {

    public static void LitrBalanceStyle(String LitrBalance, TextView TV, String val) {
        CharSequence first;
        String[] a;
        String newLitB;
        SpannableString span1;
        if(LitrBalance.contains(val)) {
            newLitB = LitrBalance.substring(0, LitrBalance.length()-1);
        } else {
            newLitB = LitrBalance;
        }
        TextColor(newLitB, TV);
        if (newLitB.contains(".")) {
            a = newLitB.split("\\.");
            span1 = new SpannableString(a[0]);
            span1.setSpan(new RelativeSizeSpan(1.4f), 0, a[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span1.setSpan(new StyleSpan(Typeface.BOLD), 0, a[0].length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            first = TextUtils.concat(span1,".",a[1] + val);
        } else if(newLitB.contains(",")) {
            a = newLitB.split(",");
            span1 = new SpannableString(a[0]);
            span1.setSpan(new RelativeSizeSpan(1.4f), 0, a[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span1.setSpan(new StyleSpan(Typeface.BOLD), 0, a[0].length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            first = TextUtils.concat(span1,".",a[1] + val);
        } else {
            span1 = new SpannableString(newLitB);
            span1.setSpan(new RelativeSizeSpan(1.4f), 0, newLitB.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span1.setSpan(new StyleSpan(Typeface.BOLD), 0, newLitB.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            first = TextUtils.concat(span1,val);
        }
        TV.setText(first);
    }
    private static void TextColor(String number, TextView TV) {
        if(number.contains(",")) {
            String[] num = number.split("\\,");
            CharSequence CS = TextUtils.concat(num[0] + "." + num[1]);
            if (Double.valueOf(CS.toString()) < 0.0) {
                TV.setTextColor(Color.RED);
            } else {
                TV.setTextColor(Color.rgb(91,155,33));
            }
        } else {
            if (Double.valueOf(number) < 0.0) {
                TV.setTextColor(Color.RED);
            } else {
                TV.setTextColor(Color.rgb(91,155,33));
            }
        }
    }
}
