package com.belvin.stem

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LearnAdapter(val learnList:List<Pair<String,String>>):RecyclerView.Adapter<LearnAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {

        fun bindData(learnItem:Pair<String,String>){
            val videoTitle = itemView.findViewById<TextView>(R.id.videoTitle)
            val videoPath = itemView.findViewById<TextView>(R.id.videoPath)

            videoTitle.text = learnItem.first
            videoPath.text = learnItem.second
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val myView = LayoutInflater.from(parent.context).inflate(R.layout.learn_list,parent,false)
        myView.setOnClickListener {
            var videoPathData = it.findViewById<TextView>(R.id.videoPath).text
            parent.context.startActivity(Intent(parent.context,VideoActivity::class.java).putExtra("VideoPath",videoPathData))
        }
        return ViewHolder(myView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(learnList[position])
    }

    override fun getItemCount(): Int = learnList.size
}