package com.lostandfound.bottomnavi;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import androidx.annotation.Nullable;

public class postGet_ListAdapter extends RecyclerView.Adapter<postGet_ListAdapter.allpost_postlistget> {

    Context context_postlistget;
    List<postGet_ListModel> modelList_postlistget;

    public postGet_ListAdapter(Context context_postlistget, List<postGet_ListModel> modelList_postlistget) {
        this.context_postlistget = context_postlistget;
        this.modelList_postlistget = modelList_postlistget;
    }

    @NonNull
    @Override
    public allpost_postlistget onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context_postlistget).inflate(R.layout.postlistget_contents, parent, false);
        allpost_postlistget allpost_postlistget = new allpost_postlistget(view);
        return allpost_postlistget;
    }

    @Override
    public void onBindViewHolder(@NonNull allpost_postlistget holder, int position) {
        postGet_ListModel model = modelList_postlistget.get(position);

        // 이미지의 전체 URL 생성
        String imageUrl = model.getImageUrl().replace("\\/", "/");

        // Glide 이미지 로딩 관련 코드 추가
        Glide.with(context_postlistget)
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
        holder.storage.setText(model.getStorage());
        holder.detailContent.setText(model.getDetailContent());
    }

    @Override
    public int getItemCount() {
        return modelList_postlistget.size();
    }

    public void filterList_postlistget(List<postGet_ListModel> filteredList_postlistget) {
        modelList_postlistget = filteredList_postlistget;
        notifyDataSetChanged();
    }

    public static class allpost_postlistget extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView userID, category, title, Ddate, firstLocation, detailLocation, storage, detailContent;
        CardView cardview_postdetailget;

        public allpost_postlistget(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            imageView = itemView.findViewById(R.id.imageViewget);
            category = itemView.findViewById(R.id.category);
            title = itemView.findViewById(R.id.title);
            Ddate = itemView.findViewById(R.id.Ddate);
            firstLocation = itemView.findViewById(R.id.firstLocation);
            detailLocation = itemView.findViewById(R.id.detailLocation);
            storage = itemView.findViewById(R.id.storage);
            detailContent = itemView.findViewById(R.id.detailContent);
            cardview_postdetailget = itemView.findViewById(R.id.layout_container_get);
        }

        public void setItem(postGet_ListModel postGet_listModel) {
            if (postGet_listModel == null) {
                return; // 빈 데이터라면 아무 작업도 하지 않음
            }

            userID.setText(postGet_listModel.getUserID());
            category.setText(postGet_listModel.getCategory());
            title.setText(postGet_listModel.getTitle());
            Ddate.setText(postGet_listModel.getDdate());
            firstLocation.setText(postGet_listModel.getFirstLocation());
            detailLocation.setText(postGet_listModel.getDetailLocation());
            storage.setText(postGet_listModel.getStorage());
            detailContent.setText(postGet_listModel.getDetailContent());

            // 이미지 로드
            String imageUrl = postGet_listModel.getImageUrl();
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