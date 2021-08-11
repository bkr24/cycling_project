package aston.rogalskb.cyclingproject_v3;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import aston.rogalskb.cyclingproject_v3.socialComponents.*;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyHolder> {

    Context context;
    ArrayList<userModel> usersList;

    public UsersAdapter(Context context, ArrayList<userModel> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_users, parent,false);

        return new MyHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String otherUID = usersList.get(position).getUid();
        String uName = usersList.get(position).getName();
        String uEmail = usersList.get(position).getEmail();
        String uImage = usersList.get(position).getImage();

        holder.userName.setText(uName);
        holder.userEmail.setText(uEmail);

        try {
            Picasso.get().load(uImage).placeholder(R.drawable.ic_profile_standard).into(holder.userImageCover);

        } catch (Exception e){
            Log.i("bartek", "Image failed loading");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
               //Toast.makeText(context,""+,Toast.LENGTH_LONG).show();
                Intent i = new Intent(context,chatActivity.class);
                Log.i("otherUID", "The value of oUID is " + otherUID);
                i.putExtra("oUID",otherUID);
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{


        public ImageView userImageCover;
        TextView userName, userEmail;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userImageCover = itemView.findViewById(R.id.usersImage);

        }
    }

}
