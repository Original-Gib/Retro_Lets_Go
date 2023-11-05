package ie.setu.retro_letsgo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.retro_letsgo.databinding.CardGamesBinding
import ie.setu.retro_letsgo.models.GameModel

interface GameListener {
    fun onGameClick(game: GameModel)
}
class GameAdapter constructor(private var games: List<GameModel>,
                              private val listener: GameListener) :
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

    class MainHolder(private val binding : CardGamesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: GameModel, listener: GameListener) {
            binding.gameTitle.text = game.gameTitle
            binding.gameDescription.text = game.gameDescription
            binding.root.setOnClickListener { listener.onGameClick(game) }
        }
    }
}