package com.example.namratas.callmonitoring;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by namrata.s on 14/02/2018.
 */

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.MyViewHolder> {

    private static final String TAG = "mAdap";
    private final Context context;
    private List<CallModel> callModelList;
    private MainActivity mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView forward_image, icon;
        TextView contact_image, sim_slot, textView1;
        public TextView person_name, phone_No, inoutcoming, day, duration;
        LinearLayout layoutImage, layoutPersonDetails, layoutSimDetails, layoutDayTime, layoutIcon;
        //RelativeLayout layoutMain;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            icon = (ImageView) view.findViewById(R.id.icon);
            contact_image = (TextView) view.findViewById(R.id.contact_image);
            textView1 = (TextView) view.findViewById(R.id.textView1);
            sim_slot = (TextView) view.findViewById(R.id.sim_slot);
            forward_image = (ImageView) view.findViewById(R.id.forward_image);
            person_name = (TextView) view.findViewById(R.id.person_name);
            phone_No = (TextView) view.findViewById(R.id.phone_No);
            inoutcoming = (TextView) view.findViewById(R.id.inoutcoming);
            duration = (TextView) view.findViewById(R.id.duration);
            day = (TextView) view.findViewById(R.id.day);
            layoutImage = (LinearLayout) view.findViewById(R.id.layoutImage);
            layoutPersonDetails = (LinearLayout) view.findViewById(R.id.layoutPersonDetails);
            layoutSimDetails = (LinearLayout) view.findViewById(R.id.layoutSimDetails);
            layoutDayTime = (LinearLayout) view.findViewById(R.id.layoutDayTime);
            layoutIcon = (LinearLayout) view.findViewById(R.id.layoutIcon);

        }
    }


    public CallAdapter(Context context, List<CallModel> callModelList, MainActivity mContext) {
        this.context = context;
        this.callModelList = callModelList;
        this.mContext = mContext;
    }

    @Override
    public CallAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final CallModel callModel = callModelList.get(position);

        if(callModelList.get(position).getPerson_name() == null ) {

            holder.person_name.setText("Unknown");
            holder.contact_image.setText("+");

        }else {
            holder.person_name.setText(callModel.getPerson_name());
            String firstLetter = String.valueOf(callModelList.get(position).getPerson_name().charAt(0));
            Log.d(TAG, "person list" + "  " + firstLetter);
            holder.contact_image.setText(firstLetter);

        }


        Log.d(TAG, "size in adap" + callModelList.size());
        holder.contact_image.setText("+");

        holder.phone_No.setText(callModel.getPhone_No());
        holder.day.setText(getSmsTodayYestFromMilli(callModel.getDay()));
        holder.duration.setText(secToTime(Integer.parseInt(callModel.getDuration())));

        if (getSubscriptionInfo(context) == "1") {

            holder.sim_slot.setText("SIM 1");
        } else {
            holder.sim_slot.setText("SIM 2");
        }


        holder.inoutcoming.setText(callModel.getInoutcoming());
        if (callModel.getInoutcoming().equals("1")) {

            holder.inoutcoming.setText("Incoming");
            holder.icon.setImageResource(R.drawable.incoming_call);

        } else if (callModel.getInoutcoming().equals("2")) {

            holder.inoutcoming.setText("Outgoing");
            holder.icon.setImageResource(R.drawable.outgoing_call);

        } else {

            holder.inoutcoming.setText("Missed");
            holder.icon.setImageResource(R.drawable.missed_call);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.textView1.setText(callModel.getPhone_No().trim());
                holder.textView1.setVisibility(View.VISIBLE);
                makeCall(holder.textView1.getText().toString());

            }

            /*-------------------------------------make call --------------------------------------------------------*/
            public void makeCall(String phone_no) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                if (phone_no.trim().isEmpty()) {
                    Toast.makeText(context, " Field Cant be Empty", Toast.LENGTH_SHORT).show();
                } else {
                    intent.setData(Uri.parse("tel:" + phone_no));
                }
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Please grant the permission to call", Toast.LENGTH_SHORT).show();
                    requestPermission();
                } else {
                    mContext.startActivity(intent);
                }
            }

            private void requestPermission() {
                ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }

        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "In getItemCount     : " + callModelList.size());
        return callModelList.size();

    }

    public void removeItem(int position) {
        Log.d(TAG,"remove Item   1   : "+callModelList.size());
        callModelList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(CallModel item, int position) {
        Log.d(TAG,"Restore Item  2    : "+callModelList.size());
        callModelList.add(position, item);
        notifyItemInserted(position);
    }


    /*----------------get days--------------------------------*/
    public String getSmsTodayYestFromMilli(String msgTimeMillis) {

        Calendar messageTime = Calendar.getInstance();
        messageTime.setTimeInMillis(Long.parseLong(msgTimeMillis));
        // get Currunt time
        Calendar now = Calendar.getInstance();

        final String strTimeFormate = "h:mm aa";
        final String strDateFormate = "dd/MM/yyyy h:mm aa";

        if (now.get(Calendar.DATE) == messageTime.get(Calendar.DATE)
                &&
                ((now.get(Calendar.MONTH) == messageTime.get(Calendar.MONTH)))
                &&
                ((now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR)))
                ) {

            return "Today \n" + DateFormat.format(strTimeFormate, messageTime);

        } else if (
                ((now.get(Calendar.DATE) - messageTime.get(Calendar.DATE)) == 1)
                        &&
                        ((now.get(Calendar.MONTH) == messageTime.get(Calendar.MONTH)))
                        &&
                        ((now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR)))
                ) {
            return "Yesterday \n" + DateFormat.format(strTimeFormate, messageTime);

        } else {

            return DateFormat.format(strDateFormate, messageTime) + "";
        }
    }

    /*---------------------------------SIM SLOT--------------------------------------------------------*/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public static String getSubscriptionInfo(Context context) {

        StringBuilder sb = new StringBuilder();
        SubscriptionManager sm = SubscriptionManager.from(context);

        List<SubscriptionInfo> subscriptions = sm.getActiveSubscriptionInfoList();
        if (subscriptions != null) {
            for (SubscriptionInfo si : subscriptions) {
                sb.append("   slot: " + si.getSimSlotIndex());

                Log.d(TAG,"slot"+ si.getSimSlotIndex());

            }
        }
        return sb.toString();
    }

    /*----------------Convert date to human readable format------------------------------------*/
    public static String millisToDate(String TimeMillis) {
        String finalDate;
        long tm = Long.parseLong(TimeMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(tm);
        Date date = calendar.getTime();
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM-dd-yyyy HH:mm");
        finalDate = outputFormat.format(date);
        return finalDate;
    }


    /*--------------------convert secoond to time-------------------------------------*/
    String secToTime(int sec) {
        int second = sec % 60;
        int minute = sec / 60;
        if (minute >= 60) {
            int hour = minute / 60;
            minute %= 60;
            return hour + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
        }
        return minute + ":" + (second < 10 ? "0" + second : second);
    }

}
