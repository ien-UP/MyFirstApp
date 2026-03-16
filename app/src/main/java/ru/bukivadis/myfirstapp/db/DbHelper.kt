package ru.bukivadis.myfirstapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.bukivadis.myfirstapp.db.PostContract.Columns

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "myfirstapp.db"
        private const val DATABASE_VERSION = 1

        // SQL для создания таблицы
        private const val SQL_CREATE_POSTS =
            "CREATE TABLE ${PostContract.TABLE_NAME} (" +
                    "${Columns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${Columns.AUTHOR} TEXT NOT NULL," +
                    "${Columns.AUTHOR_ID} INTEGER NOT NULL," +
                    "${Columns.CONTENT} TEXT NOT NULL," +
                    "${Columns.PUBLISHED} TEXT NOT NULL," +
                    "${Columns.LIKED_BY_ME} INTEGER NOT NULL DEFAULT 0," +
                    "${Columns.LIKES} INTEGER NOT NULL DEFAULT 0," +
                    "${Columns.SHARES} INTEGER NOT NULL DEFAULT 0," +
                    "${Columns.VIEWS} INTEGER NOT NULL DEFAULT 0," +
                    "${Columns.VIDEO} TEXT" +
                    ")"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Создаем таблицу при первом запуске
        db.execSQL(SQL_CREATE_POSTS)

        // Здесь можно добавить начальные данные
        insertInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // При обновлении версии удаляем старую таблицу и создаем новую
        // В реальном проекте здесь должна быть миграция данных
        db.execSQL("DROP TABLE IF EXISTS ${PostContract.TABLE_NAME}")
        onCreate(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        // Вставляем начальные посты для демонстрации
        val contentValues =  ContentValues().apply {
            put(Columns.AUTHOR, "АвтоГрам. Университет интернет‑машинок")
            put(Columns.AUTHOR_ID, 2)
            put(
                Columns.CONTENT,
                "Где автомобили учатся ездить… а люди — не ломать их.\n" +
                        "         1 курс: «Как не врезаться в столб, если ты впервые в жизни сел за руль»\n" +
                        "         2 курс: «Почему твой Tesla думает, что ты — робот»\n" +
                        "         3 курс: «Как объяснить бабушке, что “обновление ПО” — это не вирус»\n" +
                        "         4 курс: «Когда навигатор говорит “Поверните направо”, а справа — стена. Что делать?»\n" +
                        "         5 курс: «Автопилот vs. Кот на пассажирском сиденье» — практическое занятие\n" +
                        "        Мы не учим водить.\n" +
                        "        Мы учим выживать в мире, где машины умнее тебя, но ты всё ещё платишь за бензин."
            )
            put(Columns.PUBLISHED, "22 марта в 19:06")
            put(Columns.LIKED_BY_ME, 0)          // false
            put(Columns.LIKES, 999)
            put(Columns.SHARES, 25)
            put(Columns.VIEWS, 5700)
            putNull(Columns.VIDEO)
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "Космический путешественник")
            put(Columns.AUTHOR_ID, 3)
            put(
                Columns.CONTENT,
                "Учёные NASA и Европейского космического агентства обнаружили экзопланету TOI-700 e — первую в обитаемой зоне своей звезды, чья атмосфера, по данным спектроскопии JWST, содержит следы водяного пара, метана и кислорода. Это не просто «похоже на Землю» — это первый кандидат на потенциально обитаемый мир за пределами нашей Солнечной системы. Уже запланирована миссия «Aurora Probe» на 2035 год: зонд с лазерным спектрометром и микроскопом для поиска биомаркеров. Мы можем быть на пороге первого подтверждения жизни вне Земли."
            )
            put(Columns.PUBLISHED, "22 мая в 10:15")
            put(Columns.LIKED_BY_ME, 0)
            put(Columns.LIKES, 342)
            put(Columns.SHARES, 89)
            put(Columns.VIEWS, 2300)
            put(Columns.VIDEO, "https://rutube.ru/video/2854a399c9fc11e7e1793ea37f99caf2/")
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "Python Daily")
            put(Columns.AUTHOR_ID, 4)
            put(
                Columns.CONTENT,
                "Python 3.12 официально выпущен — и это не просто обновление, а прорыв в удобстве и производительности. Теперь вы можете использовать PEP 634 (структурное сопоставление с образцами) с вложенными кортежами и словарями, PEP 684 — улучшенные аннотации типов с поддержкой TypeVarTuple, а также PEP 701 — синтаксис f‑строк с поддержкой двойных фигурных скобок внутри f‑строк. Особенно впечатляет: компилятор теперь оптимизирует циклы на уровне байт‑кода, что даёт до 15% прироста скорости в вычислительно нагруженных задачах. Для Data Science — поддержка NumPy 2.0 без преобразований. Обновляйтесь — и не забудьте проверить совместимость ваших зависимостей!"
            )
            put(Columns.PUBLISHED, "23 мая в 09:42")
            put(Columns.LIKED_BY_ME, 1)          // true
            put(Columns.LIKES, 1250)
            put(Columns.SHARES, 420)
            put(Columns.VIEWS, 8900)
            put(Columns.VIDEO, "https://rutube.ru/video/d3cf75b21716fee636f947f2bf36ce54/")
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "Microsoft Build")
            put(Columns.AUTHOR_ID, 5)
            put(
                Columns.CONTENT,
                "На конференции Microsoft Build 2024 представлены ключевые инструменты для будущего разработки: .NET 8 с поддержкой Native AOT для веб‑приложений, Visual Studio 2024 с AI‑ассистентом, который пишет код, пишет тесты и даже объясняет ошибки на естественном языке, и Azure AI Studio — единая платформа для создания, развертывания и мониторинга ИИ‑моделей без знания Python. Особенно впечатляет: Visual Studio теперь может автоматически переписывать устаревший C# на современный синтаксис, поддерживая миграцию проектов 2010 года в 2024. Azure AI Studio позволяет создавать LLM‑агентов с памятью, планированием и интеграцией в реальные API — всё через графический интерфейс. Это не просто инструменты — это новый стандарт разработки ПО в эпоху ИИ."
            )
            put(Columns.PUBLISHED, "20 мая в 20:00")
            put(Columns.LIKED_BY_ME, 0)
            put(Columns.LIKES, 5678)
            put(Columns.SHARES, 1234)
            put(Columns.VIEWS, 45000)
            put(Columns.VIDEO, "https://rutube.ru/video/7a010e15530a8382cfc7c7b7a82cc1c7/")
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "Rust Lang News")
            put(Columns.AUTHOR_ID, 6)
            put(
                Columns.CONTENT,
                "Rust 1.78 вводит революционную функцию: async/await теперь можно использовать внутри const‑контекста — это означает, что вы можете создавать асинхронные константы, инициализируемые во время компиляции. Например: `const DB_CONNECTION: &AsyncConnection = async { connect_to_db().await }.await;` — теперь это компилируется! Это устраняет необходимость в unsafe‑коде для инициализации глобальных состояний и открывает путь к полностью безопасным, статически инициализированным серверным приложениям. Также добавлена поддержка SIMD‑инструкций для ARMv8.4 и улучшена интеграция с WebAssembly. Rust продолжает становиться языком будущего — не потому что он «быстрый», а потому что он делает невозможное — возможным."
            )
            put(Columns.PUBLISHED, "21 мая в 14:30")
            put(Columns.LIKED_BY_ME, 0)
            put(Columns.LIKES, 2105)
            put(Columns.SHARES, 687)
            put(Columns.VIEWS, 15400)
            put(Columns.VIDEO, "https://rutube.ru/video/f205f1b66abd9f14079bef7cca59e5bb/")
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "TechCrunch AI")
            put(Columns.AUTHOR_ID, 7)
            put(
                Columns.CONTENT,
                "Anthropic анонсировала Claude 3.5 — новую ИИ‑модель, способную генерировать код на 12 языках (включая Rust, Julia, Solidity и даже APL) с точностью 94% по метрикам HumanEval и MBPP. В тестах модель не только исправляла баги, но и предлагала оптимизации, которые ранее не были известны даже опытным разработчикам. Особенность — «мышление шаг за шагом»: модель объясняет логику перед генерацией кода, что делает её идеальной для обучения и аудита. Внедрение уже началось в GitHub Copilot Enterprise и GitLab AI Assistant. Учёные предупреждают: это не просто инструмент — это новый уровень сотрудничества между человеком и ИИ в разработке ПО."
            )
            put(Columns.PUBLISHED, "22 мая в 11:15")
            put(Columns.LIKED_BY_ME, 1)
            put(Columns.LIKES, 4890)
            put(Columns.SHARES, 1320)
            put(Columns.VIEWS, 67200)
            put(Columns.VIDEO, "https://rutube.ru/video/18b9690f8d298ad54e73a8af0d5fa48c/")
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "Apple Developers")
            put(Columns.AUTHOR_ID, 8)
            put(
                Columns.CONTENT,
                "SwiftUI 5.0, представленный на WWDC 2024, меняет правила игры для iOS и Vision Pro‑разработки. Теперь вы можете создавать полноценные 3D‑интерфейсы с поддержкой реалистичного освещения, теней и физического взаимодействия — всё на чистом SwiftUI без Metal или SceneKit. Добавлены новые модификаторы: `.threeDTransform()`, `.spatialInteraction()`, `.depthEffect()` — и интеграция с Vision Pro достигла уровня «невозможно отличить от реальности». Также появилась поддержка динамических цветовых тем по времени суток и автоматическая адаптация под аномалии зрения (цветовая слепота, дальтонизм). Xcode 15.4 теперь включает встроенный симулятор Vision Pro с трекингом глаз и жестов. Это не обновление — это новый стандарт интерфейсов."
            )
            put(Columns.PUBLISHED, "19 мая в 17:20")
            put(Columns.LIKED_BY_ME, 0)
            put(Columns.LIKES, 3210)
            put(Columns.SHARES, 945)
            put(Columns.VIEWS, 38500)
            put(Columns.VIDEO, "https://rutube.ru/video/1e2d7c8b13a2f4bacefec4d597a47c54/")
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "DevOps Today")
            put(Columns.AUTHOR_ID, 9)
            put(
                Columns.CONTENT,
                "Docker представил новый режим «Docker Buildx — Daemonless» — революцию в CI/CD. Теперь вы можете собирать образы Docker без запущенного демона, используя только контейнеризированный buildkit‑агент. Это означает: меньше прав, меньше уязвимостей, меньше зависимостей — идеально для серверов без Docker, в Kubernetes‑пайплайнах и даже в GitHub Actions без привилегий. Поддержка multi‑platform сборок улучшена до 17 архитектур, включая RISC‑V и ARM64 Mac. Также добавлены автоматические кэширования по хешам исходников и предсказуемое удаление временных файлов. DevOps‑инженеры называют это «самым важным обновлением за последние 5 лет» — теперь сборка образов стала такой же надёжной, как тестирование."
            )
            put(Columns.PUBLISHED, "20 мая в 08:55")
            put(Columns.LIKED_BY_ME, 1)
            put(Columns.LIKES, 1876)
            put(Columns.SHARES, 512)
            put(Columns.VIEWS, 22300)
            putNull(Columns.VIDEO)
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "Neural Networks Weekly")
            put(Columns.AUTHOR_ID, 10)
            put(
                Columns.CONTENT,
                "Google DeepMind представила Mamba‑X — новую архитектуру нейросети, которая заменяет трансформеры в задачах последовательного моделирования. В отличие от Transformer, Mamba‑X использует State Space Models (SSMs) с линейной сложностью O(N), а не O(N²). Это позволяет обрабатывать последовательности длиной до 1 млн токенов на одном GPU, в 5 раз быстрее и в 5 раз меньше потребляя память. В тестах на коде, музыке и геномах Mamba‑X превзошла Llama 3 и GPT‑4 по качеству и эффективности. Уже запущена open‑source версия на Hugging Face. Это может стать концом эры трансформеров — и началом эры эффективных, масштабируемых и экологичных ИИ‑моделей."
            )
            put(Columns.PUBLISHED, "24 мая в 10:03")
            put(Columns.LIKED_BY_ME, 0)
            put(Columns.LIKES, 6789)
            put(Columns.SHARES, 2011)
            put(Columns.VIEWS, 89500)
            put(Columns.VIDEO, "https://rutube.ru/video/af22ea10424506d17d00fd15ab79804f/")
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "WebDev Digest")
            put(Columns.AUTHOR_ID, 11)
            put(
                Columns.CONTENT,
                "Все современные браузеры — Chrome 125, Firefox 126, Safari 17.5 и Edge 125 — теперь полностью поддерживают CSS Container Queries. Это значит: вы можете стилизовать компоненты не по ширине экрана, а по ширине их родительского контейнера. Например: кнопка может менять размер в зависимости от ширины карточки, а не всего экрана. Это фундаментальное изменение в адаптивном дизайне — теперь компоненты становятся по‑настоящему автономными и переиспользуемыми. Уже появляются библиотеки типа 'Container‑First' и инструменты для автоматической генерации медиазапросов на основе контейнеров. Веб‑разработка больше не будет «реактивной» — она станет «контейнерно‑ориентированной»."
            )
            put(Columns.PUBLISHED, "18 мая в 16:40")
            put(Columns.LIKED_BY_ME, 1)
            put(Columns.LIKES, 2905)
            put(Columns.SHARES, 780)
            put(Columns.VIEWS, 41200)
            putNull(Columns.VIDEO)
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "Blockchain Insider")
            put(Columns.AUTHOR_ID, 12)
            put(
                Columns.CONTENT,
                "Ethereum Layer‑2 решения, включая zkSync Era, Polygon zkEVM и Arbitrum Nova, достигли рекордной пропускной способности — 120 000 транзакций в секунду (TPS) при стоимости 0.0001 $ за транзакцию. Это в 600 раз быстрее Ethereum Mainnet и дешевле, чем PayPal. Достижение стало возможным благодаря новому алгоритму ZK‑STARKs с оптимизацией по памяти и улучшенному компрессору данных. Теперь DeFi‑приложения могут работать в реальном времени: биржи, NFT‑маркетплейсы и игровые платформы больше не сталкиваются с лагами. Согласно последним данным, более 70 % всех транзакций в Ethereum‑экосистеме уже проходят через Layer‑2. Эпоха дорогих и медленных блокчейнов окончена."
            )
            put(Columns.PUBLISHED, "25 мая в 07:10")
            put(Columns.LIKED_BY_ME, 0)
            put(Columns.LIKES, 1543)
            put(Columns.SHARES, 390)
            put(Columns.VIEWS, 19800)
            put(Columns.VIDEO, "https://rutube.ru/channel/38517002/")
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "Data Science Hub")
            put(Columns.AUTHOR_ID, 13)
            put(
                Columns.CONTENT,
                "Pandas 2.2 теперь использует Apache Arrow в качестве стандартного бэкенда для хранения данных — вместо устаревшего NumPy. Это означает: обработка таблиц с миллиардами строк стала в 40 % быстрее, потребление памяти снизилось на 30 %, а совместимость с Spark, Dask и Polars улучшилась до уровня «перетаскивай и работай». Также добавлена поддержка nullable типов, встроенные методы для работы с временными рядами с пропусками и новая функция `.query()` с SQL‑подобным синтаксисом. Для аналитиков — это как получить новый суперкомпьютер в виде одной строки импорта: `import pandas as pd`. Больше никаких «MemoryError» на ноутбуке — только скорость и надёжность."
            )
            put(Columns.PUBLISHED, "21 мая в 13:22")
            put(Columns.LIKED_BY_ME, 1)
            put(Columns.LIKES, 2345)
            put(Columns.SHARES, 610)
            put(Columns.VIEWS, 32100)
            put(Columns.VIDEO, "https://rutube.ru/video/d313e06d2ce1527871b153fdb49c0637/")
            db.insert(PostContract.TABLE_NAME, null, this)
        }

        ContentValues().apply {
            put(Columns.AUTHOR, "Quantum Computing Now")
            put(Columns.AUTHOR_ID, 14)
            put(
                Columns.CONTENT,
                "IBM представила процессор Condor с 1121 кубитом — новый мировой рекорд по масштабированию квантовых вычислений. В отличие от предыдущих чипов, Condor использует топологические кубиты с низким уровнем декогеренции и систему автоматической коррекции ошибок на уровне чипа. Это позволяет выполнять вычисления длительностью до 1000 тактов — в 10 раз дольше, чем у предшественника. Уже запущены первые эксперименты: моделирование молекул лекарств, оптимизация логистических цепочек и решение NP‑полных задач за минуты вместо лет. IBM открыла доступ к Condor через IBM Quantum Cloud — любой разработчик может запустить квантовый алгоритм прямо сейчас. Квантовое превосходство больше не теория — это инфраструктура."
            )
            put(Columns.PUBLISHED, "23 мая в 15:50")
            put(Columns.LIKED_BY_ME, 0)
            put(Columns.LIKES, 4021)
            put(Columns.SHARES, 888)
            put(Columns.VIEWS, 56700)
            put(Columns.VIDEO, "https://rutube.ru/video/8465fcf2649ba9e90162e734911fb6c5/")
            db.insert(PostContract.TABLE_NAME, null, this)
        }
    }
}
