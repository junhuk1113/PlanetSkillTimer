package net.pmkjun.planetskilltimer.gui.widget;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
public class Slider extends AbstractSliderButton {
    Component text;
    protected double minValue;
    protected double maxValue;
    protected double stepSize;
    private boolean sliderFocused;

    public Slider(int x, int y, int width, int height, Component text, float minValue, float maxValue, double currentValue) {
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
            return Mth.clamp(value, 0D, 1D);

        value = Mth.lerp(Mth.clamp(value, 0D, 1D), this.minValue, this.maxValue);

        value = (stepSize * Math.round(value / stepSize));

        if (this.minValue > this.maxValue)
        {
            value = Mth.clamp(value, this.maxValue, this.minValue);
        }
        else
        {
            value = Mth.clamp(value, this.minValue, this.maxValue);
        }

        return Mth.map(value, this.minValue, this.maxValue, 0D, 1D);
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (CommonInputs.selected(keyCode)) {
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
        this.setMessage(Component.literal("").append(text).append(String.valueOf(this.getValueInt())));
    }

    @Override
    protected void applyValue() {

    }
}
