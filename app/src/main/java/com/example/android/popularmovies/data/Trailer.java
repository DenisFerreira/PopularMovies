package com.example.android.popularmovies.data;

import android.content.Intent;
import android.net.Uri;

import java.net.MalformedURLException;

/**
 * Created by lsitec205.ferreira on 22/11/17.
 */

public class Trailer {
    private String key;
    private String Name;
    private String site;
    private String type;


    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Intent getIntent(){
        if((site == "YouTube")||(key != null)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
            intent.putExtra("VIDEO_ID", key);
            return intent;
        }
        throw new UnsupportedOperationException("Unknown uri: " + key);
    }

    public String getName() {
        return Name;
    }
}
