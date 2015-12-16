package com.kisdy.sdt13411.poterduffloadingview;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandListViewActivity extends AppCompatActivity {

    private int[] logos = new int[]{R.mipmap.v2_week_hot_film_selected_img,
            R.mipmap.v2_week_hot_tvplay_selected_img,
            R.mipmap.v2_week_hot_cartoon_selected_img};
    //设置组视图的显示文字
    private String[] category = new String[]{"电影", "电视", "综艺"};
    //子视图显示文字
    private String[][] subcategory = new String[][]{
            {"全部", "高清", "黄金", "最爱", "趣味", "热播"},
            {"有线", "转播", "热门", "正在联播", "体育", "主播"},
            {"周末休闲", "趣播", "热爱", "儿童秀", "挑战", "跑男"}
    };

    //子视图图片
    private int[][] sublogos = new int[][]{
            {R.mipmap.v2_live_guide_all_selected_img,
                    R.mipmap.v2_live_guide_child_selected_img,
                    R.mipmap.v2_my_video_history_selected,
                    R.mipmap.v2_my_video_like_bg_favorited,
                    R.mipmap.v2_week_hot_cartoon_selected_img,
                    R.mipmap.v2_week_hot_film_selected_img},
            {R.mipmap.v2_week_hot_tvplay_selected_img,
                    R.mipmap.video_menu_category_decode_solution_selected,
                    R.mipmap.video_menu_category_drama_selected,
                    R.mipmap.video_menu_category_resolution_selected,
                    R.mipmap.v2_live_guide_sports_selected_img,
                    R.mipmap.video_menu_category_drama_selected},
            {R.mipmap.video_menu_category_resolution_ratio_selected,
                    R.mipmap.video_menu_category_resolution_selected,
                    R.mipmap.video_menu_category_source_selected,
                    R.mipmap.v2_live_guide_child_selected_img,
                    R.mipmap.video_menu_category_drama_selected,
                    R.mipmap.video_menu_category_drama_selected}
    };


    ExpandableListView list;
    ExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_list_view);
        list=(ExpandableListView)findViewById(R.id.list);

        adapter=new ListViewExpandAdapter(ExpandListViewActivity.this,category,subcategory,logos,sublogos);
        list.setAdapter(adapter);
        //为ExpandableListView的子列表单击事件设置监听器
        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(ExpandListViewActivity.this, "你单击了："
                        + adapter.getChild(groupPosition, childPosition), Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }



    //代码太长时，将此类单独放一个文件
    class ListViewExpandAdapter extends BaseExpandableListAdapter {
        private Context mContext;
        String[] mGroup;
        String[][] mSubGroup;
        int[] mLogos;
        int[][] mSubLogos;

        public ListViewExpandAdapter(Context context) {
            super();
        }

        public ListViewExpandAdapter(Context context, String[] group, String[][] subgroup, int[] logos, int[][] subLogos) {
            super();
            mContext = context;
            mGroup = group;
            mSubGroup = subgroup;
            mLogos = logos;
            mSubLogos = subLogos;
        }

        @Override
        public int getGroupCount() {
            return  mGroup.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mSubGroup[0].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroup[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mSubGroup[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            //定义一个LinearLayout用于存放ImageView、TextView
            LinearLayout ll = new LinearLayout(mContext);
            //水平居中
            ll.setGravity(Gravity.CENTER_VERTICAL);
            //设置子控件的显示方式为水平
            ll.setOrientation(LinearLayout.HORIZONTAL);
            //定义一个ImageView用于显示列表图片
            ImageView logo = new ImageView(mContext);
            logo.setPadding(20, 10, 10, 10);
            ViewGroup.LayoutParams lparParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            logo.setLayoutParams(lparParams);
            logo.setImageResource(mLogos[groupPosition]);
            ll.addView(logo);
            TextView textView = getTextView();
            textView.setTextSize(18);
            textView.setText(mGroup[groupPosition]);
            ll.addView(textView);
            return ll;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            //定义一个LinearLayout用于存放ImageView、TextView
            LinearLayout ll = new LinearLayout(mContext);
            ll.setGravity(Gravity.CENTER_VERTICAL);

            //ll.setLayoutParams(layoutParams);
             //设置子控件的显示方式为水平
            ll.setOrientation(LinearLayout.HORIZONTAL);

            //定义一个ImageView用于显示列表图片
            ImageView logo = new ImageView(mContext);
            logo.setPadding(120, 10, 10, 10);

            //设置logo的大小
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            logo.setLayoutParams(lp);

            logo.setImageResource(mSubLogos[groupPosition][childPosition]);
            ll.addView(logo);

            TextView textView = getTextView();
            textView.setText(mSubGroup[groupPosition][childPosition]);
            textView.setTextSize(16);
            ll.addView(textView);

            return ll;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private TextView getTextView() {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(mContext);
            //设置 textView控件的布局
            textView.setLayoutParams(lp);
            //设置该textView中的内容相对于textView的位置
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
            //设置txtView的内边距
            textView.setPadding(36, 0, 0, 0);
            //设置文本颜色
            textView.setTextColor(Color.BLACK);
            return textView;
        }
    }
}
