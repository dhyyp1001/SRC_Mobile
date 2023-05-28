package com.auto.src_mobile.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.auto.src_mobile.R;
import com.auto.src_mobile.network_side.CamInfo;
import com.auto.src_mobile.network_side.NetworkOnvifController;
import com.auto.src_mobile.network_side.NetworkUserConnection;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;

public class SRCEndActivity extends Activity {
    private Uri mediaUri;//파일 재생 위치 : String, Uri, FileDescriptor, AssetFileDescriptor 지원 함
    private static final boolean USE_TEXTURE_VIEW = false;//API 24 이상 android.view.SurfaceView 사용권장/USE_TEXTURE_VIEW 가 true = android.view.TextureView
    private static final boolean ENABLE_SUBTITLES = true;// ENABLE_SUBTITLES가 true이면 자막 ON
    private VLCVideoLayout mVideoLayout = null;// 비디오 레이아웃
    private LibVLC mLibVLC = null;// LibVLC 클래스
    private MediaPlayer mMediaPlayer = null;// 미디어 컨트롤러

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_src_end);

        //toolbar 뒤로가기
        Toolbar toolbar = findViewById(R.id.site_name_toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        mediaUri = Uri.parse(intent.getStringExtra("mediaUri"));

        // VLC 옵션
        final ArrayList<String> args = new ArrayList<>();
        args.add("-vv");
        mLibVLC = new LibVLC(this, args);// LibVLC 클래스 생성
        mMediaPlayer = new MediaPlayer(mLibVLC);// 미디어 컨트롤러 생성
        mVideoLayout = findViewById(R.id.video_layout);// 비디오 재생 레이아웃
        TextView textView = findViewById(R.id.show_site_name);
        textView.setText(intent.getStringExtra("sName"));//사이트 명 수신

        //버튼 영역
        ImageButton topButton = findViewById(R.id.circle_button_top);
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> new NetworkOnvifController(SiteListActivity.camInfo.getMediaUri(), "0", "-0.1", "no")).start();
            }
        });
        ImageButton downButton = findViewById(R.id.circle_button_down);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> new NetworkOnvifController(SiteListActivity.camInfo.getMediaUri(), "0", "0.1", "no")).start();
            }
        });
        ImageButton leftButton = findViewById(R.id.circle_button_left);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> new NetworkOnvifController(SiteListActivity.camInfo.getMediaUri(), "-0.05", "0", "no")).start();
            }
        });
        ImageButton rightButton = findViewById(R.id.circle_button_right);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> new NetworkOnvifController(SiteListActivity.camInfo.getMediaUri(), "0.05", "0", "no")).start();
            }
        });
        ImageButton homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> new NetworkOnvifController(SiteListActivity.camInfo.getMediaUri(), "0", "0", "yes")).start();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attachViews: 비디오 레이아웃 View 에 연결 // surfaceFrame(VLCVideoLayout): VLCVideoLayout 비디오가 출력될 레이아웃 변수 // dm(DisplayManager): 렌더링 전환용 변수 옵션 // subtitles(boolean): 자막 활성/비활성 // textureView(boolean): View 선택
        mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);
        //Media 미디어 로드 // ILibVLC(ILibVLC): LibVLC 클래스 변수 // path(String, Uri, FileDescriptor, AssetFileDescriptor): 미디어 객체
        final Media media = new Media(mLibVLC, mediaUri);
        mMediaPlayer.setMedia(media);// 미디어 컨트롤러 클래스에 미디어 적용
        media.release();
        mMediaPlayer.play();// 재생 시작
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();// 미디어 컨트롤러 제거
        mLibVLC.release();// VLC 제거
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();// 재생 중지
        mMediaPlayer.detachViews();// 연결된 View 제거
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
