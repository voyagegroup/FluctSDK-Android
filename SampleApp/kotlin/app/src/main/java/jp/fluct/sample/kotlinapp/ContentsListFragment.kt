package jp.fluct.sample.kotlinapp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import jp.fluct.fluctsdk.FluctAdBanner
import java.util.*

class ContentsListFragment : Fragment() {

    private var contentsRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_contents_list, container, false)
        contentsRecyclerView = layout.findViewById(R.id.content_list)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val listContents: MutableList<ListModel> = generateContents() as MutableList<ListModel>
        val listView = view!!.findViewById<RecyclerView>(R.id.content_list)
        val adapter = ContentsListAdapter(activity!!.applicationContext, listContents)

        // Fluct広告を入れる
        val sizeType = FluctAdBanner.SizeType.BANNER
        val mediaId = "0000005617"
        adapter.insertItem(Ad(sizeType, mediaId), 3)
        adapter.insertItem(Ad(sizeType, mediaId), 12)
        adapter.insertItem(Ad(sizeType, mediaId), 30)
        adapter.insertItem(Ad(sizeType, mediaId), 55)
        adapter.insertItem(Ad(sizeType, mediaId), 80)

        listView.adapter = adapter
    }

    // リストコンテンツの作成
    private fun generateContents(): MutableList<Content> {
        val contents = ArrayList<Content>()
        for (i in 0..99) {
            val name = "item " + (i + 1)
            val content = Content(i, name)
            contents.add(content)
        }
        return contents
    }

    abstract class ListModel

    /**
     * コンテンツのモデル
     */
    class Content(val identity: Int, val name: String) : ListModel()

    /**
     * 広告モデル
     */
    class Ad(val sizeType: FluctAdBanner.SizeType, val mediaId: String) : ListModel()

    class ContentsListAdapter(
            val context: Context,
            private val itemList: MutableList<ListModel>
    ) : RecyclerView.Adapter<BaseViewHolder>() {

        fun insertItem(content: ListModel, index: Int) {
            this.itemList.add(index, content)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return when (viewType) {
                ViewType.CONTENT.ordinal -> {
                    val view = LayoutInflater.from(context).inflate(R.layout.item_list_content, parent, false)
                    ContentViewHolder(view)
                }
                ViewType.AD.ordinal -> {
                    val fluctAdBanner = FluctAdBanner(context)
                    AdViewHolder(fluctAdBanner)
                }
                else -> throw IllegalStateException()
            }
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            val item = itemList[position]
            when (item) {
                is Content -> {
                    val contentVH = holder as ContentViewHolder
                    val index = position + 1
                    contentVH.index?.text = "$index"
                    contentVH.name?.text = item.name
                }
                is Ad -> {
                    val bannerView = holder.itemView as FluctAdBanner
                    bannerView.viewSettings.mediaId = item.mediaId
                }
            }
        }

        override fun getItemCount(): Int {
            return this.itemList.size
        }

        override fun getItemViewType(position: Int): Int {
            val content = this.itemList[position]
            return if (content is Ad) ViewType.AD.ordinal else ViewType.CONTENT.ordinal
        }

        enum class ViewType {
            CONTENT, AD
        }
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class AdViewHolder(view: View) : BaseViewHolder(view)

    class ContentViewHolder(view: View) : BaseViewHolder(view) {
        var index: TextView? = view.findViewById(R.id.list_index)
        var name: TextView? = view.findViewById(R.id.list_name)
    }
}