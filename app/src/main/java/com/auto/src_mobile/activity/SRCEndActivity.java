package com.auto.src_mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.auto.src_mobile.R;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;

public class SRCEndActivity extends Activity {
    private Uri OnvifUri;//파일 재생 위치 : String, Uri, FileDescriptor, AssetFileDescriptor 지원 함
    private static final boolean USE_TEXTURE_VIEW = false;//API 24 이상 android.view.SurfaceView 사용권장/USE_TEXTURE_VIEW 가 true = android.view.TextureView
    private static final boolean ENABLE_SUBTITLES = true;// ENABLE_SUBTITLES가 true이면 자막 ON
    private VLCVideoLayout mVideoLayout = null;// 비디오 레이아웃
    private LibVLC mLibVLC = null;// LibVLC 클래스
    private MediaPlayer mMediaPlayer = null;// 미디어 컨트롤러

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_src_end);
        Intent intent = getIntent();
        OnvifUri = Uri.parse(intent.getStringExtra("mediaUri"));

        // VLC 옵션
        final ArrayList<String> args = new ArrayList<>();
        args.add("-vv");
        mLibVLC = new LibVLC(this, args);// LibVLC 클래스 생성
        mMediaPlayer = new MediaPlayer(mLibVLC);// 미디어 컨트롤러 생성
        mVideoLayout = findViewById(R.id.video_layout);// 비디오 재생 레이아웃
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attachViews: 비디오 레이아웃 View 에 연결 // surfaceFrame(VLCVideoLayout): VLCVideoLayout 비디오가 출력될 레이아웃 변수 // dm(DisplayManager): 렌더링 전환용 변수 옵션 // subtitles(boolean): 자막 활성/비활성 // textureView(boolean): View 선택
        mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);
        //Media 미디어 로드 // ILibVLC(ILibVLC): LibVLC 클래스 변수 // path(String, Uri, FileDescriptor, AssetFileDescriptor): 미디어 객체
        final Media media = new Media(mLibVLC, OnvifUri);
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
}
