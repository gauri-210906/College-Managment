package com.example.userapplication;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TaskFragment extends Fragment {

    SearchView searchTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_task, container, false);

        searchTask = view.findViewById(R.id.svFragmentTaskSearchView);

        searchTask.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTask(query);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                searchTask(query);

                return false;
            }
        });


        return view;
    }

    private void searchTask(String query) {
    }
}