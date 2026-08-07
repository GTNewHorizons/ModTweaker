package modtweaker2.mods.thaumcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.player.IPlayer;
import modtweaker2.helpers.InputHelper;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import modtweaker2.mods.thaumcraft.research.*;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage.PageType;

@ZenClass("mods.thaumcraft.Research")
public class Research {

    private static final ResourceLocation defaultBackground = new ResourceLocation(
            "thaumcraft",
            "textures/gui/gui_researchback.png");

    @ZenMethod
    public static void addTab(String key, String iconDomain, String iconPath) {
        addTab(key, iconDomain, iconPath, null, null);
    }

    @ZenMethod
    public static void addTab(String key, String iconDomain, String iconPath, String backDomain, String backPath) {
        ResourceLocation icon = new ResourceLocation(iconDomain, iconPath);
        ResourceLocation background;
        if (backPath == null) background = defaultBackground;
        else background = new ResourceLocation(backDomain, backPath);
        addTab(key, icon, background);
    }

    private static void addTab(String key, ResourceLocation icon, ResourceLocation background) {

        MineTweakerAPI.apply(new AddTab(key, icon, background));
    }

    @ZenMethod
    public static void removeTab(String tab) {

        MineTweakerAPI.apply(new RemoveTab(tab));
    }

    @ZenMethod
    public static void removeResearch(String research) {

        MineTweakerAPI.apply(new RemoveResearch(research));
    }

    @ZenMethod
    public static void orphanResearch(String research) {

        MineTweakerAPI.apply(new OrphanResearch(research));
    }

    @ZenMethod
    public static void addResearch(String key, String tab, @Optional String aspects, @Optional int x, @Optional int y,
            @Optional int difficulty, @Optional String domain, @Optional String path) {

        MineTweakerAPI.apply(
                new AddResearch(
                        new ResearchItem(
                                key,
                                tab,
                                ThaumcraftHelper.parseAspects(aspects),
                                x,
                                y,
                                difficulty,
                                new ResourceLocation(domain, path)),
                        null,
                        null,
                        null));
    }

    @ZenMethod
    public static void addResearch(String key, String tab, @Optional String aspects, @Optional int x, @Optional int y,
            @Optional int difficulty, @Optional IItemStack item) {

        MineTweakerAPI.apply(
                new AddResearch(
                        new ResearchItem(
                                key,
                                tab,
                                ThaumcraftHelper.parseAspects(aspects),
                                x,
                                y,
                                difficulty,
                                toStack(item)),
                        null,
                        null,
                        null));
    }

    @ZenMethod
    public static void addResearch(String key, String tab, @Optional String aspects, @Optional int x, @Optional int y,
            @Optional int difficulty, @Optional IItemStack item, @Optional IItemStack[] triggers,
            @Optional String[] entTriggers, @Optional String aspectsTrigger) {

        MineTweakerAPI.apply(
                new AddResearch(
                        new ResearchItem(
                                key,
                                tab,
                                ThaumcraftHelper.parseAspects(aspects),
                                x,
                                y,
                                difficulty,
                                toStack(item)),
                        InputHelper.toStacks(triggers),
                        entTriggers,
                        ThaumcraftHelper.parseAspects(aspectsTrigger)));
    }

    @ZenMethod
    public static void addPage(String key, String unlocalizedPageName) {

        MineTweakerAPI.apply(new AddPage(key, PageType.TEXT, unlocalizedPageName));
    }

    @ZenMethod
    public static void addPage(String research, String unlocalizedPageName, String prereqResearch) {

        MineTweakerAPI.apply(new AddPage(research, unlocalizedPageName, prereqResearch));
    }

    @ZenMethod
    public static void addCraftingPage(String key, IIngredient items) {
        MineTweakerAPI.apply(new AddPage(key, PageType.NORMAL_CRAFTING, items.getItems()));
    }

    @ZenMethod
    public static void addCruciblePage(String key, IIngredient items) {

        MineTweakerAPI.apply(new AddPage(key, PageType.CRUCIBLE_CRAFTING, items.getItems()));
    }

    @ZenMethod
    public static void addArcanePage(String key, IIngredient items) {

        MineTweakerAPI.apply(new AddPage(key, PageType.ARCANE_CRAFTING, items.getItems()));
    }

    @ZenMethod
    public static void addInfusionPage(String key, IIngredient items) {

        MineTweakerAPI.apply(new AddPage(key, PageType.INFUSION_CRAFTING, items.getItems()));
    }

    @ZenMethod
    public static void addEnchantmentPage(String key, int i) {

        MineTweakerAPI.apply(new AddPage(key, PageType.INFUSION_ENCHANTMENT, Enchantment.enchantmentsList[i]));
    }

    @ZenMethod
    public static void clearPages(String key) {

        MineTweakerAPI.apply(new ClearPages(key));
    }

    @ZenMethod
    public static void addPrereq(String key, String req, @Optional boolean hidden) {

        MineTweakerAPI.apply(new AddPrereq(key, req, hidden));
    }

    @ZenMethod
    public static void clearPrereqs(String key) {

        MineTweakerAPI.apply(new ClearPrereqs(key));
    }

    @ZenMethod
    public static void addSibling(String key, String sibling) {

        MineTweakerAPI.apply(new AddSibling(key, sibling));
    }

    @ZenMethod
    public static void clearSiblings(String key) {

        MineTweakerAPI.apply(new ClearSiblings(key));
    }

    @ZenMethod
    public static void setRound(String key, boolean flag) {

        MineTweakerAPI.apply(new SetResearch(key, flag, SetType.ROUND));
    }

    @ZenMethod
    public static void setSpikey(String key, boolean flag) {

        MineTweakerAPI.apply(new SetResearch(key, flag, SetType.SPIKE));
    }

    @ZenMethod
    public static void setStub(String key, boolean flag) {

        MineTweakerAPI.apply(new SetResearch(key, flag, SetType.STUB));
    }

    @ZenMethod
    public static void setSecondary(String key, boolean flag) {

        MineTweakerAPI.apply(new SetResearch(key, flag, SetType.SECONDARY));
    }

    @ZenMethod
    public static void setVirtual(String key, boolean flag) {

        MineTweakerAPI.apply(new SetResearch(key, flag, SetType.VIRTUAL));
    }

    @ZenMethod
    public static void setAutoUnlock(String key, boolean flag) {

        MineTweakerAPI.apply(new SetResearch(key, flag, SetType.AUTO));
    }

    @ZenMethod
    public static void setConcealed(String key, boolean flag) {

        MineTweakerAPI.apply(new SetResearch(key, flag, SetType.CONCEAL));
    }

    @ZenMethod
    public static void setHidden(String key, boolean flag) {
        MineTweakerAPI.apply(new SetResearch(key, flag, SetType.HIDDEN));
    }

    @ZenMethod
    public static void setAspects(String key, String aspects) {

        MineTweakerAPI.apply(new SetAspects(key, ThaumcraftHelper.parseAspects(aspects)));
    }

    @ZenMethod
    public static void setComplexity(String key, int complexity) {

        MineTweakerAPI.apply(new Difficulty(key, complexity));
    }

    @ZenMethod
    public static void refreshResearchRecipe(String key) {

        MineTweakerAPI.apply(new RefreshResearch(key));
    }

    @ZenMethod
    public static void moveResearch(String key, String destination, int x, int y) {

        MineTweakerAPI.apply(new MoveResearch(key, destination, x, y));
    }

    @ZenMethod
    public static boolean hasResearched(IPlayer player, String key) {

        return ThaumcraftApiHelper.isResearchComplete(player.getName(), key);
    }

    @ZenMethod
    public static void addItemTriggers(String res, IIngredient iitem) {

        MineTweakerAPI.apply(new SetItemTriggers(res, iitem.getItems()));
    }

    @ZenMethod
    public static void clearItemTriggers(String res) {

        MineTweakerAPI.apply(new SetItemTriggers(res));
    }

    @ZenMethod
    public static void addEntityTriggers(String res, String entity) {

        MineTweakerAPI.apply(new SetEntityTriggers(res, entity));
    }

    @ZenMethod
    public static void clearEntityTriggers(String res) {

        MineTweakerAPI.apply(new SetEntityTriggers(res));
    }

    public enum SetType {
        AUTO,
        ROUND,
        SPIKE,
        SECONDARY,
        STUB,
        VIRTUAL,
        CONCEAL,
        HIDDEN
    }
}
