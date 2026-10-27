package com.example.undoo;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenTntMenuPacket() implements CustomPacketPayload {
    public static final Type<OpenTntMenuPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("undoo", "open_tnt_menu"));
    public static final StreamCodec<FriendlyByteBuf, OpenTntMenuPacket> STREAM_CODEC = StreamCodec.unit(new OpenTntMenuPacket());
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    public static void handle(OpenTntMenuPacket p, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof net.minecraft.server.level.ServerPlayer sp) {
                var stack = sp.getMainHandItem();
                sp.openMenu(new SimpleMenuProvider((id, inv, pl) -> new TntMenu(id, inv, stack), Component.literal("Сила взрыва")), buf -> buf.writeItem(stack));
            }
        });
    }
}
