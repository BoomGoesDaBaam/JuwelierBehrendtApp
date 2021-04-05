package com.example.juwelierbehrendt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 *  Diese Klasse weißt den Listenelementen ihre Werte zu.
 */
public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder>
{
        private ArrayList<Product> products;
        ItemClicked activity;

        /**
         * Dieses Interface wird vom Activity mit Liste implementiert
         */
        public interface ItemClicked
        {
                void onItemClicked(int index);
        }

        /**
         * Der Konstruktor initialisiert die Vokabelliste und setzt die Reference
         * zum Interface ItemClickt auf die, des Activities, wo die Liste implementiert werden soll.
         *
         * @param context       Activity in dem Liste dargestellt werden soll
         * @param list          Eine Arrayliste des Inhalts
         */
        public EntryAdapter (Context context, ArrayList<Product> list)
        {
                products = list;
                activity = (ItemClicked) context;
        }

        /**
         * Diese Klasse stellt ein Elemtent der Liste dar
         */
        public class ViewHolder extends RecyclerView.ViewHolder
        {
                TextView brand,nameOfClock,discount;
                public ViewHolder(@NonNull final View itemView) {
                        super(itemView);
                        discount = itemView.findViewById(R.id.tV_discount);
                        nameOfClock = itemView.findViewById(R.id.tV_nameUhr);
                        brand = itemView.findViewById(R.id.tV_brand);

                        itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        activity.onItemClicked(products.indexOf(itemView.getTag()));
                                }
                        });
                }
        }

        /**
         * Initialisiert ViewHolder.
         *
         * @param viewGroup
         * @param viewType
         * @return
         */
        @NonNull
        @Override
        public EntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items,viewGroup,false);
                return new ViewHolder(v);
        }

        /**
         * Laed die Viewholder mit den Informationen aus der Liste
         *
         * @param viewHolder    Das momentane Element der Liste.
         * @param index         Der Index des momentanen Elements.
         */
        @Override
        public void onBindViewHolder(@NonNull EntryAdapter.ViewHolder viewHolder, int index) {

                viewHolder.itemView.setTag(products.get(index));
                viewHolder.brand.setText(products.get(index).getBrand());
                viewHolder.nameOfClock.setText(products.get(index).getName());
                viewHolder.discount.setText(String.valueOf(products.get(index).getDiscount()));
        }

        /**
         * @return Gibt die laenge der Liste im Recyclerview zurueck.
         */
        @Override
        public int getItemCount() {
                return products.size();
        }
}
