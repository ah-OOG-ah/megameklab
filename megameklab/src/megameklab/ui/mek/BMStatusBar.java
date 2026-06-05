/*
 * Copyright (C) 2008-2026 The MegaMek Team. All Rights Reserved.
 *
 * This file is part of MegaMekLab.
 *
 * MegaMekLab is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPL),
 * version 3 or (at your option) any later version,
 * as published by the Free Software Foundation.
 *
 * MegaMekLab is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * A copy of the GPL should have been included with this project;
 * if not, see <https://www.gnu.org/licenses/>.
 *
 * NOTICE: The MegaMek organization is a non-profit group of volunteers
 * creating free software for the BattleTech community.
 *
 * MechWarrior, BattleMech, `Mech and AeroTech are registered trademarks
 * of The Topps Company, Inc. All Rights Reserved.
 *
 * Catalyst Game Labs and the Catalyst Game Labs logo are trademarks of
 * InMediaRes Productions, LLC.
 *
 * MechWarrior Copyright Microsoft Corporation. MegaMek was created under
 * Microsoft's "Game Content Usage Rules"
 * <https://www.xbox.com/en-US/developers/rules> and it is not endorsed by or
 * affiliated with Microsoft.
 */
package megameklab.ui.mek;

import javax.swing.JLabel;

import megamek.client.ui.clientGUI.GUIPreferences;
import megameklab.ui.generalUnit.StatusBar;
import megameklab.util.MekUtil;

public class BMStatusBar extends StatusBar {

    private static final String QUIRK_LABEL = "Quirk Points: %d";
    private static final String HEAT_LABEL = "Est. Heat: %d / %s";
    private static final String SLOTS_LABEL = "Free Slots: %d / %d";

    private final JLabel slots = new JLabel();
    private final JLabel heat = new JLabel();
    private final JLabel quirk = new JLabel();

    public BMStatusBar(BMMainUI parent) {
        super(parent);
        add(slots);
        add(heat);
        add(quirk);
    }

    @Override
    protected void additionalRefresh() {
        refreshSlots();
        refreshHeat();
        refreshQuirk();
    }

    public void refreshSlots() {
        int maxCrits = getTestEntity().totalCritSlotCount();
        int currentSlots = MekUtil.countUsedCriticalSlots(getMek());
        slots.setText(String.format(SLOTS_LABEL, maxCrits - currentSlots, maxCrits));
        slots.setForeground(currentSlots > maxCrits ? GUIPreferences.getInstance().getWarningColor() : null);
    }

    public void refreshHeat() {
        long totalHeat = estimatedHeatGeneration();
        heat.setText(String.format(HEAT_LABEL, totalHeat, getMek().formatHeat()));
        heat.setToolTipText("Estimated Total Heat Generated / Total Heat Dissipated");
    }

    public void refreshQuirk() {
        int quirkValue = getMek().getQuirkValue();
        String quirkComplications = getMek().getQuirkComplications();
        String quirkLabel = QUIRK_LABEL + (quirkComplications.isEmpty() ? "" : "*");

        quirk.setText(String.format(quirkLabel, quirkValue));
        quirk.setToolTipText("Current number of quirk points used. Not all official designs balance these!" + quirkComplications);
    }
}
