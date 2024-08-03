package tamaized.beatdownstick;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(BeatDownStick.MODID)
public class BeatDownStick {

	public static final String MODID = "beatdownstick";

	public static final ResourceKey<DamageType> ANNIHILATE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "annihilate"));

	public static final TagKey<EntityType<?>> DONT_ONE_SHOT = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "stick_doesnt_one_shot"));

	public static final DeferredRegister.Items ITEM_REGISTRY = DeferredRegister.createItems(MODID);
	public static final DeferredRegister<SoundEvent> SOUND_REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, MODID);

	public static final DeferredItem<Item> BEAT_DOWN_STICK = ITEM_REGISTRY.register("beatdownstick", () -> new ItemBeatDownStick(new Item.Properties().stacksTo(1).durability(21), false));
	public static final DeferredItem<Item> SUPER_BEAT_DOWN_STICK = ITEM_REGISTRY.register("superbeatdownstick", () -> new ItemBeatDownStick(new Item.Properties().stacksTo(1).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true), true));

	public static final DeferredHolder<SoundEvent, SoundEvent> WHAM = SOUND_REGISTRY.register("item.beatdownstick.wham", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "item.beatdownstick.wham")));

	public BeatDownStick(IEventBus bus) {
		ITEM_REGISTRY.register(bus);
		SOUND_REGISTRY.register(bus);
		bus.addListener(BuildCreativeModeTabContentsEvent.class, event -> {
			if (event.getTabKey() == CreativeModeTabs.COMBAT) {
				event.accept(BEAT_DOWN_STICK::get);
				event.accept(SUPER_BEAT_DOWN_STICK::get);
			}
		});
	}
}
