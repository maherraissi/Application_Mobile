package com.projet.first

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.projet.first.DB.FirstUpDB
import com.projet.first.Data.Post

class PostsRecyclerAdapter(
    private val mContext: Context,
    private val values: ArrayList<Post>
) : RecyclerView.Adapter<PostsRecyclerAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitre: TextView = itemView.findViewById(R.id.tvTitre)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val imagePost: ImageView = itemView.findViewById(R.id.imagePost)
        val imageShoawPopup: ImageView = itemView.findViewById(R.id.imageShoawPopup)
        val tvLikes: TextView = itemView.findViewById(R.id.tvLikes)
        val tvShare: TextView = itemView.findViewById(R.id.tvShare)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = values[position]
        holder.tvTitre.text = post.titre
        holder.tvDescription.text = post.description
        holder.imagePost.setImageBitmap(getBitmap(post.image))
        holder.tvLikes.text = "${post.jaime} J'aime"

        val db = FirstUpDB(mContext)

        holder.imageShoawPopup.setOnClickListener {
            val popupMenu = PopupMenu(mContext, holder.imageShoawPopup)
            popupMenu.menuInflater.inflate(R.menu.list_popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.itemShow -> {
                        Intent(mContext, PostDetailsActivity::class.java).also {
                            it.putExtra("titre", post.titre)
                            it.putExtra("description", post.description)
                            it.putExtra("image", post.image)
                            it.putExtra("id", post.id)
                            mContext.startActivity(it)
                        }
                    }
                    R.id.itemDelete -> {
                        val resultDelete = db.deletePost(post.id)
                        if (resultDelete) {
                            values.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, values.size)
                        } else {
                            Toast.makeText(mContext, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                true
            }
            popupMenu.show()
        }

        holder.tvLikes.setOnClickListener {
            db.incrementLikes(post)
            val incrementedLikes = post.jaime + 1
            holder.tvLikes.text = "$incrementedLikes J'aime"
        }

        holder.tvShare.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://www.google.com/${post.titre}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, post.titre)
            mContext.startActivity(shareIntent)
        }
    }

    override fun getItemCount(): Int = values.size

    private fun getBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
} 