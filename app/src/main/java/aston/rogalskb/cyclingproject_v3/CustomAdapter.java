package aston.rogalskb.cyclingproject_v3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import aston.rogalskb.cyclingproject_v3.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> routeName, routeId, dbOriginName, dbDestinationName;
    ArrayList<Double> dbOriginLat,dbOriginLon,dbDestinationLat,dbDestinationLon;
    Activity activity;

    CustomAdapter(Activity activity,Context context,ArrayList<String> routeId, ArrayList<String> routeName,ArrayList<String> dbOriginName,ArrayList<String> dbDestinationName,ArrayList<Double> dbOriginLat,
                  ArrayList<Double> dbOriginLon,ArrayList<Double> dbDestinationLat,ArrayList<Double> dbDestinationLon ){
        this.context = context;
        this.activity = activity;
        this.routeId = routeId;
        this.routeName = routeName;
        this.dbOriginName = dbOriginName;
        this.dbDestinationName = dbDestinationName;
        this.dbOriginLat = dbOriginLat;
        this.dbOriginLon = dbOriginLon;
        this.dbDestinationLat = dbDestinationLat;
        this.dbDestinationLon = dbDestinationLon;

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new MyViewHolder(view);

    }





    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.route_id.setText(String.valueOf(routeId.get(position)));
        holder.route_name.setText(String.valueOf(routeName.get(position)));
        holder.origin_name.setText(String.valueOf(dbOriginName.get(position)));
        holder.destination_name.setText(String.valueOf(dbDestinationName.get(position)));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,updateRoute.class);
                //Put the data as extras to the intent
                i.putExtra("id",String.valueOf(routeId.get(position)));
                i.putExtra("routeName",String.valueOf(routeName.get(position)));
                i.putExtra("originName",String.valueOf(dbOriginName.get(position)));
                i.putExtra("destinationName",String.valueOf(dbDestinationName.get(position)));
                i.putExtra("originLat",Double.valueOf(dbOriginLat.get(position)));
                i.putExtra("originLon",Double.valueOf(dbOriginLon.get(position)));
                i.putExtra("destinationLat",Double.valueOf(dbDestinationLat.get(position)));
                i.putExtra("destinationLon",Double.valueOf(dbDestinationLon.get(position)));


                //context.startActivity(i);
                activity.startActivityForResult(i,11);

            }
        });


    }

    @Override
    public int getItemCount() {
        return routeId.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView route_id,route_name,origin_name,destination_name;
        LinearLayout layout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            route_id = itemView.findViewById(R.id.recyclerRouteID);
            route_name = itemView.findViewById(R.id.recyclerRouteName);
            origin_name = itemView.findViewById(R.id.recyclerOrigin);
            destination_name = itemView.findViewById(R.id.recyclerDestination);
            layout = itemView.findViewById(R.id.recyclerLayout);


        }
    }






}
