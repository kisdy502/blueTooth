package com.kisdy.sdt13411.k3app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kisdy.sdt13411.bean.FestivalLab;
import com.kisdy.sdt13411.bean.Msg;
import com.kisdy.sdt13411.fragment.FestvalCategoryFragment;


public class ChooseFestivalActivity extends AppCompatActivity {

    ListView mListview;
    ArrayAdapter<Msg> mAdapter;
    FloatingActionButton mFActionButton;
    private int festivalId;
    LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_festival);
        festivalId=getIntent().getIntExtra(FestvalCategoryFragment.FESTIVAL_ID,-1);
        initView();
        initEvents();
    }

    private void initEvents() {
        mFActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //到发送短信界面
                SendMsgActivity.toActivity(ChooseFestivalActivity.this,-1,festivalId);

            }
        });
    }

    private void initView() {
        mListview= (ListView) findViewById(R.id.id_lvMsgs);
        mFActionButton= (FloatingActionButton) findViewById(R.id.id_fabSendMsg);
        mInflater=LayoutInflater.from(ChooseFestivalActivity.this);
        mListview.setAdapter(mAdapter=new ArrayAdapter<Msg>(ChooseFestivalActivity.this,-1, FestivalLab.getInstance().getMsgsByFestivalId(festivalId)){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder=null;
                if(convertView==null){
                    convertView= mInflater.inflate(R.layout.item_msg_layout,parent,false);
                    viewHolder=new ViewHolder();
                    viewHolder.tvMsgContent= (TextView) convertView.findViewById(R.id.id_tvMsgContent);
                    viewHolder.btnChooseContact= (Button) convertView.findViewById(R.id.id_btnSendMsg);
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder=(ViewHolder)convertView.getTag();
                }
                viewHolder.tvMsgContent.setText(getItem(position).getContent());
                viewHolder.btnChooseContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //短信发送界面
                        SendMsgActivity.toActivity(ChooseFestivalActivity.this, getItem(position).getMsgId(), getItem(position).getFestivalId());
                    }
                });

                return convertView;
            }
        });
        setTitle(FestivalLab.getInstance().getFestivalByFestId(festivalId).getName());
    }
    class ViewHolder{
        TextView tvMsgContent;
        Button btnChooseContact;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
