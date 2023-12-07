package com.lostandfound.bottomnavi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;


public class postLost_ListAdapter extends RecyclerView.Adapter<postLost_ListAdapter.allpost_postlistlost> {
    //then, create objects like this

    Context context_postlistlost;
    List<postLost_ListModel> modelList_postlistlost;

    //then create adapter constructor

    public postLost_ListAdapter(Context context_postlistlost, List<postLost_ListModel> modelList_postlistlost) {
        this.context_postlistlost = context_postlistlost;
        this.modelList_postlistlost = modelList_postlistlost;
    }

    //then, here you link the contents file that has views

    @NonNull
    @Override
    public allpost_postlistlost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context_postlistlost).inflate(R.layout.postlistlost_contents, parent, false);
        allpost_postlistlost allpost_postlistlost = new allpost_postlistlost(view);
        return allpost_postlistlost;
    }

    //then Bind your content with Model class

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull allpost_postlistlost holder, int position) {
        postLost_ListModel model = modelList_postlistlost.get(position);

        // 이미지의 전체 URL 생성
        String imageUrl = model.getImageUrl().replace("\\/", "/");

        // Glide 이미지 로딩 관련 코드 추가
        Glide.with(context_postlistlost)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "이미지 로드 실패: " + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "이미지 로드 성공");
                        return false;
                    }
                })
                .into(holder.imageView);

        // 나머지 내용 설정
        holder.userID.setText(model.getUserID());
        holder.category.setText(model.getCategory());
        holder.title.setText(model.getTitle());
        holder.Ddate.setText(model.getDdate());
        holder.firstLocation.setText(model.getFirstLocation());
        holder.detailLocation.setText(model.getDetailLocation());
        holder.detailContent.setText(model.getDetailContent());
    }

    //now, get number of items at a specific time

    @Override
    public int getItemCount() {
        return modelList_postlistlost.size();
    }

    public void filterList_postlistlost(List<postLost_ListModel> filteredList_postlistlost) {
        modelList_postlistlost = filteredList_postlistlost;
        notifyDataSetChanged();
    }

    //now find your views

    public static class allpost_postlistlost extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView cardview_postdetaillost;
        TextView userID, category, title, Ddate, firstLocation, detailLocation, detailContent;

        public allpost_postlistlost(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            category = itemView.findViewById(R.id.category);
            title = itemView.findViewById(R.id.title);
            Ddate = itemView.findViewById(R.id.Ddate);
            firstLocation = itemView.findViewById(R.id.firstLocation);
            detailLocation = itemView.findViewById(R.id.detailLocation);
            detailContent = itemView.findViewById(R.id.detailContent);

            imageView = itemView.findViewById(R.id.imageViewlost);
            cardview_postdetaillost = itemView.findViewById(R.id.layout_container_lost);

        }
        public void setItem(postLost_ListModel postLost_listModel) {
            if (postLost_listModel == null) {
                return; // 빈 데이터라면 아무 작업도 하지 않음
            }

            userID.setText(postLost_listModel.getUserID());
            category.setText(postLost_listModel.getCategory());
            title.setText(postLost_listModel.getTitle());
            Ddate.setText(postLost_listModel.getDdate());
            firstLocation.setText(postLost_listModel.getFirstLocation());
            detailLocation.setText(postLost_listModel.getDetailLocation());
            detailContent.setText(postLost_listModel.getDetailContent());

            // 이미지 로드
            String imageUrl = postLost_listModel.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("Glide", "이미지 로드 실패: " + e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("Glide", "이미지 로드 성공");
                                return false;
                            }
                        })
                        .into(imageView);
            }
        }
    }
}