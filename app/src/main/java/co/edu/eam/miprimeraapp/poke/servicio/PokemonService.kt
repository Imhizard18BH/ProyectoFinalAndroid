package co.edu.eam.miprimeraapp.poke.servicio

import co.edu.eam.miprimeraapp.poke.modelo.Pokemon
import co.edu.eam.miprimeraapp.poke.modelo.PokemonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://pokeapi.co/api/v2/"

interface PokemonService {

    @GET("pokemon")
    fun getPokemon(@Query("limit") limit:Int, @Query("offset") offset:Int): Call<PokemonResponse>

    @GET("pokemon/{number}")
    fun getPokemonByNumber(@Path("number") number:Int): Call<Pokemon>


}