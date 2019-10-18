package com.example.sdlproject;

import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FarmerDisplayAdapter extends RecyclerView.Adapter<FarmerDisplayAdapter.FarmerDisplayViewHolder> {

    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private ArrayList<FarmerItem2> mExampleList;
    private OnItemClickListener mListener;
    public double item_latitude, item_longitude, end_lat, end_lon;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class FarmerDisplayViewHolder extends RecyclerView.ViewHolder {
        public TextView farmerName, priceDisplay, quantityDisplay, distanceDisplay;
        public CardView cardView;

        public FarmerDisplayViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            farmerName = itemView.findViewById(R.id.farmerName);
            priceDisplay = itemView.findViewById(R.id.priceDisplay);
            quantityDisplay = itemView.findViewById(R.id.quantityDisplay);
            distanceDisplay = itemView.findViewById(R.id.distanceDisplay);
            cardView = itemView.findViewById(R.id.cardview_farmer);
//
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            DocumentReference noteref = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
//            noteref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    double latitude = documentSnapshot.getDouble("latitude");
//                    double longitude = documentSnapshot.getDouble("longitude");
//                    LatLon ll = new LatLon(latitude, longitude);
//                    latlon.add(ll);
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public FarmerDisplayAdapter(ArrayList<FarmerItem2> exampleList) {
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public FarmerDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_farmer, parent, false);
        FarmerDisplayViewHolder evh = new FarmerDisplayViewHolder(v, mListener);
        return evh;
    }


    @Override
    public void onBindViewHolder(@NonNull final FarmerDisplayViewHolder holder, int position) {
//        while (latlon.isEmpty())
//            continue;
        final FarmerItem2 currentItem = mExampleList.get(position);
        holder.farmerName.setText(currentItem.getUser_name());
        holder.priceDisplay.setText(currentItem.getPrice());
        holder.quantityDisplay.setText(currentItem.getQty());
        item_latitude = currentItem.getLatitude();
        item_longitude = currentItem.getLongitude();
        end_lat = currentItem.getLatitude_end();
        end_lon = currentItem.getLongitude_end();


        double dist = calculateDistance();
        df2.setRoundingMode(RoundingMode.UP);
        dist = Double.valueOf(df2.format(dist));
        Log.i("CheckDist", Double.toString(dist));
        holder.distanceDisplay.setText(Double.toString(dist));
        currentItem.setDist(dist);
        //holder.mTextView2.setText(Double.toString(dist));
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public double calculateDistance() {

//        LatLon currentItem = latlon.get(0);
        float results[] = new float[10];
        Log.i("Check", "Checking" + item_latitude + " " + item_longitude + " " + end_lat + " " + end_lon);
        Location.distanceBetween(item_latitude, item_longitude, end_lat, end_lon, results);
        return (results[0] / 1000);

    }

//    public class LatLon {
//        private double latitude, longitude;
//
//        public LatLon(double latitude, double longitude) {
//            this.latitude = latitude;
//            this.longitude = longitude;
//        }
//
//        public double getLatitude() {
//            return latitude;
//        }
//
//        public void setLatitude(double latitude) {
//            this.latitude = latitude;
//        }
//
//        public double getLongitude() {
//            return longitude;
//        }
//
//        public void setLongitude(double longitude) {
//            this.longitude = longitude;
//        }
//    }

}