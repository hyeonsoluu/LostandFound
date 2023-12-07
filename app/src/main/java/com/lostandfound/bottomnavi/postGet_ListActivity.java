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
import android.widget.TextView;

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

import android.util.Log;

public class postGet_ListActivity extends AppCompatActivity {

//firstly, define global variables

    private ArrayList<postGet_ListModel> list_postlistget, filteredList_postlistget;
    private postGet_ListAdapter adapter_postlistget;
    private RecyclerView postlistget_recyclerView;
    LinearLayoutManager linearLayoutManager;
    LinearLayout postlistget_errorLayout;
    private EditText searchGet;

    //이미지
    private ImageButton btn_camera_getlist;
    int imageSize = 224;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list_get);



        //then initialize adapter and recycleView
        postlistget_errorLayout = findViewById(R.id.postlistget_errorLayout);
        postlistget_recyclerView = findViewById(R.id.postlistget_recycle);

        searchGet = findViewById(R.id.searchGet);

        filteredList_postlistget = new ArrayList<>();
        list_postlistget = new ArrayList<>();

        adapter_postlistget = new postGet_ListAdapter(this, list_postlistget);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

//then format the recycle view
        postlistget_recyclerView.setAdapter(adapter_postlistget);
        postlistget_recyclerView.setHasFixedSize(true);
        postlistget_recyclerView.setLayoutManager(linearLayoutManager);
        postlistget_recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter_postlistget.notifyDataSetChanged();
//but also show progress, user to be patient while loading data from server

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("잠시만 기다려 주십시오. 검색 중입니다....");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

//then receive data from php file, then response can be success or failure

        final StringRequest request_postlistget = new StringRequest("http://hs1288.dothome.co.kr/postlist_Get.php",
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
                                String userID = object.getString("userID");
                                String imageUrl = object.getString("imageUrl");
                                String category = object.getString("category");
                                String title = object.getString("title");
                                String Ddate = object.getString("Ddate");
                                String firstLocation = object.getString("firstLocation");
                                String detailLocation = object.getString("detailLocation");
                                String storage = object.getString("storage");
                                String detailContent = object.getString("detailContent");

                                // postGet_ListModel 객체를 생성하여 데이터 저장
                                postGet_ListModel model = new postGet_ListModel(userID, imageUrl, category, title, Ddate, firstLocation, detailLocation, storage, detailContent);

                                // 리스트에 모델 추가
                                list_postlistget.add(model);
                            }

                            // 어댑터에 데이터 변경을 알림
                            adapter_postlistget.notifyDataSetChanged();

                        } catch (Exception e) {
                            // 예외가 발생한 경우 AlertDialog를 통해 메시지 표시
                            AlertDialog alertDialog = new AlertDialog.Builder(postGet_ListActivity.this).create();
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
                DisplayError("나중에 다시 시도해 보세요. 에러: " + error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(request_postlistget);

        searchGet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText_postlistget = searchGet.getText().toString();
                searchFilter_postlistget(searchText_postlistget);
            }
        });

        //이미지인식
        btn_camera_getlist = findViewById(R.id.btn_camera_getlist);
        btn_camera_getlist.setOnClickListener(new View.OnClickListener() {
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

    public void searchFilter_postlistget(String searchText_postlistget) {
        filteredList_postlistget.clear();

        for (int i=0; i < list_postlistget.size(); i++) {
            if (list_postlistget.get(i).getCategory().toLowerCase().contains(searchText_postlistget.toLowerCase())) {
                filteredList_postlistget.add(list_postlistget.get(i));
            }
        }
        adapter_postlistget.filterList_postlistget(filteredList_postlistget);
    }

//finally, put error message

    void DisplayError(String putError) {
        Snackbar.make(postlistget_errorLayout, putError, Snackbar.LENGTH_LONG).show();
    }

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

            searchGet.setText(classes[maxPos]);

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