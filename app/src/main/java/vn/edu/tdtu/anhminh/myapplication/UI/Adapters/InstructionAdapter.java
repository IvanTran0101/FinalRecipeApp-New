package vn.edu.tdtu.anhminh.myapplication.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
import vn.edu.tdtu.anhminh.myapplication.R;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {
    private List<Instruction> list = new ArrayList<>();

    public void setInstructions(List<Instruction> instructions) {
        if (instructions != null) {
            this.list = instructions;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instruction_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Instruction item = list.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.instruction_text);
        }

        void bind(Instruction instruction) {
            if (instruction != null) {
                text.setText(instruction.getStepNumber() + ". " + instruction.getInstruction());
            }
        }
    }
}
