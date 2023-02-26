package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.currentPost;
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

    private Button accommodationsTag;
    private Button eventsTag;
    private Button vacanciesTag;
    private Button visaTag;
    private Button moneyAndBanksTag;
    private Button lookingForHelpTag;
    private Button otherTag;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_search, container, false);

        setup();

        postAdapter.setOnItemClickListener(new postAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItemPost(position, "Clicked");

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFirestore(query);
                selectButton(null);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        accommodationsTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(accommodationsTag);
            }
        });

        eventsTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(eventsTag);

            }
        });

        vacanciesTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(vacanciesTag);

            }
        });

        visaTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(visaTag);

            }
        });

        moneyAndBanksTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(moneyAndBanksTag);

            }
        });

        lookingForHelpTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(lookingForHelpTag);

            }
        });

        otherTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(otherTag);

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

        accommodationsTag = view.findViewById(R.id.accommodationsTag);
        eventsTag = view.findViewById(R.id.eventsTag);
        vacanciesTag = view.findViewById(R.id.vacanciesTag);
        visaTag = view.findViewById(R.id.visaTag);
        moneyAndBanksTag = view.findViewById(R.id.moneyAndBanksTag);
        lookingForHelpTag = view.findViewById(R.id.lookingForHelpTag);
        otherTag = view.findViewById(R.id.otherTag);

    }

    private void searchFirestore(String searchText) {
        Query queryPostsBody = db.collection("Posts")
                .orderBy("body")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        Query queryPinnedPostsBody = db.collection("PinnedPosts")
                .orderBy("body")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        queryPostsBody.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                postList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    post post = document.toObject(post.class);
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }
        });

        queryPinnedPostsBody.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    post post = document.toObject(post.class);
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }
        });


    }

    public void changeItemPost(int position, String text) {

        Intent intent = new Intent(getActivity(), currentPost.class);
        intent.putExtra("currentPost", postList.get(position));
        startActivity(intent);


    }

    void filterByTag(String tag) {
        Query queryPostsTag = db.collection("Posts")
                .whereEqualTo("tag", tag);

        Query queryPinnedPostsTag = db.collection("PinnedPosts")
                .whereEqualTo("tag", tag);


        queryPostsTag.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                postList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    post post = document.toObject(post.class);
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }
        });

        queryPinnedPostsTag.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    post post = document.toObject(post.class);
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }
        });
    }

    void selectButton(Button button) {
        accommodationsTag.setSelected(false);
        eventsTag.setSelected(false);
        vacanciesTag.setSelected(false);
        visaTag.setSelected(false);
        moneyAndBanksTag.setSelected(false);
        lookingForHelpTag.setSelected(false);
        otherTag.setSelected(false);

        if (button != null) {
            button.setSelected(true);
            filterByTag(getSelectedTag());
        }
    }

    String getSelectedTag() {
        if (accommodationsTag.isSelected()) return accommodationsTag.getText().toString();
        if (eventsTag.isSelected()) return eventsTag.getText().toString();
        if (vacanciesTag.isSelected()) return vacanciesTag.getText().toString();
        if (visaTag.isSelected()) return visaTag.getText().toString();
        if (moneyAndBanksTag.isSelected()) return moneyAndBanksTag.getText().toString();
        if (lookingForHelpTag.isSelected()) return lookingForHelpTag.getText().toString();
        if (otherTag.isSelected()) return otherTag.getText().toString();

        return null;
    }


}


