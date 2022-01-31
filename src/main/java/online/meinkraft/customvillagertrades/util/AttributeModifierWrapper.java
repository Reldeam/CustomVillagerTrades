package online.meinkraft.customvillagertrades.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class AttributeModifierWrapper {

    private final Attribute attribute;
    private final AttributeModifier modifier;

    public AttributeModifierWrapper(
        Attribute attribute, 
        AttributeModifier modifier
    ) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public Attribute geAttribute() {
        return attribute;
    }

    public AttributeModifier getModifier() {
        return modifier;
    }

}