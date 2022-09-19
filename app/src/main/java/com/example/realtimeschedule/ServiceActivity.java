package com.example.realtimeschedule;


import static com.google.firebase.storage.FirebaseStorage.getInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.realtimeschedule.Model.Services;
import com.example.realtimeschedule.ViewHolder.ServiceViewHolder;
import com.example.realtimeschedule.ViewHolder.UserViewHolder;
//import com.example.realtimeschedule.model.Services;
//import com.example.realtimeschedule.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ServiceActivity extends AppCompatActivity {
    DatabaseReference ServicesRef;
    EditText inputSearch;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        recyclerView = findViewById(R.id.service_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference usersref = FirebaseDatabase.getInstance().getReference().child("Services");

        FirebaseRecyclerOptions<Services> options = new FirebaseRecyclerOptions.Builder<Services>()
                .setQuery(usersref, Services.class)
                .build();

        FirebaseRecyclerAdapter<Services, ServiceViewHolder> adapter = new FirebaseRecyclerAdapter<Services, ServiceViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ServiceViewHolder serviceViewHolder, int i, @NonNull Services services) {

                serviceViewHolder.officerName.setText(services.getSname());
                serviceViewHolder.officerEmail.setText(services.getSemail());
                Picasso.get().load(services.getImage()).into(serviceViewHolder.imageView);

                serviceViewHolder.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(ServiceActivity.this, BookService.class);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_items_layout, parent, false);
                ServiceViewHolder holder = new ServiceViewHolder(view);
                return holder;
            }

            //method to delete a service
            public void onItemLongClick(View view, int position){

                //get current service title to delete data from database
                String currentName= getItem(position).getSname();

                //get current item image url to delete from firebase storage
                String currentImage= getItem(position).getImage();

                //call method
                showDeleteDialog(currentName, currentImage);

            }
            private void showDeleteDialog(String currentName, String currentImage) {
                //alert dialog
                AlertDialog.Builder builder= new AlertDialog.Builder(ServiceActivity.this);
                builder.setTitle("Delete service");
                builder.setMessage("Are you sure to delete this service?");

                //set positive... the YES button
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int choose) {
                        //if YES, delete data

                        Query myQuery= usersref.orderByChild("sname").equalTo(currentName);
                        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                    dataSnapshot.getRef().removeValue();
                                }
                                //show message that posts deleted
                                Toast.makeText(ServiceActivity.this, "Service deleted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(ServiceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                        StorageReference storageReference= getInstance().getReferenceFromUrl(currentImage);
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //successful delete
                                Toast.makeText(ServiceActivity.this, "service deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //unable to delete
                                Toast.makeText(ServiceActivity.this, "try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                //set negative... the NO button
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int choose) {
                        //if NO, dismiss dialog
                        dialogInterface.dismiss();

                    }
                });

                //show dialog
                builder.create().show();
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

}