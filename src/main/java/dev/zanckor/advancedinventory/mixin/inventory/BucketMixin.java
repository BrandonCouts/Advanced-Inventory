package dev.zanckor.advancedinventory.mixin.inventory;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BucketItem.class, priority = 900)
public abstract class BucketMixin extends Item {

    @Shadow
    @Final
    private Fluid content;

    @Shadow
    public abstract boolean emptyContents(@Nullable Player p_150716_, Level p_150717_, BlockPos p_150718_, @Nullable BlockHitResult p_150719_, @Nullable ItemStack container);

    @Shadow
    protected abstract boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate);


    @Shadow
    public abstract void checkExtraContent(@Nullable Player p_150711_, Level p_150712_, ItemStack p_150713_, BlockPos p_150714_);

    public BucketMixin(Properties p_41383_) {
        super(p_41383_);
    }


    /**
     * @author Zanckor
     * @reason Fix bucket empty use glitch. It's a bad praxis to use overwrite, but it's the only way to fix the issue.
     */
    @Overwrite
    public InteractionResultHolder<ItemStack> use(Level p_40703_, Player p_40704_, InteractionHand p_40705_) {
        ItemStack itemstack = p_40704_.getItemInHand(p_40705_);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(p_40703_, p_40704_, this.content == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(p_40704_, p_40703_, itemstack, blockhitresult);
        if (ret != null) return ret;
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (p_40703_.mayInteract(p_40704_, blockpos) && p_40704_.mayUseItemAt(blockpos1, direction, itemstack)) {
                if (this.content == Fluids.EMPTY) {
                    BlockState blockstate1 = p_40703_.getBlockState(blockpos);
                    if (blockstate1.getBlock() instanceof BucketPickup) {
                        BucketPickup bucketpickup = (BucketPickup) blockstate1.getBlock();
                        ItemStack itemstack1 = bucketpickup.pickupBlock(p_40703_, blockpos, blockstate1);
                        if (!itemstack1.isEmpty()) {
                            p_40704_.awardStat(Stats.ITEM_USED.get(this));
                            bucketpickup.getPickupSound(blockstate1).ifPresent((p_150709_) -> {
                                p_40704_.playSound(p_150709_, 1.0F, 1.0F);
                            });
                            p_40703_.gameEvent(p_40704_, GameEvent.FLUID_PICKUP, blockpos);
                            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, p_40704_, itemstack1);
                            if (!p_40703_.isClientSide) {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) p_40704_, itemstack1);
                            }

                            return InteractionResultHolder.sidedSuccess(itemstack2, p_40703_.isClientSide());
                        }
                    }

                    return InteractionResultHolder.fail(itemstack);
                } else {
                    BlockState blockstate = p_40703_.getBlockState(blockpos);
                    BlockPos blockpos2 = canBlockContainFluid(p_40703_, blockpos, blockstate) ? blockpos : blockpos1;
                    if (this.emptyContents(p_40704_, p_40703_, blockpos2, blockhitresult, itemstack)) {
                        checkExtraContent(p_40704_, p_40703_, itemstack, blockpos2);
                        if (p_40704_ instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) p_40704_, blockpos2, itemstack);
                        }

                        p_40704_.awardStat(Stats.ITEM_USED.get(this));

                        if(!p_40704_.getAbilities().instabuild) {

                            if(itemstack.getCount() > 1) {
                                itemstack.shrink(1);
                                p_40704_.addItem(new ItemStack(Items.BUCKET));
                            } else {
                                itemstack = new ItemStack(Items.BUCKET);
                            }
                        }

                        return InteractionResultHolder.success(itemstack);
                    } else {
                        return InteractionResultHolder.fail(itemstack);
                    }
                }
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }
}
