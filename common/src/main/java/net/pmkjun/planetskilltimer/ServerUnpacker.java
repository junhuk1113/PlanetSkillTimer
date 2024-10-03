package net.pmkjun.planetskilltimer;

import net.minecraft.client.MinecraftClient;
import net.pmkjun.planetskilltimer.util.PackExtractor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerUnpacker{
    public static final Logger LOGGER = LogManager.getLogger("Server Unpacker");

	public static void extractServerPack(File file) {
		ServerUnpacker.LOGGER.info("Extracting server pack {}", file);

		var info = MinecraftClient.getInstance().getCurrentServerEntry();
		String name = info == null ? file.getName() : info.address;
		Path destination = Paths.get(System.getProperty("user.dir"), "resourcepacks/");
		try {
			PackExtractor.saveZipFile(destination, file, name+".zip");
		} catch (Exception exception) {
			LOGGER.error(exception);
		}
	}

}