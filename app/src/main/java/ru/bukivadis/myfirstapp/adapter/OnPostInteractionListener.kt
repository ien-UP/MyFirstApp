package ru.bukivadis.myfirstapp

import ru.bukivadis.myfirstapp.dto.Post

interface OnPostInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onAvatarClick(post: Post) {}
}
