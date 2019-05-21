package com.example.hellovorld;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class FirestoreRecyclerAdapter extends com.firebase.ui.firestore.FirestoreRecyclerAdapter<Projectsdata, FirestoreRecyclerAdapter.ProjectHolder> {


    public FirestoreRecyclerAdapter(@NonNull FirestoreRecyclerOptions options) {

        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull ProjectHolder holder, int position, @NonNull Projectsdata model) {
      //  holder.nocompleted.setText(String.valueOf(model.getnocompleted()));
    //    holder.total.setText(String.valueOf(model.getTotal()));
        DocumentSnapshot snapshot=getSnapshots().getSnapshot(holder.getAdapterPosition());

        holder.Name.setText(snapshot.getId());
    }

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.projectsrecyclerview, viewGroup, false);
        return new ProjectHolder(v);
    }

    class ProjectHolder extends RecyclerView.ViewHolder {
  //      TextView nocompleted;
   //     TextView total;
        TextView Name;

        public ProjectHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.projectname);
   //         nocompleted = itemView.findViewById(R.id.nocompleted);
      //      total = itemView.findViewById(R.id.total);

        }
    }
}
