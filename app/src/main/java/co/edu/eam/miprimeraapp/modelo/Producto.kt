package co.edu.eam.miprimeraapp.modelo

import android.content.ContentValues
import co.edu.eam.miprimeraapp.db.ProductoContrato
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class Producto (){

    var codigo:Int = 0
    var key:String = ""
    var precio:Float = 0f
    var nombre:String = ""
    var descripcion:String = ""
    var unidades:Int = 0
    var descuento:Float = 0f
    var vendedor:String = ""
    var imagenes:ArrayList<String> = ArrayList()
    var categorias:List<Int> = ArrayList()
    var fechaCreacion:Date = Date()

    constructor(precio:Float, nombre:String, descripcion:String, unidades:Int, descuento:Float):this(){
        this.precio = precio
        this.nombre = nombre
        this.descripcion = descripcion
        this.unidades = unidades
        this.descuento = descuento
    }

    fun calcularNuevoPrecio():Float{
        return precio - precio*descuento
    }

    fun toContentValues():ContentValues{
        val contentValues = ContentValues()
        contentValues.put(ProductoContrato.NOMBRE, nombre)
        contentValues.put(ProductoContrato.PRECIO, precio)
        contentValues.put(ProductoContrato.DESCUENTO, descuento)
        contentValues.put(ProductoContrato.DESCRIPCION, descripcion)
        contentValues.put(ProductoContrato.UNIDADES, unidades)
        contentValues.put(ProductoContrato.CODIGO_VENDEDOR, vendedor)

        return contentValues
    }

    override fun toString(): String {
        return "Producto(precio=$precio, nombre='$nombre', descripcion='$descripcion', unidades=$unidades, descuento=$descuento, codigo=$codigo, vendedor='$vendedor', imagenes=$imagenes, categorias=$categorias, key: $key)"
    }
}