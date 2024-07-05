package com.pqvel1l.structorycraft;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class AltarStructureChecker {

    public boolean checkStructure(World world, BlockPos pos) {
        // Загрузить NBT файл
        NbtCompound nbtData = loadNBTFile("data/structorycraft/structures/tier1.nbt");

        // Пройтись по всем блокам в структуре и проверить их
        for (String key : nbtData.getKeys()) {
            NbtCompound blockNBT = nbtData.getCompound(key);
            BlockPos blockPos = BlockPos.fromLong(blockNBT.getLong("pos"));
            BlockState expectedBlockState = createBlockStateFromNbt(blockNBT.getCompound("state"));

            BlockState actualBlockState = world.getBlockState(pos.add(blockPos));
            if (!actualBlockState.equals(expectedBlockState)) {
                return false;
            }
        }
        return true;
    }

    private NbtCompound loadNBTFile(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath))) {
            return NbtIo.readCompressed(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return new NbtCompound();
        }
    }

    private BlockState createBlockStateFromNbt(NbtCompound nbt) {
        Block block = Block.getBlockFromItem(net.minecraft.item.Item.byRawId(nbt.getInt("id")));
        BlockState state = block.getDefaultState();

        for (String key : nbt.getKeys()) {
            Property<?> property = block.getStateManager().getProperty(key);
            if (property != null) {
                state = applyProperty(state, property, nbt.getString(key));
            }
        }

        return state;
    }

    private <T extends Comparable<T>> BlockState applyProperty(BlockState state, Property<T> property, String value) {
        return state.with(property, property.parse(value).orElseThrow(IllegalArgumentException::new));
    }
}
