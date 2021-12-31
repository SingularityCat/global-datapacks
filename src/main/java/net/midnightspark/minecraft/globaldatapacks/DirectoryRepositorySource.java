package net.midnightspark.minecraft.globaldatapacks;

import java.io.File;
import java.util.function.Consumer;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.Pack.PackConstructor;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.PackResources;

public class DirectoryRepositorySource implements RepositorySource {
    final String prefix; 
    final File directory;
    
    public DirectoryRepositorySource(String name_prefix, File directory) {
        this.prefix = name_prefix + ":";
        this.directory = directory;
    }

    @Override
    public void loadPacks(Consumer<Pack> reg, PackConstructor fac) {
        if (!directory.isDirectory()) {
            // TODO: log something.
            return;
        }

        for (File f : directory.listFiles()) {
            final String packName = prefix + f.getName();
            final boolean isDir;
            
            if (f.isDirectory() && (new File(f, "pack.mcmeta")).exists()) {
                isDir = true;
            } else if (f.isFile() && f.getName().endsWith(".zip")) {
                isDir = false;
            } else {
                // TODO: log something.
                continue;
            }
            
            try (final PackResources packRes = isDir ? new FolderPackResources(f) : new FilePackResources(f);) {
                /* Documentation for 'Pack.create' is lacking, so:
                 *  - pack name
                 *  - required (true/false)
                 *  - some PackResources implementation
                 *  - the pack constructor factory
                 *  - pack position (priority)
                 *  - pack origin
                 */

                final Pack pack = Pack.create(packName, true, () -> packRes, fac, Pack.Position.TOP, PackSource.DEFAULT);
                reg.accept(pack);
            }
        }
    }
}
