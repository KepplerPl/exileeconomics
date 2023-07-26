package com.exileeconomics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class ExileeconomicsApplication {
    public static void main(String[] args) {
//        List<String> strings = List.of(
//                "Mirror of Kalandra","Mirror Shard","Fracturing Orb","Tempering Orb","Tainted Divine Teardrop","Blessing of Chayula","Tailoring Orb","Orb of Dominance","Sacred Crystallised Lifeforce","Secondary Regrading Lens","Blessing of Esh","Blessing of Tul","Blessing of Xoph","Divine Orb","Hunter's Exalted Orb","Blessing of Uul-Netol","Prime Regrading Lens","Sacred Orb","Fracturing Shard","Otherworldly Scouting Report","Orb of Conflict","Elevated Sextant","Awakener's Orb","Redeemer's Exalted Orb","Crusader's Exalted Orb","Exceptional Eldritch Ember","Exceptional Eldritch Ichor","Warlord's Exalted Orb","Tainted Blessing","Eldritch Chaos Orb","Eldritch Orb of Annulment","Tainted Exalted Orb","Tainted Orb of Fusing","Exalted Orb","Comprehensive Scouting Report","Charged Compass","Eldritch Exalted Orb","Oil Extractor","Tainted Mythic Orb","Prismatic Catalyst","Veiled Chaos Orb","Orb of Annulment","Ancient Orb","Unstable Catalyst","Ritual Vessel","Tainted Chaos Orb","Blighted Scouting Report","Grand Eldritch Ember","Awakened Sextant","Grand Eldritch Ichor","Influenced Scouting Report","Tainted Chromatic Orb","Fertile Catalyst","Stacked Deck","Accelerating Catalyst","Delirious Scouting Report","Singular Scouting Report","Surveyor's Compass","Harbinger's Orb","Annulment Shard","Exalted Shard","Tainted Armourer's Scrap","Tainted Jeweller's Orb","Orb of Scouring","Greater Eldritch Ember","Greater Eldritch Ichor","Orb of Unmaking","Enkindling Orb","Vaal Orb","Gemcutter's Prism","Explorer's Scouting Report","Intrinsic Catalyst","Noxious Catalyst","Regal Orb","Tempering Catalyst","Vaal Scouting Report","Orb of Regret","Blessed Orb","Turbulent Catalyst","Tainted Blacksmith's Whetstone","Orb of Fusing","Lesser Eldritch Ichor","Orb of Horizons","Glassblower's Bauble","Instilling Orb","Abrasive Catalyst","Orb of Chance","Cartographer's Chisel","Engineer's Orb","Imbued Catalyst","Lesser Eldritch Ember","Chromatic Orb","Orb of Alteration","Jeweller's Orb","Vivid Crystallised Lifeforce","Orb of Augmentation","Orb of Alchemy","Portal Scroll","Blacksmith's Whetstone","Orb of Binding","Wild Crystallised Lifeforce","Primal Crystallised Lifeforce","Orb of Transmutation","Armourer's Scrap","Scroll of Wisdom","Maven's Orb"
//                );
//
//        for(String s:strings) {
//            System.out.println(
//                    s.toUpperCase().replace(" ", "_")
//                            .replace("'", "")
//                            .replace("-", "")
//                            .replace(",", "")
//                    +"(\""+s.toLowerCase()+"\", ItemDefinitionEnumCategoryMapper.CURRENCY),"
//                    );
//        }




        SpringApplication.run(ExileeconomicsApplication.class, args);
    }
}
