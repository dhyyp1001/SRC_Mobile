package com.auto.src_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;

import com.auto.src_mobile.R;
import com.auto.src_mobile.activity_resource.SiteListAdapter;
import com.auto.src_mobile.network_side.CamInfo;
import com.auto.src_mobile.network_side.NetworkUserConnection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SiteListActivity extends AppCompatActivity {
    List<CamInfo> camInfoList;
    SiteListAdapter adapter;
    ListView listView;
    Long mLastClickTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list);

        listView = findViewById(R.id.list_view);
        camInfoList = new ArrayList<>();

        Gson gson = new Gson();
        camInfoList = gson.fromJson(NetworkUserConnection.listValues, new TypeToken<ArrayList<CamInfo>>(){}.getType());

        adapter = new SiteListAdapter(this, camInfoList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            //리스트 중복선택 지연
            if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                //클릭시 이벤트
                CamInfo selectedData = (CamInfo) adapter.getItem(position);
                Intent intent = new Intent(SiteListActivity.this, SRCEndActivity.class);
                intent.putExtra("mediaUri", selectedData.getMediaUri());
                intent.putExtra("modIP", selectedData.getModIP());

                startActivity(intent);
            }
            mLastClickTime = SystemClock.elapsedRealtime();
        });
    }
}
