package ru.bukivadis.myfirstapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.bukivadis.myfirstapp.db.AppDb
import ru.bukivadis.myfirstapp.dto.Post
import ru.bukivadis.myfirstapp.repository.PostRepository
import ru.bukivadis.myfirstapp.repository.PostRepositoryFileImpl  // или другую реализацию
import ru.bukivadis.myfirstapp.repository.PostRepositorySQLiteImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {


    // Используем SQLite репозиторий
    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao
    )
    val data: LiveData<List<Post>> = repository.getAll()

    private val _draft = MutableLiveData("")
    val draft: LiveData<String> = _draft

    fun saveDraft(text: String) {
        _draft.value = text
    }

    fun clearDraft() {
        _draft.value = ""
    }



    private val empty = Post(
        id = 0,
        author = "",
        content = "",
        published = ""
    )

    private val _edited = MutableLiveData(empty)
    val edited: LiveData<Post> = _edited

    private val _editingMode = MutableLiveData(false)
    val editingMode: LiveData<Boolean> = _editingMode

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun increaseViews(id: Long) = repository.increaseViews(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun save() {
        _edited.value?.let { post ->
            if (post.content.isNotBlank()) {
                repository.save(post)
            }
        }
        _edited.value = empty
        _editingMode.value = false
    }

    fun edit(post: Post) {
        _edited.value = post
        _editingMode.value = true
    }

    fun changeContent(content: String) {
        val text = content.trim()
        _edited.value?.let { post ->
            if (post.content != text) {
                _edited.value = post.copy(content = text)
            }
        }
    }

    fun cancelEdit() {
        _edited.value = empty
        _editingMode.value = false
    }

    fun saveEditedPost(editingPostId: Long, text: String) {}
}



