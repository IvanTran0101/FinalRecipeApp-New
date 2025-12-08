package vn.edu.tdtu.anhminh.myapplication.UI.Adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.ShoppingListItem; // Import Clean Model
import vn.edu.tdtu.anhminh.myapplication.R;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<ShoppingListItem> items = new ArrayList<>();

    public void setItems(List<ShoppingListItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingListItem item = items.get(position);

        String qtyStr = (item.getQuantity() % 1 == 0) ?
                String.valueOf((int) item.getQuantity()) : String.valueOf(item.getQuantity());

        holder.tvDetails.setText(item.getName() + " : " + qtyStr + " " + item.getUnit());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.isChecked());
        toggleStrikeThrough(holder.tvDetails, item.isChecked());

        holder.checkBox.setOnCheckedChangeListener((v, isChecked) -> {
            item.setChecked(isChecked);
            toggleStrikeThrough(holder.tvDetails, isChecked);
        });
    }

    private void toggleStrikeThrough(TextView tv, boolean isChecked) {
        if (isChecked) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv.setTextColor(tv.getContext().getResources().getColor(android.R.color.darker_gray));
        } else {
            tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            tv.setTextColor(tv.getContext().getResources().getColor(android.R.color.black));
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDetails;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDetails = itemView.findViewById(R.id.tv_ingredient_details);
            checkBox = itemView.findViewById(R.id.cb_bought);
        }
    }
}