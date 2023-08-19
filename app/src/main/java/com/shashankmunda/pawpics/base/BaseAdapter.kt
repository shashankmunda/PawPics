package com.shashankmunda.pawpics.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.request.Disposable

abstract class BaseAdapter<T, VB : ViewBinding> : RecyclerView.Adapter<BaseAdapter<T, VB>.ViewHolder>() {
    protected var disposables = arrayListOf<Disposable>()

    protected val items: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = createBinding(inflater, parent)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        bindItem(holder.binding, item)
    }

    fun setItems(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    protected abstract fun createBinding(inflater: LayoutInflater, parent: ViewGroup): VB

    protected abstract fun bindItem(binding: VB, item: T)

    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
