package com.example.tasksapp.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasksapp.R;
import com.example.tasksapp.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks = new ArrayList<>(); //Inicializa lista vazia
    private OnTaskListener listener; //Inicializa listener

    //Cria listener que depois será passado a activity
    public interface OnTaskListener{
        void onTaskComplete(Task task);
        void onTaskDelete(Task task);
    }

    //Construtor do listener para inicializar ele na activity com os metodos criados acima
    public TaskAdapter(OnTaskListener listener){
        this.listener = listener;
    }


    //Cria a viewholder e infla ela com o xml item_task
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task,parent,false);
        return new TaskViewHolder(view);
    }

    //Pega a task atual e passa pra viewholder para que ela vincule os dados
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    //Retorna quantidade de tasks
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    //Seta as tasks na lista de task, ela é quem vai tirar a lista vazia para uma lista com tasks
    //e atualizar caso novas sejam adicionadas
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    //CLASSE DO VIEWHOLDER
    class TaskViewHolder extends RecyclerView.ViewHolder{
        private TextView description;
        private CheckBox complete;
        private ImageButton delete;

        public TaskViewHolder(@NonNull View itemView){
            super(itemView);

            description = itemView.findViewById(R.id.textView_description);
            complete = itemView.findViewById(R.id.checkBox_complete);
            delete = itemView.findViewById(R.id.button_delete);
        }

        public void bind (Task task){
            description.setText(task.getDescription());
            complete.setChecked(task.isComplete());

            if(task.isComplete()){
                description.setPaintFlags(
                        description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                );
            }
            else {
                description.setPaintFlags(
                        description.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
                );
            }

            complete.setOnCheckedChangeListener(null);

            // Adiciona novo listener
            complete.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Atualiza o estado da task
                task.setComplete(isChecked);

                // Notifica a Activity através do listener
                listener.onTaskComplete(task);

                // Redesenha este item (atualiza texto riscado)
                notifyItemChanged(getBindingAdapterPosition());
            });

            // 4. LISTENER DO BOTÃO DELETAR
            delete.setOnClickListener(v -> {
                // Notifica a Activity através do listener
                listener.onTaskDelete(task);
            });
        }

    }

}
