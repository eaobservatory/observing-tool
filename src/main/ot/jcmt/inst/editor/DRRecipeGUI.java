/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot.jcmt.inst.editor;

import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Font;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TableWidgetExt;

@SuppressWarnings("serial")
public class DRRecipeGUI extends JPanel {
    JLabel recipeNameLabel = new JLabel();
    JLabel obeservationTypeLabel = new JLabel();

    private TreeMap<String, Object[]> types = new TreeMap<String, Object[]>();

    public TableWidgetExt recipeTable = new TableWidgetExt();

    public CommandButtonWidgetExt defaultName = new CommandButtonWidgetExt();

    // the following names are not very helpful
    JPanel bigPanel = new JPanel();
    JPanel thatPanel = new JPanel();
    JPanel anotherPanel = new JPanel();
    JScrollPane scrollPane = new JScrollPane();

    BorderLayout borderLayout = new BorderLayout();
    BorderLayout layout = new BorderLayout();
    GridBagLayout panelGridbag = new GridBagLayout();
    GridBagLayout anotherGridbag = new GridBagLayout();

    public DRRecipeGUI() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(layout);
        thatPanel.setLayout(panelGridbag);
        bigPanel.setLayout(borderLayout);
        anotherPanel.setLayout(anotherGridbag);

        recipeNameLabel.setFont(new Font("Dialog", 0, 12));
        recipeNameLabel.setForeground(Color.black);
        recipeNameLabel.setText("Recipe Name");

        obeservationTypeLabel.setFont(new Font("Dialog", 0, 12));
        obeservationTypeLabel.setForeground(Color.black);
        obeservationTypeLabel.setText("Observation Type");

        defaultName.setText("Default");

        // Beginning of types

        JLabel rasterLabel = new JLabel();
        rasterLabel.setFont(new Font("Dialog", 0, 12));
        rasterLabel.setForeground(Color.black);
        rasterLabel.setText("Scan");
        CommandButtonWidgetExt rasterRecipeSet = new CommandButtonWidgetExt();
        rasterRecipeSet.setText("Set");
        TextBoxWidgetExt rasterRecipe = new TextBoxWidgetExt();
        rasterRecipe.setEditable(false);
        types.put("raster", new Object[] {rasterRecipeSet, rasterRecipe});

        JLabel jiggleLabel = new JLabel();
        jiggleLabel.setFont(new Font("Dialog", 0, 12));
        jiggleLabel.setForeground(Color.black);
        jiggleLabel.setText("Jiggle");
        CommandButtonWidgetExt jiggleRecipeSet = new CommandButtonWidgetExt();
        TextBoxWidgetExt jiggleRecipe = new TextBoxWidgetExt();
        jiggleRecipe.setEditable(false);
        jiggleRecipeSet.setText("Set");
        types.put("jiggle", new Object[] {jiggleRecipeSet, jiggleRecipe});

        JLabel stareLabel = new JLabel();
        stareLabel.setFont(new Font("Dialog", 0, 12));
        stareLabel.setForeground(Color.black);
        stareLabel.setText("Stare");
        CommandButtonWidgetExt stareRecipeSet = new CommandButtonWidgetExt();
        stareRecipeSet.setText("Set");
        TextBoxWidgetExt stareRecipe = new TextBoxWidgetExt();
        stareRecipe.setEditable(false);
        types.put("stare", new Object[] {stareRecipeSet, stareRecipe});

        JLabel pointingLabel = new JLabel();
        pointingLabel.setFont(new Font("Dialog", 0, 12));
        pointingLabel.setForeground(Color.black);
        pointingLabel.setText("Pointing");
        CommandButtonWidgetExt pointingRecipeSet = new CommandButtonWidgetExt();
        pointingRecipeSet.setText("Set");
        TextBoxWidgetExt pointingRecipe = new TextBoxWidgetExt();
        pointingRecipe.setEditable(false);
        types.put("pointing", new Object[] {pointingRecipeSet, pointingRecipe});

        JLabel focusLabel = new JLabel();
        focusLabel.setFont(new Font("Dialog", 0, 12));
        focusLabel.setForeground(Color.black);
        focusLabel.setText("Focus");
        CommandButtonWidgetExt focusRecipeSet = new CommandButtonWidgetExt();
        focusRecipeSet.setText("Set");
        TextBoxWidgetExt focusRecipe = new TextBoxWidgetExt();
        focusRecipe.setEditable(false);
        types.put("focus", new Object[] {focusRecipeSet, focusRecipe});

        // End of types

        this.add(thatPanel, BorderLayout.NORTH);

        thatPanel.add(recipeNameLabel, new GridBagConstraints(
                2, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        thatPanel.add(obeservationTypeLabel, new GridBagConstraints(
                0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));

        // Beginning of types

        thatPanel.add(rasterLabel, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 15, 0));
        thatPanel.add(rasterRecipeSet, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        thatPanel.add(rasterRecipe, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        thatPanel.add(jiggleLabel, new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 15, 0));
        thatPanel.add(jiggleRecipeSet, new GridBagConstraints(
                1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        thatPanel.add(jiggleRecipe, new GridBagConstraints(
                2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        thatPanel.add(stareLabel, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 15, 0));
        thatPanel.add(stareRecipeSet, new GridBagConstraints(
                1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        thatPanel.add(stareRecipe, new GridBagConstraints(
                2, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        thatPanel.add(pointingLabel, new GridBagConstraints(
                0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 15, 0));
        thatPanel.add(pointingRecipeSet, new GridBagConstraints(
                1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        thatPanel.add(pointingRecipe, new GridBagConstraints(
                2, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        thatPanel.add(focusLabel, new GridBagConstraints(
                0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 15, 0));
        thatPanel.add(focusRecipeSet, new GridBagConstraints(
                1, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        thatPanel.add(focusRecipe, new GridBagConstraints(
                2, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        // End of types

        this.add(bigPanel, BorderLayout.CENTER);

        bigPanel.add(anotherPanel, BorderLayout.NORTH);
        anotherPanel.add(defaultName, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        bigPanel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.getViewport().add(recipeTable, null);
        reset();
    }

    private Object[] getTypeFields(String type) {
        if (type.endsWith("Recipe")) {
            type = type.substring(0, type.indexOf("Recipe"));
        }

        return types.get(type);
    }

    public void setEnabled(String type, boolean enabled) {
        Object[] typeFields = getTypeFields(type);

        if (typeFields != null) {
            setEnabled(typeFields, enabled);
        }
        else {
            System.out.println(
                "Widgets do not appear to exist for recipe type " +
                type);
        }
    }

    private void setEnabled(Object[] typeFields, boolean enabled) {
        if (typeFields != null && typeFields.length == 2) {
            if (typeFields[0] instanceof CommandButtonWidgetExt
                    && typeFields[1] instanceof TextBoxWidgetExt) {
                CommandButtonWidgetExt button =
                        (CommandButtonWidgetExt) typeFields[0];
                button.setEnabled(enabled);

                TextBoxWidgetExt textBox = (TextBoxWidgetExt) typeFields[1];
                textBox.setText("");
                textBox.setEnabled(enabled);
            }
        }
    }

    public CommandButtonWidgetExt getTypeButton(String type) {
        Object[] typeFields = getTypeFields(type);
        if (typeFields != null) {
            return (CommandButtonWidgetExt) typeFields[0];
        }
        return null;
    }

    public TextBoxWidgetExt getTypeRecipe(String type) {
        Object[] typeFields = getTypeFields(type);
        if (typeFields != null) {
            return (TextBoxWidgetExt) typeFields[1];
        }
        return null;
    }

    public void reset() {
        for (Object[] typeFields: types.values()) {
            setEnabled(typeFields, false);
        }
    }
}
