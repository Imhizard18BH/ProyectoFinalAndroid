package co.edu.eam.miprimeraapp.modelo

import java.util.*
import kotlin.collections.ArrayList

class Compra() {

    var key:String = ""
    var productosID:ArrayList<String> = ArrayList()
    var precio:ArrayList<Float> = ArrayList()
    var cantidad:ArrayList<Int> = ArrayList()
    var fecha:Date = Date()
    var metodoPago:String = ""
    var medioEnvio:String = ""

    constructor(productosID:ArrayList<String>, precio:ArrayList<Float>, cantidad:ArrayList<Int>, fecha:Date):this(){
        this.productosID = productosID
        this.precio = precio
        this.cantidad = cantidad
        this.fecha = fecha
    }

    override fun toString(): String {
        return "Compra(productosID=$productosID, precio=$precio, cantidad=$cantidad, fecha=$fecha, metodoPago='$metodoPago', medioEnvio='$medioEnvio')"
    }


}