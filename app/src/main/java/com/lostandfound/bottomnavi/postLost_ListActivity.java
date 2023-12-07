package com.lostandfound.bottomnavi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.lostandfound.bottomnavi.ml.Model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class postLost_ListActivity extends AppCompatActivity {

//firstly, define global variables

    private List<postLost_ListModel> list_postlistlost, filteredList_postlistlost;
    private postLost_ListAdapter adapter_postlistlost;
    private RecyclerView postlistlost_recyclerView;
    LinearLayoutManager linearLayoutManager;
    LinearLayout postlistlost_errorLayout;
    EditText searchLost;
    private ImageButton btn_camera_lostlist;
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list_lost);



        //then initialize adapter and recycleView
        postlistlost_errorLayout = findViewById(R.id.postlistlost_errorLayout);
        postlistlost_recyclerView = findViewById(R.id.postlistlost_recycle);

        searchLost = findViewById(R.id.searchLost);

        filteredList_postlistlost = new ArrayList<>();
        list_postlistlost = new ArrayList<>();

        adapter_postlistlost = new postLost_ListAdapter(this, list_postlistlost);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//then format the recycle view


        postlistlost_recyclerView.setAdapter(adapter_postlistlost);
        postlistlost_recyclerView.setHasFixedSize(true);
        postlistlost_recyclerView.setLayoutManager(linearLayoutManager);
        postlistlost_recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

//but also show progress, user to be patient while loading data from server

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("잠시만 기다려 주십시오. 검색 중입니다....");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

//then receive data from php file, then response can be success or failure

        final StringRequest request_postlistlost = new StringRequest("http://hs1288.dothome.co.kr/postlist_Lost.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

//make sure database exceptional are handled

                        try {
                            // 서버에서 받아온 응답 데이터를 JSONArray로 파싱
                            JSONArray array = new JSONArray(response);

                            // 받아온 데이터를 반복문을 통해 처리
                            for (int loop = 0; loop < array.length(); loop++) {
                                // 각 데이터 항목을 JSONObject로 가져옴
                                JSONObject object = array.getJSONObject(loop);

                                // JSON에서 각 필드의 데이터를 가져옴
                                String imageUrl = object.getString("imageUrl");
                                String userID = object.getString("userID");
                                String category = object.getString("category");
                                String title = object.getString("title");
                                String Ddate = object.getString("Ddate");
                                String firstLocation = object.getString("firstLocation");
                                String detailLocation = object.getString("detailLocation");
                                String detailContent = object.getString("detailContent");

                                // postGet_ListModel 객체를 생성하여 데이터 저장
                                postLost_ListModel model = new postLost_ListModel(imageUrl, userID, category, title, Ddate, firstLocation, detailLocation, detailContent);

                                // 리스트에 모델 추가
                                list_postlistlost.add(model);
                            }

                            // 어댑터에 데이터 변경을 알림
                            adapter_postlistlost.notifyDataSetChanged();
                        }

//whatever Error happens, then throw here in catch block

                        catch (Exception e) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                            alertDialog.setTitle("문제가 있습니다.");
                            alertDialog.setMessage("문제 : " + e.getMessage());
                            alertDialog.show();
                        }
                    }

//if response is failure give user the message

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                DisplayError("나중에 다시 시도해 보세요.");
            }
        });
        Volley.newRequestQueue(this).add(request_postlistlost);

        searchLost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText_postlistlost = searchLost.getText().toString();
                searchFilter_postlistlost(searchText_postlistlost);
            }
        });

        //이미지인식
        btn_camera_lostlist = findViewById(R.id.btn_camera_lostlist);

        btn_camera_lostlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 100);
                }
            }
        });

        
    }
    @Override
            protected void onResume() {
        super.onResume();
    }
    public void searchFilter_postlistlost(String searchText_postlistlost) {
        filteredList_postlistlost.clear();

        for(int i = 0; i < list_postlistlost.size(); i++) {
            if (list_postlistlost.get(i).getCategory().toLowerCase().contains(searchText_postlistlost.toLowerCase())) {
                filteredList_postlistlost.add(list_postlistlost.get(i));
            }
        }
        adapter_postlistlost.filterList_postlistlost(filteredList_postlistlost);
    }

//finally, put error message

    void DisplayError(String putError) {
        Snackbar.make(postlistlost_errorLayout, putError, Snackbar.LENGTH_LONG).show();
    }

    //이미지
    public void classifyImage(Bitmap image) {
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i = 0; i <imageSize; i++) {
                for (int j=0; j<imageSize; j++) {
                    int val = intValues[pixel++]; //RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i<confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"가방", "안경", "이어폰", "지갑", "휴대폰/태블릿", "모자", "마우스", "학용품", "usb", "충전기", "텀블러", "헤드폰", "키보드", "핸드크림", "화장품", "립", "시계", "보조배터리"};


            searchLost.setText(classes[maxPos]);


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}