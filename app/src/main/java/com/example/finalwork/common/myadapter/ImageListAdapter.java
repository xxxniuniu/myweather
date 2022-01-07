package com.example.finalwork.common.myadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalwork.R;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private List<ImageListArray> listItems_weather;

    private int mPosition;
//    listview!!!
//    private int resourceId;
//
//    public ImageListAdapter(Context context, int resource, List<ImageListArray> objects) {
//        super(context, resource, objects);
//        resourceId = resource;
//    }
//
//    @NonNull
//    @Override
//    /*
//    为什么要重写getView？因为适配器中其实自带一个返回布局的方法，
//    这个方法可以是自定义适配一行的布局显示，因为我们需要更复杂的布局内容，
//    所以我们直接重写它，，不需要在导入一个简单的TextView或者ImageView布局让适配器在写入布局数据。
//    所以在recourceId自定义布局id直接导入到getView里面，getView方法不在convertView中获取布局了。
//    最后只要返回一个view布局就可以。
//     */
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        ImageListArray imageListArray = getItem(position); //得到集合中指定位置的一组数据，并且实例化
//        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false); //用布局裁剪器(又叫布局膨胀器)，将导入的布局裁剪并且放入到当前布局中
//        ImageView image = (ImageView)view.findViewById(R.id.image);//从裁剪好的布局里获取ImageView布局ID
//        TextView tv_line1 = (TextView)view.findViewById(R.id.tv_line1); //从裁剪好的布局里获取TextView布局Id
//        TextView tv_line2 = (TextView)view.findViewById(R.id.tv_line2); //从裁剪好的布局里获取TextView布局Id
//        TextView tv_line3 = (TextView)view.findViewById(R.id.tv_line3); //从裁剪好的布局里获取TextView布局Id
//        image.setImageResource(imageListArray.getImageId());//将当前一组imageListArray类中的图片iamgeId导入到ImageView布局中
//        tv_line1.setText(imageListArray.getLine1());//将当前一组imageListArray类中的TextView内容导入到TextView布局中
//        tv_line2.setText(imageListArray.getLine2());
//        tv_line3.setText(imageListArray.getLine3());
//        return view;
//    }


//    public interface OnItemClickListener{
//        void onItemClick(View view,int position);
//        void onItemLongClick(View view,int position);
//    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView tv_line1;
        TextView tv_line2;
        TextView tv_line3;

        public ViewHolder (View view)
        {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            tv_line1 = (TextView) view.findViewById(R.id.tv_line1);
            tv_line2 = (TextView) view.findViewById(R.id.tv_line2);
            tv_line3 = (TextView) view.findViewById(R.id.tv_line3);
        }

    }
    public ImageListAdapter (List <ImageListArray> imageListArrays){
        listItems_weather = imageListArrays;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ImageListArray imageListArray = listItems_weather.get(position);
        holder.image.setImageResource(imageListArray.getImageId());
        holder.tv_line1.setText(imageListArray.getLine1());
        holder.tv_line2.setText(imageListArray.getLine2());
        holder.tv_line3.setText(imageListArray.getLine3());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick(position);
                    mPosition = holder.getAdapterPosition();//将当前position赋值保存
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems_weather.size();
    }

    // 定义接口
    public interface OnItemClickListener{
        void onClick(int position);
    }

    private OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public int getPosition(){
        return mPosition;
    }
}
