package ie.setu.retro_letsgo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.retro_letsgo.databinding.CardGamesBinding
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.models.GameModel

interface GameListener {
    fun onGameClick(game: GameModel, position: Int)
}
class GameAdapter constructor(private var games: List<GameModel>) :
    RecyclerView.Adapter<GameAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGamesBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val game = games[holder.adapterPosition]
        holder.bind(game)
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

        fun bind(game: GameModel) {
            binding.gameTitle.text = game.gameTitle
            binding.gameDescription.text = game.gameDescription
        }
    }
}