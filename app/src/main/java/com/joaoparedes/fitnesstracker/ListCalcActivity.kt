package com.joaoparedes.fitnesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joaoparedes.fitnesstracker.model.Calc
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

class ListCalcActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)

        val result = mutableListOf<Calc>()
        val rvCalc: RecyclerView = findViewById(R.id.rv_calc)
        val adapter = CalcAdapter(result)
        rvCalc.adapter = adapter

        rvCalc.layoutManager = LinearLayoutManager(this)

        val type = intent?.extras?.getString("type") ?: throw IllegalStateException("type not found")

        Thread {
            val app = (application as App)
            val dao = app.db.calcDao()
            val response = dao.getRegisterByType(type)

            runOnUiThread {
                result.addAll(response)
                adapter.notifyDataSetChanged()
            }

        }.start()


    }

    private inner class CalcAdapter(private val listCalc: List<Calc>) : RecyclerView.Adapter<CalcAdapter.CalcViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalcViewHolder {
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            return CalcViewHolder(view)
        }

        override fun onBindViewHolder(holder: CalcViewHolder, position: Int) {
            val calcCurrent = listCalc[position]
            holder.bind(calcCurrent)
        }

        override fun getItemCount(): Int {
            return listCalc.size
        }



        private inner class CalcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: Calc) {
                val text: TextView = itemView.findViewById(android.R.id.text1)

                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt","BR"))
                val data = sdf.format(item.createdDate)
                val res = item.res

                text.text = getString(R.string.list_response, res, data)
            }
        }


    }




}