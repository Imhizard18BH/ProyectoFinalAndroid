package co.edu.eam.miprimeraapp.poke.modelo

class PokemonResponse(var count:Int, var next:Int, var previous:Int, var results:ArrayList<PokemonWrapper>) {

    override fun toString(): String {
        return "PokemonResponse(count=$count, next=$next, previous=$previous, results=$results)"
    }
}