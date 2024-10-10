package net.pmkjun.planetskilltimer.mixin;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.pmkjun.planetskilltimer.PlanetSkillTimerClient;
import net.pmkjun.planetskilltimer.file.Skill;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public abstract class chatMixin {
	@Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V",at = @At(value = "RETURN", ordinal = 0))
	private void addMessageMixin(Component message, @Nullable MessageSignature signature, int ticks, @Nullable GuiMessageTag indicator, boolean refresh, CallbackInfo ci) {

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