package net.hydra.jojomod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.event.index.Corpses;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.Poses;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class CorpseBagScreen extends Screen {
    //Check out GamemodeSwitcherScreen
    static final ResourceLocation CORPSE_CHOOSER_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/gui/corpse_chooser.png");
    private static final int SPRITE_SHEET_WIDTH = 256;
    private static final int SPRITE_SHEET_HEIGHT = 256;
    private static final int SLOT_AREA = 26;
    private static final int SLOT_PADDING = 5;
    private static final int SLOT_AREA_PADDED = 31;
    private static final int HELP_TIPS_OFFSET_Y = 5;
    private static final int ALL_SLOTS_WIDTH = corpseIcon.VALUES.length * 31 - 5;
    private corpseIcon currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    public boolean zHeld;

    private final List<PoseSlot> slots = Lists.newArrayList();

    public CorpseBagScreen() {
        super(GameNarrator.NO_TITLE);
        this.currentlyHovered = null;
    }

    private ShapeShifts getDefaultSelected() {
        Player pl = Minecraft.getInstance().player;
        if (pl != null){
            return ShapeShifts.getShiftFromByte(((IPlayerEntity)pl).roundabout$getShapeShift());
        }
        return ShapeShifts.PLAYER;
    }

    @Override
    protected void init() {
        super.init();
        zHeld = true;
        Player pl = Minecraft.getInstance().player;


        this.currentlyHovered = corpseIcon.NONE;
        for (int i = 0; i < corpseIcon.VALUES.length; ++i) {
            corpseIcon pIcon = corpseIcon.VALUES[i];
            this.slots.add(new PoseSlot(pIcon, this.width / 2 + pIcon.xoff - 13, this.height / 2 + pIcon.yoff - 44));
        }
    }


    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        this.switchToHoveredGameMode();
        this.minecraft.setScreen(null);
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        if (this.checkToClose()) {
            return;
        }
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        int k = this.width / 2 - 62;
        int l = this.height / 2 - 31 - 39;
        guiGraphics.blit(CORPSE_CHOOSER_LOCATION, k, l, 0.0f, 0.0f, 125, 63, 256, 256);
        guiGraphics.pose().popPose();
        super.render(guiGraphics, i, j, f);
        if (this.currentlyHovered != null) {
            guiGraphics.drawCenteredString(this.font, this.currentlyHovered.getName(), this.width / 2, this.height / 2 - 31 - 32, -1);
        }
        if (!this.setFirstMousePos) {
            this.firstMouseX = i;
            this.firstMouseY = j;
            this.setFirstMousePos = true;
        }
        boolean bl = this.firstMouseX == i && this.firstMouseY == j;
        for (PoseSlot MobSlot : this.slots) {
            MobSlot.render(guiGraphics, i, j, f);
            MobSlot.setSelected(this.currentlyHovered == MobSlot.icon);
            if (bl || !MobSlot.isHoveredOrFocused()) continue;
            this.currentlyHovered = MobSlot.icon;
        }
    }

    private void switchToHoveredGameMode() {
        CorpseBagScreen.switchToHoveredGameMode(this.minecraft, this.currentlyHovered);
    }

    private static void switchToHoveredGameMode(Minecraft minecraft, corpseIcon pIcon) {
        if (minecraft.gameMode == null || minecraft.player == null) {
            return;
        }

        byte ppos = ((IPlayerEntity)minecraft.player).roundabout$GetPoseEmote();
        if (pIcon.id != ppos) {
            ModPacketHandler.PACKET_ACCESS.byteToServerPacket(pIcon.id, PacketDataIndex.BYTE_STRIKE_POSE);
        }
        //ModPacketHandler.PACKET_ACCESS.byteToServerPacket(pIcon3.id, PacketDataIndex.BYTE_CHANGE_MORPH);
    }
    public boolean sameKeyOne(KeyMapping key1, Options options){
        return (key1.isDown() || (key1.same(options.keyLoadHotbarActivator) && options.keyLoadHotbarActivator.isDown())
                || (key1.same(options.keySaveHotbarActivator) && options.keySaveHotbarActivator.isDown())
        );
    }
    public boolean sameKeyOneX(KeyMapping key1, Options options){
        return (InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)key1).roundabout$justTellMeTheKey().getValue())
                || (key1.same(options.keyLoadHotbarActivator) && InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)options.keyLoadHotbarActivator).roundabout$justTellMeTheKey().getValue()))
                || (key1.same(options.keySaveHotbarActivator) && InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ((IKeyMapping)options.keySaveHotbarActivator).roundabout$justTellMeTheKey().getValue()))
        );
    }
    private boolean checkToClose() {
        if (minecraft != null) {
            if (sameKeyOneX(KeyInputRegistry.abilityTwoKey, this.minecraft.options)) {
                this.switchToHoveredGameMode();
                this.minecraft.setScreen(null);
                return true;
            }
        }
        Options options = Minecraft.getInstance().options;
        return false;
    }
    @Override
    public boolean keyPressed(int i, int j, int k) {
        return super.keyPressed(i, j, k);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public enum corpseIcon {
        ZOMBIE(Component.translatable("entity.minecraft.zombie"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/corpse_icons/zombie.png"),Corpses.ZOMBIE.id,-31,31),
        SKELETON(Component.translatable("entity.minecraft.skeleton"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/corpse_icons/skeleton.png"),Corpses.SKELETON.id,0,0),
        CREEPER(Component.translatable("entity.minecraft.creeper"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/corpse_icons/creeper.png"),Corpses.CREEPER.id,31,0),
        VILLAGER(Component.translatable("entity.minecraft.villager"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/corpse_icons/villager.png"),Corpses.VILLAGER.id,-31,0),
        SPIDER(Component.translatable("entity.minecraft.spider"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/corpse_icons/spider.png"),Corpses.SPIDER.id,31,31),

        NONE(Component.translatable("roundabout.corpse.none"), new ResourceLocation(Roundabout.MOD_ID,
                "textures/gui/pose_icons/jonathan.png"),Corpses.NONE.id,0,31);

        static corpseIcon getByte(Corpses corpse) {
            return switch (corpse) {
                default -> throw new IncompatibleClassChangeError();
                case ZOMBIE -> ZOMBIE;
                case SKELETON -> SKELETON;
                case CREEPER -> CREEPER;
                case VILLAGER -> VILLAGER;
                case SPIDER -> SPIDER;
                case NONE -> NONE;
            };
        }
        protected static final corpseIcon[] VALUES;
        private static final int ICON_AREA = 16;
        protected static final int ICON_TOP_LEFT = 5;
        final Component name;
        final ResourceLocation rl;
        final byte id;

        final int xoff;
        final int yoff;

        private corpseIcon(Component component, ResourceLocation rl, byte id, int xoff, int yoff) {
            this.name = component;
            this.rl = rl;
            this.id = id;
            this.xoff = xoff;
            this.yoff = yoff;
        }

        void drawIcon(GuiGraphics guiGraphics, int i, int j) {
            guiGraphics.blit(rl, i-1, j-1, 0, 0, 18, 18, 18, 18);
        }

        Component getName() {
            return this.name;
        }

        static {
            VALUES = new corpseIcon[]{ZOMBIE,SKELETON,VILLAGER,CREEPER,SPIDER,
                    NONE};
        }
    }

    public class PoseSlot
            extends AbstractWidget {
        final corpseIcon icon;
        private boolean isSelected;

        public PoseSlot(corpseIcon pIcon, int i, int j) {
            super(i, j, 26, 26, pIcon.getName());
            this.icon = pIcon;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            if (!this.icon.equals(corpseIcon.NONE)) {
                this.drawSlot(guiGraphics);
                this.icon.drawIcon(guiGraphics, this.getX() + 5, this.getY() + 5);
                if (this.isSelected) {
                    this.drawSelection(guiGraphics);
                }
            }
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }

        @Override
        public boolean isHoveredOrFocused() {
            return super.isHoveredOrFocused() || this.isSelected;
        }

        public void setSelected(boolean bl) {
            this.isSelected = bl;
        }

        private void drawSlot(GuiGraphics guiGraphics) {
            guiGraphics.blit(CORPSE_CHOOSER_LOCATION, this.getX(), this.getY(), 144.0f, 0.0f, 26, 26, 256, 256);
        }

        private void drawSelection(GuiGraphics guiGraphics) {
            guiGraphics.blit(CORPSE_CHOOSER_LOCATION, this.getX(), this.getY(), 170.0f, 0.0f, 26, 26, 256, 256);
        }
    }
}