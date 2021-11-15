package mx.edu.tepic.ladm_u3_ejercicio3_firestore

import android.content.ContentValues
import android.content.Context

class Notas(p: Context) {


    var titulo=""
    var contenido=""
    var hora=""
    var fecha=""
    val pnt= p

    fun insertar():Boolean{
        val tablaNotas=DataBase(pnt, "casaArte", null, 1).writableDatabase
        val datos= ContentValues()
        datos.put("titulo", titulo)
        datos.put("contenido", contenido)
        datos.put("hora", hora)
        datos.put("fecha", fecha)

        val resultado= tablaNotas.insert("NOTAS", null, datos)
        if(resultado==-1L){
            return false
        }
        return true
    }

    fun consultar():ArrayList<String>{
        val tablaNotas=DataBase(pnt, "casaArte", null, 1).readableDatabase
        val resultado=ArrayList<String>()
        val cursor=tablaNotas.query("NOTAS", arrayOf("*"), null, null, null, null, null)
        if(cursor.moveToFirst()){
            do{
                var dato=cursor.getString(1)+"\n"+cursor.getString(2)+"(${cursor.getInt(0)})"
                resultado.add(dato)
            }while (cursor.moveToNext())
        }else{
            resultado.add("NO SE ENCONTRÓ NADA A MOSTRAR")
        }
        return resultado
    }


    fun obetenerIDs():ArrayList<Int>{
        //CURSOR = objeto que obtiene el resultado de un SELECT (es una especie de tabla dinamica)
        val tablaNotas = DataBase(pnt,"casaArte",null,1).readableDatabase
        val resultado= ArrayList<Int>()
        //SELECTO * FROM ARTISTA(notiene)
        val cursor = tablaNotas.query("NOTAS",arrayOf("*"),null,null,null,null,null)
        if (cursor.moveToFirst()){
            do{
                //es getInt por que el ID es un entero
                resultado.add(cursor.getInt(0))
            }while (cursor.moveToNext())
        }
        return resultado
    }

    fun eliminar(idEliminar:Int) : Boolean {
        val tablaNotas = DataBase(pnt, "casaArte", null,1).writableDatabase
        val resultado = tablaNotas.delete("NOTAS","ID=?", arrayOf(idEliminar.toString()))
        if (resultado == 0)   return false
        return true
    }

    fun consultar(idABuscar:String) : Notas {
        val tablaNotas = DataBase(pnt, "casaArte", null, 1).writableDatabase
        val cursor = tablaNotas.query(
            "NOTAS",
            arrayOf("*"),
            "ID=?",
            arrayOf(idABuscar),
            null,
            null,
            null
        )
        val notas = Notas(MainActivity())
        if (cursor.moveToFirst()) {
            notas.titulo = cursor.getString(1)
            notas.contenido = cursor.getString(2)
            notas.hora = cursor.getString(3)
            notas.fecha = cursor.getString(4)
        }
        return notas
    }

    fun actualizar(idActualizar : String) : Boolean{
        val tablaNotas = DataBase(pnt, "casaArte", null,1).writableDatabase
        val datos = ContentValues()

        datos.put("titulo",titulo )
        datos.put("contenido",contenido)
        datos.put("hora",hora )
        datos.put("fecha",fecha)

        val resultado = tablaNotas.update("NOTAS",datos, "ID=?", arrayOf(idActualizar))
        if(resultado == 0) return false
        return true
    }
}