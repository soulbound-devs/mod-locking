package net.vakror.mod_locking;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;

public record Tooltip(List<Component> tooltip) {

    public static Tooltip empty() {
        return new Tooltip(new ArrayList<>());
    }

    public static class TooltipBuilder {
        private final List<TooltipComponent> tooltips = new ArrayList<>();

        public TooltipBuilder addPart(TooltipComponent part) {
            tooltips.add(part);
            return this;
        }

        public Tooltip build() {
            List<Component> tooltip = new ArrayList<>();
            tooltips.forEach((component) -> {
                tooltip.add(component.getTooltip());
            });
            return new Tooltip(tooltip);
        }
    }

    public static class TooltipComponent {

        private final Component tooltip;

        private TooltipComponent(MutableComponent tooltip, Style style) {
            this.tooltip = tooltip.withStyle(style);
        }

        public Component getTooltip() {
            return tooltip;
        }

        public static TooltipComponent empty() {
            return new TooltipComponent(Component.empty(), Style.EMPTY);
        }
    }

    public static class TooltipComponentBuilder {
        private final MutableComponent tooltip = Component.literal("");
        private Style style = Style.EMPTY;

        public TooltipComponentBuilder addPartWithNewline(String part) {
            tooltip.append(part);
            tooltip.append("\n");
            return this;
        }

        public TooltipComponentBuilder addPartWithNewline(String part, ColorCode color) {
            tooltip.append("§" + color.id + part);
            tooltip.append("\n");
            return this;
        }

        public TooltipComponentBuilder addPart(String part) {
            tooltip.append(part);
            return this;
        }

        public TooltipComponentBuilder addPart(String part, ColorCode color) {
            tooltip.append("§" + color.id + part);
            return this;
        }

        public TooltipComponentBuilder setStyle(Style style) {
            this.style = style;
            return this;
        }

        public TooltipComponentBuilder setStyle(ChatFormatting style) {
            this.style = Style.EMPTY.applyFormat(style);
            return this;
        }

        public TooltipComponent build() {
            return new TooltipComponent(tooltip, style);
        }

        public enum ColorCode {
            BLACK(0),
            DARK_BLUE(1),
            GREEN(2),
            CYAN(3),
            DARK_RED(4),
            PURPLE(5),
            GOLD(6),
            LIGHT_GRAY(7),
            GRAY(8),
            BLUE(9),
            LIGHT_GREEN("A"),
            LIGHT_BLUE("B"),
            RED("C"),
            PINK("D"),
            YELLOW("E"),
            WHITE("F");

            final String id;

            ColorCode(String id) {
                this.id = id;
            }

            ColorCode(int id) {
                this.id = String.valueOf(id);
            }

            public String getId() {
                return id;
            }
        }
    }
}
