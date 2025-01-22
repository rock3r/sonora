package dev.sebastiano.sonora.model

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility

/**
 * Supported category types encountered within the `<itunes:category>`
 * element within a `<channel>` element, modeled as a finite set sealed
 * class.
 *
 * Defined category values are listed in
 * [Apple Podcasts Categories](https://help.apple.com/itc/podcasts_connect/#/itc9267a2f12).
 * The categories and their nested hierarchies are modeled according to the
 * table below. This classes [companion object][Factory] exposed a
 * reference for each instance.
 *
 * | Category                | Subcategory        | Property                    |
 * |-------------------------|--------------------|-----------------------------|
 * | Arts                    | –                  | [ARTS]                      |
 * | Arts                    | Books              | [BOOKS]                     |
 * | Arts                    | Design             | [DESIGN]                    |
 * | Arts                    | Fashion & Beauty   | [FASHION_AND_BEAUTY]        |
 * | Arts                    | Food               | [FOOD]                      |
 * | Arts                    | Performing Arts    | [PERFORMING_ARTS]           |
 * | Arts                    | Visual Arts        | [VISUAL_ARTS]               |
 * | Business                | –                  | [BUSINESS]                  |
 * | Business                | Careers            | [CAREERS]                   |
 * | Business                | Entrepreneurship   | [ENTREPRENEURSHIP]          |
 * | Business                | Investing          | [INVESTING]                 |
 * | Business                | Management         | [MANAGEMENT]                |
 * | Business                | Marketing          | [MARKETING]                 |
 * | Business                | Non-Profit         | [NON_PROFIT]                |
 * | Comedy                  | –                  | [COMEDY]                    |
 * | Comedy                  | Comedy Interviews  | [COMEDY_INTERVIEWS]         |
 * | Comedy                  | Improv             | [IMPROV]                    |
 * | Comedy                  | Stand-Up           | [STAND_UP]                  |
 * | Education               | –                  | [EDUCATION]                 |
 * | Education               | Courses            | [COURSES]                   |
 * | Education               | How To             | [HOW_TO]                    |
 * | Education               | Language Learning  | [LANGUAGE_LEARNING]         |
 * | Education               | Self-Improvement   | [SELF_IMPROVEMENT]          |
 * | Fiction                 | –                  | [FICTION]                   |
 * | Fiction                 | Comedy Fiction     | [COMEDY_FICTION]            |
 * | Fiction                 | Drama              | [DRAMA]                     |
 * | Fiction                 | Science Fiction    | [SCIENCE_FICTION]           |
 * | Government              | –                  | [GOVERNMENT]                |
 * | History                 | –                  | [HISTORY]                   |
 * | Health & Fitness        | –                  | [HEALTH_AND_FITNESS]        |
 * | Health & Fitness        | Alternative Health | [ALTERNATIVE_HEALTH]        |
 * | Health & Fitness        | Fitness            | [FITNESS]                   |
 * | Health & Fitness        | Medicine           | [MEDICINE]                  |
 * | Health & Fitness        | Mental Health      | [MENTAL_HEALTH]             |
 * | Health & Fitness        | Nutrition          | [NUTRITION]                 |
 * | Health & Fitness        | Sexuality          | [SEXUALITY]                 |
 * | Kids & Family           | –                  | [KIDS_AND_FAMILY]           |
 * | Kids & Family           | Education for Kids | [EDUCATION_FOR_KIDS]        |
 * | Kids & Family           | Parenting          | [PARENTING]                 |
 * | Kids & Family           | Pets & Animals     | [PETS_AND_ANIMALS]          |
 * | Kids & Family           | Stories for Kids   | [STORIES_FOR_KIDS]          |
 * | Leisure                 | –                  | [LEISURE]                   |
 * | Leisure                 | Animation & Manga  | [ANIMATION_AND_MANGA]       |
 * | Leisure                 | Automotive         | [AUTOMOTIVE]                |
 * | Leisure                 | Aviation           | [AVIATION]                  |
 * | Leisure                 | Crafts             | [CRAFTS]                    |
 * | Leisure                 | Games              | [GAMES]                     |
 * | Leisure                 | Hobbies            | [HOBBIES]                   |
 * | Leisure                 | Home & Garden      | [HOME_AND_GARDEN]           |
 * | Leisure                 | Video Games        | [VIDEO_GAMES]               |
 * | Music                   | –                  | [MUSIC]                     |
 * | Music                   | Music Commentary   | [MUSIC_COMMENTARY]          |
 * | Music                   | Music History      | [MUSIC_HISTORY]             |
 * | Music                   | Music Interviews   | [MUSIC_INTERVIEWS]          |
 * | News                    | –                  | [NEWS]                      |
 * | News                    | Business News      | [BUSINESS_NEWS]             |
 * | News                    | Daily News         | [DAILY_NEWS]                |
 * | News                    | Entertainment News | [ENTERTAINMENT_NEWS]        |
 * | News                    | News Commentary    | [NEWS_COMMENTARY]           |
 * | News                    | Politics           | [POLITICS]                  |
 * | News                    | Sports News        | [SPORTS_NEWS]               |
 * | News                    | Tech News          | [TECH_NEWS]                 |
 * | Religion & Spirituality | –                  | [RELIGION_AND_SPIRITUALITY] |
 * | Religion & Spirituality | Buddhism           | [BUDDHISM]                  |
 * | Religion & Spirituality | Christianity       | [CHRISTIANITY]              |
 * | Religion & Spirituality | Hinduism           | [HINDUISM]                  |
 * | Religion & Spirituality | Islam              | [ISLAM]                     |
 * | Religion & Spirituality | Judaism            | [JUDAISM]                   |
 * | Religion & Spirituality | Religion           | [RELIGION]                  |
 * | Religion & Spirituality | Spirituality       | [SPIRITUALITY]              |
 * | Science                 | –                  | [SCIENCE]                   |
 * | Science                 | Astronomy          | [ASTRONOMY]                 |
 * | Science                 | Chemistry          | [CHEMISTRY]                 |
 * | Science                 | Earth Sciences     | [EARTH_SCIENCES]            |
 * | Science                 | Life Sciences      | [LIFE_SCIENCES]             |
 * | Science                 | Mathematics        | [MATHEMATICS]               |
 * | Science                 | Natural Sciences   | [NATURAL_SCIENCES]          |
 * | Science                 | Nature             | [NATURE]                    |
 * | Science                 | Physics            | [PHYSICS]                   |
 * | Science                 | Social Sciences    | [SOCIAL_SCIENCES]           |
 * | Society & Culture       | –                  | [SOCIETY_AND_CULTURE]       |
 * | Society & Culture       | Documentary        | [DOCUMENTARY]               |
 * | Society & Culture       | Personal Journals  | [PERSONAL_JOURNALS]         |
 * | Society & Culture       | Philosophy         | [PHILOSOPHY]                |
 * | Society & Culture       | Places & Travel    | [PLACES_AND_TRAVEL]         |
 * | Society & Culture       | Relationships      | [RELATIONSHIPS]             |
 * | Sports                  | –                  | [SPORTS]                    |
 * | Sports                  | Baseball           | [BASEBALL]                  |
 * | Sports                  | Basketball         | [BASKETBALL]                |
 * | Sports                  | Cricket            | [CRICKET]                   |
 * | Sports                  | Fantasy Sports     | [FANTASY_SPORTS]            |
 * | Sports                  | Football           | [FOOTBALL]                  |
 * | Sports                  | Golf               | [GOLF]                      |
 * | Sports                  | Hockey             | [HOCKEY]                    |
 * | Sports                  | Rugby              | [RUGBY]                     |
 * | Sports                  | Running            | [RUNNING]                   |
 * | Sports                  | Soccer             | [SOCCER]                    |
 * | Sports                  | Swimming           | [SWIMMING]                  |
 * | Sports                  | Tennis             | [TENNIS]                    |
 * | Sports                  | Volleyball         | [VOLLEYBALL]                |
 * | Sports                  | Wilderness         | [WILDERNESS]                |
 * | Sports                  | Wrestling          | [WRESTLING]                 |
 * | Technology              | –                  | [TECHNOLOGY]                |
 * | True Crime              | –                  | [TRUE_CRIME]                |
 * | TV & Film               | –                  | [TV_AND_FILM]               |
 * | TV & Film               | After Shows        | [AFTER_SHOWS]               |
 * | TV & Film               | Film History       | [FILM_HISTORY]              |
 * | TV & Film               | Film Interviews    | [FILM_INTERVIEWS]           |
 * | TV & Film               | Film Reviews       | [FILM_REVIEWS]              |
 * | TV & Film               | TV Reviews         | [TV_REVIEWS]                |
 *
 * Use the [of][ItunesCategory.Factory.of] method to obtain an instance
 * from a string pattern.
 *
 * @property text The raw category `type` value.
 * @see Simple Subtype for simple categories.
 * @see Nested Subtype for categories that are nested in a [Simple]
 *    category.
 * @see Factory Companion object exposing references to all valid instances
 *    and a factory method.
 */
@Serializable
sealed interface ValidItunesCategory {
    val text: String

    /**
     * A simple iTunes-style category, without a nested subcategory:
     * ```
     * <itunes:category text="News" />
     * ```
     *
     * Categories are defined in the
     * [Apple Podcasts Categories](https://help.apple.com/itc/podcasts_connect/#/itc9267a2f12).
     */
    @Serializable data class Simple(override val text: String) : ValidItunesCategory

    /**
     * An iTunes-style subcategory that is contained within a parent [Simple]:
     * ```
     * <itunes:category text="News">
     *     <itunes:category text="Tech News" />
     * </itunes:category>
     * ```
     *
     * Categories and their hierarchy are defined in the
     * [Apple Podcasts Categories](https://help.apple.com/itc/podcasts_connect/#/itc9267a2f12).
     *
     * @param parent The parent [Simple].
     */
    @Serializable data class Nested(
        override val text: String,
        val parent: Simple,
    ) : ValidItunesCategory

    companion object {
        /**
         * Validates whether an ItunesCategory is within the ValidItunesCategory
         * hierarchy.
         *
         * @param category The ItunesCategory to validate.
         * @return True if the category is valid, false otherwise.
         */
        fun asValidItunesCategory(category: ItunesCategory): ValidItunesCategory? {
            // Check for null category text
            val categoryText = category.text.takeUnless { it.isNullOrBlank() } ?: return null

            // Check if the category matches any ValidItunesCategory
            val validCategories =
                Companion::class.java.declaredFields
                    .filter { ValidItunesCategory::class.java.isAssignableFrom(it.type) }

            // Validate if it's a simple valid category
            for (validCategory in validCategories) {
                when (val marco = validCategory.get(Companion)) {
                    is Simple -> {
                        if (category.subcategory == null) {
                            if (marco.text == categoryText) {
                                return marco
                            }
                        }
                    }

                    is Nested -> {
                        if (category.subcategory != null) {
                            if (marco.text == categoryText && marco.parent.text == category.subcategory.text) {
                                return marco
                            }
                        }
                    }
                }
            }
            return null
        }

        // Extension function to ensure nested classes are final and objects
        private fun KClass<*>.isFinalClass(): Boolean =
            this.visibility == KVisibility.PUBLIC &&
                this.objectInstance != null

        /** Category type for `Arts`. */
        val ARTS = Simple("Arts")

        /**
         * Category type for the value `Books` nested in the [ARTS] parent
         * category.
         */
        val BOOKS = Nested("Books", ARTS)

        /** Category type for value `Design` nested in the [ARTS] parent category. */
        val DESIGN = Nested("Design", ARTS)

        /**
         * Category type for the value `Fashion & Beauty` nested in the [ARTS]
         * parent category.
         */
        val FASHION_AND_BEAUTY = Nested("Fashion & Beauty", ARTS)

        /** Category type for the value `Food` nested in the [ARTS] parent category. */
        val FOOD = Nested("Food", ARTS)

        /**
         * Category type for the value `Performing Arts` nested in the [ARTS]
         * parent category.
         */
        val PERFORMING_ARTS = Nested("Performing Arts", ARTS)

        /**
         * Category type for the value `Visual Arts` nested in the [ARTS] parent
         * category.
         */
        val VISUAL_ARTS = Nested("Visual Arts", ARTS)

        /** Category type for the value `Business`. */
        val BUSINESS = Simple("Business")

        /**
         * Category type for the value `Careers` nested in the [BUSINESS] parent
         * category.
         */
        val CAREERS = Nested("Careers", BUSINESS)

        /**
         * Category type for the value `Entrepreneurship` nested in the [BUSINESS]
         * parent category.
         */
        val ENTREPRENEURSHIP = Nested("Entrepreneurship", BUSINESS)

        /**
         * Category type for the value `Investing` nested in the [BUSINESS] parent
         * category.
         */
        val INVESTING = Nested("Investing", BUSINESS)

        /**
         * Category type for the value `Management` nested in the [BUSINESS] parent
         * category.
         */
        val MANAGEMENT = Nested("Management", BUSINESS)

        /**
         * Category type for the value `Marketing` nested in the [BUSINESS] parent
         * category.
         */
        val MARKETING = Nested("Marketing", BUSINESS)

        /**
         * Category type for the value `Non-Profit` nested in the [BUSINESS] parent
         * category.
         */
        val NON_PROFIT = Nested("Non-Profit", BUSINESS)

        /** Category type for the value `Comedy`. */
        val COMEDY = Simple("Comedy")

        /**
         * Category type for the value `Comedy Interviews` nested in the [COMEDY]
         * parent category.
         */
        val COMEDY_INTERVIEWS = Nested("Comedy Interviews", COMEDY)

        /**
         * Category type for the value `Improv` nested in the [COMEDY] parent
         * category.
         */
        val IMPROV = Nested("Improv", COMEDY)

        /**
         * Category type for the value `Stand-Up` nested in the [COMEDY] parent
         * category.
         */
        val STAND_UP = Nested("Stand-Up", COMEDY)

        /** Category type for the value `Education`. */
        val EDUCATION = Simple("Education")

        /**
         * Category type for the value `Courses` nested in the [EDUCATION] parent
         * category.
         */
        val COURSES = Nested("Courses", EDUCATION)

        /**
         * Category type for the value `HowTo` nested in the [EDUCATION] parent
         * category.
         */
        val HOW_TO = Nested("How To", EDUCATION)

        /**
         * Category type for the value `Language Learning` nested in the
         * [EDUCATION] parent category.
         */
        val LANGUAGE_LEARNING = Nested("Language Learning", EDUCATION)

        /**
         * Category type for the value `Self-Improvement` nested in the [EDUCATION]
         * parent category.
         */
        val SELF_IMPROVEMENT = Nested("Self-Improvement", EDUCATION)

        /** Category type for the value `Fiction`. */
        val FICTION = Simple("Fiction")

        /**
         * Category type for the value `Comedy Fiction` nested in the [FICTION]
         * parent category.
         */
        val COMEDY_FICTION = Nested("Comedy Fiction", FICTION)

        /**
         * Category type for the value `Drama` nested in the [FICTION] parent
         * category.
         */
        val DRAMA = Nested("Drama", FICTION)

        /**
         * Category type for the value `Science Fiction` nested in the [FICTION]
         * parent category.
         */
        val SCIENCE_FICTION = Nested("Science Fiction", FICTION)

        /** Category type for the value `Government`. */
        val GOVERNMENT = Simple("Government")

        /** Category type for the value `History`. */
        val HISTORY = Simple("History")

        /** Category type for the value `Health & Fitness`. */
        val HEALTH_AND_FITNESS = Simple("Health & Fitness")

        /**
         * Category type for the value `Alternative Health` nested in the
         * [HEALTH_AND_FITNESS] parent category.
         */
        val ALTERNATIVE_HEALTH = Nested("Alternative Health", HEALTH_AND_FITNESS)

        /**
         * Category type for the value `Fitness` nested in the [HEALTH_AND_FITNESS]
         * parent category.
         */
        val FITNESS = Nested("Fitness", HEALTH_AND_FITNESS)

        /**
         * Category type for the value `Medicine` nested in the
         * [HEALTH_AND_FITNESS] parent category.
         */
        val MEDICINE = Nested("Medicine", HEALTH_AND_FITNESS)

        /**
         * Category type for the value `Mental Health` nested in the
         * [HEALTH_AND_FITNESS] parent category.
         */
        val MENTAL_HEALTH = Nested("Mental Health", HEALTH_AND_FITNESS)

        /**
         * Category type for the value `Nutrition` nested in the
         * [HEALTH_AND_FITNESS] parent category.
         */
        val NUTRITION = Nested("Nutrition", HEALTH_AND_FITNESS)

        /**
         * Category type for the value `Sexuality` nested in the
         * [HEALTH_AND_FITNESS] parent category.
         */
        val SEXUALITY = Nested("Sexuality", HEALTH_AND_FITNESS)

        /** Category type for the value `Kids & Family`. */
        val KIDS_AND_FAMILY = Simple("Kids & Family")

        /**
         * Category type for the value `Education for Kids` nested in the
         * [KIDS_AND_FAMILY] parent category.
         */
        val EDUCATION_FOR_KIDS = Nested("Education for Kids", KIDS_AND_FAMILY)

        /**
         * Category type for the value `Parentings` nested in the [KIDS_AND_FAMILY]
         * parent category.
         */
        val PARENTING = Nested("Parenting", KIDS_AND_FAMILY)

        /**
         * Category type for the value `Pets & Animals` nested in the
         * [KIDS_AND_FAMILY] parent category.
         */
        val PETS_AND_ANIMALS = Nested("Pets & Animals", KIDS_AND_FAMILY)

        /**
         * Category type for the value `Stories for Kids` nested in the
         * [KIDS_AND_FAMILY] parent category.
         */
        val STORIES_FOR_KIDS = Nested("Stories for Kids", KIDS_AND_FAMILY)

        /** Category type for the value `Leisure`. */
        val LEISURE = Simple("Leisure")

        /**
         * Category type for the value `Animation & Manga` nested in the [LEISURE]
         * parent category.
         */
        val ANIMATION_AND_MANGA = Nested("Animation & Manga", LEISURE)

        /**
         * Category type for the value `Automotive` nested in the [LEISURE] parent
         * category.
         */
        val AUTOMOTIVE = Nested("Automotive", LEISURE)

        /**
         * Category type for the value `Aviation` nested in the [LEISURE] parent
         * category.
         */
        val AVIATION = Nested("Aviation", LEISURE)

        /**
         * Category type for the value `Crafts` nested in the [LEISURE] parent
         * category.
         */
        val CRAFTS = Nested("Crafts", LEISURE)

        /**
         * Category type for the value `Games` nested in the [LEISURE] parent
         * category.
         */
        val GAMES = Nested("Games", LEISURE)

        /**
         * Category type for the value `Hobbies` nested in the [LEISURE] parent
         * category.
         */
        val HOBBIES = Nested("Hobbies", LEISURE)

        /**
         * Category type for the value `Home & Garden` nested in the [LEISURE]
         * parent category.
         */
        val HOME_AND_GARDEN = Nested("Home & Garden", LEISURE)

        /**
         * Category type for the value `Video Games` nested in the [LEISURE] parent
         * category.
         */
        val VIDEO_GAMES = Nested("Video Games", LEISURE)

        /** Category type for the value `Music`. */
        val MUSIC = Simple("Music")

        /**
         * Category type for the value `Music Commentary` nested in the [MUSIC]
         * parent category.
         */
        val MUSIC_COMMENTARY = Nested("Music Commentary", MUSIC)

        /**
         * Category type for the value `Music History` nested in the [MUSIC] parent
         * category.
         */
        val MUSIC_HISTORY = Nested("Music History", MUSIC)

        /**
         * Category type for the value `Music Interviews` nested in the [MUSIC]
         * parent category.
         */
        val MUSIC_INTERVIEWS = Nested("Music Interviews", MUSIC)

        /** Category type for the value `News`. */
        val NEWS = Simple("News")

        /**
         * Category type for the value `Business News` nested in the [NEWS] parent
         * category.
         */
        val BUSINESS_NEWS = Nested("Business News", NEWS)

        /**
         * Category type for the value `Daily News` nested in the [NEWS] parent
         * category.
         */
        val DAILY_NEWS = Nested("Daily News", NEWS)

        /**
         * Category type for the value `Entertainment News` nested in the [NEWS]
         * parent category.
         */
        val ENTERTAINMENT_NEWS = Nested("Entertainment News", NEWS)

        /**
         * Category type for the value `News Commentary` nested in the [NEWS]
         * parent category.
         */
        val NEWS_COMMENTARY = Nested("News Commentary", NEWS)

        /**
         * Category type for the value `Politics` nested in the [NEWS] parent
         * category.
         */
        val POLITICS = Nested("Politics", NEWS)

        /**
         * Category type for the value `Sports News` nested in the [NEWS] parent
         * category.
         */
        val SPORTS_NEWS = Nested("Sports News", NEWS)

        /**
         * Category type for the value `Tech News` nested in the [NEWS] parent
         * category.
         */
        val TECH_NEWS = Nested("Tech News", NEWS)

        /** Category type for the value `Religion & Spirituality`. */
        val RELIGION_AND_SPIRITUALITY = Simple("Religion & Spirituality")

        /**
         * Category type for the value `Buddhism` nested in the
         * [RELIGION_AND_SPIRITUALITY] parent category.
         */
        val BUDDHISM = Nested("Buddhism", RELIGION_AND_SPIRITUALITY)

        /**
         * Category type for the value `Christianity` nested in the
         * [RELIGION_AND_SPIRITUALITY] parent category.
         */
        val CHRISTIANITY = Nested("Christianity", RELIGION_AND_SPIRITUALITY)

        /**
         * Category type for the value `Hinduism` nested in the
         * [RELIGION_AND_SPIRITUALITY] parent category.
         */
        val HINDUISM = Nested("Hinduism", RELIGION_AND_SPIRITUALITY)

        /**
         * Category type for the value `Islam` nested in the
         * [RELIGION_AND_SPIRITUALITY] parent category.
         */
        val ISLAM = Nested("Islam", RELIGION_AND_SPIRITUALITY)

        /**
         * Category type for the value `Judaism` nested in the
         * [RELIGION_AND_SPIRITUALITY] parent category.
         */
        val JUDAISM = Nested("Judaism", RELIGION_AND_SPIRITUALITY)

        /**
         * Category type for the value `Religion` nested in the
         * [RELIGION_AND_SPIRITUALITY] parent category.
         */
        val RELIGION = Nested("Religion", RELIGION_AND_SPIRITUALITY)

        /**
         * Category type for the value `Spirituality` nested in the
         * [RELIGION_AND_SPIRITUALITY] parent category.
         */
        val SPIRITUALITY = Nested("Spirituality", RELIGION_AND_SPIRITUALITY)

        /** Category type for the value `Science`. */
        val SCIENCE = Simple("Science")

        /**
         * Category type for the value `Astronomy` nested in the [SCIENCE] parent
         * category.
         */
        val ASTRONOMY = Nested("Astronomy", SCIENCE)

        /**
         * Category type for the value `Chemistry` nested in the [SCIENCE] parent
         * category.
         */
        val CHEMISTRY = Nested("Chemistry", SCIENCE)

        /**
         * Category type for the value `Earth Sciences` nested in the [SCIENCE]
         * parent category.
         */
        val EARTH_SCIENCES = Nested("Earth Sciences", SCIENCE)

        /**
         * Category type for the value `Life Sciences` nested in the [SCIENCE]
         * parent category.
         */
        val LIFE_SCIENCES = Nested("Life Sciences", SCIENCE)

        /**
         * Category type for the value `Mathematics` nested in the [SCIENCE] parent
         * category.
         */
        val MATHEMATICS = Nested("Mathematics", SCIENCE)

        /**
         * Category type for the value `Natural Sciences` nested in the [SCIENCE]
         * parent category.
         */
        val NATURAL_SCIENCES = Nested("Natural Sciences", SCIENCE)

        /**
         * Category type for the value `Nature` nested in the [SCIENCE] parent
         * category.
         */
        val NATURE = Nested("Nature", SCIENCE)

        /**
         * Category type for the value `Physics` nested in the [SCIENCE] parent
         * category.
         */
        val PHYSICS = Nested("Physics", SCIENCE)

        /**
         * Category type for the value `Social Sciences` nested in the [SCIENCE]
         * parent category.
         */
        val SOCIAL_SCIENCES = Nested("Social Sciences", SCIENCE)

        /** Category type for the value `Society & Culture`. */
        val SOCIETY_AND_CULTURE = Simple("Society & Culture")

        /**
         * Category type for the value `Documentary` nested in the
         * [SOCIETY_AND_CULTURE] parent category.
         */
        val DOCUMENTARY = Nested("Documentary", SOCIETY_AND_CULTURE)

        /**
         * Category type for the value `Personal Journals` nested in the
         * [SOCIETY_AND_CULTURE] parent category.
         */
        val PERSONAL_JOURNALS = Nested("Personal Journals", SOCIETY_AND_CULTURE)

        /**
         * Category type for the value `Philosophy` nested in the
         * [SOCIETY_AND_CULTURE] parent category.
         */
        val PHILOSOPHY = Nested("Philosophy", SOCIETY_AND_CULTURE)

        /**
         * Category type for the value `Places & Travel` nested in the
         * [SOCIETY_AND_CULTURE] parent category.
         */
        val PLACES_AND_TRAVEL = Nested("Places & Travel", SOCIETY_AND_CULTURE)

        /**
         * Category type for the value `Relationships` nested in the
         * [SOCIETY_AND_CULTURE] parent category.
         */
        val RELATIONSHIPS = Nested("Relationships", SOCIETY_AND_CULTURE)

        /** Category type for the value `Sports`. */
        val SPORTS = Simple("Sports")

        /**
         * Category type for the value `Baseball` nested in the [SPORTS] parent
         * category.
         */
        val BASEBALL = Nested("Baseball", SPORTS)

        /**
         * Category type for the value `Basketball` nested in the [SPORTS] parent
         * category.
         */
        val BASKETBALL = Nested("Basketball", SPORTS)

        /**
         * Category type for the value `Cricket` nested in the [SPORTS] parent
         * category.
         */
        val CRICKET = Nested("Cricket", SPORTS)

        /**
         * Category type for the value `Fantasy Sports` nested in the [SPORTS]
         * parent category.
         */
        val FANTASY_SPORTS = Nested("Fantasy Sports", SPORTS)

        /**
         * Category type for the value `Football` nested in the [SPORTS] parent
         * category.
         */
        val FOOTBALL = Nested("Football", SPORTS)

        /**
         * Category type for the value `Golf` nested in the [SPORTS] parent
         * category.
         */
        val GOLF = Nested("Golf", SPORTS)

        /**
         * Category type for the value `Hockey` nested in the [SPORTS] parent
         * category.
         */
        val HOCKEY = Nested("Hockey", SPORTS)

        /**
         * Category type for the value `Rugby` nested in the [SPORTS] parent
         * category.
         */
        val RUGBY = Nested("Rugby", SPORTS)

        /**
         * Category type for the value `Running` nested in the [SPORTS] parent
         * category.
         */
        val RUNNING = Nested("Running", SPORTS)

        /**
         * Category type for the value `Soccer` nested in the [SPORTS] parent
         * category.
         */
        val SOCCER = Nested("Soccer", SPORTS)

        /**
         * Category type for the value `Swimming` nested in the [SPORTS] parent
         * category.
         */
        val SWIMMING = Nested("Swimming", SPORTS)

        /**
         * Category type for the value `Tennis` nested in the [SPORTS] parent
         * category.
         */
        val TENNIS = Nested("Tennis", SPORTS)

        /**
         * Category type for the value `Volleyball` nested in the [SPORTS] parent
         * category.
         */
        val VOLLEYBALL = Nested("Volleyball", SPORTS)

        /**
         * Category type for the value `Wilderness` nested in the [SPORTS] parent
         * category.
         */
        val WILDERNESS = Nested("Wilderness", SPORTS)

        /**
         * Category type for the value `Wrestling` nested in the [SPORTS] parent
         * category.
         */
        val WRESTLING = Nested("Wrestling", SPORTS)

        /** Category type for the value `Technology`. */
        val TECHNOLOGY = Simple("Technology")

        /** Category type for the value `True Crime`. */
        val TRUE_CRIME = Simple("True Crime")

        /** Category type for the value `TV & Film`. */
        val TV_AND_FILM = Simple("TV & Film")

        /**
         * Category type for the value `After Shows` nested in the [TV_AND_FILM]
         * parent category.
         */
        val AFTER_SHOWS = Nested("After Shows", TV_AND_FILM)

        /**
         * Category type for the value `Film History` nested in the [TV_AND_FILM]
         * parent category.
         */
        val FILM_HISTORY = Nested("Film History", TV_AND_FILM)

        /**
         * Category type for the value `Film Interviews` nested in the
         * [TV_AND_FILM] parent category.
         */
        val FILM_INTERVIEWS = Nested("Film Interviews", TV_AND_FILM)

        /**
         * Category type for the value `Film Reviews` nested in the [TV_AND_FILM]
         * parent category.
         */
        val FILM_REVIEWS = Nested("Film Reviews", TV_AND_FILM)

        /**
         * Category type for the value `TV Reviews` nested in the [TV_AND_FILM]
         * parent category.
         */
        val TV_REVIEWS = Nested("TV Reviews", TV_AND_FILM)
    }
}
