package co.edu.eam.miprimeraapp.modelo

class Usuario() {

    var cedula:String = ""
    var nombre:String = ""
    var correo:String = ""
    var password:String = ""

    constructor(cedula:String, nombre:String, correo:String, password:String):this(){
        this.cedula = cedula
        this.nombre = nombre
        this.correo = correo
        this.password = password
    }

    override fun toString(): String {
        return "Usuario(cedula='$cedula', nombre='$nombre', correo='$correo', password='$password')"
    }
}