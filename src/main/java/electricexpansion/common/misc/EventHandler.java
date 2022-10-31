package electricexpansion.common.misc;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.ElectricExpansion;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.WorldEvent;

public class EventHandler {
    @SubscribeEvent
    public void onEntityDropItems(final LivingDropsEvent event) {
        if (((EntityEvent) event).entity != null &&
                ((EntityEvent) event).entity instanceof EntitySkeleton &&
                ((EntitySkeleton) ((EntityEvent) event).entity).getSkeletonType() == 1) {
            final Random dropNumber = new Random();
            final int numberOfDrops = dropNumber.nextInt(4) + 1;
            final ItemStack leadIS = new ItemStack(ElectricExpansionItems.itemParts, numberOfDrops, 7);
            event.drops.add(new EntityItem(
                    ((Entity) event.entityLiving).worldObj,
                    ((Entity) event.entityLiving).posX, ((Entity) event.entityLiving).posY,
                    ((Entity) event.entityLiving).posZ, leadIS.copy()));
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onWorldSave(final WorldEvent.Save event) {
        ElectricExpansion.DistributionNetworksInstance.onWorldSave(
                (WorldEvent) event);
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onWorldLoad(final WorldEvent.Load event) {
        (ElectricExpansion.DistributionNetworksInstance = new DistributionNetworks())
                .onWorldLoad();
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onWorldUnload(final WorldEvent.Unload event) {
        ElectricExpansion.DistributionNetworksInstance.onWorldSave(
                (WorldEvent) event);
    }
}
