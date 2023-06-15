package com.auto.src_mobile.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.auto.src_mobile.R;
import com.auto.src_mobile.network_side.NetworkModbusRead;
import com.auto.src_mobile.network_side.NetworkOnvifController;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SRCEndActivity extends Activity {
    private Uri mediaUri;//파일 재생 위치 : String, Uri, FileDescriptor, AssetFileDescriptor 지원 함
    private static final boolean USE_TEXTURE_VIEW = false;//API 24 이상 android.view.SurfaceView 사용권장/USE_TEXTURE_VIEW 가 true = android.view.TextureView
    private static final boolean ENABLE_SUBTITLES = true;// ENABLE_SUBTITLES가 true이면 자막 ON
    private VLCVideoLayout mVideoLayout = null;// 비디오 레이아웃
    private LibVLC mLibVLC = null;// LibVLC 클래스
    private MediaPlayer mMediaPlayer = null;// 미디어 컨트롤러
    int focusingCount = 0;
    int toastCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_src_end);

        // toolbar 뒤로가기
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
        TextView site_name_textView = findViewById(R.id.show_site_name);
        site_name_textView.setText(intent.getStringExtra("sName"));//사이트 명 수신

        // 방향키 버튼 영역
        ImageButton topButton = findViewById(R.id.circle_button_top);
        topButton.setOnClickListener(view -> new Thread(() -> new NetworkOnvifController(SiteListActivity.camInfo.getMediaUri(), "0", "-0.1", "no")).start());

        ImageButton downButton = findViewById(R.id.circle_button_down);
        downButton.setOnClickListener(view -> new Thread(() -> new NetworkOnvifController(SiteListActivity.camInfo.getMediaUri(), "0", "0.1", "no")).start());

        ImageButton leftButton = findViewById(R.id.circle_button_left);
        leftButton.setOnClickListener(view -> new Thread(() -> new NetworkOnvifController(SiteListActivity.camInfo.getMediaUri(), "-0.05", "0", "no")).start());

        ImageButton rightButton = findViewById(R.id.circle_button_right);
        rightButton.setOnClickListener(view -> new Thread(() -> new NetworkOnvifController(SiteListActivity.camInfo.getMediaUri(), "0.05", "0", "no")).start());

        ImageButton homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(view -> new Thread(() -> new NetworkOnvifController(SiteListActivity.camInfo.getMediaUri(), "0", "0", "yes")).start());

        // 살포 버튼 영역
        ImageButton startButton = findViewById(R.id.spray_button);
        startButton.setOnClickListener(view -> new Thread(() -> new NetworkOnvifController(SiteListActivity.camInfo.getMediaUri(), "0", "0", "yes")).start());

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
        timer.cancel();
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (focusingCount == 0) {
            //스레드 정책
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    timer.schedule(timerTask, 0, 5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            focusingCount++;
        } else if (focusingCount == 1) {
            focusingCount--;
        }
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        //메인 쓰레드 실행 위해 핸들러 사용
        public void handleMessage(Message msg) {

            NetworkModbusRead nmr = new NetworkModbusRead(SiteListActivity.camInfo.getModIP());

            String[] arr = nmr.responseArr;
            if (nmr.responseArr.length <= 1) {
                timer.cancel();
                timerTask.cancel();
                if (toastCount == 0) {
                    Toast.makeText(getBaseContext(), "모드버스 서버와 연결이 불가 합니다. \n관리자에게 문의 바랍니다.", Toast.LENGTH_LONG).show();
                    toastCount++;
                } else {
                    toastCount--;
                }
            } else {
                String tankA = arr[0];
                String tankB = arr[1];
                String sprayStatus = arr[2];
                String season = arr[3];

                TextView tank_a_text_status_textView = findViewById(R.id.tank_a_text_status);
                TextView tank_b_text_status_textView = findViewById(R.id.tank_b_text_status);
                TextView season_status_textView = findViewById(R.id.season_status);
                TextView main_status_textView = findViewById(R.id.main_status);
                tank_a_text_status_textView.setText(tankA + " %");
                tank_b_text_status_textView.setText(tankB + " %");
                main_status_textView.setText(sprayStatus);
                season_status_textView.setText(season);
                ProgressBar pgbA = findViewById(R.id.tank_a_bar_status);
                ProgressBar pgbB = findViewById(R.id.tank_b_bar_status);
                pgbA.setProgress(Integer.valueOf(tankA));
                pgbB.setProgress(Integer.valueOf(tankB));
            }
        }
    };

    Timer timer = new Timer(true);
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);
        }

        @Override
        public boolean cancel() {
            return super.cancel();
        }
    };
}
