/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
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

package edfreq;

import javax.swing.JLabel;

/**
 * @author Dennis Kelly (bdk@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class ResolutionDisplay extends JLabel implements SamplerWatcher {
    private int resolution;
    private double width;
    private int nMixers;

    public ResolutionDisplay(int channels, double width, int nMixers) {
        super();

        setHorizontalAlignment(CENTER);

        this.width = width;
        this.nMixers = nMixers;

        resolution = nMixers * (int) (1.0E-3 * width / (double) channels);
        setText(String.valueOf(resolution));
    }

    public void setChannels(int channels) {
        resolution = nMixers * ((int) (1.0E-3 * width / (double) channels));

        System.out.println("Setting text of ResolutionDisplay to "
                + resolution);

        setText(String.valueOf(resolution));

        repaint();
    }

    public void updateSamplerValues(double centre, double width, int channels) {
        this.width = width;

        resolution = (int) Math.rint(nMixers
                * ((width * 1.0E-3) / (double) channels));

        setText(String.valueOf(resolution));
    }
}
