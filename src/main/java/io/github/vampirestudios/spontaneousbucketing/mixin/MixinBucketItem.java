package io.github.vampirestudios.spontaneousbucketing.mixin;

import io.github.vampirestudios.spontaneousbucketing.impl.BucketMaterial;
import io.github.vampirestudios.spontaneousbucketing.impl.BucketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class MixinBucketItem {
	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN"), name = "itemStack2", method = "use")
	private ItemStack bucketing$getFilledBucket(ItemStack stack) {
		System.out.println(stack.toString());
		System.out.println(Registry.ITEM.getId((Item)(Object) this).toString());
		BucketMaterial material = BucketRegistry.getMaterialFromBucket((Item)(Object) this);
		BucketMaterial wrongMaterial = BucketRegistry.getMaterialFromBucket(stack.getItem());
		Identifier bucketType = wrongMaterial.getTypeFromBucket(stack.getItem());
		return new ItemStack(material.getBucketFromType(bucketType), stack.getCount());
	}

	@Inject(at = @At("RETURN"), method = "getEmptiedStack", cancellable = true)
	private void bucketing$getEmptyBucket(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
		BucketMaterial bucketMaterial = BucketRegistry.getMaterialFromBucket(stack.getItem());
		cir.setReturnValue(!player.abilities.creativeMode ? new ItemStack(bucketMaterial.getBucketFromType(new Identifier("empty"))) : stack);
	}
}