package co.edu.eam.miprimeraapp.poke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.eam.miprimeraapp.adapter.GenericoAdapter
import co.edu.eam.miprimeraapp.databinding.ActivityPokemonBinding
import co.edu.eam.miprimeraapp.databinding.PokemonItemBinding
import co.edu.eam.miprimeraapp.R
import co.edu.eam.miprimeraapp.poke.modelo.Pokemon
import co.edu.eam.miprimeraapp.poke.servicio.PokemonService
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonActivity : AppCompatActivity() {

    lateinit var binding: ActivityPokemonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lista = ArrayList<Pokemon?>()
        for (i in 0 .. 150){
            lista.add(null)
        }

        val adapter = object : GenericoAdapter<Pokemon, PokemonItemBinding>(lista){

            override fun onBindData(pokemon: Pokemon?, pos: Int, itemV: PokemonItemBinding) {

                if(pokemon!=null) {

                    Glide.with( itemV.root.context )
                        .load(pokemon.sprite)
                        .into(itemV.imgPokemon)

                    itemV.numPokemon.text = pokemon.id.toString()
                    itemV.nombrePokemon.text = pokemon.name

                    var tipos = ""
                    var aux = false

                    tipos = pokemon.types.map { p -> p.type.name }.reduce{ acc, string -> "$acc $string" }

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

            override fun onItemClick(dato: Pokemon?, pos: Int) {
                Log.e("POKE_CLICK", dato.toString())
            }

            override fun inflateView(inflater: LayoutInflater, parent: ViewGroup): PokemonItemBinding {
                return PokemonItemBinding.inflate( inflater, parent, false )
            }


        }
        binding.listaPokemon.adapter = adapter
        binding.listaPokemon.layoutManager = LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false)

        val retrofit = Retrofit.Builder()
            .baseUrl( co.edu.eam.miprimeraapp.poke.servicio.BASE_URL )
            .addConverterFactory( GsonConverterFactory.create() )
            .build()

        val pokemonService = retrofit.create( PokemonService::class.java )

        for( i in 1 .. 151) {

            pokemonService.getPokemonByNumber(i).enqueue(object : Callback<Pokemon> {
                override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                    val pokemon = response.body()
                    pokemon!!.sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$i.png"
                    lista[i-1] = pokemon
                    adapter.notifyItemChanged(i-1)
                }

                override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

    }
}