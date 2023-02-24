package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.adapters.postAdapter;
import com.abdulghffar.gju_outgoings_app.objects.post;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class fragmentSearch extends Fragment {

    private FirebaseFirestore db;
    private ArrayList<post> postList;
    private postAdapter postAdapter;
    private RecyclerView recyclerView;
    private SearchView searchView;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_search, container, false);

        setup();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFirestore(query);
                System.out.println(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return view;
    }
    void setup() {
        db = FirebaseFirestore.getInstance();
        postList = new ArrayList<>();
        postAdapter = new postAdapter(postList);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(postAdapter);
        searchView = view.findViewById(R.id.search_view);

    }

    private void searchFirestore(String searchText) {
        Query queryBody = db.collection("Posts")
                .orderBy("body")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        Query queryTitle = db.collection("Posts")
                .orderBy("title")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        queryBody.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                postList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    post post = document.toObject(post.class);
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }
        });

        queryTitle.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    post post = document.toObject(post.class);
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }
        });
    }


}


