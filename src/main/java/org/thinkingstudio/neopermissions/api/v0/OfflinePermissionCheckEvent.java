/*
 * This file is part of fabric-permissions-api, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package org.thinkingstudio.neopermissions.api.v0;

import org.thinkingstudio.neopermissions.fabric.api.event.Event;
import org.thinkingstudio.neopermissions.fabric.api.event.EventFactory;
import org.thinkingstudio.neopermissions.fabric.api.util.TriState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Simple permissions check event for (potentially) offline players.
 */
public interface OfflinePermissionCheckEvent {

    Event<OfflinePermissionCheckEvent> EVENT = EventFactory.createArrayBacked(OfflinePermissionCheckEvent.class, (callbacks) -> (uuid, permission) -> {
        CompletableFuture<TriState> res = CompletableFuture.completedFuture(TriState.DEFAULT);
        for (OfflinePermissionCheckEvent callback : callbacks) {
            res = res.thenCompose(triState -> {
                if (triState != TriState.DEFAULT) {
                    return CompletableFuture.completedFuture(triState);
                }
                return callback.onPermissionCheck(uuid, permission);
            });
        }
        return res;
    });

    @NotNull CompletableFuture<TriState> onPermissionCheck(@NotNull UUID uuid, @NotNull String permission);

}
