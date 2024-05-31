package net.hydra.jojomod.networking;

import net.hydra.jojomod.access.IPacketAccess;
import net.hydra.jojomod.networking.c2s.*;
import net.hydra.jojomod.networking.s2c.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class ForgePackets implements IPacketAccess {
    @Override
    public void StandGuardPointPacket(ServerPlayer sp, float guard, boolean broken) {
        ForgePacketHandler.sendToClient(new ForgeGuardUpdatePacket(guard,broken), sp);
    }

    @Override
    public void DazeTimePacket(ServerPlayer sp, byte dazeTime) {
        ForgePacketHandler.sendToClient(new ForgeUpdateDazePacket(dazeTime), sp);
    }

    @Override
    public void NBTSyncPacket(ServerPlayer sp, CompoundTag NBT) {
        ForgePacketHandler.sendToClient(new ForgeNBTPacket(NBT), sp);
    }

    @Override
    public void syncCooldownPacket(ServerPlayer sp, int attackTime, int attackTimeMax, int attackTimeDuring, byte activePower, byte activePowerPhase) {
        ForgePacketHandler.sendToClient(new ForgeCDSyncPacket(attackTime,attackTimeMax,attackTimeDuring,activePower,activePowerPhase), sp);
    }

    @Override
    public void updateClashPacket(ServerPlayer sp, int id, float clashProgress) {
        ForgePacketHandler.sendToClient(new ForgeClashUpdatePacket(id, clashProgress), sp);
    }

    @Override
    public void stopSoundPacket(ServerPlayer sp, int id) {
        ForgePacketHandler.sendToClient(new ForgeStopSoundPacket(id), sp);
    }

    @Override
    public void startSoundPacket(ServerPlayer sp, int id, byte soundNo) {
        ForgePacketHandler.sendToClient(new ForgePlaySoundPacket(id, soundNo), sp);
    }

    @Override
    public void timeStoppingEntityPacket(ServerPlayer sp, int entityID, boolean remove) {
        ForgePacketHandler.sendToClient(new ForgeTimeStoppingEntityPacket(entityID, remove), sp);
    }


    @Override
    public void StandGuardCancelClientPacket() {
        ForgePacketHandler.sendToServer(new ForgeGuardCancelPacket());
    }

    @Override
    public void StandPowerPacket(byte power) {
        ForgePacketHandler.sendToServer(new ForgeSwitchPowerPacket(power));

    }
    @Override
    public void StandPunchPacket(int targetID, byte APP) {
        ForgePacketHandler.sendToServer(new ForgePunchPacket(targetID,APP));

    }

    @Override
    public void StandBarrageHitPacket(int targetID, int ATD) {
        ForgePacketHandler.sendToServer(new ForgeBarrageHitPacket(targetID,ATD));
    }

    @Override
    public void updateClashPacket(float clashProgress, boolean clashDone) {
        ForgePacketHandler.sendToServer(new ForgeClashUpdatePacketC2S(clashProgress,clashDone));
    }
    @Override
    public void standSummonPacket() {
        ForgePacketHandler.sendToServer(new ForgeSummonPacket());
    }
    @Override
    public void moveSyncPacket(byte forward, byte strafe) {
        ForgePacketHandler.sendToServer(new ForgeMoveSyncPacket(forward, strafe));
    }
}