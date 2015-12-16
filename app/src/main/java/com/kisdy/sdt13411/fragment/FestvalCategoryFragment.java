package com.kisdy.sdt13411.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.kisdy.sdt13411.bean.FestivalBean;
import com.kisdy.sdt13411.bean.FestivalLab;
import com.kisdy.sdt13411.k3app.ChooseFestivalActivity;
import com.kisdy.sdt13411.k3app.R;


/**
 * Created by sdt13411 on 2015/12/3.
 */
public class FestvalCategoryFragment extends Fragment {
    public static final String FESTIVAL_ID = "festival_id";

    private GridView mGridView;
    private LayoutInflater mInflater;

    private ArrayAdapter<FestivalBean> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_festval_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mInflater = getActivity().getLayoutInflater();
        mGridView = (GridView) view.findViewById(R.id.id_gvFestvals);
        mGridView.setAdapter(mAdapter = new ArrayAdapter<FestivalBean>(getActivity(), -1, FestivalLab.getInstance().getFestivalList()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_festval, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.tvName = (TextView) convertView.findViewById(R.id.id_tvFestval);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tvName.setText(getItem(position).getName());
                return convertView;
            }
        });


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ChooseFestivalActivity.class);
                intent.putExtra(FESTIVAL_ID, mAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });
    }

    class ViewHolder {
        TextView tvName;
    }
}


