package orac.jcmt.iter;

import gemini.sp.SpType;

/**
 * Class which adds the ability to place components in the
 * beam to the SpIterJCMTObs base class.
 *
 * Components supported are FTS-2 and POL-2. This
 * class is designed to be used for focus and pointing
 * observations, and potentially others where the
 * presence of these components is optional.
 */
@SuppressWarnings("serial")
public abstract class SpIterJCMTObsInBeamChoice extends SpIterJCMTObs {
        /**
         * Name of the XML element controlling what is in the beam.
         */
        private static final String IN_BEAM = "in_beam";

        /**
         * Component name of POL-2.
         */
        private static final String IN_BEAM_POL2 = "pol2";

        /**
         * Component name of FTS-2.
         */
        private static final String IN_BEAM_FTS2 = "fts2";

        /**
         * Constructor which just passes its argument to the
         * superclass constructor.
         */
        public SpIterJCMTObsInBeamChoice(SpType type) {
                super(type);
        }

        /**
         * Determine whether POL-2 is in the beam.
         */
        public boolean isPol2InBeam() {
                return isInBeam(IN_BEAM_POL2);
        }

        /**
         * Determine whether FTS-2 is in the beam.
         */
        public boolean isFts2InBeam() {
                return isInBeam(IN_BEAM_FTS2);
        }

        /**
         * Determine whether an arbitrary component
         * identified by name is in the beam.
         */
        private boolean isInBeam(String component) {
            if (! _avTable.exists(IN_BEAM)) {
                    return false;
            }

            for (String x: _avTable.get(IN_BEAM).split("\\s")) {
                    if (x.equalsIgnoreCase(component)) {
                        return true;
                    }
            }

            return false;
        }

        /**
         * Set whether POL-2 is in the beam.
         */
        public void setPol2InBeam(boolean pol2_in_beam) {
                setInBeam(IN_BEAM_POL2, pol2_in_beam);
        }

        /**
         * Set whether FTS-2 is in the beam.
         */
        public void setFts2InBeam(boolean fts2_in_beam) {
                setInBeam(IN_BEAM_FTS2, fts2_in_beam);
        }

        /**
         * Set whether an arbitrary component identified
         * by name is in the beam.
         */
        private void setInBeam(String component, boolean in_beam) {
                StringBuilder list = new StringBuilder();
                boolean found = false;

                if (_avTable.exists(IN_BEAM)) {
                        for (String x: _avTable.get(IN_BEAM).split("\\s")) {
                            if (x.equalsIgnoreCase(component)) {
                                found = true;

                                if (! in_beam)  {
                                        continue;
                                }
                            }

                            if (list.length() > 0) {
                                    list.append(" ");
                            }

                            list.append(x);
                        }
                }

                if (in_beam && ! found) {
                        if (list.length() > 0) {
                                list.append(" ");
                        }

                        list.append(component);
                }

                if (list.length() > 0) {
                        _avTable.set(IN_BEAM, list.toString());
                }
                else {
                        if (_avTable.exists(IN_BEAM)) {
                                _avTable.rm(IN_BEAM);
                        }
                }
        }
}
