package com.example.data.local

import com.example.data.model.Book
import com.example.data.model.BookPage
import com.example.data.model.ChildProfile
import com.example.data.model.ParentReward
import com.example.data.model.QuestionType
import com.example.data.model.QuizQuestion
import com.example.data.model.RevisionItem
import com.example.data.model.ScreenTimeConfig
import com.example.data.model.VocabularyWord
import com.example.data.model.VoiceRecording

object PreloadedData {

    fun initialChildren(): List<ChildProfile> = listOf(
        ChildProfile(
            id = 1,
            name = "Leo",
            avatarKey = "fox",
            age = 7,
            gradeLevel = "Grade 2",
            dailyGoalMinutes = 20,
            totalStars = 140,
            currentStreakDays = 4,
            unlockedBadgesJson = "[\"first_book\", \"streak_hero\", \"star_reader\"]",
            unlockedAvatarsJson = "[\"fox\", \"bear\", \"owl\"]"
        ),
        ChildProfile(
            id = 2,
            name = "Maya",
            avatarKey = "owl",
            age = 9,
            gradeLevel = "Grade 4",
            dailyGoalMinutes = 30,
            totalStars = 280,
            currentStreakDays = 7,
            unlockedBadgesJson = "[\"first_book\", \"quiz_ace\", \"streak_hero\", \"science_whiz\"]",
            unlockedAvatarsJson = "[\"owl\", \"dino\", \"star\", \"fox\"]"
        ),
        ChildProfile(
            id = 3,
            name = "Sam",
            avatarKey = "bear",
            age = 5,
            gradeLevel = "Kindergarten",
            dailyGoalMinutes = 15,
            totalStars = 75,
            currentStreakDays = 2,
            unlockedBadgesJson = "[\"first_book\"]",
            unlockedAvatarsJson = "[\"bear\", \"bunny\"]"
        )
    )

    fun initialScreenTimeConfigs(): List<ScreenTimeConfig> = listOf(
        ScreenTimeConfig(childId = 1, dailyLimitMinutes = 40, maxSessionMinutes = 20, breakMinutes = 5),
        ScreenTimeConfig(childId = 2, dailyLimitMinutes = 50, maxSessionMinutes = 25, breakMinutes = 5),
        ScreenTimeConfig(childId = 3, dailyLimitMinutes = 25, maxSessionMinutes = 15, breakMinutes = 5)
    )

    fun initialRewards(): List<ParentReward> = listOf(
        ParentReward(id = 1, title = "Delicious Ice Cream Cone", description = "Choose your favorite flavor at the park parlor!", starsCost = 80, iconKey = "icecream"),
        ParentReward(id = 2, title = "30 Mins Extra Free Play", description = "Bonus game or playground time on the weekend", starsCost = 120, iconKey = "game"),
        ParentReward(id = 3, title = "Family Movie Night Choice", description = "You pick the movie and popcorn flavor this Friday!", starsCost = 150, iconKey = "movie"),
        ParentReward(id = 4, title = "Trip to the Science Zoo", description = "Fun weekend adventure to meet wild animals and dinosaurs", starsCost = 300, iconKey = "park")
    )

    fun initialBooks(): List<Book> = listOf(
        Book(
            id = 1,
            title = "The Brave Little Fox & The Star Compass",
            author = "Elena Wood",
            subject = "Story & Adventure",
            ageRange = "6-10 yrs",
            description = "Follow Felix the curious fox as he discovers a glowing star compass that leads him through the Whispering Woods to rescue lost forest friends.",
            coverGradientStartHex = "#4F46E5",
            coverGradientEndHex = "#2563EB",
            iconName = "explore",
            isPreloaded = true,
            isDownloaded = true,
            assignedChildIdsJson = "[]",
            totalPages = 4,
            language = "en"
        ),
        Book(
            id = 2,
            title = "Mystery of the Solar System",
            author = "Dr. Neil Adams",
            subject = "Science & Space",
            ageRange = "7-12 yrs",
            description = "Blast off in a cosmic rocket to explore the blazing Sun, the red dunes of Mars, the icy rings of Saturn, and mysterious comets.",
            coverGradientStartHex = "#7C3AED",
            coverGradientEndHex = "#4338CA",
            iconName = "public",
            isPreloaded = true,
            isDownloaded = true,
            assignedChildIdsJson = "[]",
            totalPages = 4,
            language = "en"
        ),
        Book(
            id = 3,
            title = "আনন্দপাঠ: চাঁদের বুড়ি ও নীল পাখি",
            author = "রবীন্দ্রনাথ ঠাকুর ও লোকগাথা",
            subject = "Bangla Learning",
            ageRange = "5-10 yrs",
            description = "চাঁদের বুড়ির রূপকথার চরকা, নীল পাখির রঙিন ডানা আর রাতের আকাশের চমৎকার বাংলা গল্প ও ছড়া।",
            coverGradientStartHex = "#059669",
            coverGradientEndHex = "#0284C7",
            iconName = "menu_book",
            isPreloaded = true,
            isDownloaded = true,
            assignedChildIdsJson = "[]",
            totalPages = 3,
            language = "bn"
        ),
        Book(
            id = 4,
            title = "The Magic of Numbers & Shapes",
            author = "Sarah Jenkins",
            subject = "Math Adventures",
            ageRange = "5-9 yrs",
            description = "Join Tommy the Triangle and Circe the Circle on a colorful quest to rebuild the Grand Geometric Castle using numbers and patterns.",
            coverGradientStartHex = "#D97706",
            coverGradientEndHex = "#DC2626",
            iconName = "category",
            isPreloaded = true,
            isDownloaded = true,
            assignedChildIdsJson = "[]",
            totalPages = 3,
            language = "en"
        )
    )

    fun initialPages(): List<BookPage> = listOf(
        // Book 1: The Brave Little Fox
        BookPage(
            id = 1,
            bookId = 1,
            pageNumber = 1,
            chapterTitle = "Chapter 1: The Glowing Spark",
            textContent = "Deep inside the Emerald Forest, a little orange fox named Felix loved watching the night sky. One chilly evening, a tiny golden meteor zipped between the pine needles. It landed softly on a bed of green moss. When Felix crept closer, he found a shiny bronze compass with a glowing starlight needle that spun with magical warmth.",
            illustrationKey = "forest_star",
            keyVocabularyJson = "[\"meteor\", \"compass\", \"bronze\", \"curious\"]"
        ),
        BookPage(
            id = 2,
            bookId = 1,
            pageNumber = 2,
            chapterTitle = "Chapter 1: The Glowing Spark",
            textContent = "The needle did not point north. Instead, it pointed directly toward the Whispering Pines! Felix heard a faint rustling sound in the shadows. 'Who is there?' called Felix bravely. Out hopped Barnaby the bunny, shivering with worry. 'Felix! The baby hedgehogs lost their way back to Sunflower Glen before sundown!'",
            illustrationKey = "rabbit_friend",
            keyVocabularyJson = "[\"whispering\", \"rustling\", \"hedgehogs\", \"sundown\"]"
        ),
        BookPage(
            id = 3,
            bookId = 1,
            pageNumber = 3,
            chapterTitle = "Chapter 2: The Whispering Path",
            textContent = "Felix held the star compass high in the air. Its golden beam cut through the thick twilight mist like a lighthouse beam. Step by step, Felix and Barnaby followed the radiant glow over sparkling pebble streams and around gentle slumbering deer. The compass hummed a soft, reassuring melody that banished all fear.",
            illustrationKey = "glowing_path",
            keyVocabularyJson = "[\"twilight\", \"lighthouse\", \"radiant\", \"melody\"]"
        ),
        BookPage(
            id = 4,
            bookId = 1,
            pageNumber = 4,
            chapterTitle = "Chapter 2: The Whispering Path",
            textContent = "Beside a cozy hollow oak tree, they heard joyful giggles! The hedgehog family was safely gathered around warm berry tea. 'You brought us together!' cheered Barnaby. Felix smiled proudly. True courage was not about having no fear—it was about following kindness wherever the starlight leads.",
            illustrationKey = "happy_reunion",
            keyVocabularyJson = "[\"courage\", \"hollow\", \"kindness\", \"reunion\"]"
        ),

        // Book 2: Mystery of the Solar System
        BookPage(
            id = 5,
            bookId = 2,
            pageNumber = 1,
            chapterTitle = "Chapter 1: The Blazing Heart",
            textContent = "Our cosmic neighborhood begins with the Sun. The Sun is a colossal glowing sphere of super-hot plasma that anchors eight planets with its powerful gravitational pull. Over one million Earths could fit inside the Sun! Its warmth and golden sunlight give energy to every plant, animal, and ocean on our home planet.",
            illustrationKey = "space_sun",
            keyVocabularyJson = "[\"cosmic\", \"plasma\", \"gravity\", \"colossal\"]"
        ),
        BookPage(
            id = 6,
            bookId = 2,
            pageNumber = 2,
            chapterTitle = "Chapter 1: The Blazing Heart",
            textContent = "The first four planets are rocky worlds. Tiny Mercury speeds around the Sun fastest of all. Venus is wrapped in scorching acid clouds hotter than an oven. Earth is our vibrant oasis filled with liquid blue water and breathable air. And Mars, the Red Planet, features enormous ancient volcanoes and dried riverbeds.",
            illustrationKey = "inner_planets",
            keyVocabularyJson = "[\"oasis\", \"scorching\", \"volcanoes\", \"mercury\"]"
        ),
        BookPage(
            id = 7,
            bookId = 2,
            pageNumber = 3,
            chapterTitle = "Chapter 2: The Gas Giants",
            textContent = "Beyond the rocky asteroid belt lie the mammoth gas giants. Jupiter is the king of planets, with its swirling Great Red Spot storm that has raged for centuries. Saturn is celebrated as the crown jewel of space, encircled by thousands of sparkling rings crafted from billions of shimmering ice crystals and rock fragments.",
            illustrationKey = "gas_giants",
            keyVocabularyJson = "[\"asteroid\", \"mammoth\", \"cyclone\", \"crystals\"]"
        ),
        BookPage(
            id = 8,
            bookId = 2,
            pageNumber = 4,
            chapterTitle = "Chapter 2: The Gas Giants",
            textContent = "At the chilly edges of our solar system spin Uranus and Neptune, the icy blue giants. Neptune whips up the fiercest sonic winds in the planetary family. Beyond them lies the mysterious Kuiper Belt where dwarf planets like Pluto quietly drift in frozen starlight, waiting for future astronauts like you to explore!",
            illustrationKey = "outer_space",
            keyVocabularyJson = "[\"astronaut\", \"kuiper\", \"fiercest\", \"frozen\"]"
        ),

        // Book 3: Bangla Learning: চাঁদের বুড়ি ও নীল পাখি
        BookPage(
            id = 9,
            bookId = 3,
            pageNumber = 1,
            chapterTitle = "অধ্যায় ১: রুপোলি চাঁদ ও বুড়ির চরকা",
            textContent = "মেঘের দেশে এক বিশাল চাঁদের পাহাড় ছিল। সেখানে বাস করত এক মায়াবী চাঁদের বুড়ি। বুড়ি প্রতিদিন রুপোলি সুতো দিয়ে মেঘের রঙিন চাদর বুনত। যখন আকাশে তারা জ্বলত, তখন চাঁদের আলোয় পৃথিবী হাসত। চাঁদের বুড়ি গাইত মিষ্টি গান: 'চাঁদ উঠেছে ফুল ফুটেছে, কদম তলায় কে? হাতি নাচছে ঘোড়া নাচছে সোনামণির বে!'",
            illustrationKey = "bangla_moon",
            keyVocabularyJson = "[\"চাঁদ\", \"চরকা\", \"রুপোলি\", \"সুতো\"]"
        ),
        BookPage(
            id = 10,
            bookId = 3,
            pageNumber = 2,
            chapterTitle = "অধ্যায় ১: রুপোলি চাঁদ ও বুড়ির চরকা",
            textContent = "একদিন একটি ছোট্ট নীল পাখি উড়ে এল চাঁদের দ্বীপে। নীল পাখির ডানা থেকে ঝরে পড়ত রামধনুর সাতটি রঙ। পাখিটি মিষ্টি সুরে বলল, 'বুড়িমা, পৃথিবীর সোনামণিরা বই পড়তে খুব ভালোবাসে। তাদের জন্য তোমার চাঁদের রূপকথা পাঠাও।' বুড়িমা খুশি হয়ে নীল পাখির ডানায় একটি সোনালী তারা বেঁধে দিলেন।",
            illustrationKey = "bangla_bird",
            keyVocabularyJson = "[\"রামধনু\", \"রঙিন\", \"রূপকথা\", \"সোনালী\"]"
        ),
        BookPage(
            id = 11,
            bookId = 3,
            pageNumber = 3,
            chapterTitle = "অধ্যায় ২: রাতের মিষ্টি স্বপ্ন",
            textContent = "নীল পাখি সোনালী তারা নিয়ে ডানা মেলে দিল পৃথিবীর দিকে। যেখানেই শিশুরা মনোযোগ দিয়ে বই পড়ছিল, সেখানেই তারাটি ছড়াতে লাগল মিষ্টি জ্ঞানের আলো। পড়ার আনন্দ হলো সবচেয়ে সুন্দর জাদুকরী শক্তি। শিশুরা হাসিমুখ নিয়ে ঘুমিয়ে পড়ল নতুন দিনের স্বপ্ন দেখতে।",
            illustrationKey = "bangla_dream",
            keyVocabularyJson = "[\"মনোযোগ\", \"জ্ঞান\", \"আনন্দ\", \"স্বপ্ন\"]"
        ),

        // Book 4: The Magic of Numbers & Shapes
        BookPage(
            id = 12,
            bookId = 4,
            pageNumber = 1,
            chapterTitle = "Chapter 1: The Shape Kingdom",
            textContent = "Welcome to Polygonia, the magical kingdom of shapes! Here, Sammy the Square had four equal sides and four sharp corners. He met Tommy the Triangle, who was proud of having exactly three sides. 'Together, we can build anything!' yelled Tommy. Sammy knelt down to make a sturdy wall, and Tommy hopped on top to become a pointed rooftop!",
            illustrationKey = "shapes_friends",
            keyVocabularyJson = "[\"polygon\", \"triangle\", \"square\", \"sturdy\"]"
        ),
        BookPage(
            id = 13,
            bookId = 4,
            pageNumber = 2,
            chapterTitle = "Chapter 1: The Shape Kingdom",
            textContent = "Next rolled in Penny the Circle. Penny had no corners at all—she was completely round and smooth! Penny loved rolling smoothly across meadows. 'Can I help build too?' asked Penny. 'Yes!' cheered Sammy and Tommy. Penny placed herself above the door as a sunbeam window, and two smaller circles became wheels for a wagon.",
            illustrationKey = "circle_wheel",
            keyVocabularyJson = "[\"circle\", \"smooth\", \"sunbeam\", \"wheel\"]"
        ),
        BookPage(
            id = 14,
            bookId = 4,
            pageNumber = 3,
            chapterTitle = "Chapter 2: Counting Patterns",
            textContent = "The king of Polygonia announced a grand puzzle: 'What comes next in the pattern: Circle, Square, Triangle, Circle, Square...?' Children all over the kingdom cheered together: 'Triangle!' With numbers and shapes working as a team, every puzzle has an answer, and math becomes the most exciting adventure of all!",
            illustrationKey = "math_puzzle",
            keyVocabularyJson = "[\"pattern\", \"puzzle\", \"sequence\", \"geometry\"]"
        )
    )

    fun initialQuestions(): List<QuizQuestion> = listOf(
        // Book 1 Questions
        QuizQuestion(
            id = 1,
            bookId = 1,
            chapterTitle = "Chapter 1: The Glowing Spark",
            questionText = "What magical object did Felix the fox discover on the moss?",
            questionType = QuestionType.MCQ,
            optionsJson = "[\"A bronze star compass\", \"A gold watch\", \"A shiny flashlight\", \"A magic wand\"]",
            correctAnswer = "A bronze star compass",
            explanation = "Felix found a shiny bronze compass with a glowing starlight needle on a bed of green moss.",
            difficulty = "Easy"
        ),
        QuizQuestion(
            id = 2,
            bookId = 1,
            chapterTitle = "Chapter 1: The Glowing Spark",
            questionText = "True or False: The starlight needle pointed north like a normal compass.",
            questionType = QuestionType.TRUE_FALSE,
            optionsJson = "[\"True\", \"False\"]",
            correctAnswer = "False",
            explanation = "The needle pointed directly toward the Whispering Pines to lead the way to friends in need.",
            difficulty = "Easy"
        ),
        QuizQuestion(
            id = 3,
            bookId = 1,
            chapterTitle = "Chapter 2: The Whispering Path",
            questionText = "Whom did Felix and Barnaby rescue near the hollow oak tree?",
            questionType = QuestionType.MCQ,
            optionsJson = "[\"The baby hedgehogs\", \"A lost baby bear\", \"A squirrel family\", \"A flock of owls\"]",
            correctAnswer = "The baby hedgehogs",
            explanation = "The baby hedgehog family was safely reunited around warm berry tea beside the oak tree.",
            difficulty = "Medium"
        ),

        // Book 2 Questions (Space)
        QuizQuestion(
            id = 4,
            bookId = 2,
            chapterTitle = "Chapter 1: The Blazing Heart",
            questionText = "Approximately how many Earths could fit inside the Sun?",
            questionType = QuestionType.MCQ,
            optionsJson = "[\"Over 1 Million\", \"10,000\", \"500\", \"100\"]",
            correctAnswer = "Over 1 Million",
            explanation = "The Sun is colossal—more than one million Earths could fit inside its volume!",
            difficulty = "Easy"
        ),
        QuizQuestion(
            id = 5,
            bookId = 2,
            chapterTitle = "Chapter 2: The Gas Giants",
            questionText = "What are the sparkling rings of Saturn primarily composed of?",
            questionType = QuestionType.MCQ,
            optionsJson = "[\"Ice crystals and rock fragments\", \"Pure gold dust\", \"Hot gas clouds\", \"Alien glass\"]",
            correctAnswer = "Ice crystals and rock fragments",
            explanation = "Saturn's rings are made of billions of shimmering water ice crystals and rock particles.",
            difficulty = "Medium"
        ),
        QuizQuestion(
            id = 6,
            bookId = 2,
            chapterTitle = "Chapter 2: The Gas Giants",
            questionText = "True or False: Neptune experiences the fiercest winds in the solar system.",
            questionType = QuestionType.TRUE_FALSE,
            optionsJson = "[\"True\", \"False\"]",
            correctAnswer = "True",
            explanation = "Neptune has sonic-speed storms with the fastest recorded winds among all planets.",
            difficulty = "Easy"
        ),

        // Book 3 Questions (Bangla)
        QuizQuestion(
            id = 7,
            bookId = 3,
            chapterTitle = "অধ্যায় ১: রুপোলি চাঁদ ও বুড়ির চরকা",
            questionText = "চাঁদের বুড়ি চরকা দিয়ে কী বুনত?",
            questionType = QuestionType.MCQ,
            optionsJson = "[\"রুপোলি সুতো দিয়ে মেঘের চাদর\", \"রেশমি শাড়ি\", \"উলের টুপি\", \"পাটের দড়ি\"]",
            correctAnswer = "রুপোলি সুতো দিয়ে মেঘের চাদর",
            explanation = "চাঁদের বুড়ি প্রতিদিন রুপোলি সুতো দিয়ে মেঘের রঙিন চাদর বুনত।",
            difficulty = "Easy"
        ),
        QuizQuestion(
            id = 8,
            bookId = 3,
            chapterTitle = "অধ্যায় ২: রাতের মিষ্টি স্বপ্ন",
            questionText = "নীল পাখি কোথা থেকে সোনালী তারা নিয়ে এসেছিল?",
            questionType = QuestionType.MCQ,
            optionsJson = "[\"চাঁদের বুড়ির কাছ থেকে\", \"সমুদ্র থেকে\", \"পাহাড়ের চূড়া থেকে\", \"বনের ভেতর থেকে\"]",
            correctAnswer = "চাঁদের বুড়ির কাছ থেকে",
            explanation = "বুড়িমা খুশি হয়ে নীল পাখির ডানায় একটি সোনালী তারা বেঁধে দিয়েছিলেন।",
            difficulty = "Easy"
        ),

        // Book 4 Questions (Math & Shapes)
        QuizQuestion(
            id = 9,
            bookId = 4,
            chapterTitle = "Chapter 1: The Shape Kingdom",
            questionText = "How many sides does Tommy the Triangle have?",
            questionType = QuestionType.MCQ,
            optionsJson = "[\"3\", \"4\", \"5\", \"6\"]",
            correctAnswer = "3",
            explanation = "A triangle has exactly three straight sides and three corners.",
            difficulty = "Easy"
        ),
        QuizQuestion(
            id = 10,
            bookId = 4,
            chapterTitle = "Chapter 2: Counting Patterns",
            questionText = "What shape comes next: Circle, Square, Triangle, Circle, Square...?",
            questionType = QuestionType.MCQ,
            optionsJson = "[\"Triangle\", \"Circle\", \"Star\", \"Square\"]",
            correctAnswer = "Triangle",
            explanation = "The repeating pattern goes: Circle, Square, Triangle!",
            difficulty = "Easy"
        )
    )

    fun initialRevisionItems(): List<RevisionItem> = listOf(
        RevisionItem(
            id = 1,
            childId = 1,
            questionId = 2,
            bookId = 1,
            bookTitle = "The Brave Little Fox & The Star Compass",
            chapterTitle = "Chapter 1: The Glowing Spark",
            questionText = "True or False: The starlight needle pointed north like a normal compass.",
            questionType = QuestionType.TRUE_FALSE,
            optionsJson = "[\"True\", \"False\"]",
            correctAnswer = "False",
            explanation = "The needle pointed directly toward the Whispering Pines to lead the way to friends in need.",
            difficulty = "Easy",
            incorrectCount = 1,
            masteryStreak = 0,
            isMastered = false,
            stage = 1
        ),
        RevisionItem(
            id = 2,
            childId = 2,
            questionId = 5,
            bookId = 2,
            bookTitle = "Mystery of the Solar System",
            chapterTitle = "Chapter 2: The Gas Giants",
            questionText = "What are the sparkling rings of Saturn primarily composed of?",
            questionType = QuestionType.MCQ,
            optionsJson = "[\"Ice crystals and rock fragments\", \"Pure gold dust\", \"Hot gas clouds\", \"Alien glass\"]",
            correctAnswer = "Ice crystals and rock fragments",
            explanation = "Saturn's rings are made of billions of shimmering water ice crystals and rock particles.",
            difficulty = "Medium",
            incorrectCount = 2,
            masteryStreak = 1,
            isMastered = false,
            stage = 1
        ),
        RevisionItem(
            id = 3,
            childId = 2,
            questionId = 4,
            bookId = 2,
            bookTitle = "Mystery of the Solar System",
            chapterTitle = "Chapter 1: The Blazing Heart",
            questionText = "Approximately how many Earths could fit inside the Sun?",
            questionType = QuestionType.MCQ,
            optionsJson = "[\"Over 1 Million\", \"10,000\", \"500\", \"100\"]",
            correctAnswer = "Over 1 Million",
            explanation = "The Sun is colossal—more than one million Earths could fit inside its volume!",
            difficulty = "Easy",
            incorrectCount = 1,
            masteryStreak = 2,
            isMastered = true,
            stage = 4
        )
    )

    fun initialVocabularyWords(): List<VocabularyWord> = listOf(
        VocabularyWord(
            id = 1,
            childId = 1,
            word = "Photosynthesis",
            syllables = "pho · to · syn · the · sis",
            definition = "How green plants use golden sunshine and water to create sweet sugar and fresh oxygen.",
            banglaMeaning = "সালোকসংশ্লেষ (উদ্ভিদের খাদ্য তৈরির প্রক্রিয়া)",
            category = "Science",
            exampleSentence = "Leaves use photosynthesis to feed the tall oak tree.",
            isCollected = true,
            masteryLevel = 2
        ),
        VocabularyWord(
            id = 2,
            childId = 1,
            word = "Chlorophyll",
            syllables = "chlo · ro · phyll",
            definition = "The magical green pigment inside leaves that absorbs bright sunlight like a tiny solar panel.",
            banglaMeaning = "ক্লোরোফিল (পাতার সবুজ রঞ্জক পদার্থ)",
            category = "Science",
            exampleSentence = "Chlorophyll turns the spring forest into a vibrant emerald sea.",
            isCollected = true,
            masteryLevel = 1
        ),
        VocabularyWord(
            id = 3,
            childId = 2,
            word = "Constellation",
            syllables = "con · stel · la · tion",
            definition = "A group of sparkling stars that form an imaginary picture or shape in the night sky.",
            banglaMeaning = "নক্ষত্রমণ্ডল (তারামণ্ডল)",
            category = "Space",
            exampleSentence = "Maya spotted the Big Dipper constellation through her silver telescope.",
            isCollected = true,
            masteryLevel = 3
        ),
        VocabularyWord(
            id = 4,
            childId = 2,
            word = "Oxygen",
            syllables = "ox · y · gen",
            definition = "The invisible, refreshing gas in the air that animals and humans breathe to live and grow strong.",
            banglaMeaning = "অক্সিজেন (প্রাণবায়ু)",
            category = "Nature",
            exampleSentence = "Rainforests produce abundant fresh oxygen for our planet.",
            isCollected = true,
            masteryLevel = 2
        ),
        VocabularyWord(
            id = 5,
            childId = 2,
            word = "Saturn",
            syllables = "Sat · urn",
            definition = "The majestic sixth planet from the Sun, surrounded by shimmering rings of ice crystals.",
            banglaMeaning = "শনি গ্রহ",
            category = "Space",
            exampleSentence = "Saturn's dazzling rings glowed in the astronomy book.",
            isCollected = true,
            masteryLevel = 2
        ),
        VocabularyWord(
            id = 6,
            childId = 1,
            word = "আনন্দ (Anondo)",
            syllables = "আ · ন · ন্দ",
            definition = "A warm feeling of great joy, delight, and happiness in your heart.",
            banglaMeaning = "পরম সুখ ও উল্লাস (Joy & Happiness)",
            category = "Bangla",
            exampleSentence = "Leo felt anondo when he finished reading his first storybook.",
            isCollected = true,
            masteryLevel = 3
        )
    )

    fun initialVoiceRecordings(): List<VoiceRecording> = listOf(
        VoiceRecording(
            id = 1,
            childId = 1,
            bookId = 1,
            bookTitle = "Oliver the Brave Owl",
            pageNumber = 1,
            pageSnippet = "Deep in the Whispering Woods, a little barn owl named Oliver dreamed of touching the starry night sky.",
            audioFilePath = "sample_recording_1.mp3",
            durationSeconds = 12,
            fluencyScore = 96
        ),
        VoiceRecording(
            id = 2,
            childId = 2,
            bookId = 2,
            bookTitle = "Mystery of the Solar System",
            pageNumber = 1,
            pageSnippet = "At the center of our solar neighborhood burns the Sun, an enormous powerhouse of radiant warmth and light.",
            audioFilePath = "sample_recording_2.mp3",
            durationSeconds = 15,
            fluencyScore = 98
        )
    )
}
