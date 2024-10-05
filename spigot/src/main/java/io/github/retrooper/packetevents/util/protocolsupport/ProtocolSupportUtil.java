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

package io.github.retrooper.packetevents.util.protocolsupport;

import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;

import java.net.SocketAddress;

public class ProtocolSupportUtil {

    private static boolean CHECKED_FOR_PS = false;
    private static boolean PS_PRESENT = false;

    public static boolean isAvailable() {
        if (!CHECKED_FOR_PS) {
            try {
                ClassLoader classLoader = PacketEvents.getAPI().getPlugin().getClass().getClassLoader();
                classLoader.loadClass("protocolsupport.api.ProtocolSupportAPI");
                PS_PRESENT = true;
                CHECKED_FOR_PS = true;
                return true;
            } catch (Exception e) {
                PS_PRESENT = false;
                CHECKED_FOR_PS = true;
                return false;
            }
        } else {
            return PS_PRESENT;
        }
    }

    public static void checkIfProtocolSupportIsPresent() {
        PS_PRESENT = Bukkit.getPluginManager().isPluginEnabled("ProtocolSupport");
        CHECKED_FOR_PS = true;
    }

    public static int getProtocolVersion(SocketAddress address) {
        return ProtocolSupportAPI.getProtocolVersion(address).getId();
    }

    public static int getProtocolVersion(Player player) {
        return ProtocolSupportAPI.getProtocolVersion(player).getId();
    }

}
