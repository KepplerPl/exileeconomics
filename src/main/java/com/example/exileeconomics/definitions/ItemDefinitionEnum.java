package com.example.exileeconomics.definitions;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public enum ItemDefinitionEnum {
    WINGED_TORMENT_SCARAB("winged torment scarab"),
    SCREAMING_ESSENCE_OF_ENVY("screaming essence of envy"),
    AMORPHOUS_DELIRIUM_ORB("amorphous delirium orb"),
    XOPHS_BREACHSTONE("xoph's breachstone"),
    SCREAMING_ESSENCE_OF_FEAR("screaming essence of fear"),
    WEALTH_AND_POWER("wealth and power"),
    GILDED_SHAPER_SCARAB("gilded shaper scarab"),
    WINGED_SULPHITE_SCARAB("winged sulphite scarab"),
    DEAFENING_ESSENCE_OF_LOATHING("deafening essence of loathing"),
    LUMINOUS_TROVE("luminous trove"),
    WEEPING_ESSENCE_OF_RAGE("weeping essence of rage"),
    PRISTINE_FOSSIL("pristine fossil"),
    SHRIEKING_ESSENCE_OF_HATRED("shrieking essence of hatred"),
    UULNETOLS_BREACHSTONE("uul-netol's breachstone"),
    MIRROR_OF_KALANDRA("mirror of kalandra"),
    ESHS_BREACHSTONE("esh's breachstone"),
    HUNTERS_EXALTED_ORB("hunter's exalted orb"),
    ORB_OF_CHANCE("orb of chance"),
    DEAFENING_ESSENCE_OF_ENVY("deafening essence of envy"),
    ELDRITCH_EXALTED_ORB("eldritch exalted orb"),
    ELDRITCH_ORB_OF_ANNULMENT("eldritch orb of annulment"),
    ESSENCE_OF_DELIRIUM("essence of delirium"),
    SOMETHING_DARK("something dark"),
    DEAFENING_ESSENCE_OF_FEAR("deafening essence of fear"),
    FRAGMENT_OF_ENSLAVEMENT("fragment of enslavement"),
    FRAGMENT_OF_THE_HYDRA("fragment of the hydra"),
    SHRIEKING_ESSENCE_OF_ZEAL("shrieking essence of zeal"),
    FRAGMENT_OF_THE_PHOENIX("fragment of the phoenix"),
    A_FAMILIAR_CALL("a familiar call"),
    THE_ONE_THAT_GOT_AWAY("the one that got away"),
    SHRIEKING_ESSENCE_OF_CONTEMPT("shrieking essence of contempt"),
    GLYPHIC_FOSSIL("glyphic fossil"),
    WAILING_ESSENCE_OF_WOE("wailing essence of woe"),
    GILDED_LEGION_SCARAB("gilded legion scarab"),
    SHRIEKING_ESSENCE_OF_ANGUISH("shrieking essence of anguish"),
    A_MODEST_REQUEST("a modest request"),
    BLIGHTED_DELIRIUM_ORB("blighted delirium orb"),
    RUSTED_BREACH_SCARAB("rusted breach scarab"),
    THE_ARTIST("the artist"),
    SHRIEKING_ESSENCE_OF_SUFFERING("shrieking essence of suffering"),
    DEAFENING_ESSENCE_OF_SCORN("deafening essence of scorn"),
    WINGED_HARBINGER_SCARAB("winged harbinger scarab"),
    WAILING_ESSENCE_OF_RAGE("wailing essence of rage"),
    RUSTED_BLIGHT_SCARAB("rusted blight scarab"),
    WEEPING_ESSENCE_OF_WRATH("weeping essence of wrath"),
    ANCIENT_ORB("ancient orb"),
    FINE_DELIRIUM_ORB("fine delirium orb"),
    POLISHED_METAMORPH_SCARAB("polished metamorph scarab"),
    REGAL_ORB("regal orb"),
    ABYSSAL_DELIRIUM_ORB("abyssal delirium orb"),
    THE_STRATEGIST("the strategist"),
    VIAL_OF_THE_GHOST("vial of the ghost"),
    GRAND_ELDRITCH_ICHOR("grand eldritch ichor"),
    WAILING_ESSENCE_OF_ANGER("wailing essence of anger"),
    THE_FISHMONGER("the fishmonger"),
    CHAYULAS_FLAWLESS_BREACHSTONE("chayula's flawless breachstone"),
    SCREAMING_ESSENCE_OF_ANGER("screaming essence of anger"),
    SCREAMING_ESSENCE_OF_ZEAL("screaming essence of zeal"),
    DROXS_CREST("drox's crest"),
    THE_NURSE("the nurse"),
    MUTTERING_ESSENCE_OF_WOE("muttering essence of woe"),
    GILDED_TORMENT_SCARAB("gilded torment scarab"),
    GOLDEN_OIL("golden oil"),
    EXALTED_ORB("exalted orb"),
    THE_LEVIATHAN("the leviathan"),
    ORB_OF_SCOURING("orb of scouring"),
    THE_BITTER_BLOSSOM("the bitter blossom"),
    FRAGMENT_OF_PURIFICATION("fragment of purification"),
    GEMCUTTERS_PRISM("gemcutter's prism"),
    DEAFENING_ESSENCE_OF_SUFFERING("deafening essence of suffering"),
    CHOKING_GUILT("choking guilt"),
    OPALESCENT_OIL("opalescent oil"),
    BARANS_CREST("baran's crest"),
    MUTTERING_ESSENCE_OF_CONTEMPT("muttering essence of contempt"),
    THE_CHEATER("the cheater"),
    WAILING_ESSENCE_OF_SUFFERING("wailing essence of suffering"),
    IMPERFECT_MEMORIES("imperfect memories"),
    MUTTERING_ESSENCE_OF_ANGER("muttering essence of anger"),
    PROMETHEUS_ARMOURY("prometheus' armoury"),
    ABERRANT_FOSSIL("aberrant fossil"),
    WINGED_EXPEDITION_SCARAB("winged expedition scarab"),
    OBSCURED_DELIRIUM_ORB("obscured delirium orb"),
    RUSTED_LEGION_SCARAB("rusted legion scarab"),
    SUCCOR_OF_THE_SINLESS("succor of the sinless"),
    SHRIEKING_ESSENCE_OF_DREAD("shrieking essence of dread"),
    SCREAMING_ESSENCE_OF_DREAD("screaming essence of dread"),
    PRISMATIC_FOSSIL("prismatic fossil"),
    MUTTERING_ESSENCE_OF_FEAR("muttering essence of fear"),
    WAILING_ESSENCE_OF_SPITE("wailing essence of spite"),
    THE_ASPIRANT("the aspirant"),
    DEAFENING_ESSENCE_OF_WRATH("deafening essence of wrath"),
    THE_GULF("the gulf"),
    SHRIEKING_ESSENCE_OF_SPITE("shrieking essence of spite"),
    WHISPERING_ESSENCE_OF_GREED("whispering essence of greed"),
    HOME("home"),
    SCREAMING_ESSENCE_OF_SPITE("screaming essence of spite"),
    FRACTURING_ORB("fracturing orb"),
    VIAL_OF_THE_RITUAL("vial of the ritual"),
    AWAKENED_SEXTANT("awakened sextant"),
    DRAPED_IN_DREAMS("draped in dreams"),
    SHRIEKING_ESSENCE_OF_ANGER("shrieking essence of anger"),
    THE_SCOUT("the scout"),
    RUSTED_ELDER_SCARAB("rusted elder scarab"),
    GIFT_OF_ASENATH("gift of asenath"),
    POLISHED_CARTOGRAPHY_SCARAB("polished cartography scarab"),
    DENSE_FOSSIL("dense fossil"),
    UNREQUITED_LOVE("unrequited love"),
    DEAFENING_ESSENCE_OF_WOE("deafening essence of woe"),
    THE_DOCTOR("the doctor"),
    WAILING_ESSENCE_OF_SORROW("wailing essence of sorrow"),
    SHRIEKING_ESSENCE_OF_LOATHING("shrieking essence of loathing"),
    RUSTED_TORMENT_SCARAB("rusted torment scarab"),
    RUSTED_DIVINATION_SCARAB("rusted divination scarab"),
    WHISPERING_ESSENCE_OF_WOE("whispering essence of woe"),
    GILDED_AMBUSH_SCARAB("gilded ambush scarab"),
    SHRIEKING_ESSENCE_OF_ENVY("shrieking essence of envy"),
    SHRIEKING_ESSENCE_OF_FEAR("shrieking essence of fear"),
    POLISHED_DIVINATION_SCARAB("polished divination scarab"),
    WHISPERING_ESSENCE_OF_HATRED("whispering essence of hatred"),
    REDEEMERS_EXALTED_ORB("redeemer's exalted orb"),
    SCORCHED_FOSSIL("scorched fossil"),
    SCREAMING_ESSENCE_OF_TORMENT("screaming essence of torment"),
    EXCEPTIONAL_ELDRITCH_EMBER("exceptional eldritch ember"),
    GILDED_SULPHITE_SCARAB("gilded sulphite scarab"),
    GILDED_ELDER_SCARAB("gilded elder scarab"),
    ESSENCE_OF_HORROR("essence of horror"),
    THE_ASTROMANCER("the astromancer"),
    BEAUTY_THROUGH_DEATH("beauty through death"),
    STACKED_DECK("stacked deck"),
    WARLORDS_EXALTED_ORB("warlord's exalted orb"),
    GIFT_TO_THE_GODDESS("gift to the goddess"),
    POLISHED_ABYSS_SCARAB("polished abyss scarab"),
    THE_INSANE_CAT("the insane cat"),
    TAINTED_ORB_OF_FUSING("tainted orb of fusing"),
    POLISHED_LEGION_SCARAB("polished legion scarab"),
    THE_MAVENS_WRIT("the maven's writ"),
    VERITANIAS_CREST("veritania's crest"),
    SCREAMING_ESSENCE_OF_SUFFERING("screaming essence of suffering"),
    MUTTERING_ESSENCE_OF_GREED("muttering essence of greed"),
    GILDED_RELIQUARY_SCARAB("gilded reliquary scarab"),
    WINGED_BLIGHT_SCARAB("winged blight scarab"),
    THE_AWAKENED("the awakened"),
    FOREBODING_DELIRIUM_ORB("foreboding delirium orb"),
    ALLURING_BOUNTY("alluring bounty"),
    WINGED_SHAPER_SCARAB("winged shaper scarab"),
    REMEMBRANCE("remembrance"),
    BLACK_OIL("black oil"),
    LUCENT_FOSSIL("lucent fossil"),
    TAINTED_MYTHIC_ORB("tainted mythic orb"),
    DEDICATION_TO_THE_GODDESS("dedication to the goddess"),
    FRAGMENT_OF_THE_CHIMERA("fragment of the chimera"),
    BLOODSTAINED_FOSSIL("bloodstained fossil"),
    TAINTED_BLESSING("tainted blessing"),
    SHRIEKING_ESSENCE_OF_SCORN("shrieking essence of scorn"),
    BROKEN_PROMISES("broken promises"),
    ENKINDLING_ORB("enkindling orb"),
    TANGLED_FOSSIL("tangled fossil"),
    THE_FIEND("the fiend"),
    TORMENT_SCARAB("torment scarab"),
    HOUSE_OF_MIRRORS("house of mirrors"),
    MUTTERING_ESSENCE_OF_SORROW("muttering essence of sorrow"),
    CRUSADERS_EXALTED_ORB("crusader's exalted orb"),
    MONOCHROME("monochrome"),
    THE_VAST("the vast"),
    WEEPING_ESSENCE_OF_ANGER("weeping essence of anger"),
    GILDED_DIVINATION_SCARAB("gilded divination scarab"),
    THE_ENLIGHTENED("the enlightened"),
    POLISHED_RELIQUARY_SCARAB("polished reliquary scarab"),
    THE_OLD_MAN("the old man"),
    SHRIEKING_ESSENCE_OF_TORMENT("shrieking essence of torment"),
    WINGED_BESTIARY_SCARAB("winged bestiary scarab"),
    DARKER_HALF("darker half"),
    RUSTED_ABYSS_SCARAB("rusted abyss scarab"),
    METALLIC_FOSSIL("metallic fossil"),
    TULS_BREACHSTONE("tul's breachstone"),
    THE_SHORTCUT("the shortcut"),
    VIAL_OF_SUMMONING("vial of summoning"),
    ARMOURSMITHS_DELIRIUM_ORB("armoursmith's delirium orb"),
    DIVINE_BEAUTY("divine beauty"),
    SCREAMING_ESSENCE_OF_DOUBT("screaming essence of doubt"),
    BLESSED_ORB("blessed orb"),
    ELDRITCH_CHAOS_ORB("eldritch chaos orb"),
    THE_SOUL("the soul"),
    RUSTED_AMBUSH_SCARAB("rusted ambush scarab"),
    WEEPING_ESSENCE_OF_GREED("weeping essence of greed"),
    BROTHERS_GIFT("brother's gift"),
    WAILING_ESSENCE_OF_CONTEMPT("wailing essence of contempt"),
    DEAFENING_ESSENCE_OF_ZEAL("deafening essence of zeal"),
    DUALITY("duality"),
    VEILED_CHAOS_ORB("veiled chaos orb"),
    IMPERIAL_DELIRIUM_ORB("imperial delirium orb"),
    SHRIEKING_ESSENCE_OF_DOUBT("shrieking essence of doubt"),
    RUSTED_SHAPER_SCARAB("rusted shaper scarab"),
    DORYANIS_EPIPHANY("doryani's epiphany"),
    A_FATE_WORSE_THAN_DEATH("a fate worse than death"),
    THE_ETERNAL_WAR("the eternal war"),
    ORB_OF_FUSING("orb of fusing"),
    REMNANT_OF_CORRUPTION("remnant of corruption"),
    FRAGMENT_OF_ERADICATION("fragment of eradication"),
    FACETED_FOSSIL("faceted fossil"),
    DEAFENING_ESSENCE_OF_ANGUISH("deafening essence of anguish"),
    POLISHED_ELDER_SCARAB("polished elder scarab"),
    GILDED_ABYSS_SCARAB("gilded abyss scarab"),
    JAGGED_FOSSIL("jagged fossil"),
    THE_SACRIFICE("the sacrifice"),
    VIAL_OF_AWAKENING("vial of awakening"),
    THE_PRICE_OF_LOYALTY("the price of loyalty"),
    FRAGMENT_OF_TERROR("fragment of terror"),
    SCREAMING_ESSENCE_OF_WRATH("screaming essence of wrath"),
    SCREAMING_ESSENCE_OF_GREED("screaming essence of greed"),
    MIRROR_SHARD("mirror shard"),
    SERRATED_FOSSIL("serrated fossil"),
    VIAL_OF_CONSEQUENCE("vial of consequence"),
    WINGED_CARTOGRAPHY_SCARAB("winged cartography scarab"),
    WINGED_LEGION_SCARAB("winged legion scarab"),
    GILDED_BREACH_SCARAB("gilded breach scarab"),
    SEVEN_YEARS_BAD_LUCK("seven years bad luck"),
    SHRIEKING_ESSENCE_OF_WRATH("shrieking essence of wrath"),
    SANCTIFIED_FOSSIL("sanctified fossil"),
    BOUND_FOSSIL("bound fossil"),
    BLACKSMITHS_DELIRIUM_ORB("blacksmith's delirium orb"),
    WINTERS_EMBRACE("winter's embrace"),
    POLISHED_AMBUSH_SCARAB("polished ambush scarab"),
    THAUMATURGES_DELIRIUM_ORB("thaumaturge's delirium orb"),
    WAILING_ESSENCE_OF_ANGUISH("wailing essence of anguish"),
    FRAGMENT_OF_EMPTINESS("fragment of emptiness"),
    POLISHED_SHAPER_SCARAB("polished shaper scarab"),
    DEAFENING_ESSENCE_OF_SPITE("deafening essence of spite"),
    WEEPING_ESSENCE_OF_TORMENT("weeping essence of torment"),
    SCREAMING_ESSENCE_OF_ANGUISH("screaming essence of anguish"),
    ETERNAL_BONDS("eternal bonds"),
    POLISHED_BREACH_SCARAB("polished breach scarab"),
    GILDED_FOSSIL("gilded fossil"),
    INSTILLING_ORB("instilling orb"),
    WAILING_ESSENCE_OF_FEAR("wailing essence of fear"),
    DESECRATED_VIRTUE("desecrated virtue"),
    WAILING_ESSENCE_OF_TORMENT("wailing essence of torment"),
    WEEPING_ESSENCE_OF_CONTEMPT("weeping essence of contempt"),
    THE_DRAGONS_HEART("the dragon's heart"),
    WILD_CRYSTALLISED_LIFEFORCE("wild crystallised lifeforce"),
    REBIRTH("rebirth"),
    CRIMSON_OIL("crimson oil"),
    TAINTED_CHAOS_ORB("tainted chaos orb"),
    DEAFENING_ESSENCE_OF_RAGE("deafening essence of rage"),
    LOVE_THROUGH_ICE("love through ice"),
    GILDED_METAMORPH_SCARAB("gilded metamorph scarab"),
    SHRIEKING_ESSENCE_OF_GREED("shrieking essence of greed"),
    WAILING_ESSENCE_OF_GREED("wailing essence of greed"),
    SACRED_ORB("sacred orb"),
    POLISHED_BLIGHT_SCARAB("polished blight scarab"),
    WEEPING_ESSENCE_OF_DOUBT("weeping essence of doubt"),
    GILDED_BESTIARY_SCARAB("gilded bestiary scarab"),
    WAILING_ESSENCE_OF_HATRED("wailing essence of hatred"),
    SKITTERING_DELIRIUM_ORB("skittering delirium orb"),
    MAGNUM_OPUS("magnum opus"),
    THE_TUMBLEWEED("the tumbleweed"),
    GREATER_ELDRITCH_EMBER("greater eldritch ember"),
    WINGED_ABYSS_SCARAB("winged abyss scarab"),
    SCREAMING_ESSENCE_OF_SCORN("screaming essence of scorn"),
    WEEPING_ESSENCE_OF_FEAR("weeping essence of fear"),
    FRAGMENT_OF_THE_MINOTAUR("fragment of the minotaur"),
    WINGED_AMBUSH_SCARAB("winged ambush scarab"),
    RUSTED_HARBINGER_SCARAB("rusted harbinger scarab"),
    FATEFUL_MEETING("fateful meeting"),
    ALHEZMINS_CREST("al-hezmin's crest"),
    MUTTERING_ESSENCE_OF_HATRED("muttering essence of hatred"),
    WEEPING_ESSENCE_OF_HATRED("weeping essence of hatred"),
    SCREAMING_ESSENCE_OF_HATRED("screaming essence of hatred"),
    AETHERIC_FOSSIL("aetheric fossil"),
    PRIMAL_CRYSTALLISED_LIFEFORCE("primal crystallised lifeforce"),
    THE_CHOSEN("the chosen"),
    WAILING_ESSENCE_OF_ZEAL("wailing essence of zeal"),
    THE_SEPHIROT("the sephirot"),
    FRAGMENT_OF_SHAPE("fragment of shape"),
    ESSENCE_OF_INSANITY("essence of insanity"),
    MATRYOSHKA("matryoshka"),
    DIVINE_ORB("divine orb"),
    FRAGMENTED_DELIRIUM_ORB("fragmented delirium orb"),
    GRAND_ELDRITCH_EMBER("grand eldritch ember"),
    THE_EYE_OF_TERROR("the eye of terror"),
    DEAFENING_ESSENCE_OF_GREED("deafening essence of greed"),
    SHUDDERING_FOSSIL("shuddering fossil"),
    FUNDAMENTAL_FOSSIL("fundamental fossil"),
    GILDED_HARBINGER_SCARAB("gilded harbinger scarab"),
    PERFECT_FOSSIL("perfect fossil"),
    SCREAMING_ESSENCE_OF_LOATHING("screaming essence of loathing"),
    THE_HOOK("the hook"),
    WHISPERING_ESSENCE_OF_CONTEMPT("whispering essence of contempt"),
    RUSTED_SULPHITE_SCARAB("rusted sulphite scarab"),
    WINGED_DIVINATION_SCARAB("winged divination scarab"),
    CHAOS_ORB("chaos orb"),
    RUSTED_CARTOGRAPHY_SCARAB("rusted cartography scarab"),
    VIAL_OF_SACRIFICE("vial of sacrifice"),
    TAINTED_JEWELLERS_ORB("tainted jeweller's orb"),
    DEAFENING_ESSENCE_OF_DOUBT("deafening essence of doubt"),
    WINGED_ELDER_SCARAB("winged elder scarab"),
    ORB_OF_ANNULMENT("orb of annulment"),
    DIVINERS_DELIRIUM_ORB("diviner's delirium orb"),
    POLISHED_SULPHITE_SCARAB("polished sulphite scarab"),
    SILVER_OIL("silver oil"),
    ORB_OF_REGRET("orb of regret"),
    THE_DESTINATION("the destination"),
    RUSTED_METAMORPH_SCARAB("rusted metamorph scarab"),
    WINGED_RELIQUARY_SCARAB("winged reliquary scarab"),
    SCREAMING_ESSENCE_OF_WOE("screaming essence of woe"),
    THE_GARISH_POWER("the garish power"),
    SINGULAR_DELIRIUM_ORB("singular delirium orb"),
    SHRIEKING_ESSENCE_OF_RAGE("shrieking essence of rage"),
    FURTHER_INVENTION("further invention"),
    DEAFENING_ESSENCE_OF_CONTEMPT("deafening essence of contempt"),
    EXCEPTIONAL_ELDRITCH_ICHOR("exceptional eldritch ichor"),
    THE_ENDLESS_DARKNESS("the endless darkness"),
    BROTHERS_STASH("brother's stash"),
    SHRIEKING_ESSENCE_OF_SORROW("shrieking essence of sorrow"),
    SCREAMING_ESSENCE_OF_MISERY("screaming essence of misery"),
    ORB_OF_ALCHEMY("orb of alchemy"),
    POLISHED_EXPEDITION_SCARAB("polished expedition scarab"),
    THE_PRICE_OF_DEVOTION("the price of devotion"),
    FRIGID_FOSSIL("frigid fossil"),
    VIAL_OF_TRANSCENDENCE("vial of transcendence"),
    RUSTED_EXPEDITION_SCARAB("rusted expedition scarab"),
    THE_IMMORTAL("the immortal"),
    HOLLOW_FOSSIL("hollow fossil"),
    GILDED_CARTOGRAPHY_SCARAB("gilded cartography scarab"),
    THE_PATIENT("the patient"),
    THE_RABBITS_FOOT("the rabbit's foot"),
    ORB_OF_ALTERATION("orb of alteration"),
    FRACTURED_FOSSIL("fractured fossil"),
    ESSENCE_OF_HYSTERIA("essence of hysteria"),
    TAINTED_OIL("tainted oil"),
    VIAL_OF_DOMINANCE("vial of dominance"),
    WEEPING_ESSENCE_OF_WOE("weeping essence of woe"),
    DEFT_FOSSIL("deft fossil"),
    POLISHED_BESTIARY_SCARAB("polished bestiary scarab"),
    FOSSILISED_DELIRIUM_ORB("fossilised delirium orb"),
    CARTOGRAPHERS_DELIRIUM_ORB("cartographer's delirium orb"),
    FRAGMENT_OF_KNOWLEDGE("fragment of knowledge"),
    VIAL_OF_FATE("vial of fate"),
    WINGED_BREACH_SCARAB("winged breach scarab"),
    THE_APOTHECARY("the apothecary"),
    FRAGMENT_OF_CONSTRICTION("fragment of constriction"),
    VAAL_ORB("vaal orb"),
    CORRODED_FOSSIL("corroded fossil"),
    MUTTERING_ESSENCE_OF_TORMENT("muttering essence of torment"),
    WHISPERING_DELIRIUM_ORB("whispering delirium orb"),
    SCREAMING_ESSENCE_OF_SORROW("screaming essence of sorrow"),
    WEEPING_ESSENCE_OF_SORROW("weeping essence of sorrow"),
    GILDED_EXPEDITION_SCARAB("gilded expedition scarab"),
    WAILING_ESSENCE_OF_LOATHING("wailing essence of loathing"),
    DEAFENING_ESSENCE_OF_MISERY("deafening essence of misery"),
    THE_SAMURAIS_EYE("the samurai's eye"),
    SCREAMING_ESSENCE_OF_CONTEMPT("screaming essence of contempt"),
    THE_SHIELDBEARER("the shieldbearer"),
    ELEVATED_SEXTANT("elevated sextant"),
    CHAYULAS_BREACHSTONE("chayula's breachstone"),
    GREATER_ELDRITCH_ICHOR("greater eldritch ichor"),
    WAILING_ESSENCE_OF_DOUBT("wailing essence of doubt"),
    GEMCUTTERS_MERCY("gemcutter's mercy"),
    DEAFENING_ESSENCE_OF_SORROW("deafening essence of sorrow"),
    TAINTED_EXALTED_ORB("tainted exalted orb"),
    TIMELESS_DELIRIUM_ORB("timeless delirium orb"),
    REFLECTIVE_OIL("reflective oil"),
    WEEPING_ESSENCE_OF_SUFFERING("weeping essence of suffering"),
    DEAFENING_ESSENCE_OF_TORMENT("deafening essence of torment"),
    SACRED_CRYSTALLISED_LIFEFORCE("sacred crystallised lifeforce"),
    THE_GREATEST_INTENTIONS("the greatest intentions"),
    SHRIEKING_ESSENCE_OF_WOE("shrieking essence of woe"),
    RUSTED_BESTIARY_SCARAB("rusted bestiary scarab"),
    POLISHED_HARBINGER_SCARAB("polished harbinger scarab"),
    VIVID_CRYSTALLISED_LIFEFORCE("vivid crystallised lifeforce"),
    SHRIEKING_ESSENCE_OF_MISERY("shrieking essence of misery"),
    THE_LAST_ONE_STANDING("the last one standing"),
    DEAFENING_ESSENCE_OF_ANGER("deafening essence of anger"),
    GLASSBLOWERS_BAUBLE("glassblower's bauble"),
    DEAFENING_ESSENCE_OF_HATRED("deafening essence of hatred"),
    THE_DEMON("the demon"),
    SCREAMING_ESSENCE_OF_RAGE("screaming essence of rage"),
    WINGED_METAMORPH_SCARAB("winged metamorph scarab"),
    JEWELLERS_DELIRIUM_ORB("jeweller's delirium orb"),
    GILDED_BLIGHT_SCARAB("gilded blight scarab"),
    WAILING_ESSENCE_OF_WRATH("wailing essence of wrath"),
    ORB_OF_UNMAKING("orb of unmaking"),
    SACRED_BLOSSOM("sacred blossom"),
    DEAFENING_ESSENCE_OF_DREAD("deafening essence of dread"),
    RUSTED_RELIQUARY_SCARAB("rusted reliquary scarab")
    ;

    private static final Set<String> enumAsString = new HashSet<>();
    private static final Map<String, ItemDefinitionEnum> stringMap;

    public static ItemDefinitionEnum fromString(String value) {
        if(!ItemDefinitionEnum.contains(value.toLowerCase())) {
            throw new IllegalArgumentException("Unrecognized enum value, got " + value.toLowerCase());
        }

        return stringMap.get(value.toLowerCase());
    }

    // questionable static use here tbh
    public static boolean contains(String name) {
        return enumAsString.contains(name.toLowerCase());
    }

    private final String name;

    ItemDefinitionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    static {
        stringMap = Arrays.stream(values())
                .collect(Collectors.toMap(ItemDefinitionEnum::getName, Function.identity()));

        for (ItemDefinitionEnum c : ItemDefinitionEnum.values()) {
            ItemDefinitionEnum.enumAsString.add(c.getName());
        }
    }
}
