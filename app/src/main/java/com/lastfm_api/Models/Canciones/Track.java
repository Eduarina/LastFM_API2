package com.lastfm_api.Models.Canciones;

import com.lastfm_api.Models.Artistas.Artist;
import com.lastfm_api.Models.Artistas.Images;

import java.util.ArrayList;

public class Track {
    public String name;
    public Artist artist;
    public ArrayList<Images> image;
}