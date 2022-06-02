package co.edu.eam.miprimeraapp.data

import co.edu.eam.miprimeraapp.modelo.Producto

object Carrito {

    private var lista:ArrayList<String> = ArrayList()

    fun obtener():ArrayList<String>{
        return lista
    }

    fun agregar(productoKey: String){
        lista.add(productoKey)
    }

    fun limpiar(){
        lista.clear()
    }

}