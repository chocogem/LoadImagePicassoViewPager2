package com.demo.imagetest.viewmodel;



import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.demo.imagetest.model.ImageList;
import com.demo.imagetest.model.ImageData;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ImageViewModel extends ViewModel {

    public MutableLiveData<List<ImageList>> imageListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ImageData> imageResponseMutableLiveData = new MutableLiveData<>();
    private Context context;

    public ImageViewModel(Context context){
        this.context = context;
        fetchImageData();
    }

    public void fetchImageData() {

        Observable.fromCallable(new Callable<ImageData>() {

            @Override
            public ImageData call() throws Exception {
                String jsonStr = "{'title':'Civil War','image':['http://movie.phinf.naver.net/20151127_272/1448585271749MCMVs_JPEG/movie_image.jpg?type=m665_443_2','http://movie.phinf.naver.net/20151127_84/1448585272016tiBsF_JPEG/movie_image.jpg?type=m665_443_2','http://movie.phinf.naver.net/20151125_36/1448434523214fPmj0_JPEG/movie_image.jpg?type=m665_443_2']}";
                Gson gson = new Gson();
                ImageData imageData = gson.fromJson(jsonStr, ImageData.class);
                return imageData;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ImageData imageData) {
                        imageResponseMutableLiveData.setValue(imageData);

                       // mBuilder.load("url").into(target);
                        saveAndGetImageURI(imageData.getImage().get(0),imageData.getTitle());


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<ImageData> getAllImageData() {
        return imageResponseMutableLiveData;
    }
    public MutableLiveData<List<ImageList>> getImageList() {
        return imageListMutableLiveData;
    }
    public void saveAndGetImageURI( final String ImgUrl,final String title){
        final ProgressDialog progress = new ProgressDialog(context);

        class SaveThisImage extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               /* progress.setTitle("Processing");
                progress.setMessage("Please Wait...");
                progress.setCancelable(false);
                progress.show();*/
            }
            @Override
            protected Void doInBackground(Void... arg0) {
                try{

                    File sdCard = Environment.getExternalStorageDirectory();
                    String urlRemoveDoubleSlash = ImgUrl.replace("//","");
                    int firstSlash = urlRemoveDoubleSlash.indexOf("/");
                    int secondSlash = urlRemoveDoubleSlash.indexOf("/", firstSlash + 1);
                    String fileName = urlRemoveDoubleSlash.substring(secondSlash+1,urlRemoveDoubleSlash.lastIndexOf("/"))+".jpg";
                    String path = sdCard.getAbsolutePath() + "/savedImage";
                    String URI = path+"/"+fileName;
                    File dir = new File(path);
                    dir.mkdirs();
                    final File myImageFile = new File(dir, fileName); // Create image file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(myImageFile);
                        Picasso mPicasso = Picasso.with(context);
                        Bitmap bitmap = mPicasso.load(ImgUrl).get();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(myImageFile));
                        context.sendBroadcast(intent);
                        ImageList image = new ImageList();
                        image.setTitle(title);
                        image.setImageUri(URI);
                        if(imageListMutableLiveData.getValue()==null){
                            List<ImageList> images = new ArrayList<>();
                            images.add(image);
                            imageListMutableLiveData.postValue(images);
                        }else {
                            imageListMutableLiveData.getValue().add(image);
                            imageListMutableLiveData.postValue(imageListMutableLiveData.getValue());
                        }
                        //
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
               /* if(progress.isShowing()){
                    progress.dismiss();
                }
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();*/
            }
        }
        SaveThisImage shareimg = new SaveThisImage();
        shareimg.execute();
    }
}
