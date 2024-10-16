package net.pmkjun.planetskilltimer.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.pmkjun.planetskilltimer.PlanetSkillTimerClient;
import net.pmkjun.planetskilltimer.file.Skill;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class chatMixin {



	@Shadow @Final private MinecraftClient client;

	@Inject(at = @At(value = "RETURN", ordinal = 0), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V")
	private void addMessageMixin(Text message, @Nullable MessageSignatureData signature, int ticks, @Nullable MessageIndicator indicator, boolean refresh, CallbackInfo ci) {

		// This code is injected into the start of MinecraftServer.loadWorld()V
		if(message.getString().contains(" 발동되었습니다!") && !message.getString().contains("|")){
			//System.out.println("변수 초기화 됨");
			for (int i = 0; i < Skill.list.length ; i++)
			{
				if(message.getString().contains(Skill.list[i])){
					//System.out.println(Skill.list[i]+" 발동감지!");
					PlanetSkillTimerClient.getInstance().updateLastSkilltime(i);
				}
			}
		}
		//System.out.println(message.getString());
	}
}