package com.pqvel1l.structorycraft;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StructoryCraftMod implements ModInitializer {
	public static final String MODID = "structorycraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);



	@Override
	public void onInitialize() {
	}
	public boolean checkAltarStructure(World world, BlockPos pos) {
		AltarStructureChecker checker = new AltarStructureChecker();
		return checker.checkStructure(world, pos);
	}
}