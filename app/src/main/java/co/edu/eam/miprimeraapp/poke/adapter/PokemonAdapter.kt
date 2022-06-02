package co.edu.eam.miprimeraapp.poke.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.edu.eam.miprimeraapp.databinding.PokemonItemBinding
import co.edu.eam.miprimeraapp.poke.modelo.Pokemon
import com.bumptech.glide.Glide
import co.edu.eam.miprimeraapp.R

class PokemonAdapter(var lista:Array<Pokemon?>): RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = PokemonItemBinding.inflate( LayoutInflater.from(parent.context), parent, false )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindPokemon( lista[position] )
    }

    override fun getItemCount() = lista.size

    class ViewHolder(private var itemV:PokemonItemBinding): RecyclerView.ViewHolder(itemV.root){

        fun bindPokemon(pokemon: Pokemon?){
            if(pokemon!=null) {

                Glide.with( itemV.root.context )
                    .load(pokemon.sprite)
                    .into(itemV.imgPokemon)

                itemV.numPokemon.text = pokemon.id.toString()
                itemV.nombrePokemon.text = pokemon.name

                var tipos = ""
                var aux = false
                pokemon.types.forEach { t ->

                    tipos+=t.type.name+" "

                    if(!aux) {

                        when( t.type.name ) {
                            "fire" -> itemV.root.setBackgroundColor(ContextCompat.getColor(itemV.root.context, R.color.fire))
                            "grass" -> itemV.root.setBackgroundColor(ContextCompat.getColor(itemV.root.context, R.color.grass))
                            else -> itemV.root.setBackgroundColor(ContextCompat.getColor(itemV.root.context, R.color.white))
                        }

                        aux = true
                    }

                }

                itemV.tipoPokemon.text = tipos

            }
        }

    }

}