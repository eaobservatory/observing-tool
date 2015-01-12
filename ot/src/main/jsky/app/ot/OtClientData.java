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

package jsky.app.ot;

import gemini.sp.SpCloneableClientData;
import gemini.sp.SpItem;
import jsky.app.ot.tpe.TpeFeatureClientData;
import jsky.app.ot.tpe.TpeImageFeature;
import ot.util.DialogUtil;

/**
 * This class groups information that should be stored with each SpItem
 * on behalf of the OT.
 *
 * Since SpCloneableClientData is implemented, when the associated SpItem
 * is cloned, this client data will be cloned as well (which causes
 * the OtTreeNodeWidget to be cloned).
 */
public final class OtClientData implements SpCloneableClientData,
        TpeFeatureClientData {
    /**
     * The TreeNodeWidget representing the item in the structure/hierarchy
     * editor display.
     */
    public OtTreeNodeWidget tnw;

    /**
     * The full class name of the editor for this node.
     */
    public String itemEditorClass;

    /**
     * The full class name of any TpeImageFeature subclasses that should
     * be associated with this item (if any).
     *
     * This may turn into an array in a future release.
     */
    public String tpeImageFeatureClass;

    /**
     * A reference to an instantiated TpeImageFeature.
     */
    TpeImageFeature _feature;

    /**
     * Construct with an OtTreeNodeWidget and the name of the item editor
     * for this class.
     */
    public OtClientData(OtTreeNodeWidget tnw, String itemEditorClass) {
        this.tnw = tnw;
        this.itemEditorClass = itemEditorClass;
    }

    /**
     * Construct with tree node widget, item editor class name, and an
     * image feature class name.
     */
    public OtClientData(OtTreeNodeWidget tnw, String itemEditorClass,
            String imageFeatureClass) {
        this(tnw, itemEditorClass);
        tpeImageFeatureClass = imageFeatureClass;
    }

    /**
     * Create a new object of the same class as this object, with the same
     * values.
     */
    public Object clone(SpItem spItem) {
        OtClientData cd;

        try {
            cd = (OtClientData) super.clone();

        } catch (CloneNotSupportedException ex) {
            // This won't happen so long as SpCloneableClientData implements
            // Cloneable
            return null;
        }

        cd.tnw = this.tnw.copy();
        cd.tnw.setItem(spItem);
        cd.tnw.setText(spItem.getTitle());

        return cd;
    }

    /**
     * Get the TpeImageFeature, instantiating it if necessary.
     */
    public TpeImageFeature getImageFeature() {
        if ((_feature == null) && (tpeImageFeatureClass != null)) {
            try {
                Class<?> c = Class.forName(tpeImageFeatureClass);
                _feature = (TpeImageFeature) c.newInstance();

            } catch (Exception ex) {
                DialogUtil.error(tnw.getTreeWidget(), "Problem instantiating: "
                        + tpeImageFeatureClass, ex);
                _feature = null;
            }
        }

        return _feature;
    }
}
