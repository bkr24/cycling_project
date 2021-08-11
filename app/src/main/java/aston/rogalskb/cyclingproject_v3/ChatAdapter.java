package aston.rogalskb.cyclingproject_v3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import aston.rogalskb.cyclingproject_v3.socialComponents.*;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyHolder> {

    private static final int MESSAGE_TYPE_LEFT = 0;
    private static final int MESSAGE_TYPE_RIGHT = 1;
    Context context;
    List<chatModel> chatList;
    String imageURL;

    FirebaseUser firebaseUser;

    public ChatAdapter(Context context, List<chatModel> arrayList, String imageURL) {
        this.context = context;
        this.chatList = arrayList;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MESSAGE_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String msg = chatList.get(position).getMessage();
        String timeSt = chatList.get(position).getTimeStamp();


        /*
        Calendar c = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatedDate = sdf.format(c.getTime());
        String dateTime = formatedDate;

         */

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        try {
            cal.setTimeInMillis(Long.parseLong(timeSt));

        } catch(Exception ex) {

            ex.printStackTrace();
        }

        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

        /*
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timeSt));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

         */

        //Set data to views
        holder.messageTV.setText(msg);
        holder.timeStampTV.setText(dateTime);
        try {
            Picasso.get().load(imageURL).into(holder.profilePictureIV);

        } catch (Exception E){
            Picasso.get().load(R.drawable.ic_profile_standard).into(holder.profilePictureIV);

        }

        //On click show delete message dialog
        holder.chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete message");
                builder.setMessage("Do you want to delete the selected message?");
                builder.setPositiveButton("Yes, delete it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMsg(which);
                    }
                });
                //Don't delete message
                builder.setNegativeButton("No, don't delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });


        //Set seen status of message
        if(position == chatList.size()-1){
            if(chatList.get(position).isWasSeen()){
                holder.wasSeenTV.setText("Seen");
            } else {
                holder.wasSeenTV.setText("Sent");
            }
        }
        else {
            holder.wasSeenTV.setVisibility(View.GONE);
        }


    }

    private void deleteMsg(int pos) {


        final String myUID = FirebaseAuth.getInstance().getUid();

        String msgTimeStamp = chatList.get(pos).getTimeStamp();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = databaseReference.orderByChild("timeStamp").equalTo(msgTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //If the sender is trying to delete his own message
                    if(myUID.equals(ds.child("sender").getValue())){
                    //if(ds.child("sender").getValue().equals(myUID)){
                        ds.getRef().removeValue();

                        /*
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("message","Message deleted");
                        ds.getRef().updateChildren(hashMap);

                         */


                        Toast.makeText(context,"Message deleted successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context,"You can only delete your own mistakes...", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MESSAGE_TYPE_RIGHT;
        } else {
            return MESSAGE_TYPE_LEFT;
        }
        //return super.getItemViewType(position);
    }

    class MyHolder extends RecyclerView.ViewHolder{

        //View variables
        ImageView profilePictureIV;
        TextView wasSeenTV, timeStampTV, messageTV;
        LinearLayout chatLayout;




        public MyHolder(@NonNull View view){
            super(view);

            //Initialize views
            profilePictureIV = view.findViewById(R.id.chatImageIV);
            wasSeenTV = view.findViewById(R.id.chatWasSeenTV);
            timeStampTV = view.findViewById(R.id.chatTimeTV);
            messageTV = view.findViewById(R.id.chatMessageTV);
            chatLayout = view.findViewById(R.id.chatLayout);





        }
    }


}
