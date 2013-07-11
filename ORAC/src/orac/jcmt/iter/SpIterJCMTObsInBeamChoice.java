package orac.jcmt.iter;

import gemini.sp.SpType;
import orac.util.InBeam;

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
                return InBeam.isInBeam(_avTable, IN_BEAM_POL2);
        }

        /**
         * Determine whether FTS-2 is in the beam.
         */
        public boolean isFts2InBeam() {
                return InBeam.isInBeam(_avTable, IN_BEAM_FTS2);
        }

        /**
         * Set whether POL-2 is in the beam.
         */
        public void setPol2InBeam(boolean pol2_in_beam) {
                InBeam.setInBeam(_avTable, IN_BEAM_POL2, pol2_in_beam);
        }

        /**
         * Set whether FTS-2 is in the beam.
         */
        public void setFts2InBeam(boolean fts2_in_beam) {
                InBeam.setInBeam(_avTable, IN_BEAM_FTS2, fts2_in_beam);
        }

}
