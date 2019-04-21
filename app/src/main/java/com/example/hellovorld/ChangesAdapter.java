package com.example.hellovorld;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChangesAdapter extends RecyclerView.Adapter<ChangesAdapter.MyViewHolder> {
    Context mcontext;
    List<Changes> changes;

    public ChangesAdapter(Context mcontext, List<Changes> changes) {
        this.mcontext = mcontext;
        this.changes = changes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v;
        v = LayoutInflater.from(mcontext).inflate(R.layout.renderrecyclerview, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder ViewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return changes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
           textView = itemView.findViewById(R.id.changestv);
        }
    }
}
