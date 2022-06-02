package co.edu.eam.miprimeraapp.modelo

import java.util.*

class Comentario() {

    var key:String = ""
    var mensaje:String = ""
    var fecha:Date = Date()
    var usuarioID: String = ""
    var calificacion:Int = 0

    constructor(mensaje:String, fecha:Date, usuarioID: String, calificacion:Int):this(){
        this.mensaje = mensaje
        this.fecha = fecha
        this.usuarioID = usuarioID
        this.calificacion = calificacion
    }

    override fun toString(): String {
        return "Comentario(mensaje='$mensaje', fecha=$fecha, usuario=$usuarioID)"
    }
}