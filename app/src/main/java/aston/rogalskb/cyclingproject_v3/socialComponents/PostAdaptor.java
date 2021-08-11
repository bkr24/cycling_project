package aston.rogalskb.cyclingproject_v3.socialComponents;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import java.text.DateFormat;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import aston.rogalskb.cyclingproject_v3.R;

public class PostAdaptor extends RecyclerView.Adapter<PostAdaptor.MyHolder> {

    //Get the views of row_posts file
    Context context;
    ArrayList<post_model> postList;

    public PostAdaptor(Context context, ArrayList<post_model> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_posts, parent, false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String uId = postList.get(position).getuId();
        String uEmail = postList.get(position).getuEmail();
        String uDp = postList.get(position).getuDp();
        String uName = postList.get(position).getuName();
        String postTitle = postList.get(position).getPostTitle();
        String postDesc = postList.get(position).getPostDesc();
        String postTime = postList.get(position).getPostTime();
        String postImage = postList.get(position).getPostImage();

        //change post time to actual date :)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(postTime));
        String postDate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        holder.postEmailView.setText(uEmail);
        holder.postUserNameView.setText(uName);
        holder.postPostTitleView.setText(postTitle);
        holder.postDescriptionView.setText(postDesc);
        holder.postPostTimeView.setText(postDate);

        try{
            Picasso.get().load(uDp).placeholder(R.drawable.ic_add_image).into(holder.postProfilePicture);

        } catch (Exception e){

        }


        try{
            if(postImage.equals("noImage")){
                holder.postImageView.setVisibility(View.GONE);
            } else {
                Picasso.get().load(postImage).placeholder(R.drawable.ic_add_image).into(holder.postImageView);

            }

        }  catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{


        public ImageView postProfilePicture;
        ImageView postImageView;
        TextView postUserNameView,postPostTimeView,postPostTitleView,postDescriptionView,postEmailView;

        public MyHolder(@NonNull View itemView){
            super(itemView);

            postImageView = itemView.findViewById(R.id.postImageView);
            postProfilePicture = itemView.findViewById(R.id.postProfilePicture);

            postUserNameView = itemView.findViewById(R.id.postUserNameView);
            postPostTimeView = itemView.findViewById(R.id.postPostTimeView);
            postPostTitleView = itemView.findViewById(R.id.postPostTitleView);
            postDescriptionView = itemView.findViewById(R.id.postDescriptionView);
            postEmailView = itemView.findViewById(R.id.postEmailView);

        }



    }




}
