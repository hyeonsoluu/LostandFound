package com.lostandfound.bottomnavi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lostandfound.bottomnavi.R;
import com.lostandfound.bottomnavi.ml.Model;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.android.volley.VolleyError;


public class Posting_lost extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView imageView3;

    //음성인식
    private static final int REQUEST_CODE_SPEECH_INPUT_1 = 1000; //제목
    private static final int REQUEST_CODE_SPEECH_INPUT_2 = 1001; //위치1
    private static final int REQUEST_CODE_SPEECH_INPUT_3 = 1002; //위치2
    private static final int REQUEST_CODE_SPEECH_INPUT_4 = 1003; //내용
    private static final int REQUEST_CODE_SPEECH_INPUT_5 = 1004; //보관장소
    EditText et_title_lost, et_location1_lost, et_location2_lost, et_content_lost;
    ImageButton VoiceBtn1_lost, VoiceBtn2_lost, VoiceBtn3_lost, VoiceBtn4_lost;

    //이미지
    private ImageView imageView_lost;
    private EditText et_image_result_lost;
    ImageButton btn_camera_lost;
    int imageSize = 224;

    //갤러리
    Uri uri;
    ImageButton btn_gallery_lost;

    //업로드
    Button btn_upload_lost;
    Bitmap bitmap;

    EditText dateTextView_lost;

    TextView et_userID;
    EditText tv_markerTitle_lost;

    int y=0, m=0, d=0, h=0, mi=0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posting_lost);

        //Id값 가져오기
        et_userID = findViewById(R.id.et_userID);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras(); //Extra들을 가져옴

        String userID = bundle.getString("userID"); //가져온 Extras 중에서 꺼내기
        et_userID.setText(userID);




        //달력
        AppCompatImageButton btn_datepicker_lost = (AppCompatImageButton) findViewById(R.id.btn_datepicker_lost);
        EditText dateTextView = findViewById(R.id.et_Ddate);
        btn_datepicker_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        //이미지인식
        et_image_result_lost = findViewById(R.id.image_result_lost);
        imageView_lost = findViewById(R.id.imageView_lost);
        btn_camera_lost = findViewById(R.id.btn_camera_lost);

        btn_camera_lost.setOnClickListener(new View.OnClickListener() {
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

        //mic
        et_title_lost = findViewById(R.id.et_title_lost);
        et_location1_lost = findViewById(R.id.et_location1_lost);
        et_location2_lost = findViewById(R.id.et_location2_lost);
        et_content_lost = findViewById(R.id.et_content_lost);
        VoiceBtn1_lost = findViewById(R.id.voiceBtn1_lost); //제목
        VoiceBtn2_lost = findViewById(R.id.voiceBtn2_lost); //위치1
        VoiceBtn3_lost = findViewById(R.id.voiceBtn3_lost); //위치2
        VoiceBtn4_lost = findViewById(R.id.voicdBtn4_lost); //내용

        //제목 음성 버튼 클릭하면 음성을 텍스트로 변환하는 대화상자 표시
        VoiceBtn1_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak_title();
            }
        });
//위치1 음성 버튼 클릭하면 음성을 텍스트로 변환하는 대화상자 표시
        VoiceBtn2_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak_location1();
            }
        });

        //위치2 음성 버튼 클릭하면 음성을 텍스트로 변환하는 대화상자 표시
        VoiceBtn3_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak_location2();
            }
        });
        //내용 음성 버튼 클릭하면 음성을 텍스트로 변환하는 대화상자 표시
        VoiceBtn4_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak_content();
            }
        });


        //갤러리 연동
        AppCompatImageButton btn_gallery = (AppCompatImageButton) findViewById(R.id.btn_gallery_lost);
        imageView_lost = findViewById(R.id.imageView_lost);
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);


        //업로드
        btn_upload_lost = findViewById(R.id.btn_upload_lost);
        // 이미지 및 게시글 전송 코드 추가
        btn_upload_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이미지 선택 후 bitmap 초기화
                if (uri != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                        // 음성으로 입력한 텍스트 가져오기
                        String title = et_title_lost.getText().toString();
                        String location1 = et_location1_lost.getText().toString();
                        String location2 = et_location2_lost.getText().toString();
                        String detailContent = et_content_lost.getText().toString();

                        String category = et_image_result_lost.getText().toString();
                        String userID = et_userID.getText().toString();
                        String Ddate = dateTextView_lost.getText().toString();

                        // 서버에 이미지와 텍스트 정보를 함께 전송
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        String url = "http://hs1288.dothome.co.kr/PostUpload_LOST.php";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("Server Response", "Response: " + response);
                                        if (response.equals("success")) {
                                            Toast.makeText(Posting_lost.this, "이미지 및 게시물이 업로드되었습니다", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Posting_lost.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "이미지 및 게시물 업로드 실패", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley Error", "Error: " + error.getMessage());
                                Toast.makeText(getApplicationContext(), "이미지 및 게시물 업로드 실패", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> paramV = new HashMap<>();
                                // 이미지를 Base64로 변환
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] bytes = byteArrayOutputStream.toByteArray();
                                final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

                                paramV.put("image", base64Image);
                                paramV.put("userID", userID);
                                paramV.put("category", category);
                                paramV.put("title", title);
                                paramV.put("Ddate", Ddate);
                                paramV.put("firstLocation", location1);
                                paramV.put("detailLocation", location2);
                                paramV.put("detailContent", detailContent);

                                return paramV;
                            }
                        };
                        queue.add(stringRequest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 이미지를 선택하지 않았을 때 오류 메시지 표시
                    Toast.makeText(getApplicationContext(), "이미지를 선택하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //갤러리 연동
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null ) {
                        Intent data = result.getData();
                        uri = result.getData().getData();

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView_lost.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    public void onButtonimgClicked(View v) {
        Toast.makeText(this, "사진을 촬영해 주세요.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), Image.class);
        startActivity(intent);
    }

    public void onButtongalleryClicked(View v) {
        Toast.makeText(this, "사진을 선택해 주세요.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), Image.class);
        startActivity(intent);
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

            et_image_result_lost.setText(classes[maxPos]);


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }


    //제목 음성인식
    public void speak_title() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT_1);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //위치 음성인식
    public void speak_location1() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT_2);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //위치 음성인식
    public void speak_location2() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT_3);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //
//내용 음성인식
    public void speak_content() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT_4);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void speak_storage() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT_5);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleImage(Bitmap image) {
        int dimension = Math.min(image.getWidth(), image.getHeight());
        image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
        imageView_lost.setImageBitmap(image);

        image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
        classifyImage(image);

        // 이미지 선택 시 bitmap 초기화
        bitmap = image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if ((requestCode == 2 || requestCode == 1) && data != null) {
                // 갤러리에서 선택한 이미지 또는 카메라로 찍은 이미지 처리
                if (requestCode == 2) {
                    uri = data.getData();
                } else if (requestCode == 1) {
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    uri = getImageUri(getApplicationContext(), image);
                }

                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    handleImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//음성인식
        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT_1:{
                if (resultCode == RESULT_OK && null != data) {
                    //음성 인텐트에서 텍스트 배열 가져오기
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //텍스트 보기로 설정
                    et_title_lost.setText(result.get(0));
                }
                break;
            }
            case REQUEST_CODE_SPEECH_INPUT_2:{
                if (resultCode == RESULT_OK && null != data) {
                    //음성 인텐트에서 텍스트 배열 가져오기
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //텍스트 보기로 설정
                    et_location1_lost.setText(result.get(0));
                }
                break;
            }
            case REQUEST_CODE_SPEECH_INPUT_3:{
                if (resultCode == RESULT_OK && null != data) {
                    //음성 인텐트에서 텍스트 배열 가져오기
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //텍스트 보기로 설정
                    et_location2_lost.setText(result.get(0));
                }
                break;
            }
            case REQUEST_CODE_SPEECH_INPUT_4:{
                if (resultCode == RESULT_OK && null != data) {
                    //음성 인텐트에서 텍스트 배열 가져오기
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //텍스트 보기로 설정
                    et_content_lost.setText(result.get(0));
                }
                break;
            }
        }

    }
    // Bitmap을 Uri로 변환하는 함수
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    void showDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {

                // Date Picker에서 선택한 날짜를 TextView에 설정

                dateTextView_lost = findViewById(R.id.et_Ddate_lost);

                dateTextView_lost.setText(String.format("%d-%d-%d", yy,mm+1,dd));

            }
        }, 2023, 9, 1);

        datePickerDialog.show();
    }



}


