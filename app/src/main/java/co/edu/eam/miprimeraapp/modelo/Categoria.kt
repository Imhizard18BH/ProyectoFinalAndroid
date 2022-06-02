package co.edu.eam.miprimeraapp.modelo

import android.content.ContentValues
import co.edu.eam.miprimeraapp.db.CategoriaContrato
import co.edu.eam.miprimeraapp.db.ProductoContrato

class Categoria() {

    var codigo:Int = 0
    var key:String = ""
    var nombre:String = ""
    var icono: String = ""
    var color:String = ""

    constructor(codigo:Int, nombre:String, icono: String, color:String):this(){
        this.codigo = codigo
        this.nombre = nombre
        this.icono = icono
        this.color = color
    }

    override fun toString(): String {
        return "Categoria(nombre='$nombre', icono='$icono', color='$color')"
    }

    fun toContentValues(): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(CategoriaContrato.CODIGO, codigo)
        contentValues.put(CategoriaContrato.NOMBRE, nombre)
        contentValues.put(CategoriaContrato.ICONO, icono)

        return contentValues
    }
}