package ie.setu.retro_letsgo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.retro_letsgo.databinding.CardArcadeBinding
import ie.setu.retro_letsgo.models.ArcadeModel

interface ArcadeListener {
    fun onArcadeClick(arcade: ArcadeModel, position: Int)
}

class ArcadeAdapter constructor(private var arcades: List<ArcadeModel>) : RecyclerView.Adapter<ArcadeAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardArcadeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val arcade = arcades[holder.adapterPosition]
        holder.bind(arcade)
    }

    override fun getItemCount(): Int = arcades.size

    fun updateDataSet(newArcades: List<ArcadeModel>) {
        arcades = newArcades
        notifyDataSetChanged() // Notify the adapter that the dataset has changed
    }

    fun removeItem(position: Int) {
        if (position in 0 until arcades.size) {
            arcades = arcades.toMutableList().apply { removeAt(position) }
            notifyItemRemoved(position)
        }
    }

    class MainHolder(private val binding: CardArcadeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(arcade: ArcadeModel) {
            binding.arcadeTitle.text = arcade.title
            binding.arcadeDescription.text = arcade.description
            Picasso.get().load(arcade.image).resize(200,200).into(binding.imageIcon)
        }
    }
}


