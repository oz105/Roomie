package ShowInfo;

import android.graphics.Bitmap;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import java.util.Map;

public class ShowInfoController {

    ShowInfoView showInfoView;
    ShowInfoModel showInfoModel;
    Bitmap[] photos;
    Map<String,Object> details;
    int numOfPhotos;
    int currentIndex;


    public ShowInfoController(ShowInfoView showInfoView){
        this.showInfoView = showInfoView;
        showInfoModel = new ShowInfoModel(this);
    }


    public void init_info(String userId){
        showInfoModel.get_info(userId);
    }

    public void init_photo(int size,Bitmap bitmap){
        numOfPhotos = size;
        if(size==0){
            showInfoView.image.setVisibility(View.GONE);
            showInfoView.pre.setVisibility(View.GONE);
            showInfoView.next.setVisibility(View.GONE);
            showInfoView.noPhoto.setVisibility(View.VISIBLE);
        }
        else {
            showInfoView.pre.setOnClickListener(showInfoView);
            showInfoView.next.setOnClickListener(showInfoView);
            currentIndex = 0;
            this.photos = new Bitmap[size];
            this.photos[0] = bitmap;
            showInfoView.wait.setVisibility(View.GONE);
            showInfoView.image.setImageBitmap(bitmap);

        }
    }

    public void show_details(Map<String,Object> details) {
        showInfoView.address.setText(details.get("address").toString());
        showInfoView.price.setText("$"+details.get("price").toString()+"/month");
        showInfoView.details.setText(details.get("details").toString());
        showInfoView.rooms.setText(details.get("numOfRoom").toString()+" bedrooms");
        showInfoView.details.setMovementMethod(new ScrollingMovementMethod());
    }
    //0 pre 1 -next
    public void change_photo(int flag){
        showInfoView.progressBar.setVisibility(View.VISIBLE);
        if(flag==1){
            if(currentIndex==numOfPhotos-1){
                currentIndex = 0;
            }
            else{
                currentIndex++;
            }
        }
        else {
            if(currentIndex==0){
                currentIndex = numOfPhotos-1;
            }
            else{
                currentIndex--;
            }
        }

        if(photos[currentIndex]!=null){
            showInfoView.progressBar.setVisibility(View.GONE);
            showInfoView.image.setImageBitmap(photos[currentIndex]);
        }
        else{
            showInfoModel.get_photos(currentIndex);
        }
    }

    public void show_photo(Bitmap photo){
        photos[currentIndex] = photo;
        showInfoView.progressBar.setVisibility(View.GONE);
        showInfoView.image.setImageBitmap(photo);
    }



}
