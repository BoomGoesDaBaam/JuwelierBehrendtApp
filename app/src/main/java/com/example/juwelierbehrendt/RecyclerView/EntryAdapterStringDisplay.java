package com.example.juwelierbehrendt.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.juwelierbehrendt.R;

import java.util.ArrayList;
import java.util.Calendar;

import lombok.Getter;

/**
 *  Diese Klasse weißt den Listenelementen ihre Werte zu.
 */
public class EntryAdapterStringDisplay extends RecyclerView.Adapter<EntryAdapterStringDisplay.ViewHolderString>
{
        private ArrayList<String> entries;
        private ItemClicked activity;
        @Getter
        private Long lastPress=2L;
        /**
         * Dieses Interface wird von der Klasse "VocabularyOverview" implementiert und diese
         * wird dadurch immer aufgerufen, wenn ein OnClickEvent von Recyclerview stattfindet.
         */
        public interface ItemClicked
        {
                void onItemClicked(int index);
        }

        /**
         * Der Konstruktor initialisiert die Vokabelliste und setzt die Reference
         * zum Interface ItemClickt auf die, des VocabularyOverviews.
         *
         * @param context       In diesem Fall eine Instanz der Klasse "VocabularyOverview"
         * @param list          Eine Arrayliste der Vokabeln
         */
        public EntryAdapterStringDisplay (Context context, ArrayList<String> list)
        {
                entries = list;
                activity = (ItemClicked) context;
        }

        /**
         * Diese Klasse stellt ein Elemtent der Liste dar
         */
        public class ViewHolderString extends RecyclerView.ViewHolder
        {
                TextView tv_string;
                public ViewHolderString(@NonNull final View itemView) {
                        super(itemView);
                        tv_string = itemView.findViewById(R.id.tv_ListitemStringg);

                        itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        lastPress = Calendar.getInstance().getTimeInMillis();
                                        activity.onItemClicked(entries.indexOf(itemView.getTag()));
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
        public EntryAdapterStringDisplay.ViewHolderString onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_itemsstring,viewGroup,false);
                return new ViewHolderString(v);
        }

        /**
         * Laed die Viewholder mit den Informationen aus der Liste
         *
         * @param viewHolder    Das momentane Element der Liste.
         * @param index         Der Index des momentanen Elements.
         */
        @Override
        public void onBindViewHolder(@NonNull EntryAdapterStringDisplay.ViewHolderString viewHolder, int index) {

                viewHolder.itemView.setTag(entries.get(index)); //needed!!!
                viewHolder.tv_string.setText(entries.get(index));
        }

        /**
         * @return Gibt die laenge der Liste im Recyclerview zurueck.
         */
        @Override
        public int getItemCount() {
                return entries.size();
        }
}
