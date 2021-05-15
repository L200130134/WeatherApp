package com.rikyahmadfathoni.test.weatherapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.rikyahmadfathoni.test.weatherapp.R;
import com.rikyahmadfathoni.test.weatherapp.data.model.DetailsModel;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsList;

import java.util.ArrayList;
import java.util.List;

public class ForecastDetailsAdapter extends RecyclerView.Adapter<ForecastDetailsAdapter.ViewHolder> {

    private List<DetailsModel> models = new ArrayList<>();

    public void setModels(List<DetailsModel> models) {
        this.models = models;
    }

    public List<DetailsModel> getModels() {
        return models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_details_forecast, parent, false);
        return new ForecastDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    private DetailsModel getItem(int position) {
        return UtilsList.get(models, position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView icon;
        private final TextView textName;
        private final TextView textValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            textName = itemView.findViewById(R.id.text_name);
            textValue = itemView.findViewById(R.id.text_value);
        }

        public void bindData(@Nullable DetailsModel item) {
            if (item != null) {
                final int resId = item.getIcon();
                if (resId != 0) {
                    icon.setImageResource(resId);
                }
                textName.setText(item.getName());
                textValue.setText(item.getValue());
            }
        }
    }
}
