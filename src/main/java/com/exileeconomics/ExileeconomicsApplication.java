package com.exileeconomics;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.definitions.ItemDefinitionEnumCategoryMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class ExileeconomicsApplication {
    public static void main(String[] args) {
//        List<String> strings = List.of(
//                "Progenesis, Amethyst Flask",
//                "Oriath's End, Bismuth Flask",
//                "Bottled Faith, Sulphur Flask",
//                "Starlight Chalice, Iron Flask",
//                "Olroth's Resolve, Iron Flask",
//                "Bottled Faith, Sulphur Flask",
//                "Dying Sun, Ruby Flask",
//                "Soul Ripper, Quartz Flask",
//                "Taste of Hate, Sapphire Flask",
//                "Dying Sun, Ruby Flask",
//                "Elixir of the Unbroken Circle, Iron Flask",
//                "Replica Rumi's Concoction, Granite Flask",
//                "Replica Sorrow of the Divine, Sulphur Flask",
//                "Sin's Rebirth, Stibnite Flask",
//                "Cinderswallow Urn, Silver Flask",
//                "Replica Lavianga's Spirit, Sanctified Mana Flask",
//                "Vessel of Vinktar, Topaz Flask",
//                "Divination Distillate, Large Hybrid Flask",
//                "Lion's Roar, Granite Flask",
//                "Rumi's Concoction, Granite Flask",
//                "Vorana's Preparation, Iron Flask",
//                "Zerphi's Last Breath, Grand Mana Flask",
//                "Coruscating Elixir, Ruby Flask",
//                "Atziri's Promise, Amethyst Flask",
//                "The Wise Oak, Bismuth Flask",
//                "Witchfire Brew, Stibnite Flask",
//                "Blood of the Karui, Sanctified Life Flask",
//                "Coralito's Signature, Diamond Flask",
//                "Doedre's Elixir, Greater Mana Flask",
//                "Forbidden Taste, Quartz Flask",
//                "Kiara's Determination, Silver Flask",
//                "Lavianga's Spirit, Sanctified Mana Flask",
//                "Rotgut, Quicksilver Flask",
//                "Soul Catcher, Quartz Flask",
//                "The Overflowing Chalice, Sulphur Flask",
//                "The Sorrow of the Divine, Sulphur Flask",
//                "The Writhing Jar, Hallowed Hybrid Flask"
//
//                );
//
//        for(String s:strings) {
//            String name = s.split(",")[0].toLowerCase();
//            System.out.println(
//                    s.toUpperCase().replace(" ", "_")
//                            .replace("'", "")
//                            .replace("-", "")
//                            .replace(",", "")
//                    +"(\""+name+"\", ItemDefinitionEnumCategoryMapper.UNIQUE_FLAKS),"
//                    );
//        }

//        ItemDefinitionEnum[] itemDefinitionEnums = ItemDefinitionEnum.values();
//
//        for(ItemDefinitionEnum item: itemDefinitionEnums) {
//            String name = item.getName().split(",")[0].toLowerCase();
//            System.out.println(
//                    item.getName().toUpperCase().replace(" ", "_")
//                            .replace("'", "")
//                            .replace("-", "")
//                            .replace(":", "")
//                            .replace(",", "")
//                    +"(\""+name+"\", ItemDefinitionEnumCategoryMapper."+ item.getCategory().name().toUpperCase()+"),"
//            );
//        }
//        THE_ONE_THAT_GOT_AWAY("the one that got away", ItemDefinitionEnumCategoryMapper.DIVINATION_CARD),


        SpringApplication.run(ExileeconomicsApplication.class, args);
    }
}
