package it.hurts.metallurgy_reforged.util.handler;

import com.google.common.collect.Lists;
import it.hurts.metallurgy_reforged.Metallurgy;
import it.hurts.metallurgy_reforged.block.ModBlocks;
import it.hurts.metallurgy_reforged.config.ArmorEffectsConfig;
import it.hurts.metallurgy_reforged.config.GeneralConfig;
import it.hurts.metallurgy_reforged.config.ToolEffectsConfig;
import it.hurts.metallurgy_reforged.item.armor.ModArmors;
import it.hurts.metallurgy_reforged.item.tool.ModTools;
import it.hurts.metallurgy_reforged.material.ModMetals;
import it.hurts.metallurgy_reforged.util.Utils;
import it.hurts.metallurgy_reforged.util.custom_slot.ArmorCustomSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


/***************************
 *
 * Author : ItHurtsLikeHell
 * Project: Metallurgy-5
 * Date   : 28 ago 2018
 * Time   : 18:24:07
 *
 ***************************/

@EventBusSubscriber(modid= Metallurgy.MODID)
public class EventHandler {

	private static MovementInput inputCheck;

	//the speed sword modifier UUID
	public static final UUID SHADOW_STEEL_ARMOR_MODIFIER_UUID =  UUID.fromString("9bfd3581-6559-468f-a5a5-66c46ff7b70c");

//	Don't touch this
//	private final static double speed = 0.10000000149011612D;
//	Mithril Armor (Ultra istinto)
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void glowingArmorEffect(RenderLivingEvent.Pre<EntityLivingBase> ev)
	{
		
		//Get Client Side Player
		EntityPlayer pl = Minecraft.getMinecraft().player;
		if(pl != null && !ev.getEntity().equals(pl)) //Check if player exists and the Rendered Entity isn't the player himself
		{	 	
		 if(isPlayerWearingArmor(pl, new Item[] {ModArmors.mithril_helmet,ModArmors.mithril_chest,ModArmors.mithril_legs,ModArmors.mithril_boots}) && ev.getEntity().getDistance(Minecraft.getMinecraft().player) < 30D && !ev.getEntity().isGlowing() && ArmorEffectsConfig.mithrilArmorEffect) //checks if:  the player wears The Mithrill Armor, the rendered entity is not glowing and it's within 30 blocks from the player, the effect is not disabled in the config
		 {
			ev.getEntity().setGlowing(true);
		 } 
		 else 
		 {
			ev.getEntity().setGlowing(false);
		 }		
		 
		}
	}

	@SubscribeEvent
	public static void onArmorTick(PlayerTickEvent event)
	{		
        EntityPlayer pl = event.player; //The Player   
        World world = pl.world;
//		Astral Silver Armor (Jump Boost)
        if(isPlayerWearingArmor(event.player, new Item[] {ModArmors.astral_silver_helmet,ModArmors.astral_silver_chest,ModArmors.astral_silver_legs,ModArmors.astral_silver_boots}) && ArmorEffectsConfig.astralSilverArmorEffect)
    		event.player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 100, 1, false, false));
		
//		Celenegil Armor (Resistence)
		if(isPlayerWearingArmor(event.player, new Item[] {ModArmors.celenegil_helmet,ModArmors.celenegil_chest,ModArmors.celenegil_legs,ModArmors.celenegil_boots}) && ArmorEffectsConfig.celenegilArmorEffect)
			event.player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 3, false, false));
		
		
//		Deep Iron Armor (Swimming Speed when the player is in water and on ground)
		if(isPlayerWearingArmor(event.player, new Item[] {ModArmors.deep_iron_helmet,ModArmors.deep_iron_chest,ModArmors.deep_iron_legs,ModArmors.deep_iron_boots}) && event.player.isInWater() && ArmorEffectsConfig.deepIronArmorEffect){
			
//			Slot index of Armor : 5 - 6 - 7 - 8	
			 for(int i = 5;i < 9; i++)
			 {
				 if(!(pl.inventoryContainer.inventorySlots.get(i) instanceof ArmorCustomSlot) && !pl.isCreative()) {
//					 Inseriamo nello slot dell'inventario in posizione i un custom slot
                     pl.inventoryContainer.inventorySlots.set(i, new ArmorCustomSlot(pl, i - 5, true));
                 }
					 
			 }			
//			Add effect to Player
			pl.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 230, 3, false, false));
			pl.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 230, 1, false, false));
			
//		   Checks if the player is tourching ground
           if(pl.onGround) {
				//adds more motion in his movement
			  if(pl.motionX <= 3D)
			    pl.motionX *= 1.1D;
			  if(pl.motionZ <= 3D)
			    pl.motionZ *= 1.1D;
			}
			else
			{
//			    Stop player motion
				pl.motionX = 0D;
				pl.motionZ = 0D;
			}

//			The player can no longer swim upwards
			pl.motionY = -0.3D;

//			When the player is in the water he can step one block height like a horse
			if(pl.stepHeight != 1.0F)
				pl.stepHeight = 1.0F;
		}
		else //turns the stepHeight to normal if the player isn't wearing the deep iron armor or if he is not in water
		{
		  if(pl.stepHeight != 0.6F)
			pl.stepHeight = 0.6F;
		  	  
		    	 if(pl.inventoryContainer.inventorySlots.get(5) instanceof ArmorCustomSlot)
		    	 { 
//		    		 Insert in c the container "vanilla"
		    		 ContainerPlayer c = new ContainerPlayer(pl.inventory, !pl.world.isRemote, pl);
	    			 List<Slot> slots = c.inventorySlots;
		    		 for(int i = 5;i < 9; i++)
					 {
		    			 pl.inventoryContainer.inventorySlots.set(i, slots.get(i));
					 }
		    	 }
		    
		  
		}
		
//		Vulcanite Armor (Fire Immunity) //Removes Fire Render 
		if(isPlayerWearingArmor(event.player, new Item[] {ModArmors.vulcanite_helmet,ModArmors.vulcanite_chest,ModArmors.vulcanite_legs,ModArmors.vulcanite_boots}) && event.player.isBurning() && ArmorEffectsConfig.vulcaniteArmorEffect)
			event.player.extinguish();
 
//		Angmallen Armor (Luck I for Vampirism)
		if(isPlayerWearingArmor(event.player, new Item[] {ModArmors.angmallen_helmet,ModArmors.angmallen_chest,ModArmors.angmallen_legs,ModArmors.angmallen_boots}) && ArmorEffectsConfig.angmallenArmorEffect)
			event.player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 80, 0, false, false));
		

//		Kalendrite Armor (Strenght I)
		if(isPlayerWearingArmor(event.player, new Item[] {ModArmors.kalendrite_helmet,ModArmors.kalendrite_chest,ModArmors.kalendrite_legs,ModArmors.kalendrite_boots}) && ArmorEffectsConfig.kaledriteArmorEffect)
			event.player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 60, 0, false, false));
		

//		Amordrine Armor (Strenght II)
		if(isPlayerWearingArmor(event.player, new Item[] {ModArmors.amordrine_helmet,ModArmors.amordrine_chest,ModArmors.amordrine_legs,ModArmors.amordrine_boots}) && ArmorEffectsConfig.amordrineArmorEffect)
			event.player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 60, 1, false, false));
		

//		Adamantine Armor (Saturation)
		if(ArmorEffectsConfig.adamantineArmorEffect && !world.isRemote && isPlayerWearingArmor(event.player, new Item[] {ModArmors.adamantine_helmet,ModArmors.adamantine_chest,ModArmors.adamantine_legs,ModArmors.adamantine_boots}) && ArmorEffectsConfig.adamantineArmorEffect)
		{
			FoodStats foodStat = pl.getFoodStats();
			int amount = 2;						
			//quantity experience to remove
			float removeTot = (float)amount / (float)pl.xpBarCap();
			//check if the player needs food ,if he has enough experience and if the tick is a multiple of 20 (which means that the effect will be applied every second)
			if(pl instanceof EntityPlayerMP && pl.canEat(false) && (pl.experience >= removeTot || pl.experienceLevel > 0) && pl.ticksExisted % 20 == 0)
			{
				EntityPlayerMP mp = (EntityPlayerMP) pl;
				Random rand = new Random();				
				mp.experience -= removeTot;

		        if(mp.experienceTotal - amount >= 0)
		        	mp.experienceTotal -= amount;
		        
		        if(mp.experience < 0.0F)
		        {
		        	mp.experience = 1F - mp.experience;
		        	mp.addExperienceLevel(-1);
		        }
	        
		        //add Food Level
			    foodStat.addStats(1, 0.5F);
			    //update experience count on the client side
			    mp.connection.sendPacket(new SPacketSetExperience(mp.experience, mp.experienceTotal, mp.experienceLevel));
			    //play generic eat sound
			    mp.connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_GENERIC_EAT,SoundCategory.PLAYERS,mp.posX,mp.posY + mp.getEyeHeight(),mp.posZ, 0.3F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F));
			          
			}
		}
					
		
//		Platinum Armor (Night Vision, Needed Vanishing Curse)
		if(isPlayerWearingSpecificArmorPiece(event.player, 3,ModArmors.platinum_helmet) && ArmorEffectsConfig.platinumArmorEffect)
			event.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 220, 0, false, false));
		

//		Carmot Armor (Haste I)
		if(isPlayerWearingArmor(event.player, new Item[] {ModArmors.carmot_helmet,ModArmors.carmot_chest,ModArmors.carmot_legs,ModArmors.carmot_boots}) && ArmorEffectsConfig.carmotArmorEffect)
			event.player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 60, 0, false, false));
		

//		Prometheum Armor (No potion, need to implement a new Effect)
		if(isPlayerWearingArmor(event.player, new Item[] {ModArmors.prometheum_helmet,ModArmors.prometheum_chest,ModArmors.prometheum_legs,ModArmors.prometheum_boots}) && ArmorEffectsConfig.prometheumArmorEffect)
			event.player.removePotionEffect(MobEffects.POISON);
		
//		Shadow Iron Armor (No Blindness)
		if(isPlayerWearingArmor(event.player, new Item[] {ModArmors.shadow_iron_helmet,ModArmors.shadow_iron_chest,ModArmors.shadow_iron_legs,ModArmors.shadow_iron_boots}))
			event.player.removePotionEffect(MobEffects.BLINDNESS);

//		removes the blindness effect when wearing shadow steel armor
		if(isPlayerWearingArmor(event.player, new Item[] {ModArmors.shadow_steel_helmet,ModArmors.shadow_steel_chest,ModArmors.shadow_steel_legs,ModArmors.shadow_steel_boots}) && pl.isPotionActive(MobEffects.BLINDNESS))
	       pl.removeActivePotionEffect(MobEffects.BLINDNESS);


		ItemStack stack =  pl.getHeldItemMainhand();
		IAttributeInstance attackSpeedInstance = pl.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED);
		if(ToolEffectsConfig.shadowSteelSwordEffect && stack.isItemEqualIgnoreDurability(new ItemStack(ModTools.shadow_steel_sword)))
		{

			float percentage = Utils.getLightArmorPercentage(pl, 50F);
			//calculate the Speed to add to the sword
			double added_speed = attackSpeedInstance.getBaseValue() * percentage / 100F;
	    	//the modifier UUID
			AttributeModifier shadow_steel_modifier = new AttributeModifier(SHADOW_STEEL_ARMOR_MODIFIER_UUID,"Shadow Steel Armor Modifier", added_speed, 0);
            //checks if player has the modifier
		    if(attackSpeedInstance.getModifier(SHADOW_STEEL_ARMOR_MODIFIER_UUID) == null)
			{
		    	//if not,add the modifier
 				attackSpeedInstance.applyModifier(shadow_steel_modifier);
			}
		    else if(attackSpeedInstance.getModifier(SHADOW_STEEL_ARMOR_MODIFIER_UUID) != null && attackSpeedInstance.getModifier(SHADOW_STEEL_ARMOR_MODIFIER_UUID).getAmount() != added_speed)
		    {
		    	//if  player has already the modifier and there is a light change,this method will update the speed attack
		    	attackSpeedInstance.removeModifier(SHADOW_STEEL_ARMOR_MODIFIER_UUID);
		    	attackSpeedInstance.applyModifier(shadow_steel_modifier);
		    }

		}
		else if(attackSpeedInstance.getModifier(SHADOW_STEEL_ARMOR_MODIFIER_UUID) != null)
		{
			//removes the modifier if player doesn't held the sword
			attackSpeedInstance.removeModifier(SHADOW_STEEL_ARMOR_MODIFIER_UUID);
		}
	}

//	Road Speed Effect
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void roadSpeed(PlayerTickEvent event) {
		if ((event.player.world.getBlockState(new BlockPos(event.player.posX, event.player.posY - 0.5D, event.player.posZ)).getBlock() == ModBlocks.blockRoad
				|| event.player.world.getBlockState(new BlockPos(event.player.posX, event.player.posY - 0.5D, event.player.posZ)).getBlock() == ModBlocks.blockStripedRoad)
				&& event.phase == TickEvent.Phase.START && event.side.isClient() && event.player.onGround)
		{
			if(inputCheck == null)
				inputCheck = new MovementInputFromOptions(Minecraft.getMinecraft().gameSettings);

			inputCheck.updatePlayerMoveState();

			if((inputCheck.moveForward != 0 || inputCheck.moveStrafe != 0))
			{
				event.player.motionX *= GeneralConfig.roadSpeed;
				event.player.motionZ *= GeneralConfig.roadSpeed;
			}
		}
	}
	
	//method to check if player wears the complete Armor.
	public static boolean isPlayerWearingArmor(EntityPlayer pl,Item[] armor)
	{
			
		boolean flag = true;
			
		  List<ItemStack> list = Lists.newArrayList(pl.getArmorInventoryList().iterator());
		  for(int i = 0; i < list.size();i++) {
		  if(!list.get(i).getItem().equals(armor[3 - i]))
           flag = false;
		   }
		 return flag;
		}
		
//  get Specific Armor Equip [3 = helmet,2 = chest,1 = legs, boots = 0]
	public static boolean isPlayerWearingSpecificArmorPiece(EntityPlayer pl,int index,Item armorEquip)
	{			
		List<ItemStack> list = Lists.newArrayList(pl.getArmorInventoryList().iterator());	      
	    return list.get(index).getItem().equals(armorEquip);
	}
	
	@SubscribeEvent
	public static void onAttack(AttackEntityEvent event)
	{

		EntityPlayer player = event.getEntityPlayer();
		if (!player.world.isRemote) {

			Entity foe = event.getTarget();
//			Shadow Iron Sword (Blindness)
			if (player.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.shadow_iron_sword))
					&& ToolEffectsConfig.shadowIronSwordEffect) {

				EntityLivingBase foe2 = (EntityLivingBase) foe;

				if ((int) (Math.random() * 100) <= 25)
					foe2.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100));
			}

//			Desichalkos Sword ( Give Random Effect to entity )
			if(player.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.desichalkos_sword))
					&& ToolEffectsConfig.desichalkosSwordEffect){
				((EntityLivingBase)foe).addPotionEffect(new PotionEffect(Utils.getRandomEffect(), 80, 0));
			}

//			Vyroxeres Sword (Potion)
			if (player.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.vyroxeres_sword))
					&& ToolEffectsConfig.vyroxeresSwordEffect)
			{

				if ((int) (Math.random() * 100) <= 25)
					((EntityLivingBase) foe).addPotionEffect(new PotionEffect(MobEffects.POISON, 100));
			}

//			Ignatius Sword (Fire Aspect)
			if (player.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.ignatius_sword))
					&& ToolEffectsConfig.ignatiusSwordEffect) {

				if ((int) (Math.random() * 100) <= 15)
					foe.setFire(5);
			}

//			Vulcanite Sword (Fire Aspect)
			if (player.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.vulcanite_sword))
					&& ToolEffectsConfig.vulcaniteSwordEffect) {

				if ((int) (Math.random() * 100) <= 30)
					foe.setFire(5);
			}

//			Tartarite Sword (Withering II)
			if (player.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.tartarite_sword))
					&& ToolEffectsConfig.tartariteSwordEffect) {

				if ((int) (Math.random() * 100) <= 20)
					((EntityLivingBase) foe).addPotionEffect(new PotionEffect(MobEffects.WITHER, 60, 1, false, false));
			}
			
//			Mithril Sword (Give Glowing to entity Hitted)
			if(player.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.mithril_sword))) {
				if ((int) (Math.random() * 100) <= 20)
					((EntityLivingBase) foe).addPotionEffect(new PotionEffect(MobEffects.GLOWING, 200, 1, false, false));
			}

//			Kalendrite sword (Regeneration)
			if (player.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.kalendrite_sword))
					&& ToolEffectsConfig.kalendriteSwordEffect) {

				if ((int) (Math.random() * 100) <= 30)
					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1, false, false));
			}
			
//			Ceruclase Sword (Give slowness)
			if (player.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.ceruclase_sword))) {
				
				if ((int) (Math.random() * 100) <= 25)
					((EntityLivingBase) foe).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 70, 1, false, false));
			}
		}
	}

	@SubscribeEvent
	public static void onBreakBlock(PlayerEvent.BreakSpeed event)
	{
		EntityPlayer pl = event.getEntityPlayer();
		ItemStack mainHandStack = pl.getHeldItemMainhand();

		if(pl.isInWater()
				&& mainHandStack.isItemEqualIgnoreDurability(new ItemStack(ModTools.deep_iron_pickaxe))
				&& ToolEffectsConfig.deepIronPickaxeEffect)
			event.setNewSpeed(6F);
		//set tools break speed based on light except for hoe and sword
				if(ToolEffectsConfig.shadowSteelToolSpeedEffect && Utils.isItemStackASpecificToolMaterial(ModMetals.SHADOW_STEEL, mainHandStack,"hoe","sword")) {
					float percentage = Utils.getLightArmorPercentage(pl,100F);
					float speed = event.getNewSpeed()  * percentage / 40F;
					event.setNewSpeed(event.getOriginalSpeed() + speed);
				}
	}

	//replaces the enderman's AI
	@SubscribeEvent
	public static void constructEntity(EntityEvent.EnteringChunk event)
	{

		//check if spawned entity is an enderman
		if(event.getEntity() instanceof EntityEnderman && ArmorEffectsConfig.eximiteArmorEffect)
		{

			EntityEnderman end = (EntityEnderman) event.getEntity();
			EntityAIBase aifindPlayer = null;
			int priority = 0;
			Iterator<EntityAITaskEntry> entries = end.targetTasks.taskEntries.iterator();
			while(entries.hasNext())
			{
				EntityAITaskEntry entry = entries.next();
				if(entry.action instanceof EntityAINearestAttackableTarget)
					//checks if the AI Class is the AIFindPlayer Class(The Class Used to check if player is watching an enderman)
					if(entry.action.getClass().getName().contains("EntityEnderman$AIFindPlayer"))
					{
						aifindPlayer =  entry.action;
						priority = entry.priority;
					}

			}
			//if the AI class isn't null it will replace the original AI with the AIFindPlayerWithoutHelmet( a new custom AI similar to the original one)
			if(aifindPlayer != null) {
				end.targetTasks.removeTask(aifindPlayer);
				end.targetTasks.addTask(priority, new AIFindPlayerWithoutHelmet(aifindPlayer));
			}
		}
	}


	@SubscribeEvent
	public static void onEntityDeth(LivingDeathEvent event) {
		Entity attacker = event.getSource().getImmediateSource();
		if(attacker instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) attacker;
//			Celenegil Sword ( Give Speed and Strenght on entity kill )
			if(player.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.celenegil_sword)) && (player.isPotionActive(MobEffects.STRENGTH) ? player.getActivePotionEffect(MobEffects.STRENGTH).getDuration() < 8:true && player.isPotionActive(MobEffects.SPEED) ? player.getActivePotionEffect(MobEffects.SPEED).getDuration() < 8:true)) {
				player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 140, 0, false, false));
				player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 140, 0, false, false));
			}

//			Orichalcum Sword ( Give Strenght on entity kill )
			if(player.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.orichalcum_sword)) && (player.isPotionActive(MobEffects.STRENGTH) ? player.getActivePotionEffect(MobEffects.STRENGTH).getDuration() < 8:true))
				player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 140, 0, false, false));
		}
	}
	
//	Midasium Effect
	@SubscribeEvent
    public static void drop(LivingDropsEvent ev)
    {

        DamageSource source = ev.getSource();
        Entity entity = source.getTrueSource();
        
        if(entity instanceof EntityPlayer)
        {
            EntityPlayer pl = (EntityPlayer) entity;

            if(pl.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.midasium_sword))&& !(ev.getEntity() instanceof EntityPlayer)){
	            ArrayList<EntityItem> drops = new ArrayList<>();
	
//            	Duplica il drop
	            if((int) (Math.random() * 100) <= 50) {
			        for(EntityItem item : ev.getDrops()){
			            EntityItem clone = new EntityItem(item.world, item.posX, item.posY, item.posZ, item.getItem());
			            drops.add(clone);
			        }
		            ev.getDrops().addAll(drops);
	            }
            }
        }
    }
	
	@SubscribeEvent
    public static void drop(BlockEvent.HarvestDropsEvent ev)
    {
		if(ev.getHarvester() instanceof EntityPlayer) {
            EntityPlayer pl = ev.getHarvester();
		
	            if(Utils.isItemStackASpecificToolMaterial(ModMetals.MIDASIUM, pl.getHeldItemMainhand())){
		            ArrayList<ItemStack> drops = new ArrayList<>();
		
		            if((int) (Math.random() * 100) <= 50) {
			            for(ItemStack stack : ev.getDrops())
			            {
			            	if(stack != null)
			            		drops.add(stack);
			            }
			            ev.getDrops().addAll(drops);
		            }
	            }
		}
    }


//	Sanguinite Sword (Vampirism)
	@SubscribeEvent
	public static void entityHurtEvent(LivingHurtEvent event)
	{
		//the entity that damaged the event entity
		Entity source = event.getSource().getImmediateSource();
		if(source instanceof EntityPlayer)
		{

			//the player that damaged the event entity
			EntityPlayer pl = (EntityPlayer) source;

			if(pl.getHeldItemMainhand().isItemEqualIgnoreDurability(new ItemStack(ModTools.sanguinite_sword))) {
				{
					//check if the player is missing hearts.
					if(pl.getHealth() < pl.getMaxHealth())
					{

						int luck_level = Math.round(pl.getLuck());
						//percentage to get healed based on the luck of the player (example: luck 0 = 15%,luck 1 = 20%...)
						int percentage = 15 + (luck_level * 5);
						if(new Random().nextInt(100) < percentage)
						{
							//the heal Amount ,that is the 10% of the damage
							float healAmount = event.getAmount() * 0.15F;
							if(pl.getHealth() + healAmount >= pl.getMaxHealth())
								healAmount = 0;
							//set the player health
							pl.setHealth(pl.getHealth() + healAmount);
						}
					}
				}
			}
		}
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer pl = (EntityPlayer) event.getEntityLiving();
			//check if player is wearing the shadow steel armor
			if(ArmorEffectsConfig.shadowSteelArmorEffect && isPlayerWearingArmor(pl, new Item[] {ModArmors.shadow_steel_helmet,ModArmors.shadow_steel_chest,ModArmors.shadow_steel_legs,ModArmors.shadow_steel_boots}))
			{
				//get light percentage,maximum 30%
				float percentage = Utils.getLightArmorPercentage(pl,40F);
				float removedDamage = event.getAmount() * percentage / 100F;
				event.setAmount(event.getAmount() - removedDamage);
			}
		}
	}


//	FireImmunity
	@SubscribeEvent
	public static void cancelFireDamage (LivingAttackEvent event){
		if (event.getEntity() instanceof EntityPlayer) {
			if(event.getSource().isFireDamage()) {
				if(isPlayerWearingArmor((EntityPlayer) event.getEntity(), new Item[] {ModArmors.vulcanite_helmet,ModArmors.vulcanite_chest,ModArmors.vulcanite_legs,ModArmors.vulcanite_boots}))
					event.setCanceled(true);
			}
		}
	}

//	Increase the speed of item action [ Aggiungere la possibilità di scelta della velocità della quicksilver ]
	@SubscribeEvent
	public static void increaseVelocity(LivingEntityUseItemEvent.Start ev){
		if(ev.getEntityLiving() instanceof EntityPlayer)
			if(isPlayerWearingArmor((EntityPlayer)ev.getEntityLiving(), new Item[] {ModArmors.quicksilver_helmet, ModArmors.quicksilver_chest, ModArmors.quicksilver_legs, ModArmors.quicksilver_boots}) && ArmorEffectsConfig.quicksilverArmorEffect) {
				if(ev.getItem().getItem().getItemUseAction(ev.getItem()) == EnumAction.BOW)
					ev.setDuration(ev.getDuration() - 6);
				else
					ev.setDuration(Math.round(ev.getDuration() / 2F));
			}
	}
}
