package com.example.namratas.callmonitoring;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final String TAG = "mAct";
    private static final int URL_LOADER = 1;


    private List<CallModel> callModelList;
    private RecyclerView recyclerView;
    private CallAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.my_cart));
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        coordinatorLayout = findViewById(R.id.coordinator_layout);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        callList();

    }

    //Call logs
    public void callList() {
        callModelList = new ArrayList<CallModel>();

        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Uri uri = Uri.parse("content://call_log/calls");
        Cursor c = getContentResolver().query(uri, null, null, null, strOrder);
        startManagingCursor(c);

        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {

                CallModel call = new CallModel();

                /*----------------Getting first letter of name-------------------*/
                //call.setContact_image(c.getString(c.getColumnIndex(CallLog.Calls._ID)));
                call.setPerson_name(c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)));

                if (c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)) == null) {
                    call.setPerson_name("Unknown");
                    call.setContact_image("+");
                } else
                    call.setPerson_name(c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                /*----------------Getting first letter of name-------------------*/

                call.setPhone_No(c.getString(c.getColumnIndex(CallLog.Calls.CACHED_FORMATTED_NUMBER)));
                call.setDay(c.getString(c.getColumnIndex(CallLog.Calls.DATE)));
                call.setInoutcoming(c.getString(c.getColumnIndex(CallLog.Calls.TYPE)));
                call.setDuration(c.getString(c.getColumnIndex(CallLog.Calls.DURATION)));
                call.setSim_slot(c.getString(c.getColumnIndex(CallLog.Calls.COUNTRY_ISO)));

                callModelList.add(call);
                Log.d(TAG, "Inside CallList" + callModelList.size());
                c.moveToNext();

            }
        }

        mAdapter = new CallAdapter(this, callModelList, MainActivity.this);
        recyclerView.setAdapter(mAdapter);
        //c.close();

    }


    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CallAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = callModelList.get(viewHolder.getAdapterPosition()).getPerson_name();

            // backup of removed item for undo purpose
            final CallModel deletedItem = callModelList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());


            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from Call Logs!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
