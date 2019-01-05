/*
 * Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package orac.ukirt.iter;

public interface SpUISTCalConstants {
    public static final String ATTR_CALTYPE = "calType";
    public static final String ATTR_FILTER = "filter";
    public static final String ATTR_MODE = "mode";
    public static final String ATTR_EXPOSURE_TIME = "exposureTime";

    public static final String ATTR_NREADS = "nreads";

    public static final String ATTR_READ_INTERVAL = "readInterval";

    public static final String ATTR_DUTY_CYCLE = "dutyCycle";
    public static final String ATTR_CHOP_FREQUENCY = "chopFrequency";
    public static final String ATTR_CHOP_DELAY = "chopDelay";
    public static final String ATTR_COADDS = "coadds";
    public static final String ATTR_FLAT_SOURCE = "flatSource";
    public static final String ATTR_ARC_SOURCE = "arcSource";
    public static final String ATTR_FOCUS = "focus";
    public static final String ATTR_ORDER = "order";
    public static final String ATTR_CENTRAL_WAVELENGTH = "centralWavelength";
    public static final String ATTR_OBSERVATION_TIME = "observationTime";
    public static final String ATTR_EXPTIME_OT = "expTimeOT";

    public static final String DEFAULT_FLAT_OBSERVATION_TIME = "20.0";
    public static final String DEFAULT_ARC_OBSERVATION_TIME = "20.0";
}
