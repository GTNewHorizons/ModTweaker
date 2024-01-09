package modtweaker2.mods.exnihilo;

import java.util.Map;

import exnihilo.registries.HeatRegistry;
import exnihilo.utils.ItemInfo;
import modtweaker2.helpers.ReflectionHelper;

public class ExNihiloHelper {

    public static Map<ItemInfo, Float> getHeatMap() {
        return ReflectionHelper.<Map<ItemInfo, Float>>getStaticObject(HeatRegistry.class, "heatmap");
    }
}
