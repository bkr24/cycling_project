package aston.rogalskb.cyclingproject_v3.socialComponents;

public class userModel {

    //Same data used as in the database in firestore
    String name,email,image,cover,uid;


    public userModel(){

    }

    public userModel(String name, String email, String image, String cover, String uid) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.cover = cover;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
