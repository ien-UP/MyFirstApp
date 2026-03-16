package ru.bukivadis.myfirstapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.bukivadis.myfirstapp.dto.Post
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostRepositoryFileImpl(
    private val context: Context
) : PostRepository {

    private val gson = Gson()
    private val filename = "posts.json"

    // Тип для десериализации списка постов
    private val type = object : TypeToken<List<Post>>() {}.type

    // Счетчик для генерации ID
    private var nextId = 1L

    // Текущий пользователь
    private val currentUserId = 1L
    private val currentUserName = "Я"

    // Данные в памяти
    private var posts = emptyList<Post>()
    private val _data = MutableLiveData(posts)

    init {
        // При создании репозитория пытаемся загрузить данные из файла
        loadData()
    }

    override fun getAll(): LiveData<List<Post>> = _data

    override fun likeById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
                )
            } else {
                post
            }
        }
        _data.value = posts
        saveData()
    }

    override fun shareById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(shares = post.shares + 1)
            } else {
                post
            }
        }
        _data.value = posts
        saveData()
    }

    override fun increaseViews(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(views = post.views + 1)
            } else {
                post
            }
        }
        _data.value = posts
        saveData()
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            // Создание нового поста
            val newPost = post.copy(
                id = generateNextId(),
                author = currentUserName,
                authorId = currentUserId,
                published = formatDate(Date()),
                likedByMe = false,
                likes = 0,
                shares = 0,
                views = 0
            )
            listOf(newPost) + posts
        } else {
            // Обновление существующего поста
            posts.map { existingPost ->
                if (existingPost.id == post.id) {
                    existingPost.copy(content = post.content)
                } else {
                    existingPost
                }
            }
        }
        _data.value = posts
        saveData()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        _data.value = posts
        saveData()
    }


    private fun loadData() {
        val file = getPostsFile()
        if (!file.exists()) {
            // Если файла нет, создаем начальные данные
            createInitialData()
            saveData()
            return
        }

        try {
            context.openFileInput(filename).bufferedReader().use { reader ->
                val loadedPosts: List<Post> = gson.fromJson(reader, type)
                if (loadedPosts.isNotEmpty()) {
                    posts = loadedPosts
                    // Вычисляем следующий ID на основе максимального существующего
                    nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
                    _data.value = posts
                } else {
                    createInitialData()
                    saveData()
                }
            }
        } catch (e: Exception) {
            // В случае ошибки создаем начальные данные
            e.printStackTrace()
            createInitialData()
            saveData()
        }
    }


    private fun saveData() {
        try {
            context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
                gson.toJson(posts, writer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPostsFile(): File = context.filesDir.resolve(filename)

    private fun createInitialData() {
        posts = listOf(
            Post(
                id = 1,
                author = "АвтоГрам. Университет интернет-машинок",
                authorId = 2,
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
                views = 5700,
                video = null
            ),
            Post(
                id = 2,
                author = "Космический путешественник",
                authorId = 3,
                content = "Учёные NASA и Европейского космического агентства обнаружили экзопланету TOI-700 e — первую в обитаемой зоне своей звезды, чья атмосфера, по данным спектроскопии JWST, содержит следы водяного пара, метана и кислорода. Это не просто «похоже на Землю» — это первый кандидат на потенциально обитаемый мир за пределами нашей Солнечной системы. Уже запланирована миссия «Aurora Probe» на 2035 год: зонд с лазерным спектрометром и микроскопом для поиска биомаркеров. Мы можем быть на пороге первого подтверждения жизни вне Земли.",
                published = "22 мая в 10:15",
                likedByMe = false,
                likes = 342,
                shares = 89,
                views = 2300,
                video = "https://rutube.ru/video/2854a399c9fc11e7e1793ea37f99caf2/"
            ),
            Post(
                id = 3,
                author = "Python Daily",
                authorId = 4,
                content = "Python 3.12 официально выпущен — и это не просто обновление, а прорыв в удобстве и производительности. Теперь вы можете использовать PEP 634 (структурное сопоставление с образцами) с вложенными кортежами и словарями, PEP 684 — улучшенные аннотации типов с поддержкой TypeVarTuple, а также PEP 701 — синтаксис f-строк с поддержкой двойных фигурных скобок внутри f-строк. Особенно впечатляет: компилятор теперь оптимизирует циклы на уровне байт-кода, что даёт до 15% прироста скорости в вычислительно нагруженных задачах. Для Data Science — поддержка NumPy 2.0 без преобразований. Обновляйтесь — и не забудьте проверить совместимость ваших зависимостей!",
                published = "23 мая в 09:42",
                likedByMe = true,
                likes = 1250,
                shares = 420,
                views = 8900,
                video = "https://rutube.ru/video/d3cf75b21716fee636f947f2bf36ce54/"
            ),
            Post(
                id = 4,
                author = "Microsoft Build",
                authorId = 5,
                content = "На конференции Microsoft Build 2024 представлены ключевые инструменты для будущего разработки: .NET 8 с поддержкой Native AOT для веб-приложений, Visual Studio 2024 с AI-ассистентом, который пишет код, пишет тесты и даже объясняет ошибки на естественном языке, и Azure AI Studio — единая платформа для создания, развертывания и мониторинга ИИ-моделей без знания Python. Особенно впечатляет: Visual Studio теперь может автоматически переписывать устаревший C# на современный синтаксис, поддерживая миграцию проектов 2010 года в 2024. Azure AI Studio позволяет создавать LLM-агентов с памятью, планированием и интеграцией в реальные API — всё через графический интерфейс. Это не просто инструменты — это новый стандарт разработки ПО в эпоху ИИ.",
                published = "20 мая в 20:00",
                likedByMe = false,
                likes = 5678,
                shares = 1234,
                views = 45000,
                video = "https://rutube.ru/video/7a010e15530a8382cfc7c7b7a82cc1c7/"
            ),
            Post(
                id = 5,
                author = "Rust Lang News",
                authorId = 6,
                content = "Rust 1.78 вводит революционную функцию: async/await теперь можно использовать внутри const-контекста — это означает, что вы можете создавать асинхронные константы, инициализируемые во время компиляции. Например: `const DB_CONNECTION: &AsyncConnection = async { connect_to_db().await }.await;` — теперь это компилируется! Это устраняет необходимость в unsafe-коде для инициализации глобальных состояний и открывает путь к полностью безопасным, статически инициализированным серверным приложениям. Также добавлена поддержка SIMD-инструкций для ARMv8.4 и улучшена интеграция с WebAssembly. Rust продолжает становиться языком будущего — не потому что он «быстрый», а потому что он делает невозможное — возможным.",
                published = "21 мая в 14:30",
                likedByMe = false,
                likes = 2105,
                shares = 687,
                views = 15400,
                video = "https://rutube.ru/video/f205f1b66abd9f14079bef7cca59e5bb/"
            ),
            Post(
                id = 6,
                author = "TechCrunch AI",
                authorId = 7,
                content = "Anthropic анонсировала Claude 3.5 — новую ИИ-модель, способную генерировать код на 12 языках (включая Rust, Julia, Solidity и даже APL) с точностью 94% по метрикам HumanEval и MBPP. В тестах модель не только исправляла баги, но и предлагала оптимизации, которые ранее не были известны даже опытным разработчикам. Особенность — «мышление шаг за шагом»: модель объясняет логику перед генерацией кода, что делает её идеальной для обучения и аудита. Внедрение уже началось в GitHub Copilot Enterprise и GitLab AI Assistant. Учёные предупреждают: это не просто инструмент — это новый уровень сотрудничества между человеком и ИИ в разработке ПО.",
                published = "22 мая в 11:15",
                likedByMe = true,
                likes = 4890,
                shares = 1320,
                views = 67200,
                video = "https://rutube.ru/video/18b9690f8d298ad54e73a8af0d5fa48c/"
            ),
            Post(
                id = 7,
                author = "Apple Developers",
                authorId = 8,
                content = "SwiftUI 5.0, представленный на WWDC 2024, меняет правила игры для iOS и Vision Pro-разработки. Теперь вы можете создавать полноценные 3D-интерфейсы с поддержкой реалистичного освещения, теней и физического взаимодействия — всё на чистом SwiftUI без Metal или SceneKit. Добавлены новые модификаторы: `.threeDTransform()`, `.spatialInteraction()`, `.depthEffect()` — и интеграция с Vision Pro достигла уровня «невозможно отличить от реальности». Также появилась поддержка динамических цветовых тем по времени суток и автоматическая адаптация под аномалии зрения (цветовая слепота, дальтонизм). Xcode 15.4 теперь включает встроенный симулятор Vision Pro с трекингом глаз и жестов. Это не обновление — это новый стандарт интерфейсов.",
                published = "19 мая в 17:20",
                likedByMe = false,
                likes = 3210,
                shares = 945,
                views = 38500,
                video = "https://rutube.ru/video/1e2d7c8b13a2f4bacefec4d597a47c54/"
            ),
            Post(
                id = 8,
                author = "DevOps Today",
                authorId = 9,
                content = "Docker представил новый режим «Docker Buildx — Daemonless» — революцию в CI/CD. Теперь вы можете собирать образы Docker без запущенного демона, используя только контейнеризированный buildkit-агент. Это означает: меньше прав, меньше уязвимостей, меньше зависимостей — идеально для серверов без Docker, в Kubernetes-пайплайнах и даже в GitHub Actions без привилегий. Поддержка multi-platform сборок улучшена до 17 архитектур, включая RISC-V и ARM64 Mac. Также добавлены автоматические кэширования по хешам исходников и предсказуемое удаление временных файлов. DevOps-инженеры называют это «самым важным обновлением за последние 5 лет» — теперь сборка образов стала такой же надёжной, как тестирование.",
                published = "20 мая в 08:55",
                likedByMe = true,
                likes = 1876,
                shares = 512,
                views = 22300,
                video = null,
            ),
            Post(
                id = 9,
                author = "Neural Networks Weekly",
                authorId = 10,
                content = "Google DeepMind представила Mamba-X — новую архитектуру нейросети, которая заменяет трансформеры в задачах последовательного моделирования. В отличие от Transformer, Mamba-X использует State Space Models (SSMs) с линейной сложностью O(N), а не O(N²). Это позволяет обрабатывать последовательности длиной до 1 млн токенов на одном GPU, в 5 раз быстрее и с в 5 раз меньшим потреблением памяти. В тестах на коде, музыке и геномах Mamba-X превзошла Llama 3 и GPT-4 по качеству и эффективности. Уже запущена open-source версия на Hugging Face. Это может стать концом эры трансформеров — и началом эры эффективных, масштабируемых и экологичных ИИ-моделей.",
                published = "24 мая в 10:03",
                likedByMe = false,
                likes = 6789,
                shares = 2011,
                views = 89500,
                video = "https://rutube.ru/video/af22ea10424506d17d00fd15ab79804f/"
            ),
            Post(
                id = 10,
                author = "WebDev Digest",
                authorId = 11,
                content = "Все современные браузеры — Chrome 125, Firefox 126, Safari 17.5 и Edge 125 — теперь полностью поддерживают CSS Container Queries. Это значит: вы можете стилизовать компоненты не по ширине экрана, а по ширине их родительского контейнера. Например: кнопка может менять размер в зависимости от ширины карточки, а не всего экрана. Это фундаментальное изменение в адаптивном дизайне — теперь компоненты становятся по-настоящему автономными и переиспользуемыми. Уже появляются библиотеки типа 'Container-First' и инструменты для автоматической генерации медиазапросов на основе контейнеров. Веб-разработка больше не будет «реактивной» — она станет «контейнерно-ориентированной».",
                published = "18 мая в 16:40",
                likedByMe = true,
                likes = 2905,
                shares = 780,
                views = 41200,
                video = null
            ),
            Post(
                id = 11,
                author = "Blockchain Insider",
                authorId = 12,
                content = "Ethereum Layer-2 решения, включая zkSync Era, Polygon zkEVM и Arbitrum Nova, достигли рекордной пропускной способности — 120 000 транзакций в секунду (TPS) при стоимости 0.0001$ за транзакцию. Это в 600 раз быстрее Ethereum Mainnet и дешевле, чем PayPal. Достижение стало возможным благодаря новому алгоритму ZK-STARKs с оптимизацией по памяти и улучшенному компрессору данных. Теперь DeFi-приложения могут работать в реальном времени: биржи, NFT-маркетплейсы и игровые платформы больше не сталкиваются с лагами. Согласно последним данным, более 70% всех транзакций в Ethereum-экосистеме уже проходят через Layer-2. Эпоха дорогих и медленных блокчейнов окончена.",
                published = "25 мая в 07:10",
                likedByMe = false,
                likes = 1543,
                shares = 390,
                views = 19800,
                video = "https://rutube.ru/channel/38517002/"
            ),
            Post(
                id = 12,
                author = "Data Science Hub",
                authorId = 13,
                content = "Pandas 2.2 теперь использует Apache Arrow в качестве стандартного бэкенда для хранения данных — вместо устаревшего NumPy. Это означает: обработка таблиц с миллиардами строк стала в 40% быстрее, потребление памяти снизилось на 30%, а совместимость с Spark, Dask и Polars улучшилась до уровня «перетаскивай и работай». Также добавлена поддержка nullable типов, встроенные методы для работы с временными рядами с пропусками и новая функция `.query()` с SQL-подобным синтаксисом. Для аналитиков — это как получить новый суперкомпьютер в виде одной строки импорта: `import pandas as pd`. Больше никаких «MemoryError» на ноутбуке — только скорость и надёжность.",
                published = "21 мая в 13:22",
                likedByMe = true,
                likes = 2345,
                shares = 610,
                views = 32100,
                video = "https://rutube.ru/video/d313e06d2ce1527871b153fdb49c0637/",
            ),
            Post(
                id = 13,
                author = "Quantum Computing Now",
                authorId = 14,
                content = "IBM представила процессор Condor с 1121 кубитом — новый мировой рекорд по масштабированию квантовых вычислений. В отличие от предыдущих чипов, Condor использует топологические кубиты с низким уровнем декогеренции и систему автоматической коррекции ошибок на уровне чипа. Это позволяет выполнять вычисления длительностью до 1000 тактов — в 10 раз дольше, чем у предшественника. Уже запущены первые эксперименты: моделирование молекул лекарств, оптимизация логистических цепочек и решение NP-полных задач за минуты вместо лет. IBM открыла доступ к Condor через IBM Quantum Cloud — любой разработчик может запустить квантовый алгоритм прямо сейчас. Квантовое превосходство больше не теория — это инфраструктура.",
                published = "23 мая в 15:50",
                likedByMe = false,
                likes = 4021,
                shares = 888,
                views = 56700,
                video = "https://rutube.ru/video/8465fcf2649ba9e90162e734911fb6c5/"
            )
        )
        _data.value = posts
    }


    private fun generateNextId(): Long = nextId++

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("d MMM в HH:mm", Locale("ru"))
        return format.format(date)
    }
}
