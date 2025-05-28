package com.example.android_doan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.R;
import com.example.android_doan.model.Delivery;
import android.content.Intent;
import com.example.android_doan.Activity.ChiTietDonHangActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeliveryOrdersAdapter extends RecyclerView.Adapter<DeliveryOrdersAdapter.DeliveryViewHolder> {

    private final List<Delivery> deliveries;
    private final Context context;

    public DeliveryOrdersAdapter(Context context, List<Delivery> deliveries) {
        this.context = context;
        this.deliveries = deliveries;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivered_order, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        Delivery delivery = deliveries.get(position);

        // Format thời gian giao hàng từ chuỗi deliveredAt
        String formattedDate = formatDate(delivery.getDeliveredAt());

        // Hiển thị thông tin
        holder.txtDate.setText(formattedDate);
        holder.txtPrice.setText("Giá: " + formatPrice(delivery.getOrder().getTotalPrice()) + "đ");

        // Xử lý sự kiện click để mở chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietDonHangActivity.class);
            intent.putExtra("deliveryId", delivery.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }

    static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtPrice;

        public DeliveryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_delivery_date);
            txtPrice = itemView.findViewById(R.id.txt_delivery_price);
        }
    }

    // Định dạng ngày giờ từ chuỗi ISO String
    private String formatDate(String dateString) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(dateString);

            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a - d 'Tháng' M 'năm' yyyy", new Locale("vi"));
            return displayFormat.format(date);
        } catch (ParseException | NullPointerException e) {
            return "Không rõ thời gian";
        }
    }

    // Format giá tiền
    private String formatPrice(double price) {
        return String.format("%,.0f", price);
    }
}
