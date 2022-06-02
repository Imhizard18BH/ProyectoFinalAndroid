package co.edu.eam.miprimeraapp.poke.modelo

class Pokemon(var id:Int, var name:String, var height:Int, var weight:Int, var types:ArrayList<TypeWrapper>) {

    var sprite:String = ""

    override fun toString(): String {
        return "Pokemon(id=$id, name='$name', height=$height, weight=$weight, types=$types, sprite='$sprite')"
    }

}