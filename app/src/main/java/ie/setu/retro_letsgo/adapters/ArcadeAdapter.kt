package ie.setu.retro_letsgo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.retro_letsgo.databinding.CardArcadeBinding
import ie.setu.retro_letsgo.models.ArcadeModel

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

    class MainHolder(private val binding: CardArcadeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(arcade: ArcadeModel) {
            binding.arcadeTitle.text = arcade.title
            binding.arcadeDescription.text = arcade.description
        }
    }
}
