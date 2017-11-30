package com.example.android.popularmovies.data;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by lsitec205.ferreira on 22/11/17.
 */

public class Trailer {
    private String key;
    private String name;
    private String site;


    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSite(String site) {
        this.site = site;
    }


    public Intent getYoutubeIntent(){
        if((site == "YouTube")||(key != null)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
            intent.putExtra("VIDEO_ID", key);
            return intent;
        }
        throw new UnsupportedOperationException("Unknown uri: " + key);
    }

    public Intent getWebIntent(){
        if((site == "YouTube")||(key != null)) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + key));
            webIntent.putExtra("VIDEO_ID", key);
            return webIntent;
        }
        throw new UnsupportedOperationException("Unknown uri: " + key);
    }

    public String getName() {
        return name;
    }
}
