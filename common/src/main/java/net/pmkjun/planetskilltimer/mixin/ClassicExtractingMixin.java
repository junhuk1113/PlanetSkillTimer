package net.pmkjun.planetskilltimer.mixin;

import net.minecraft.client.resource.ServerResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.pmkjun.planetskilltimer.ServerUnpacker;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Mixin(ServerResourcePackProvider.class)
public class ClassicExtractingMixin {

	@Inject(at = @At("TAIL"), method = "loadServerPack")
    public void loadServerPack(File file, ResourcePackSource source, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
		  ServerUnpacker.extractServerPack(file);
      
    }

}