package com.techno.launcher.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.techno.launcher.R;
import com.techno.launcher.model.AppListMain;

import java.util.ArrayList;


public class CustomAppListAdapter extends RecyclerView.Adapter<CustomAppListAdapter.ViewHolder> {

    ArrayList<AppListMain> appListMainArrayList;
    AppListMain appListMain;
    Context context;
    boolean isSelected;

    public CustomAppListAdapter(Context context, ArrayList<AppListMain> appListMainArrayList, boolean isSelected) {
        this.context = context;
        this.appListMainArrayList = appListMainArrayList;
        this.isSelected = isSelected;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            appListMain = appListMainArrayList.get(position);
            holder.ivAppIcon.setImageDrawable(appListMainArrayList.get(position).getAppIcon());
            holder.tvAppLabel.setText(appListMainArrayList.get(position).getAppName());

            if(isSelected){
                holder.checkBox.setVisibility(View.VISIBLE);
                if(appListMainArrayList.get(position).isSelected()){
                    holder.checkBox.setChecked(true);
                }else{
                    holder.checkBox.setChecked(false);
                }
            }else{
                holder.checkBox.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return appListMainArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView ivAppIcon;
        public TextView tvAppLabel;
        public CheckBox checkBox;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivAppIcon = view.findViewById(R.id.ivAppIcon);
            tvAppLabel = view.findViewById(R.id.tvAppLabel);
            checkBox = view.findViewById(R.id.checkBox);


        }
    }

    public void updateList(ArrayList<AppListMain> appListMainArrayList,int position) {
        appListMainArrayList.remove(position);
        this.appListMainArrayList = appListMainArrayList;
        notifyDataSetChanged();
    }
}
