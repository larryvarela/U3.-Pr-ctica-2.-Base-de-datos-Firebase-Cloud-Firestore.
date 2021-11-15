package mx.edu.tepic.ladm_u3_ejercicio3_firestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var baseRemota = FirebaseFirestore.getInstance()
    var datalista = ArrayList<String>()
    var listaID = ArrayList<String>()
    var idNotas = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mostrarNotasCapturados()
        baseRemota.collection("notas")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null){
                    mensaje(error.message!!)
                    return@addSnapshotListener
                }

                datalista.clear()
                listaID.clear()
                for (document in querySnapshot!!){
                    var cadena = "${document.getString("titulo")} -- ${document.get("contenido")}" +
                            "-- ${document.get("hora")} -- ${document.get("fecha")}"
                    datalista.add(cadena)
                    listaID.add(document.id.toString())
                }

                listaPersonas.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,datalista)
                listaPersonas.setOnItemClickListener { adapterView, view, posicion, l ->
                    dialogoEliminarActualiza(posicion)
                }
            }

        button.setOnClickListener {
            insertar()
        }

        button2.setOnClickListener {
            val notas=Notas(this)

            notas.titulo=titulo.text.toString()
            notas.contenido=contenido.text.toString()
            notas.hora=hora.text.toString()
            notas.fecha=fecha.text.toString()

            val resultado= notas.insertar()
            if(resultado){
                Toast.makeText(this, "EXITO!, SE INSERTÓ", Toast.LENGTH_LONG).show()
                titulo.text.clear()
                contenido.text.clear()
                hora.text.clear()
                fecha.text.clear()
                mostrarNotasCapturados()
            }else{
                Toast.makeText(this, "ERROR NO SE INSERTO", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun mostrarNotasCapturados(){
        val arregloNotas=Notas(this).consultar()
        listaPersonas.adapter=ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arregloNotas)
        idNotas.clear()
        idNotas = Notas(this).obetenerIDs()
        activarEvento(listaPersonas)
    }

    private fun activarEvento(listaPersonas2: ListView) {
        listaPersonas2.setOnItemClickListener { adapterView, view, indiceSeleccionado, l ->
            val idSeleccionado = idNotas[indiceSeleccionado]
            AlertDialog.Builder(this)
                .setTitle("+ATENCION")
                .setMessage("¿QUE DESEA HACER CON LA NOTA")
                .setPositiveButton("EDITAR"){d,i-> actualizar(idSeleccionado)}
                .setNegativeButton("ELIMINAR"){d,i-> eliminar(idSeleccionado)}
                .setNeutralButton("CANCELAR"){d,i->
                    d.cancel()
                }
                .show()

        }
    }

    private fun actualizar(idSeleccionado: Int) {
        val intento = Intent(this,MainActivity2::class.java)
        intento.putExtra("idActualizar",idSeleccionado.toString())
        startActivity(intento)
        AlertDialog.Builder(this).setMessage("DESEAS ACTUALIZAR LA LISTA")
            .setPositiveButton("SI"){d,i-> mostrarNotasCapturados()}
            .setNegativeButton("NO"){d,i->d.cancel()}
            .show()
    }

    private fun eliminar(idSeleccionado: Int) {
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage("¿SEGURO QUE DESEAS ELIMINAR ID ${idSeleccionado}")
            .setPositiveButton("SI"){d,i->
                val resultado = Notas(this).eliminar(idSeleccionado)
                if(resultado){
                    Toast.makeText(this, "SE ELIMINO CON EXITO", Toast.LENGTH_LONG)
                    mostrarNotasCapturados()
                }else{
                    Toast.makeText(this, "NO SE ELIMINO CON EXITO", Toast.LENGTH_LONG)
                }
            }
            .setNegativeButton("NO"){d,i->
                d.cancel()
            }
            .show()
    }

    //////////////////LA NUBE////////////////////////
    private fun dialogoEliminarActualiza(posicion: Int) {
        var idElegido = listaID.get(posicion)
        AlertDialog.Builder(this).setTitle("ATENCION!!")
            .setMessage("¿QUE DESEAS HACER CON \n${datalista.get(posicion)}?")
            .setPositiveButton("ELIMINAR"){d,i->
                eliminar(idElegido)
            }
            .setNeutralButton("ACTUALIZAR"){d,i->}
            .setNegativeButton("CANCELAR"){d,i->}
            .show()
    }

    private fun eliminar(idElegido: String) {
        baseRemota.collection("notas")
            .document(idElegido)
            .delete()
            .addOnSuccessListener {
                alerta("SE ELIMINO CON EXITO")
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message!!}")
            }
    }

    private fun insertar() {
        //para insertar el metodo a usar  as ADD
        //ADD es para TODOS los campos del documento
        //con formato CLAVE VALOR

        var datosInsertar = hashMapOf(
            "titulo" to titulo.text.toString(),
            "contenido" to contenido.text.toString(),
            "hora" to hora.text.toString(),
            "fecha" to fecha.text.toString()
        )

        baseRemota.collection("notas")
            .add(datosInsertar as Any)
            .addOnSuccessListener {
                alerta("se inserto correctamente a la nube")
            }
            .addOnFailureListener{
                mensaje("ERROR ${it.message!!}")
            }
        titulo.setText("")
        contenido.setText("")
        hora.setText("")
        fecha.setText("")
    }

    private fun alerta(s: String) {
    Toast.makeText(this,s,Toast.LENGTH_LONG).show()
    }

    private fun mensaje(s: String) {
    AlertDialog.Builder(this).setTitle("ATENCION")
        .setMessage(s)
        .setPositiveButton("OK"){d,i->}
        .show()
    }
}