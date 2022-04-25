package com.example.homeganizer_client.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homeganizer_client.R
import com.example.homeganizer_client.models.Component


class ListComponentAdapter(private val listElement: Array<Component>?) : RecyclerView.Adapter<ListComponentAdapter.ViewHolder>()
{
    // MISE EN PLACE D'UNE INTERFACE LISTENER AFIN DE RENDRE LA RECYCLERVIEW COMPONENT CLICKABLE
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view){
        var nameView: TextView
        var descriptionView: TextView
        var imageView: ImageView

        init {
            descriptionView = view.findViewById<TextView>(R.id.list_component_descrition)
            nameView = view.findViewById<TextView>(R.id.list_component_name)
            imageView = view.findViewById<ImageView>(R.id.img_component)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_component, parent, false)

        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentEl = listElement?.get(position)

        if (currentEl != null) {
            holder.descriptionView.text = currentEl.description
            holder.nameView.text = currentEl.name

            // DIFFERENCIATION DES ELEMENT ET CONTAINER POUR L'IMAGE LES REPRESENTANT
            if(listElement?.get(position)?.objects != null){
                holder.imageView.setImageResource(R.drawable.`cargo`)
            }
        }
    }

    override fun getItemCount() = listElement!!.size
}
