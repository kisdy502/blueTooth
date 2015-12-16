package com.kisdy.sdt13411.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kisdy.sdt13411.contentprovider.SmsContentProvider;
import com.kisdy.sdt13411.db.SmsDbOpenHelper;
import com.kisdy.sdt13411.k3app.R;
import com.kisdy.sdt13411.view.FlowLayout;

/**
 * Created by sdt13411 on 2015/12/8.
 */
public class SmsHistoryFragment extends ListFragment {
    private static final int LOADER_ID = 1;
    private static final String TAG ="SmsHistoryFragment" ;
    CursorAdapter mAdapter;

    LayoutInflater inflater;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        inflater = LayoutInflater.from(getActivity());

        initLoader();

        initAdapter();
        super.onViewCreated(view, savedInstanceState);
    }


    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader loader = new CursorLoader(getActivity(), SmsContentProvider.URI_SMS_ALL, null, null, null, null);
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (loader.getId() == LOADER_ID) {
                    mAdapter.swapCursor(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mAdapter.swapCursor(null);
            }
        });
    }


    private void addTag(FlowLayout flowView, String contactName) {
        TextView view = (TextView) inflater.inflate(R.layout.tag, flowView, false);
        view.setText(contactName);
        flowView.addView(view);
    }


    private void initAdapter() {
        mAdapter = new CursorAdapter(getActivity(), null, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = inflater.inflate(R.layout.item_sented_msg, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                TextView tvContent = (TextView) view.findViewById(R.id.id_tvSms);
                FlowLayout fl_contacts = (FlowLayout) view.findViewById(R.id.id_fl_contacts);
                fl_contacts.removeAllViews();
                TextView tvFes = (TextView) view.findViewById(R.id.id_tv_fes);
                TextView tvDate = (TextView) view.findViewById(R.id.id_tv_date);

                tvContent.setText(cursor.getString(cursor.getColumnIndex(SmsDbOpenHelper.SmsItem.FILED_SMSCONTENT)));
                tvFes.setText(cursor.getString(cursor.getColumnIndex(SmsDbOpenHelper.SmsItem.FILED_FESTIVALENAME)));
                tvDate.setText(cursor.getString(cursor.getColumnIndex(SmsDbOpenHelper.SmsItem.FILED_SENDEDDATE)));

                String contacts = cursor.getString(cursor.getColumnIndex(SmsDbOpenHelper.SmsItem.FILED_CONTACTNAMES));
                if(TextUtils.isEmpty(contacts))
                    return;
                String[] names = contacts.split(";");

                for(String n:names){
                    addTag(fl_contacts,n);
                }

            }
        };

        setListAdapter(mAdapter);
    }
}
