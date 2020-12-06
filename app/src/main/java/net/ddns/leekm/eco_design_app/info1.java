package net.ddns.leekm.eco_design_app;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;

// 정보
public class info1 extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info1);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map,mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(@androidx.annotation.NonNull NaverMap naverMap) {
        //지도 유형
        naverMap.setMapType( NaverMap.MapType.Navi);

        //심벌 크기
        naverMap.setSymbolScale(1.5f);

        //각 지점 위도, 경도 설정
        LatLng latLng1 = new LatLng(35.19499500908695,128.0588906507621);

        //지도 중심부
        CameraUpdate cameraUpdate1 = CameraUpdate.scrollTo(latLng1);
        naverMap.moveCamera(cameraUpdate1);

        //지도 사이즈
        CameraUpdate cameraUpdate2 = CameraUpdate.zoomTo(15);
        naverMap.moveCamera(cameraUpdate2);

        // 매장 1
        Marker marker1 = new Marker();
        marker1.setPosition(latLng1);
        marker1.setMap(naverMap);

        marker1.setSubCaptionText("에코건축자재");
        marker1.setCaptionColor(android.graphics.Color.RED);
        marker1.setSubCaptionHaloColor(android.graphics.Color.YELLOW);
        marker1.setSubCaptionTextSize(10);

        InfoWindow infoWindow1 = new InfoWindow();
        infoWindow1.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @androidx.annotation.NonNull
            @Override
            public CharSequence getText(@androidx.annotation.NonNull InfoWindow infoWindow) {
                return "에코건축자재";
            }
        });
        infoWindow1.open(marker1);

    }
    public void call(View v){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-3508-1413"));
        startActivity(intent);
    }
}
