/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package com.github.retrooper.packetevents.protocol.world.chunk.palette;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * A palette backed by a map.
 */
public class MapPalette implements Palette {
    private final int maxId;

    private final int[] idToState;
    private final Int2IntMap stateToId = new Int2IntOpenHashMap();
    private int nextId = 0;

    public MapPalette(int bitsPerEntry) {
        this.maxId = (1 << bitsPerEntry) - 1;

        this.idToState = new int[this.maxId + 1];
    }

    public MapPalette(int bitsPerEntry, NetStreamInput in) {
        this(bitsPerEntry);

        int paletteLength = in.readVarInt();
        for (int i = 0; i < paletteLength; i++) {
            int state = in.readVarInt();
            this.idToState[i] = state;
            this.stateToId.putIfAbsent(state, i);
        }
        this.nextId = paletteLength;
    }

    @Override
    public int size() {
        return this.nextId;
    }

    @Override
    public int stateToId(int state) {
        int id = this.stateToId.getOrDefault(state, -1);
        if (id == -1 && this.size() < this.maxId + 1) {
            id = this.nextId++;
            this.idToState[id] = state;
            this.stateToId.put(state, id);
        }

        return id;
    }

    @Override
    public int idToState(int id) {
        if (id >= 0 && id < this.size()) {
            return this.idToState[id];
        } else {
            return 0;
        }
    }
}
