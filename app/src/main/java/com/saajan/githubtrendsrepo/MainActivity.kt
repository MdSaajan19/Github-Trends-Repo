package com.saajan.githubtrendsrepo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.saajan.githubtrendsrepo.adapters.TrendingRepoListAdapter
import com.saajan.githubtrendsrepo.models.GithubTrendData
import com.saajan.githubtrendsrepo.utils.ConnectivityManager
import com.saajan.githubtrendsrepo.utils.ProgressDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class MainActivity : AppCompatActivity() {

    var trendingRepoList: ArrayList<GithubTrendData> = ArrayList()
    var pageTitle: String? = null
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = ProgressDialog.progressDialog(this)
        title = resources.getString(R.string.app_name)
        rvTrendingRepo.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        if (ConnectivityManager.isInternetAvailable(this@MainActivity,null,121)) {
            dialog.show()
            getHtmlFromWeb("https://github.com/trending/")
        }
    }

    override fun onResume() {
        super.onResume()
        if (ConnectivityManager.isInternetAvailable(this@MainActivity,null,121)) {
            dialog.show()
            getHtmlFromWeb("https://github.com/trending/")
        }
    }

    private fun getHtmlFromWeb(url: String) {
        Thread(Runnable {
            val stringBuilder = StringBuilder()
            try {
                val doc: Document = Jsoup.connect(url).get()
                pageTitle = doc.title()
                val links: Elements = doc.select("article[class=\"Box-row\"]")
                    for (link in links) {
                        val authRepoName = link.child(1).child(0).attr("href").toString()
                        val repoDescription = link.child(2).text()
                        val authName = authRepoName.substringAfter("/").substringBefore("/")
                        val repoName = authRepoName.substringAfterLast("/")
                        trendingRepoList.add(GithubTrendData(authName,repoName,repoDescription))
                    }
            } catch (e: IOException) {
                stringBuilder.append("Error : ").append(e.message).append("")
            }
            runOnUiThread {
                if (dialog.isShowing){
                    dialog.dismiss()
                }
                assignDataToView(trendingRepoList)
            }
        }).start()
    }

    private fun assignDataToView(trendingRepoList: ArrayList<GithubTrendData>) {
        tvPageTitle.text = pageTitle!!
        rvTrendingRepo.adapter = TrendingRepoListAdapter(this@MainActivity,trendingRepoList)
    }
}