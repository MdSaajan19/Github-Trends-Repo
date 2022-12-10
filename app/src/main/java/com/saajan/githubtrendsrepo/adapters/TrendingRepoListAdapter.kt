package com.saajan.githubtrendsrepo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.saajan.githubtrendsrepo.MainActivity
import com.saajan.githubtrendsrepo.R
import com.saajan.githubtrendsrepo.databinding.TrendsRepoItemBinding
import com.saajan.githubtrendsrepo.models.GithubTrendData

class TrendingRepoListAdapter(
    var context: MainActivity,
    private val mList: ArrayList<GithubTrendData>?
    ): RecyclerView.Adapter<TrendingRepoListAdapter.ViewHolder>() {

    // Holds the views for adding it to image and text
    class ViewHolder(var binding: TrendsRepoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: GithubTrendData
        ){
            binding.data =item
            binding.executePendingBindings()
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val binding = DataBindingUtil.inflate<TrendsRepoItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.trends_repo_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList!![position])
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList!!.size
    }
}