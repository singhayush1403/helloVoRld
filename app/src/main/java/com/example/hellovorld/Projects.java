package com.example.hellovorld;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class Projects extends Fragment {
private RecyclerView recyclerView;
private List<Projectsdata> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_projects, container, false);
recyclerView=view.findViewById(R.id.projectsrv);
ProjectsAdapter projectsAdapter=new ProjectsAdapter(getContext(),list);
recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
recyclerView.setAdapter(projectsAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list=new ArrayList<>();
list.add(new Projectsdata("Delhi"));
        list.add(new Projectsdata("Bangalore"));
        list.add(new Projectsdata("Chennai"));
        list.add(new Projectsdata("Delhi"));
        list.add(new Projectsdata("Roorkee"));

    }
}
