package com.example.homeganizer_client.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homeganizer_client.R

class ListCollectionAdapter(private val listElement: Array<String>?) : RecyclerView.Adapter<ListCollectionAdapter.CollectionViewHolder>()
{
    // MISE EN PLACE D'UNE INTERFACE LISTENER AFIN DE RENDRE LA RECYCLERVIEW COLLECTION CLICKABLE
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class CollectionViewHolder(view: View, listener: ListCollectionAdapter.onItemClickListener): RecyclerView.ViewHolder(view){

        var nameCollection: TextView

        init {
            nameCollection = view.findViewById(R.id.list_collection_name)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_collection, parent, false)

        return CollectionViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val currentEl = listElement?.get(position)

        if (currentEl != null) {
            holder.nameCollection.text = currentEl
        }
    }

    override fun getItemCount() = listElement!!.size
}
