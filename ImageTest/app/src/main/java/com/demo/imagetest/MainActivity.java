package com.demo.imagetest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.demo.imagetest.model.ImageList;
import com.demo.imagetest.model.ImageData;
import com.demo.imagetest.viewmodel.ImageViewModel;
import com.demo.imagetest.viewmodel.ViewModelFactory;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    ImageViewModel imageViewModel;
    ViewPager2 myviewpager;
    ImageViewAdapter adapter;
    Button nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewModelFactory viewModelFactory = new ViewModelFactory(this);
        imageViewModel = new ViewModelFactory(getApplication()).create(ImageViewModel.class);
        myviewpager=(ViewPager2)findViewById(R.id.viewpager);

        getShowImage();

        nextButton=(Button)findViewById(R.id.nextbtn);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageData imageData = imageViewModel.imageResponseMutableLiveData.getValue();
                int nextPosition = myviewpager.getCurrentItem()+1;
                if (nextPosition<imageData.image.size()){
                    imageViewModel.saveAndGetImageURI(imageData.image.get(nextPosition),imageData.getTitle());
                 }
            }


        });

    }

    private void getShowImage() {
        imageViewModel.getImageList().observe(MainActivity.this, new Observer<List<ImageList>>() {
            @Override
            public void onChanged(List<ImageList> imageList) {
                if(adapter==null) {
                    adapter = new ImageViewAdapter(imageList, getApplicationContext());
                    myviewpager.setAdapter(adapter);
                    nextButton.setVisibility(View.VISIBLE);
                }else {
                    adapter.notifyDataSetChanged();
                }
                int nextPosition = myviewpager.getCurrentItem()+1;
                if (nextPosition<imageList.size()){
                    myviewpager.setCurrentItem(nextPosition);
                }

            }
        });
    }

}
