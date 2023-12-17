package ie.setu.retro_letsgo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.databinding.CardGamesBinding
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.models.GameModel

interface GameListener {
    fun onGameClick(game: GameModel)
}
class GameAdapter constructor(private var games: List<GameModel>, private val listener: GameListener) :
    RecyclerView.Adapter<GameAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGamesBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val game = games[holder.adapterPosition]
        holder.bind(game, listener)
    }

    override fun getItemCount(): Int = games.size

    fun updateDataSet(newGames: List<GameModel>) {
        games = newGames
        notifyDataSetChanged() // Notify the adapter that the dataset has changed
    }

    fun removeItem(position: Int) {
        if (position in 0 until games.size) {
            games = games.toMutableList().apply { removeAt(position) }
            notifyItemRemoved(position)
        }
    }

    class MainHolder(private val binding : CardGamesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: GameModel, listener: GameListener) {
//            binding.gameTitle.text = game.gameTitle
//            binding.gameDescription.text = game.gameDescription
            binding.game = game
            binding.root.setOnClickListener { listener.onGameClick(game) }
            binding.executePendingBindings()
        }
    }
}