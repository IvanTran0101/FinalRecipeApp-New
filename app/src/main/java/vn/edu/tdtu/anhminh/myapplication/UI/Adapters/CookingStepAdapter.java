package vn.edu.tdtu.anhminh.myapplication.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
import vn.edu.tdtu.anhminh.myapplication.R;

public class CookingStepAdapter extends RecyclerView.Adapter<CookingStepAdapter.StepViewHolder> {
    private final List<Instruction> instructionList;

    public CookingStepAdapter(List<Instruction> instructionList) {
        this.instructionList = instructionList;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cooking_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Instruction step = instructionList.get(position);
        holder.tvTitle.setText("Step " + step.getStepNumber());

        holder.tvContent.setText(step.getInstruction());
    }

    @Override
    public int getItemCount() {
        return instructionList == null ? 0 : instructionList.size();
    }

    static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;
        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_step_title);
            tvContent = itemView.findViewById(R.id.tv_step_content);
        }
    }
}