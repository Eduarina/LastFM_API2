package com.lastfm_api.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lastfm_api.API.ApiConstants;
import com.lastfm_api.API.DownloadAsyncTask;
import com.lastfm_api.Models.Artistas.Images;
import com.lastfm_api.Models.Canciones.Track;
import com.lastfm_api.Models.Canciones.TrackHeader;
import com.lastfm_api.Models.Canciones.Tracks;
import com.lastfm_api.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class TracksFragment extends Fragment {

    public static int with;

    ArrayList<Track> foo = new ArrayList<>();
    RecyclerView recyclerView;

    public TracksFragment(){}

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate (R.layout.fragment_api, container, false);
        recyclerView = v.findViewById(R.id.rootContainer);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        new DownloadAsyncTask(s -> parseJSON(s)).execute(ApiConstants.API_BASE_TRACK);
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void parseJSON (String json) {

        TrackHeader trackHeader = new Gson().fromJson(json, TrackHeader.class);
        if (trackHeader == null) return;
        if(trackHeader.tracks != null){
            Tracks track = trackHeader.tracks;
            ArrayList<Track> ring = track.track;
            foo.addAll(ring);
            recyclerView.setAdapter(new TrackAdapter(foo,getContext()));
        }
    }
}

class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder>{

    Context context;
    ArrayList<Track> list;

    public TrackAdapter(ArrayList<Track> foo,Context con) {
        context = con;
        list = foo;
    }


    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());
        View view = inflater.inflate (R.layout.list_api_track, viewGroup, false);
        return new TrackViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder hold, int i) {
        Track rolon = list.get(i);
        ArrayList<Images> img = rolon.image;
        Images ima = img.get(3);
        hold.setData(rolon.name,ima.text);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView image;

        public TrackViewHolder(View view, Context context) {
            super(view);
            name = view.findViewById(R.id.Texto1);
            image = view.findViewById(R.id.imageTrack);
        }

        public void setData(String name, String text) {
            this.name.setText(name);
            Picasso
                    .get()
                    .load(text)
                    .resize(ArtistFragment.with,300)
                    .into(image);
        }
    }
}
