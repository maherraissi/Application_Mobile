package com.projet.first

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView

class PostsAdapter(
    var mContext: Context,
    var resource: Int,
    var values: ArrayList<Post>
): ArrayAdapter<Post>(mContext, resource, values) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val post = values[position]
        val itemView= LayoutInflater.from(mContext).inflate(resource, parent, false)
        val tvTitre = itemView.findViewById<TextView>(R.id.tvTitre)
        val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        val imagePost = itemView.findViewById<ImageView>(R.id.imagePost)
        val imageShoawPopup = itemView.findViewById<ImageView>(R.id.imageShoawPopup)

        tvTitre.text = post.titre
        tvDescription.text = post.description
        imagePost.setImageResource(post.image)

        imageShoawPopup.setOnClickListener {
            val popupMenu = PopupMenu(mContext, imageShoawPopup)
            popupMenu.menuInflater.inflate(R.menu.list_popup_menu,popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.itemShow -> {
                        Intent(mContext, PostDetailsActivity::class.java).also {
                            it.putExtra("titre", post.titre)
                            mContext.startActivity(it)

                        }
                    }

                    R.id.itemDelete -> {
                        values.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
                true

            }

            popupMenu.show()


        }
        return itemView
    }
}