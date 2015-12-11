package ru.onyanov.geni;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import ru.onyanov.geni.models.Photo;
import ru.onyanov.geni.models.Sizes;
import ru.onyanov.geni.network.GeniAPI;
import ru.onyanov.geni.network.ImageLoadCallback;
import ru.onyanov.geni.network.PhotoLoader;

public class PlaceholderFragment extends Fragment implements Callback<Photo>,ImageLoadCallback {

    private static final String ARG_PHOTO_ID = "photo_id";
    private static final String ARG_IMAGE_URI = "image_uri";

    private static final String TAG = "PlaceholderFragment";
    private ImageView imageView;
    private ProgressBar progress;
    private String imageUri;
    private String imageFilePath;
    private FloatingActionButton fab;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int photoId) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PHOTO_ID, photoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        setRetainInstance(true);

        imageView = (ImageView) rootView.findViewById(R.id.image);
        progress = (ProgressBar) rootView.findViewById(R.id.progressBar);

        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getString(ARG_IMAGE_URI);
        }

        if (imageUri != null) {
            loadImage();
        } else {
            int photoId = getArguments().getInt(ARG_PHOTO_ID);
            loadPhotoData(photoId);
        }

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(getImageFilePath());
                String[] emails = getResources().getStringArray(R.array.emails);
                int emailPos = Utils.getEmail(getContext());
                Utils.sendFileToEmail(getContext(), file, emails[emailPos]);
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_IMAGE_URI, imageUri);
    }

    private void loadPhotoData(int photoId) {
        Log.d(TAG, "retrofit response " + photoId);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.geni.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeniAPI geniAPI = retrofit.create(GeniAPI.class);

        Call<Photo> call = geniAPI.getPhoto(photoId);
        call.enqueue(this);

    }

    @Override
    public void onResponse(Response<Photo> response, Retrofit retrofit) {

        Photo photo = response.body();
        Sizes sizes = photo.getSizes();

        Log.d(TAG, "sizes " + (sizes != null));
        if (sizes != null) {
            imageUri = sizes.getLarge();
        }
        loadImage();
    }

    @Override
    public void onFailure(Throwable t) {
        showLoadingError();
    }

    private void loadImage() {
        PhotoLoader imagerLoader = new PhotoLoader(imageUri, imageView, getContext(), this);
        imagerLoader.load();
    }

    private void showLoadingError() {
        progress.setVisibility(View.GONE);
        Toast.makeText(getContext(), R.string.loading_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFileLoaded(String filePath) {
        imageFilePath = filePath;
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError() {
        showLoadingError();
    }

    public String getImageFilePath() {
        return imageFilePath;
    }
}