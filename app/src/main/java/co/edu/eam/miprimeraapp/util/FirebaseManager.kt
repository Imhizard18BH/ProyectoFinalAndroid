package co.edu.eam.miprimeraapp.util

import android.util.Log
import co.edu.eam.miprimeraapp.modelo.Categoria
import co.edu.eam.miprimeraapp.modelo.Comentario
import co.edu.eam.miprimeraapp.modelo.Compra
import co.edu.eam.miprimeraapp.modelo.Producto
import com.google.firebase.database.*

object FirebaseManager {

    private var database:FirebaseDatabase = FirebaseDatabase.getInstance()
    var dataRef: DatabaseReference = database.reference
    lateinit var listener:OnActualizarAdaptador

    fun guardarProducto(producto: Producto){
        dataRef.child("productos").push().setValue(producto)
    }

    fun guardarComentario(comentario: Comentario, keyProducto:String){
        dataRef.child("productos").child(keyProducto).child("comentarios").push().setValue(comentario)
    }

    fun guardarFavorito(productoKey: String, keyUsuario:String){
        dataRef.child("usuarios").child(keyUsuario).child("favoritos").push().setValue(productoKey)
    }

    fun eliminarFavorito(productoKey: String, keyUsuario:String){
        dataRef.child("usuarios").child(keyUsuario).child("favoritos").child(productoKey).removeValue()
    }

    fun guardarCompra(compra: Compra, keyUsuario:String){
        dataRef.child("usuarios").child(keyUsuario).child("compras").push().setValue(compra)
    }

    fun guardarCategoria(categoria: Categoria){
        dataRef.child("categorias").push().setValue(categoria)
    }

    fun eliminarProducto(key: String){
        dataRef.child("productos").child(key).removeValue()
    }

    fun actualizarProducto(key: String, producto: Producto){
        dataRef.child("productos").child(key).setValue(producto)
    }

    fun escucharEventosCategorias(respuesta: (cat: Categoria) -> Unit){
        dataRef.child("categorias").addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val cat = snapshot.getValue(Categoria::class.java)
                respuesta(cat!!)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun escucharEventosProductos(){

        dataRef.child("productos").addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val producto = snapshot.getValue(Producto::class.java)
                if(producto!=null){
                    producto.key = snapshot.key!!
                    listener.productoAgregado(producto)
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.e("FIREBASE", "onChildChanged")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.e("FIREBASE", "onChildRemoved")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    interface OnActualizarAdaptador{
        fun productoAgregado(producto: Producto)
    }


}