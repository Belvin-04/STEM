package com.belvin.stem

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class ModuleAdapter(var modules:Array<String>): RecyclerView.Adapter<ModuleAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var moduleName = itemView.findViewById<TextView>(R.id.moduleName)
        fun bindData(module:String){
            moduleName.text = module
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val myView = LayoutInflater.from(parent.context).inflate(R.layout.home_layout,parent,false)
        myView.setOnClickListener {
            var text = it.findViewById<TextView>(R.id.moduleName).text
            if(text == "Resistor Calculator"){
                parent.context.startActivity(Intent(parent.context,ResistorCalculator::class.java))
            }
            else if(text == "Number System Converter"){
                parent.context.startActivity(Intent(parent.context,NumberSystemConverter::class.java))
            }
            else if(text == "K-Map Solver"){
                parent.context.startActivity(Intent(parent.context,KMapSolver::class.java))
            }
            else if(text == "Matrix"){
                parent.context.startActivity(Intent(parent.context,MatrixSolver::class.java))
            }
        }
        return ViewHolder(myView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(modules[position])
    }

    override fun getItemCount(): Int {
        return modules.size
    }


}