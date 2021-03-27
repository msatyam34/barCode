package com.example.barcode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<String> list;

    public Adapter(List<String>list){
        this.list=list;
    }


    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        String index = Integer.toString(position+1);
        String data = list.get(position);
        holder.setData(index,data);






    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private TextView textView1;
        private TextView textView2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1=itemView.findViewById(R.id.textview1);
            textView2=itemView.findViewById(R.id.textview2);
            //here use item_design xml ids
            //give different name not like constructor

        }

        public void setData( String index, String data) {

            textView1.setText(index);
            textView2.setText(data);


        }

    }



}
