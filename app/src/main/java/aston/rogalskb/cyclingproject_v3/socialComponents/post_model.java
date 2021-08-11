package aston.rogalskb.cyclingproject_v3.socialComponents;

public class post_model {
    //
    String uId, uEmail, uDp,uName, postTitle, postDesc,postImage, postTime;

    public post_model() {
    }


    public post_model(String uId, String uEmail, String uDp,String uName, String postTitle, String postDesc, String postImage, String postTime) {
        this.uId = uId;
        this.uEmail = uEmail;
        this.uDp = uDp;
        this.uName = uName;
        this.postTitle = postTitle;
        this.postDesc = postDesc;
        this.postImage = postImage;
        this.postTime = postTime;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
