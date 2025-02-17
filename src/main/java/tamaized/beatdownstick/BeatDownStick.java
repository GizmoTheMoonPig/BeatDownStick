package tamaized.beatdownstick;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.beatdownstick.common.items.ItemBeatDownStick;
import tamaized.beatdownstick.common.loot.BeatDownStickModifier;

import java.util.function.Consumer;

@Mod(BeatDownStick.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeatDownStick {

	public static final String MODID = "beatdownstick";

	public static final DamageSource DAMAGE_SOURCE_ANNIHILATE = new DamageSource(BeatDownStick.MODID + ".annihilate").bypassArmor().bypassInvul();

	public static final TagKey<EntityType<?>> DONT_ONE_SHOT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MODID, "stick_doesnt_one_shot"));

	public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);
	public static final DeferredRegister<SoundEvent> SOUND_REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

	public static final RegistryObject<Item> BEAT_DOWN_STICK = ITEM_REGISTRY.register("beatdownstick", () -> new ItemBeatDownStick(new Item.Properties().stacksTo(1).durability(21), false));
	public static final RegistryObject<Item> SUPER_BEAT_DOWN_STICK = ITEM_REGISTRY.register("superbeatdownstick", () -> new ItemBeatDownStick(new Item.Properties().stacksTo(1), true));

	public static final RegistryObject<Codec<BeatDownStickModifier>> BEAT_DOWN_STICK_LOOT_INJECTION = LOOT_MODIFIER_REGISTRY.register("loot_injection", () -> BeatDownStickModifier.CODEC);

	public static final RegistryObject<SoundEvent> WHAM = SOUND_REGISTRY.register("item.beatdownstick.wham", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "item.beatdownstick.wham")));

	public BeatDownStick() {
		ITEM_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		LOOT_MODIFIER_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		SOUND_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<CreativeModeTabEvent.BuildContents>) event -> {
			if (event.getTab() == CreativeModeTabs.COMBAT) {
				event.accept(BEAT_DOWN_STICK::get);
				event.accept(SUPER_BEAT_DOWN_STICK::get);
			}
		});
	}

}
