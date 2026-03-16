package ru.bukivadis.myfirstapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.bukivadis.myfirstapp.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {

    // Исходные данные
    private var post = Post(
        id = 1,
        author = "АвтоГрам. Университет интернет-машинок",
        content = "Где автомобили учатся ездить… а люди — не ломать их.\n" +
                "         1 курс: «Как не врезаться в столб, если ты впервые в жизни сел за руль»\n" +
                "         2 курс: «Почему твой Tesla думает, что ты — робот»\n" +
                "         3 курс: «Как объяснить бабушке, что “обновление ПО” — это не вирус»\n" +
                "         4 курс: «Когда навигатор говорит “Поверните направо”, а справа — стена. Что делать?»\n" +
                "         5 курс: «Автопилот vs. Кот на пассажирском сиденье» — практическое занятие\n" +
                "        Мы не учим водить.\n" +
                "        Мы учим выживать в мире, где машины умнее тебя, но ты всё ещё платишь за бензин.",
        published = "22 марта в 19:06",
        likedByMe = false,
        likes = 999,
        shares = 25,
        views = 5700)

    // MutableLiveData, который можно изменять
    private val _data = MutableLiveData(post)

    // Внешний доступ только для чтения (LiveData, а не MutableLiveData)
    override fun get(): LiveData<Post> = _data

    override fun like() {
        // Меняем состояние лайка на противоположное
        post = post.copy(
            likedByMe = !post.likedByMe,
            likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
        )
        // Оповещаем подписчиков об изменении
        _data.value = post
    }

    override fun share() {
        post = post.copy(
            shares = post.shares + 1
        )
        _data.value = post
    }

    override fun increaseViews() {
        // Можно будет реализовать позже
        post = post.copy(
            views = post.views + 1
        )
        _data.value = post
    }
}
