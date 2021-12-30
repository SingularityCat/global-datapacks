package net.midnightspark.minecraft.globaldatapacks;

import java.io.File;

import net.minecraft.server.packs.PackType;

import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("globaldatapacks")
public class GlobalDatapacks {
    public GlobalDatapacks() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void onAddPackFinders(AddPackFindersEvent pfe) {
        if (pfe.getPackType() == PackType.SERVER_DATA) {
            pfe.addRepositorySource(new DirectoryRepositorySource("gdp", new File("datapacks")));
        }
    }
}