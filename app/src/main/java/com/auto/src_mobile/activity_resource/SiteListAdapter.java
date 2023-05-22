package com.auto.src_mobile.activity_resource;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.auto.src_mobile.R;
import com.auto.src_mobile.network_side.CamInfo;

import java.util.List;

public class SiteListAdapter extends BaseAdapter {

    private Context mContext;
    private List mCamInfoList;

    public SiteListAdapter(Context context, List<CamInfo> camInfoList){
        mContext = context;
        mCamInfoList = camInfoList;
    }
    @Override
    public int getCount() {
        return mCamInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCamInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //최초 로드 시
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.adapter_site_list, parent, false);
        }

        //view 가져오기
        TextView location = convertView.findViewById(R.id.data_location);
        TextView url = convertView.findViewById(R.id.data_url_address);

        //data 불러오기
        CamInfo camInfo = (CamInfo) getItem(position);

        //view 에 data 삽입
        location.setText(camInfo.getsName());
        //url.setText("url : "+data.getRtspUrl());

        return convertView;
    }
}
