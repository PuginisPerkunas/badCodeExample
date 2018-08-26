package buttons.games.sounds.mc_ultra_skins;

import android.os.Parcel;
import android.os.Parcelable;

public class SkinModel implements Parcelable{
    int id;
    String title, downloadUrl, photoUrl, category, likes;

    public SkinModel() {
    }

    public SkinModel(int id, String title, String downloadUrl, String photoUrl, String category, String likes) {
        this.id = id;
        this.title = title;
        this.downloadUrl = downloadUrl;
        this.photoUrl = photoUrl;
        this.category = category;
        this.likes = likes;
    }

    protected SkinModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        downloadUrl = in.readString();
        photoUrl = in.readString();
        category = in.readString();
        likes = in.readString();
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(downloadUrl);
        dest.writeString(photoUrl);
        dest.writeString(category);
        dest.writeString(likes);
    }

    public static final Creator<SkinModel> CREATOR
            = new Creator<SkinModel>(){

        @Override
        public SkinModel createFromParcel(Parcel source) {
            return new SkinModel(source);
        }

        @Override
        public SkinModel[] newArray(int i) {
            return new SkinModel[0];
        }
    };
}
