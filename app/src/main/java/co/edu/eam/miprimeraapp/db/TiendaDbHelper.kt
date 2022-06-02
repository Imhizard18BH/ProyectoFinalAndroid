package co.edu.eam.miprimeraapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import co.edu.eam.miprimeraapp.modelo.Categoria
import co.edu.eam.miprimeraapp.modelo.Producto

const val DATABASE_VERSION = 1
const val DATABASE_NAME = "tienda.db"

class TiendaDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private var instance: TiendaDbHelper? = null

        fun getInstance(context: Context):TiendaDbHelper{
            if(instance==null){
                instance = TiendaDbHelper(context)
            }
            return instance!!
        }

    }

    override fun onCreate(p0: SQLiteDatabase?) {

        p0?.execSQL("create table ${ProductoContrato.TABLE_NAME} (" +
                "${ProductoContrato.CODIGO} integer primary key autoincrement, " +
                "${ProductoContrato.NOMBRE} varchar not null," +
                "${ProductoContrato.PRECIO} float not null," +
                "${ProductoContrato.DESCUENTO} float not null," +
                "${ProductoContrato.DESCRIPCION} text not null," +
                "${ProductoContrato.CODIGO_VENDEDOR} varchar not null," +
                "${ProductoContrato.UNIDADES} int not null )")

        p0?.execSQL("create table ${CategoriaContrato.TABLE_NAME} (" +
                "${CategoriaContrato.CODIGO} integer primary key, " +
                "${CategoriaContrato.NOMBRE} varchar not null," +
                "${CategoriaContrato.ICONO} varchar not null )")

        p0?.execSQL("create table ${ProductoCategoriaContrato.TABLE_NAME} (" +
                "${ProductoCategoriaContrato.CODIGO_CATEGORIA} integer not null," +
                "${ProductoCategoriaContrato.CODIGO_PRODUCTO} integer not null," +
                "primary key ( ${ProductoCategoriaContrato.CODIGO_CATEGORIA}, ${ProductoCategoriaContrato.CODIGO_PRODUCTO} )," +
                "foreign key (${ProductoCategoriaContrato.CODIGO_CATEGORIA}) references ${CategoriaContrato.TABLE_NAME} ( ${CategoriaContrato.CODIGO})," +
                "foreign key (${ProductoCategoriaContrato.CODIGO_PRODUCTO}) references ${ProductoContrato.TABLE_NAME} ( ${ProductoContrato.CODIGO})  )")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists ${ProductoContrato.TABLE_NAME}")
        p0?.execSQL("drop table if exists ${CategoriaContrato.TABLE_NAME}")
        p0?.execSQL("drop table if exists ${ProductoCategoriaContrato.TABLE_NAME}")
        onCreate(p0)
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        if(!db!!.isReadOnly){
            db.setForeignKeyConstraintsEnabled(true);
        }

    }

    fun crearCategoria(categoria: Categoria){
        writableDatabase.insert(
            CategoriaContrato.TABLE_NAME,
            null,
            categoria.toContentValues()
        )
    }

    fun guardarProducto(producto: Producto){

        val codigo = writableDatabase.insert(
            ProductoContrato.TABLE_NAME,
            null,
            producto.toContentValues()
        )

        producto.categorias.forEach { c ->

            val contentValues = ContentValues()
            contentValues.put( ProductoCategoriaContrato.CODIGO_PRODUCTO, codigo )
            contentValues.put( ProductoCategoriaContrato.CODIGO_CATEGORIA, c )

            writableDatabase.insert(
                ProductoCategoriaContrato.TABLE_NAME,
                null,
                contentValues
            )
        }


    }

    fun eliminarProducto(codigoProducto: String){
        writableDatabase.delete(
            ProductoContrato.TABLE_NAME,
            "${ProductoContrato.CODIGO} = ?",
            arrayOf(codigoProducto)
        )
    }

    fun actualizarProducto(producto: Producto){
        writableDatabase.update(
            ProductoContrato.TABLE_NAME,
            producto.toContentValues(),
            "${ProductoContrato.CODIGO} = ?",
            arrayOf(producto.codigo.toString())
        )
    }

    fun obtenerProductos():ArrayList<Producto>{

        var lista:ArrayList<Producto> = ArrayList()

        val c:Cursor = readableDatabase.query(
            ProductoContrato.TABLE_NAME,
            arrayOf(ProductoContrato.CODIGO, ProductoContrato.NOMBRE, ProductoContrato.PRECIO,
                ProductoContrato.DESCRIPCION, ProductoContrato.DESCUENTO, ProductoContrato.UNIDADES, ProductoContrato.CODIGO_VENDEDOR),
            null,
            null,
            null,
            null,
            null
        )

        if( c.moveToFirst() ){

            do{
                val p = Producto( c.getFloat(2), c.getString(1), c.getString(3), c.getInt(5), c.getFloat(4) )
                p.codigo = c.getInt(0)
                p.vendedor = c.getString(6)
                lista.add(p)
            }while (c.moveToNext())

        }

        return lista

    }

    fun obtenerProductos(codigoCategoria:Int):ArrayList<Producto>{

        var lista:ArrayList<Producto> = ArrayList()

        val c:Cursor = readableDatabase.query(
            "${ProductoCategoriaContrato.TABLE_NAME} inner join ${ProductoContrato.TABLE_NAME} on ${ProductoCategoriaContrato.CODIGO_PRODUCTO} = ${ProductoContrato.CODIGO}",
            arrayOf(ProductoContrato.CODIGO, ProductoContrato.NOMBRE, ProductoContrato.PRECIO,
                ProductoContrato.DESCRIPCION, ProductoContrato.DESCUENTO, ProductoContrato.UNIDADES, ProductoContrato.CODIGO_VENDEDOR),
            "${ProductoCategoriaContrato.CODIGO_CATEGORIA} = ?",
            arrayOf(codigoCategoria.toString()),
            null,
            null,
            null
        )

        if( c.moveToFirst() ){

            do{
                val p = Producto( c.getFloat(2), c.getString(1), c.getString(3), c.getInt(5), c.getFloat(4) )
                p.codigo = c.getInt(0)
                p.vendedor = c.getString(6)
                lista.add(p)
            }while (c.moveToNext())

        }

        return lista

    }

    fun obtenerProducto(codigoProducto: Int):Producto?{

        var producto:Producto? = null

        val c:Cursor = readableDatabase.query(
            " ${ProductoContrato.TABLE_NAME} inner join ${ProductoCategoriaContrato.TABLE_NAME} on ${ProductoContrato.CODIGO} = ${ProductoCategoriaContrato.CODIGO_PRODUCTO}",
            arrayOf(ProductoContrato.CODIGO, ProductoContrato.NOMBRE, ProductoContrato.PRECIO,
                ProductoContrato.DESCRIPCION, ProductoContrato.DESCUENTO, ProductoContrato.UNIDADES, ProductoContrato.CODIGO_VENDEDOR, ProductoCategoriaContrato.CODIGO_CATEGORIA),
            "${ProductoContrato.CODIGO} = ?",
            arrayOf(codigoProducto.toString()),
            null,
            null,
            null
        )

        val listaCategoria:ArrayList<Int> = ArrayList()
        var bandera = true

        if( c.moveToFirst() ){
            var p:Producto? = null

            do {

                if (bandera) {
                    p = Producto(
                        c.getFloat(2),
                        c.getString(1),
                        c.getString(3),
                        c.getInt(5),
                        c.getFloat(4)
                    )
                    p.codigo = c.getInt(0)
                    p.vendedor = c.getString(6)
                    bandera = false
                }
                listaCategoria.add(c.getInt(7))

            }while (c.moveToNext());

            p!!.categorias = listaCategoria
            producto = p
        }

        return producto
    }

    fun obtenerCategorias():ArrayList<Categoria>{

        var lista:ArrayList<Categoria> = ArrayList()

        val c:Cursor = readableDatabase.query(
            CategoriaContrato.TABLE_NAME,
            arrayOf(CategoriaContrato.CODIGO, CategoriaContrato.NOMBRE, CategoriaContrato.ICONO),
            null,
            null,
            null,
            null,
            null
        )

        if( c.moveToFirst() ){

            do{
                lista.add( Categoria( c.getString(0).toInt(), c.getString(1), c.getString(2), "" ) )
            }while (c.moveToNext())

        }

        return lista

    }

    fun obtenerCategoria(codigoCategoria:Int):Categoria?{

        val c:Cursor = readableDatabase.query(
            CategoriaContrato.TABLE_NAME,
            arrayOf(CategoriaContrato.CODIGO, CategoriaContrato.NOMBRE, CategoriaContrato.ICONO),
            "${CategoriaContrato.CODIGO} = ?",
            arrayOf(codigoCategoria.toString()),
            null,
            null,
            null
        )

        if( c.moveToFirst() ){

            val c = Categoria( c.getInt(0), c.getString(1), c.getString(2), "" )
            return c
        }

        return null

    }

}