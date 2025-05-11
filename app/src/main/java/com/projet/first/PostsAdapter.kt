package com.projet.first

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.projet.first.DB.FirstUpDB
import com.projet.first.Data.Post

class PostsAdapter(
    var mContext: Context,
    var resource: Int,
    private var values: ArrayList<Post>
): ArrayAdapter<Post>(mContext, resource, values) {

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val post = values[position]
        val itemView = LayoutInflater.from(mContext).inflate(resource, parent, false)
        val tvTitre = itemView.findViewById<TextView>(R.id.tvTitre)
        val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        val imagePost = itemView.findViewById<ImageView>(R.id.imagePost)
        val imageShoawPopup = itemView.findViewById<ImageView>(R.id.imageShoawPopup)
        val tvLikes = itemView.findViewById<TextView>(R.id.tvLikes)
        val tvShare = itemView.findViewById<TextView>(R.id.tvShare)

        tvTitre.text = post.titre
        tvDescription.text = post.description
        val bitmap = getBitmap(post.image)
        imagePost.setImageBitmap(bitmap)
        tvLikes.text = "${post.jaime} J'aime"

        val db = FirstUpDB(mContext)

        imageShoawPopup.setOnClickListener {
            val popupMenu = PopupMenu(mContext, imageShoawPopup)
            popupMenu.menuInflater.inflate(R.menu.list_popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.itemShow -> {
                        Intent(mContext, PostDetailsActivity::class.java).also {
                            it.putExtra("titre", post.titre)
                            mContext.startActivity(it)

                        }
                    }

                    R.id.itemDelete -> {
                        val resultDelete = db.deletePost(post.id)
                        if (resultDelete) {
                            values.removeAt(position)
                            notifyDataSetChanged()
                        }else {
                            Toast.makeText(mContext, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                true

            }

            popupMenu.show()


        }

        tvLikes.setOnClickListener {
            // TODO: incrementer le nombre de like

            db.incrementLikes(post)
            val incrementedLikes = post.jaime + 1
            tvLikes.text = "$incrementedLikes J'aime"

        }

        tvShare.setOnClickListener {
            // TODO: partager le post
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://www.google.com/${post.titre}")
                type = "text/plain"

            }
            val shareIntent = Intent.createChooser(sendIntent, post.titre)
            mContext.startActivity(shareIntent)
        }




        return itemView
    }

     fun getBitmap(byteArray: ByteArray): Bitmap {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return bitmap

    }

}