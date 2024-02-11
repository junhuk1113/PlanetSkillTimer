package net.pmkjun.planetskilltimer.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.navigation.GuiNavigationType;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class Slider extends SliderWidget {
    Text text;
    protected double minValue;
    protected double maxValue;
    protected double stepSize;
    private boolean sliderFocused;

    public Slider(int x, int y, int width, int height, Text text, float minValue, float maxValue, double currentValue) {
        super(x, y, width, height, text, currentValue);
        this.text = text;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = 1D;
        this.value = this.snapToNearest((currentValue - minValue) / (maxValue - minValue));
        this.updateMessage();
    }

    private double snapToNearest(double value)
    {
        if(stepSize <= 0D)
            return MathHelper.clamp(value, 0D, 1D);

        value = MathHelper.lerp(MathHelper.clamp(value, 0D, 1D), this.minValue, this.maxValue);

        value = (stepSize * Math.round(value / stepSize));

        if (this.minValue > this.maxValue)
        {
            value = MathHelper.clamp(value, this.maxValue, this.minValue);
        }
        else
        {
            value = MathHelper.clamp(value, this.minValue, this.maxValue);
        }

        return MathHelper.map(value, this.minValue, this.maxValue, 0D, 1D);
    }

    public double getValue()
    {
        return this.value * (maxValue - minValue) + minValue;
    }
    public long getValueLong()
    {
        return Math.round(this.getValue());
    }
    public int getValueInt()
    {
        return (int) this.getValueLong();
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.sliderFocused = false;
        } else {
            GuiNavigationType guiNavigationType = MinecraftClient.getInstance().getNavigationType();
            if (guiNavigationType == GuiNavigationType.MOUSE || guiNavigationType == GuiNavigationType.KEYBOARD_TAB) {
                this.sliderFocused = true;
            }

        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (KeyCodes.isToggle(keyCode)) {
            this.sliderFocused = !this.sliderFocused;
            return true;
        } else {
            if (this.sliderFocused) {
                boolean bl = keyCode == 263;
                if (bl || keyCode == 262) {
                    float f = bl ? -1.0F : 1.0F;
                    this.setValue(this.getValue() + f * this.stepSize);
                    return true;
                }
            }

            return false;
        }
    }

    private void setValue(double value) {
        this.value = this.snapToNearest((value - this.minValue) / (this.maxValue - this.minValue));
        this.updateMessage();
        this.applyValue();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.literal("").append(text).append(String.valueOf(this.getValueInt())));
    }

    @Override
    protected void applyValue() {

    }
}
