package modtweaker2.utils;

import java.util.Map;
import java.util.Map.Entry;

import modtweaker2.helpers.LogHelper;

public abstract class BaseMapRemoval<K, V> extends BaseMapModification<K, V> {

    protected BaseMapRemoval(String name, Map<K, V> map) {
        super(name, map);
    }

    protected BaseMapRemoval(String name, Map<K, V> map, Map<K, V> recipes) {
        this(name, map);

        if (recipes != null) {
            this.recipes.putAll(recipes);
        }
    }

    @Override
    public void apply() {
        if (recipes.isEmpty()) return;

        for (K key : recipes.keySet()) {
            V oldValue = map.remove(key);

            if (oldValue != null) {
                successful.put(key, oldValue);
            } else {
                LogHelper.logError(String.format("Error removing %s Recipe : null object", name));
            }
        }
    }

    @Override
    public void undo() {
        if (successful.isEmpty()) return;

        for (Entry<K, V> entry : successful.entrySet()) {
            if (entry != null) {
                V oldValue = map.put(entry.getKey(), entry.getValue());
                if (oldValue != null) {
                    LogHelper.logWarning(
                            String.format("Overwritten %s Recipe for %s while restoring.", name, getRecipeInfo(entry)));
                }
            }
        }
    }

    @Override
    public String describe() {
        return String.format("[ModTweaker2] Removing %d %s Recipe(s) for %s", recipes.size(), name, getRecipeInfo());
    }

    @Override
    public String describeUndo() {
        return String.format("[ModTweaker2] Restoring %d %s Recipe(s) for %s", recipes.size(), name, getRecipeInfo());
    }
}
