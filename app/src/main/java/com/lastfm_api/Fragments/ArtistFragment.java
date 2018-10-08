package com.lastfm_api.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lastfm_api.API.ApiConstants;
import com.lastfm_api.API.DownloadAsyncTask;
import com.lastfm_api.Models.Artistas.Artist;
import com.lastfm_api.Models.Artistas.ArtistHeader;
import com.lastfm_api.Models.Artistas.Artists;
import com.lastfm_api.Models.Artistas.Images;
import com.lastfm_api.R;


import com.squareup.picasso.Picasso;


import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ArtistFragment extends Fragment {

    public static int with;

    ArrayList<Artist> foo = new ArrayList<>();
    RecyclerView recyclerView;

    public ArtistFragment(){}

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate (R.layout.fragment_api, container, false);
        recyclerView = v.findViewById(R.id.rootContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        with = getResources().getConfiguration().screenWidthDp;
        new DownloadAsyncTask(s -> parseJSON(s)).execute(ApiConstants.API_BASE_URL);
        return v;
    }

    private void parseJSON (String json) {

        ArtistHeader artistHeader = new Gson().fromJson(json, ArtistHeader.class);
        if (artistHeader == null) return;
        if(artistHeader.artists != null){
            Artists artista = artistHeader.artists;
            if(artista.artist != null){
                ArrayList<Artist>art = artista.artist;
                foo.addAll(art);
                recyclerView.setAdapter(new ArtistAdapter(foo,getContext()));
            }
        }
    }
}

class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>{

    Context context;
    ArrayList<Artist> list;

    public ArtistAdapter(ArrayList<Artist> foo,Context con) {
        context = con;
        list = foo;
    }


    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());
        View view = inflater.inflate (R.layout.list_api_artist, viewGroup, false);
        return new ArtistViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder hold, int i) {
        Artist artist = list.get(i);
        ArrayList<Images> img = artist.image;
        Images ima = img.get(3);
        hold.setData(artist.name,artist.listeners,ima.text,artist.url);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder{
        Context context;
        public static String url;
        TextView name, listener;
        ImageView image;

        public ArtistViewHolder(@NonNull View itemView,Context cont) {
            super(itemView);
            context = cont;
            name = itemView.findViewById(R.id.Cantante);
            listener = itemView.findViewById(R.id.Numero);
            image = itemView.findViewById(R.id.Image1);
        }

        public void setData(String name, int listeners, String text, String url) {
            this.name.setText(name);
            listener.setText(""+listeners);
            Picasso
                    .get()
                    .load(text)
                    .resize(ArtistFragment.with,220)
                    .into(image);
        }
    }
}
