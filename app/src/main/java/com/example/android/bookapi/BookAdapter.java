package com.example.android.bookapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufya on 02-08-2017.
 */

public class BookAdapter extends
        RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private Uri bookurl;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView imageView;
        public TextView titleView;
        public TextView nameTextView;
        public TextView priceView;
        public Button purchaseBtn;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.imgview);
            titleView=(TextView)itemView.findViewById(R.id.book_title) ;
            nameTextView = (TextView) itemView.findViewById(R.id.author_name);
            priceView=(TextView)itemView.findViewById(R.id.price_tag);
            purchaseBtn = (Button) itemView.findViewById(R.id.buynow_lnk);
        }
    }

    // Store a member variable for the books
    private ArrayList<Book_Data> mBooks;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public BookAdapter(Context context, ArrayList<Book_Data> books) {
        mBooks = books;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bookView = inflater.inflate(R.layout.bookdata_view, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(bookView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Book_Data book=mBooks.get(position);
        ImageView imgv=viewHolder.imageView;
        Picasso.with(getContext())
                .load(book.getImgid())
                .resize(50, 50)
                .centerCrop()
                .into(imgv);
        //imgv.setImageURI(book.getImgid());
        TextView tlev=viewHolder.titleView;
        tlev.setText(book.getTitle());
        TextView autv=viewHolder.nameTextView;
        autv.setText(book.getAut_nme());
        TextView pricev=viewHolder.priceView;
        pricev.setText(book.getMprice());
        Button bv=viewHolder.purchaseBtn;
        bv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookurl=Uri.EMPTY;
                if(book.getPurchase_Link()=="NA"){
                    Toast.makeText(getContext(),"Url Not Available",Toast.LENGTH_SHORT).show();
                 }
                 else{
                    bookurl = Uri.parse(book.getPurchase_Link());
                // Create a new intent to view the earthquake URI
                Intent buyIntent = new Intent(Intent.ACTION_VIEW,bookurl);
                getContext().startActivity(buyIntent);}
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }


}
