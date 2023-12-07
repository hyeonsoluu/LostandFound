package com.lostandfound.bottomnavi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; //하단바
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1 frag1;
    private Frag2 frag2;
    private Frag3 frag3;

    String userID = "";
    String markerTitle = "";
    //Button btn_Location;
    TextView tv_userID, tv_mainLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        markerTitle = getIntent().getStringExtra("markerTitle");
        userID = getIntent().getStringExtra("userID");



        //fragment
        Intent intent = getIntent();

        String userID = intent.getStringExtra("userID");
        String markerTitle = intent.getStringExtra("markerTitle");

        frag2 = new Frag2();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, frag2).commit();

        Bundle bundle = new Bundle();
        bundle.putString("markerTitle", markerTitle);
        bundle.putString("userID", userID);

        frag2.setArguments(bundle);


        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setSelectedItemId(R.id.action_circle);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.action_rectangle) {
                    setFrag(0);
                } else if (itemId == R.id.action_circle) {
                    setFrag(1);
                } else if (itemId == R.id.action_rectangle2) {
                    setFrag(2);
                }
                return true;
            }
        });
        frag1 = new Frag1();
        //frag2 = new Frag2();
        frag3 = new Frag3();
        setFrag(1); //첫 프래그먼트 화면 지정

    }



    //프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, frag1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, frag2);
                ft.commit();

                break;
            case 2:
                ft.replace(R.id.main_frame, frag3);
                ft.commit();
                break;

        }
    }

    public void onButtonaddClicked_postget(View v) {
        Toast.makeText(this, "습득물 게시글을 작성해 주세요.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, Posting_get.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
    public void onButtonaddClicked_postlost(View v) {
        Toast.makeText(this, "분실물 게시글을 작성해 주세요.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, Posting_lost.class);
        intent.putExtra("userID", userID);

        startActivity(intent);
    }
    public void onButtonLocation(View v) {
        Toast.makeText(this, "위치 동기화", Toast.LENGTH_SHORT).show();

    }

    public void btn_listlost(View v) {
        Intent intent = new Intent(MainActivity.this, postLost_ListActivity.class);
        startActivity(intent);
    }

    public void btn_listget(View v) {
        Intent intent = new Intent(MainActivity.this, postGet_ListActivity.class);
        startActivity(intent);
    }

    public void btn_Location(View v) {
        Intent intent = new Intent(MainActivity.this, settingLocation.class);
        startActivity(intent);
    }
}

