package co.edu.eam.miprimeraapp.data

import co.edu.eam.miprimeraapp.modelo.Categoria

object Categorias {

    private var categorias:ArrayList<Categoria> = ArrayList()

    init {
      /*  categorias.add( Categoria(2,"Tecnología", '\uf390', "") )
        categorias.add( Categoria(1,"Hogar", '\uf236', "") )
        categorias.add( Categoria(4,"Ropa", '\uf553', "") )
        categorias.add( Categoria(5,"Electrodomésticos", '\uf1e6', "") )
        categorias.add( Categoria(6,"Herremientas", '\uf6e3', "") )
        categorias.add( Categoria(3,"Otros", '\uf1b9', "") ) */
    }

    fun getLista():ArrayList<Categoria>{
        return categorias
    }

    fun agregar(categoria: Categoria){
        categorias.add(categoria)
    }

    fun obtener(codigo:Int):Categoria?{
        return categorias.find { c -> c.codigo == codigo }
    }

}