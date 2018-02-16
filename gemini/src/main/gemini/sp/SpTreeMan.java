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

package gemini.sp;

import gemini.sp.obsComp.SpDRObsComp;
import gemini.sp.obsComp.SpObsComp;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;

import gemini.sp.iter.SpIterFolder;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterComp;

import gemini.util.Assert;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This is the base class for specific insert policies.
 *
 * This is a "function object" type construct. Override the evalInsert method
 * for the particular policy being implemented.
 */
class InsertPolicy implements SpInsertConstants {
    public SpInsertInfo evalInsert(SpItem newItem, SpItem target) {
        return null;
    }
}

/**
 * Insert the newItem into the parent after any components, notes, or
 * iterator folders in the parent.
 */
final class InsidePolicy_AfterComponents extends InsertPolicy {
    public SpInsertInfo evalInsert(SpItem newItem, SpItem parent) {
        SpItem lastComp = null;
        Enumeration<SpItem> children = parent.children();

        while (children.hasMoreElements()) {
            SpItem child = children.nextElement();

            if ((child instanceof SpObsComp) || (child instanceof SpNote)) {
                lastComp = child;

            } else {
                break;
            }
        }

        if (lastComp == null) {
            return new SpInsertInfo(INS_INSIDE, parent);

        } else {
            // Check for an Iterator Folder item
            SpItem temp = lastComp.next();

            if ((temp != null) && (temp instanceof SpIterFolder)) {
                lastComp = temp;
            }

            return new SpInsertInfo(INS_AFTER, lastComp);
        }
    }
}

/**
 * Insert an iterator folder inside the parent.
 */
final class InsidePolicy_IteratorFolder extends InsertPolicy {
    public SpInsertInfo evalInsert(SpItem newItem, SpItem parent) {
        Assert.notFalse(newItem instanceof SpIterFolder);

        // This is almost identical to InsidePolicy_AfterComponents, but
        // if an SpIterFolder is found in this scope, it is an illegal
        // insertion.

        SpItem lastComp = null;
        Enumeration<SpItem> children = parent.children();

        while (children.hasMoreElements()) {
            SpItem child = children.nextElement();

            if (child instanceof SpObsComp) {
                lastComp = child;

            } else if (child instanceof SpIterFolder) {
                // ILLEGAL, there's already an IF in this scope.
                return null;
            }
        }

        if (lastComp == null) {
            return new SpInsertInfo(INS_INSIDE, parent);

        } else {
            return new SpInsertInfo(INS_AFTER, lastComp);
        }
    }
}

/**
 * Insert an iterator component inside the parent.
 */
final class InsidePolicy_IteratorComp extends InsertPolicy {
    public SpInsertInfo evalInsert(SpItem newItem, SpItem parent) {
        Assert.notFalse((newItem instanceof SpIterComp)
                || (newItem instanceof SpNote));
        Assert.notFalse((parent instanceof SpIterComp)
                || (parent instanceof SpIterFolder));

        // First, don't allow nesting inside of "Observe" iterators.
        if (parent instanceof SpIterObserveBase) {
            return null;
        }

        // Make sure that the newItem isn't already the parent of parent.
        // In other words, prevent moving a parent down to be a child of one
        // of its own children...
        if (parent instanceof SpIterComp) {
            SpItem temp = parent;

            while ((temp != null) && (temp instanceof SpIterComp)) {
                if (temp == newItem) {
                    return null;
                }

                temp = temp.parent();
            }
        }

        return new SpInsertInfo(INS_INSIDE, parent);
    }

}

/**
 * Insert a library folder inside the parent.
 */
final class InsidePolicy_LibraryFolder extends InsertPolicy {
    public SpInsertInfo evalInsert(SpItem newItem, SpItem parent) {
        Assert.notFalse(newItem instanceof SpLibraryFolder);
        Assert.notFalse((parent instanceof SpLibraryFolder)
                || (parent instanceof SpLibrary));

        if (parent instanceof SpLibraryFolder) {
            // Make sure that the newItem isn't already the parent of parent.
            // In other words, prevent moving a parent down to be a child of
            // one  of its own children...
            SpItem temp = parent;

            while ((temp != null) && (temp instanceof SpLibraryFolder)) {
                if (temp == newItem) {
                    return null;
                }

                temp = temp.parent();
            }

            // Can't insert a library folder inside a library folder that
            // contains anything but library folders (or notes).
            temp = parent.child();

            while (temp != null) {
                if (!(temp instanceof SpLibraryFolder)
                        && !(temp instanceof SpNote)) {
                    return null;
                }

                temp = temp.next();
            }
        }

        return new SpInsertInfo(INS_INSIDE, parent);
    }
}

/**
 * Insert an item into a library folder.
 *
 * This policy class just checks to make sure that there are no library folders
 * inside the parent library folder (i.e., that the library folder only
 * contains normal, non-library-folder SpItems). If so, it delegates to the
 * policy passed into the constructor.
 */
final class InsidePolicy_ItemIntoLibraryFolder extends InsertPolicy {
    private InsertPolicy _policy;

    public InsidePolicy_ItemIntoLibraryFolder(InsertPolicy policy) {
        _policy = policy;
    }

    public SpInsertInfo evalInsert(SpItem newItem, SpItem parent) {
        Assert.notFalse(parent instanceof SpLibraryFolder);

        // Make sure there aren't any library folders in this folder.
        SpItem temp = parent.child();

        while (temp != null) {
            if (temp instanceof SpLibraryFolder) {
                return null;
            }

            temp = temp.next();
        }

        // Delegate to the other policy
        return _policy.evalInsert(newItem, parent);
    }
}

/**
 * Insert a component inside an item.
 */
final class InsidePolicy_Component extends InsertPolicy {
    public SpInsertInfo evalInsert(SpItem newChild, SpItem parent) {
        SpObsComp oc = SpTreeMan.findConflictingObsComp(parent,
                (SpObsComp) newChild);

        return new SpInsertInfo(INS_INSIDE, parent, oc);
    }
}

/**
 * Insert a component inside a link.
 *
 * This only works if the link is currently linked to an observation, and
 * only if the observation will  take the component. Components are not made
 * children of the link, but rather children of the observation linked to.
 */
final class InsidePolicy_IntoLink extends InsertPolicy {
    public SpInsertInfo evalInsert(SpItem newItem, SpItem parent) {
        Assert.notFalse(parent instanceof SpObsLink);
        SpObsLink obsLink = (SpObsLink) parent;

        SpItem child = obsLink.child();
        if (child == null) {
            return null;
        }

        Assert.notFalse(child instanceof SpObs);

        return SpTreeMan.doEvalInsertInside(newItem, child);
    }
}

/**
 * Insert an obs inside a link.
 *
 * This only works if the link is currently not linked to an observation.
 */
final class InsidePolicy_ObsIntoLink extends InsertPolicy {
    public SpInsertInfo evalInsert(SpItem newItem, SpItem parent) {
        Assert.notFalse(parent instanceof SpObsLink);
        Assert.notFalse(newItem instanceof SpObs);

        SpObsLink obsLink = (SpObsLink) parent;

        SpItem child = obsLink.child();
        if (child != null) {
            return null;
        }

        return new SpInsertInfo(INS_INSIDE, parent);
    }
}

/**
 * Insert an item immediately inside another item.
 */
final class InsidePolicy_Basic extends InsertPolicy {
    public SpInsertInfo evalInsert(SpItem newItem, SpItem parent) {
        return new SpInsertInfo(INS_INSIDE, parent);
    }
}

/**
 * Insert the newItem after the target.
 *
 * If the target is a component, insert it after any siblings of the target
 * that are also components.  This keeps the components at the top of the tree.
 */
final class AfterPolicy_AfterComponents extends InsertPolicy {
    public SpInsertInfo evalInsert(SpItem newItem, SpItem sibling) {
        // Find the end of the components
        SpItem last = sibling;
        while ((sibling instanceof SpObsComp) || (sibling instanceof SpNote)) {
            last = sibling;
            sibling = sibling.next();
        }

        if (sibling instanceof SpIterFolder) {
            last = sibling;
        }

        return new SpInsertInfo(INS_AFTER, last);
    }
}

/**
 * Insert a component after another component.
 */
final class AfterPolicy_Component extends InsertPolicy {
    public SpInsertInfo evalInsert(SpItem newItem, SpItem sibling) {
        // If the sibling is a note, make sure that the note isn't in a place
        // where it would be illegal to place an observation component.

        if (sibling instanceof SpNote) {
            SpItem prev = sibling;

            while ((prev != null) && (prev instanceof SpNote)) {
                prev = prev.prev();
            }

            if ((prev != null) && !(prev instanceof SpObsComp)) {
                return null;
            }
        }

        SpObsComp oc;
        oc = SpTreeMan.findConflictingObsComp(sibling.parent(),
                (SpObsComp) newItem);

        return new SpInsertInfo(INS_AFTER, sibling, oc);
    }
}

/**
 * Insert an item immediately after another item.
 */
final class AfterPolicy_Basic extends InsertPolicy {
    public SpInsertInfo evalInsert(SpItem newItem, SpItem sibling) {
        return new SpInsertInfo(INS_AFTER, sibling);
    }
}

/**
 * This is a utility class used for insertion and extraction of SpItems to form
 * legal Science Programs and Plans.
 *
 * The SpItem base class contains the primitive insert and extract methods.
 * This utility uses those methods but performs additional evaluation to make
 * sure that only legal programs are created.
 *
 * @see SpItem#insert
 * @see SpItem#extract
 */
public final class SpTreeMan implements SpInsertConstants {
    private static Hashtable<String, InsertPolicy> _insertInside =
            new Hashtable<String, InsertPolicy>();

    private static Hashtable<String, InsertPolicy> _insertAfter =
            new Hashtable<String, InsertPolicy>();

    static {
        InsidePolicy_ItemIntoLibraryFolder libraryPolicy;
        InsertPolicy ip;

        // ======= Insert inside (item,parent) =======

        ip = new InsidePolicy_AfterComponents();
        libraryPolicy = new InsidePolicy_ItemIntoLibraryFolder(ip);

        // OR Folder (for OMP project)
        // Note that for the time being "fo" (Obs Folders) is used for
        // AND folders and "og" (Obs Groups) is used for MSB folders
        // added by MFO (06 July 2001)
        _insertInside.put("of,pr", ip);
        _insertInside.put("of,pl", ip);
        _insertInside.put("of,p1", ip);
        _insertInside.put("of,lf", libraryPolicy);

        // Survey components can be inserted either within MSBs or within
        // root items
        _insertInside.put("sc,pr", ip);
        _insertInside.put("sc,pl", ip);
        _insertInside.put("sc,p1", ip);
        _insertInside.put("sc,og", ip);
        _insertInside.put("sc,of", ip);
        _insertInside.put("sc,fo", ip);
        _insertInside.put("sc,lf", libraryPolicy);

        // Inserting Obs Folders
        _insertInside.put("fo,pr", ip);
        _insertInside.put("fo,pl", ip);
        _insertInside.put("fo,p1", ip);
        _insertInside.put("fo,of", ip); // MFO
        _insertInside.put("fo,sc", ip); // SDW
        _insertInside.put("fo,lf", libraryPolicy);

        // Inserting Obs Groups
        _insertInside.put("og,pr", ip);
        _insertInside.put("og,pl", ip);
        _insertInside.put("og,p1", ip);
        _insertInside.put("og,of", ip); // MFO
        _insertInside.put("og,fo", ip);
        _insertInside.put("og,sc", ip); // SDW
        _insertInside.put("og,lf", libraryPolicy);

        // Inserting Observations
        _insertInside.put("ob,pr", ip);
        _insertInside.put("ob,pl", ip);
        _insertInside.put("ob,p1", ip);
        _insertInside.put("ob,of", ip); // MFO
        _insertInside.put("ob,fo", ip);
        _insertInside.put("ob,og", ip);
        _insertInside.put("ob,sc", ip);
        _insertInside.put("ob,lf", libraryPolicy);

        // Inserting Obs Links
        _insertInside.put("li,pr", ip);
        _insertInside.put("li,pl", ip);
        _insertInside.put("li,p1", ip);
        _insertInside.put("li,of", ip); // MFO
        _insertInside.put("li,fo", ip);
        _insertInside.put("li,og", ip);
        _insertInside.put("li,lf", libraryPolicy);

        // Inserting Iterator Folders
        ip = new InsidePolicy_IteratorFolder();
        libraryPolicy = new InsidePolicy_ItemIntoLibraryFolder(ip);
        _insertInside.put("if,pr", ip);
        _insertInside.put("if,pl", ip);
        _insertInside.put("if,p1", ip);
        _insertInside.put("if,of", ip); // MFO
        _insertInside.put("if,fo", ip);
        _insertInside.put("if,ob", ip);
        _insertInside.put("if,og", ip);
        _insertInside.put("if,lf", libraryPolicy);

        // Inserting Iterator Components
        ip = new InsidePolicy_IteratorComp();
        _insertInside.put("ic,if", ip);
        _insertInside.put("ic,ic", ip);
        _insertInside.put("no,ic", ip);

        // Inserting Components
        ip = new InsidePolicy_Component();
        libraryPolicy = new InsidePolicy_ItemIntoLibraryFolder(ip);
        _insertInside.put("oc,pr", ip);
        _insertInside.put("oc,pl", ip);
        _insertInside.put("oc,p1", ip);
        _insertInside.put("oc,of", ip); // MFO
        _insertInside.put("oc,fo", ip);
        _insertInside.put("oc,ob", ip);
        _insertInside.put("oc,og", ip);
        _insertInside.put("oc,lf", libraryPolicy);

        // Inserting inside links
        ip = new InsidePolicy_IntoLink();
        _insertInside.put("oc,li", ip);
        _insertInside.put("if,li", ip);
        _insertInside.put("ob,li", new InsidePolicy_ObsIntoLink());

        // Inserting Library Folders
        ip = new InsidePolicy_LibraryFolder();
        _insertInside.put("lf,lb", ip);
        _insertInside.put("lf,lf", ip);

        // Notes
        ip = new InsidePolicy_Basic();
        _insertInside.put("no,pr", ip);
        _insertInside.put("no,pl", ip);
        _insertInside.put("no,p1", ip);
        _insertInside.put("no,lb", ip);
        _insertInside.put("no,lf", ip);
        _insertInside.put("no,of", ip); // MFO
        _insertInside.put("no,fo", ip);
        _insertInside.put("no,ob", ip);
        _insertInside.put("no,li", ip);
        _insertInside.put("no,if", ip);
        _insertInside.put("no,og", ip);
        _insertInside.put("no,sc", ip);
        // also _insertInside.put("no,ic", ip); defined above to use
        // the "InsidePolicy_IteratorComp".

        // ======= Insert after (item,sibling) =======

        ip = new AfterPolicy_AfterComponents();

        // OR Folder (for OMP project)
        // Note that for the time being "fo" (Obs Folders) is used for
        // AND folders and "og" (Obs Groups) is used for MSB folders
        // added by MFO (06 July 2001)
        _insertAfter.put("of,of", ip);
        _insertAfter.put("of,fo", ip);
        _insertAfter.put("of,ob", ip);
        _insertAfter.put("of,li", ip);
        _insertAfter.put("of,oc", ip);
        _insertAfter.put("of,no", ip);
        _insertAfter.put("of,if", ip);
        _insertAfter.put("of,og", ip);

        // Obs Folders
        _insertAfter.put("fo,of", ip); // MFO
        _insertAfter.put("fo,fo", ip);
        _insertAfter.put("fo,ob", ip);
        _insertAfter.put("fo,li", ip);
        _insertAfter.put("fo,oc", ip);
        _insertAfter.put("fo,no", ip);
        _insertAfter.put("fo,if", ip);
        _insertAfter.put("fo,og", ip);

        // Obs Groups
        _insertAfter.put("og,of", ip); // MFO
        _insertAfter.put("og,fo", ip);
        _insertAfter.put("og,ob", ip);
        _insertAfter.put("og,li", ip);
        _insertAfter.put("og,oc", ip);
        _insertAfter.put("og,no", ip);
        _insertAfter.put("og,if", ip);
        _insertAfter.put("og,og", ip);
        _insertAfter.put("og,sc", ip);

        // Survey Container
        _insertAfter.put("sc,of", ip); // MFO
        _insertAfter.put("sc,fo", ip);
        _insertAfter.put("sc,ob", ip);
        _insertAfter.put("sc,og", ip);
        _insertAfter.put("sc,li", ip);
        _insertAfter.put("sc,oc", ip);
        _insertAfter.put("sc,no", ip);
        _insertAfter.put("sc,if", ip);
        _insertAfter.put("sc,sc", ip);

        // Observations
        _insertAfter.put("ob,of", ip); // MFO
        _insertAfter.put("ob,fo", ip);
        _insertAfter.put("ob,ob", ip);
        _insertAfter.put("ob,li", ip);
        _insertAfter.put("ob,oc", ip);
        _insertAfter.put("ob,no", ip);
        _insertAfter.put("ob,if", ip);
        _insertAfter.put("ob,og", ip);
        _insertAfter.put("ob,sc", ip);

        // Obs Links
        _insertAfter.put("li,of", ip); // MFO
        _insertAfter.put("li,fo", ip);
        _insertAfter.put("li,ob", ip);
        _insertAfter.put("li,li", ip);
        _insertAfter.put("li,oc", ip);
        _insertAfter.put("li,no", ip);
        _insertAfter.put("li,if", ip);
        _insertAfter.put("li,og", ip);

        // Iterator Folders
        _insertAfter.put("if,oc", ip);
        _insertAfter.put("if,no", ip);

        ip = new AfterPolicy_Basic();

        // Iterator Components
        _insertAfter.put("ic,ic", ip);
        _insertAfter.put("ic,no", ip);

        // Library Folders
        _insertAfter.put("lf,lf", ip);
        _insertAfter.put("lf,no", ip);

        // Notes
        _insertAfter.put("no,of", ip); // MFO
        _insertAfter.put("no,fo", ip);
        _insertAfter.put("no,og", ip);
        _insertAfter.put("no,ob", ip);
        _insertAfter.put("no,lf", ip);
        _insertAfter.put("no,li", ip);
        _insertAfter.put("no,oc", ip);
        _insertAfter.put("no,ic", ip);
        _insertAfter.put("no,no", ip);
        _insertAfter.put("no,pr", ip); // SdW
        _insertAfter.put("no,sc", ip);

        ip = new AfterPolicy_Component();

        // Components
        _insertAfter.put("oc,oc", ip);
        _insertAfter.put("oc,no", ip);
        _insertAfter.put("oc,og", ip); // Schema allows Site Quality
        _insertAfter.put("oc,of", ip); // in the top level, so
        _insertAfter.put("oc,fo", ip); // we seem to need all these?
        _insertAfter.put("oc,sc", ip); // GSB 8/13/13
    }

    /**
     * The constructor is made private so that instances of this class cannot
     * be created.
     *
     * This is a utility class and should be used via its static methods.
     */
    private SpTreeMan() {
    }

    /**
     * In the given scope, find an observation component (if any) of a type
     * conflicting with the given observation component.
     */
    public static SpObsComp findConflictingObsComp(SpItem parent,
            SpObsComp spObsComp) {
        // See if this obs comp must be unique a scope.
        // If not, nothing can conflict with it.
        if (!spObsComp.mustBeUnique()) {
            return null;
        }

        // Evaluate the scope to see whether a component of the given subtype
        // already exists at this scope.
        SpType type = spObsComp.type();
        SpObsComp oc = SpTreeMan.findObsCompSubtype(parent, type);

        // If the component is the same object (identical object ref) as the
        // one being inserted, then there is no conflict.
        if (oc == spObsComp) {
            return null;
        }

        // If there already is a component of this exact subtype, then there
        // is a conflict.
        if (oc != null) {
            return oc;
        }

        // Now we know there was no component with the same subtype as the
        // new one. Unless the new component is an instrument, and there
        // is already an instrument in this scope, then there is no conflict.

        if (spObsComp instanceof SpInstObsComp) {
            return SpTreeMan.findInstrumentInContext(parent);
        }

        return null;
    }

    /**
     * Find an observation component of a given subtype in the given scope.
     *
     * Only searches the given scope. It does not navigate the tree hierarchy.
     *
     * @param parent  the SpItem defining the scope to search
     * @param type    the observation component subtype to look for
     */
    public static SpObsComp findObsCompSubtype(SpItem parent, SpType type) {
        SpObsComp oc = null;
        Enumeration<SpItem> children = parent.children();

        while (children.hasMoreElements()) {
            SpItem child = children.nextElement();

            if (child instanceof SpObsComp && child.type().equals(type)) {
                oc = (SpObsComp) child;
                break;
            }
        }

        return oc;
    }

    /**
     * Find the SpTelescopeObsComp associated with this context, if any.
     *
     * Only searches the given scope. It does not navigate the tree hierarchy.
     */
    public static SpTelescopeObsComp findTargetListInContext(SpItem spItem) {
        SpTelescopeObsComp toc = null;
        SpItem returned = findSpItemInContext(spItem, SpTelescopeObsComp.class);

        if (returned != null) {
            toc = (SpTelescopeObsComp) returned;
        }

        return toc;
    }

    /**
     * Find the SpSurveyContainer associated with this context, if any.
     *
     * Only searches up the tree from the given item.
     */
    public static SpSurveyContainer findSurveyContainerInContext(
            SpItem spItem) {
        SpSurveyContainer container = null;

        while (spItem != null) {
            if (spItem instanceof SpSurveyContainer) {
                container = (SpSurveyContainer) spItem;
                break;
            }

            spItem = spItem.parent();
        }

        return container;
    }

    /**
     * Find the SpInstObsComp associated with this context, if any.
     *
     * Only searches the given scope. It does not navigate the tree hierarchy.
     */
    public static SpInstObsComp findInstrumentInContext(SpItem spItem) {
        SpInstObsComp ioc = null;
        SpItem returned = findSpItemInContext(spItem, SpInstObsComp.class);

        if (returned != null) {
            ioc = (SpInstObsComp) returned;
        }

        return ioc;
    }

    public static SpNote findObserverNoteInContext(SpItem spItem) {
        SpNote note = null;
        Enumeration<SpItem> e = spItem.children();

        while (e.hasMoreElements()) {
            SpItem child = e.nextElement();

            if (child instanceof SpNote) {
                SpNote tmp = (SpNote) child;

                if (tmp.isObserveInstruction()) {
                    note = tmp;
                    break;
                }
            }
        }

        return note;
    }

    /**
     * Find the "root" item of the given item.
     */
    public static SpItem findRootItem(SpItem spItem) {
        SpItem parent = spItem.parent();

        while (parent != null) {
            spItem = parent;
            parent = spItem.parent();
        }

        return spItem;
    }

    /**
     * Generic method for finding an SpItem.
     *
     * @param spItem Root item for search
     * @param c Class of type to match
     * @return SpItem of Class type c.
     */
    public static SpItem findSpItemOfType(SpItem spItem, Class<?> c) {
        if (c.isInstance(spItem)) {
            return spItem;
        }

        SpItem parent = spItem.parent();

        SpItem foundItem = null;

        if (spItem instanceof SpObsContextItem) {
            foundItem = findSpItemInContext(spItem, c);
        }

        if (foundItem == null && parent != null) {
            foundItem = findSpItemOfType(parent, c);
        }

        return foundItem;
    }

    /**
     * Find the target list component associated with the given scope scope of
     * the given item.
     *
     * @param spItem  the SpItem defining the scope to search
     */
    public static SpTelescopeObsComp findTargetList(SpItem spItem) {
        SpTelescopeObsComp telescopeObsComp = null;
        SpItem returned = findSpItemOfType(spItem, SpTelescopeObsComp.class);

        if (returned != null) {
            telescopeObsComp = (SpTelescopeObsComp) returned;
        }

        return telescopeObsComp;
    }

    /**
     * Find an instrument observation component associated with the scope of
     * the given item.
     *
     * @param spItem the SpItem defining the scope to search
     */
    public static SpInstObsComp findInstrument(SpItem spItem) {
        if (spItem instanceof SpInstObsComp) {
            return (SpInstObsComp) spItem;
        }

        SpItem parent = spItem.parent();
        SpItem searchItem;

        if (!(spItem instanceof SpObsContextItem)) {
            searchItem = parent;

            if (parent == null) {
                return null;
            }
        } else {
            searchItem = spItem;
        }

        Enumeration<SpItem> children = searchItem.children();

        while (children.hasMoreElements()) {
            SpItem child = children.nextElement();

            if (child instanceof SpInstObsComp) {
                return (SpInstObsComp) child;
            }
        }

        if (parent != null) {
            return findInstrument(parent);
        }

        return null;
    }

    /**
     * Find the ObservationContext item that contains the given SpItem.
     */
    public static SpObsContextItem findObsContext(SpItem spItem) {
        while ((spItem != null) && !(spItem instanceof SpObsContextItem)) {
            spItem = spItem.parent();
        }

        return (SpObsContextItem) spItem;
    }

    private static Class<?> classForName(String name) {
        Class<?> c = null;

        try {
            c = Class.forName(name);
        } catch (Exception ex) {
            System.out.println("Problem instantiating: " + name);
            System.out.println(ex);
        }

        return c;
    }

    /**
     * Find all of the items which are instances of the class indicated by the
     * given fully-qualified className that are in the scope of the given
     * SpItem.
     */
    public static Vector<SpItem> findAllItems(SpItem rootItem,
            String className) {
        Vector<SpItem> v = new Vector<SpItem>();
        // Get the class from the className
        Class<?> c = classForName(className);

        if (c != null) {
            if (rootItem instanceof SpObsComp) {
                _findAllItems(rootItem.parent(), c, v);

            } else {
                if (rootItem.getClass().equals(c)) {
                    v.addElement(rootItem);
                }

                _findAllItems(rootItem, c, v);
            }
        }

        return v;
    }

    /**
     * Find all of the items which are instances of the class that are in the
     * scope of the given SpItem.
     *
     * This can be used to find instances of superclasses
     */
    public static Vector<SpItem> findAllInstances(SpItem rootItem,
            String name) {
        Vector<SpItem> v = new Vector<SpItem>();

        if (name != null && name.length() > 0) {
            Class<?> c = classForName(name);

            if (c != null) {
                if (rootItem instanceof SpObsContextItem) {
                    // Start searching from the parent node
                    _findAllInstances(rootItem, c, v);

                } else {
                    if (c.isInstance(rootItem)) {
                        v.addElement(rootItem);
                    }

                    _findAllInstances(rootItem.parent(), c, v);
                }
            }
        }

        return v;
    }

    /**
     * Find all of the items of the given Class in the scope of the given
     * SpItem.
     */
    private static void _findAllInstances(SpItem rootItem, Class<?> c,
            Vector<SpItem> v) {
        SpItem child = rootItem.child();
        while (child != null) {
            if (c.isInstance(child)) {
                v.addElement(child);
            }

            // For efficiency, only recurse if the child has children ...
            if (child.child() != null) {
                _findAllInstances(child, c, v);
            }

            child = child.next();
        }
    }

    /**
     * Find all of the items of the given Class in the scope of the given
     * SpItem.
     */
    private static void _findAllItems(SpItem rootItem, Class<?> c,
            Vector<SpItem> v) {
        SpItem child = rootItem.child();

        while (child != null) {
            if (child.getClass().equals(c)) {
                v.addElement(child);
            }

            // For efficiency, only recurse if the child has children ...
            if (child.child() != null) {
                _findAllItems(child, c, v);
            }

            child = child.next();
        }
    }

    /**
     * Find the DR recipe component associated with the given scope
     * scope of the given item.
     *
     * @param spItem the SpItem defining the scope to search
     *
     * copied from orac.util.SpItemUtilities
     */
    public static SpDRObsComp findDRRecipe(SpItem spItem) {
        SpDRObsComp drObsComp = null;
        SpItem returned = findSpItemOfType(spItem, SpDRObsComp.class);

        if (returned != null) {
            drObsComp = (SpDRObsComp) returned;
        }

        return drObsComp;
    }

    /**
     * Find the SpDRObsComp associated with this context, if any.
     *
     * Only searches the given scope.  It does not navigate the tree hierarchy.
     *
     * copied from orac.util.SpItemUtilities
     */
    public static SpDRObsComp findDRRecipeInContext(SpItem spItem) {
        SpDRObsComp drr = null;
        SpItem returned = findSpItemInContext(spItem, SpDRObsComp.class);

        if (returned != null) {
            drr = (SpDRObsComp) returned;
        }

        return drr;
    }

    /**
     * Generic method for finding a single item in context.
     *
     * @param spItem Root item
     * @param c Class of type
     *
     * @return first instance of Class c or null.
     */
    public static SpItem findSpItemInContext(SpItem spItem, Class<?> c) {
        SpItem returnableItem = null;
        Enumeration<SpItem> e = spItem.children();

        while (e.hasMoreElements()) {
            SpItem child = e.nextElement();

            if (c.isInstance(child)) {
                returnableItem = child;

                break;
            }
        }

        return returnableItem;
    }

    /**
     * Method to find a parent of a given type.
     *
     * @param spItem item of which to search ancestry
     * @param klass class for which to search
     *
     * @return first parent of given class or null
     */
    public static SpItem findParentOfType(SpItem spItem, Class<?> klass) {
        SpItem parent = spItem.parent();

        while (parent != null) {
            if (klass.isInstance(parent)) {
                return parent;
            }

            parent = parent.parent();
        }

        return null;
    }

    /**
     * Helper for the evalInsertInside() methods.
     */
    static SpInsertInfo doEvalInsertInside(SpItem newItem, SpItem parent) {
        if (parent == null) {
            return null;
        }

        // Is there a definition for this combination?
        String key = newItem.typeStr() + "," + parent.typeStr();
        InsertPolicy ip = _insertInside.get(key);

        if (ip == null) {
            return null;
        }

        return ip.evalInsert(newItem, parent);
    }

    /**
     * Evaluate the insertion of the <code>newItem</code> inside of the
     * <code>parent</code>.
     *
     * @param newItem The item to insert.
     * @param parent  The item (already in the Science Program) in which to
     *                insert.
     *
     * @return A SpInsertData structure that describes where the newItem will
     *         be inserted, if legal. Otherwise, if the item cannot be legally
     *         inserted inside the parent, null is returned.
     */
    public static SpInsertData evalInsertInside(SpItem newItem, SpItem parent) {
        SpItem[] spItemA = {newItem};

        return evalInsertInside(spItemA, parent);
    }

    /**
     * Evaluate the insertion of the <code>newItems</code> inside of the
     * <code>parent</code>.
     *
     * @param newItems The array of items to insert.
     * @param parent   The item (already in the Science Program) in which to
     *                 insert.
     *
     * @return A SpInsertData structure that describes where the newItems will
     *         be inserted, if legal. Otherwise, if the item cannot be legally
     *         inserted inside the parent, null is returned.
     */
    public static SpInsertData evalInsertInside(SpItem[] newItems,
            SpItem parent) {
        // Make sure the parent isn't one of the newItems
        for (int i = 0; i < newItems.length; ++i) {
            if (parent == newItems[i]) {
                return null;
            }
        }

        // Make sure the parent isn't already the parent of the newItems

        // Evaluate the insertion of the last item. Work from last to first
        // to make sure that all the items stay together. They could be
        // separated otherwise, for instance if the group contains an obs
        // comp and an obs and is being inserted inside a group.
        SpInsertInfo spII = doEvalInsertInside(
                newItems[newItems.length - 1], parent);

        return _completeEvalInsert(newItems, spII);
    }

    /**
     * Helper for the evalInsertAfter() methods.
     */
    static SpInsertInfo doEvalInsertAfter(SpItem newItem, SpItem sibling) {
        // Is there a definition for this combination?
        String key = newItem.typeStr() + "," + sibling.typeStr();
        InsertPolicy ip = _insertAfter.get(key);

        if (ip == null) {
            return null;
        }

        // Can the newItem legally be placed at the sibling's scope?
        SpInsertInfo spII;
        spII = SpTreeMan.doEvalInsertInside(newItem, sibling.parent());

        if (spII == null) {
            return null;
        }

        // See if the new item can be inserted using the insertion policy
        // found in the first step.
        return ip.evalInsert(newItem, sibling);
    }

    /**
     * Evaluate the insertion of the <code>newItem</code> after the
     * <code>sibling</code>.
     *
     * @param newItem The item to insert.
     * @param sibling The item (already in the Science Program) after which to
     *                insert.
     *
     * @return A SpInsertData structure that describes where the newItems will
     *         be inserted, if legal. Otherwise, if the item cannot be legally
     *         inserted after the sibling, null is returned.
     */
    public static SpInsertData evalInsertAfter(SpItem newItem, SpItem sibling) {
        SpItem[] spItemA = {newItem};

        return evalInsertAfter(spItemA, sibling);
    }

    /**
     * Evaluate the insertion of the <code>newItems</code> after the
     * <code>sibling</code>.
     *
     * @param newItems The array of items to insert.
     * @param sibling  The item (already in the Science Program) after which to
     *                 insert.
     *
     * @return A SpInsertData structure that describes where the newItems will
     *         be inserted, if legal. Otherwise, if the item cannot be legally
     *         inserted after the sibling, null is returned.
     */
    public static SpInsertData evalInsertAfter(SpItem[] newItems,
            SpItem sibling) {
        // Make sure the sibling isn't one of the newItems
        for (int i = 0; i < newItems.length; ++i) {
            if (sibling == newItems[i]) {
                return null;
            }
        }

        // Evaluate the insertion of the last item. Work from last to first
        // to make sure that all the items stay together. They could be
        // separated otherwise, for instance if the group contains an obs
        // comp and an obs and is being inserted inside a group.

        SpInsertInfo spII = doEvalInsertAfter(
                newItems[newItems.length - 1], sibling);

        return _completeEvalInsert(newItems, spII);
    }

    /**
     * Complete the evalution of a set of components either inside or after
     * a reference component.
     *
     * This method is called to complete evalInsertInside and evalInsertAfter.
     * It is assumed that spII is the result of evaluating the last component.
     * The remaining components are evaluated from next to last to first
     * (to keep the items grouped).
     */
    private static SpInsertData _completeEvalInsert(SpItem[] newItems,
            SpInsertInfo spII) {
        if (spII == null) {
            return null;
        }

        int result = spII.result;
        SpItem referant = spII.referant;

        // Make sure the referant isn't in the set of newItems. Can't move
        // to a position relative to an item in the set itself.
        for (int i = 0; i < newItems.length; ++i) {
            if (referant == newItems[i]) {
                return null;
            }
        }

        // Vector of replaced items.
        Vector<SpItem> repV = null;

        // If it replaces an existing item, then create the replaced items
        // vector and add the replaced item to it.
        if (spII.replaceItem != null) {
            repV = new Vector<SpItem>();
            repV.addElement(spII.replaceItem);
        }

        // Now evaluate the remaining items.
        for (int i = newItems.length - 2; i >= 0; --i) {
            if (result == INS_INSIDE) {
                spII = doEvalInsertInside(newItems[i], referant);

            } else {
                spII = doEvalInsertAfter(newItems[i], referant);
            }

            if (spII == null) {
                return null;
            }

            // Make sure this item would get inserted in the same place ...
            if ((result != spII.result) || (referant != spII.referant)) {
                return null;
            }

            if (spII.replaceItem != null) {
                if (repV == null) {
                    repV = new Vector<SpItem>();
                }

                repV.addElement(spII.replaceItem);
            }
        }

        // If there were any replaced items, move them into an array.
        SpItem[] repA = null;

        if (repV != null) {
            repA = new SpItem[repV.size()];

            for (int i = 0; i < repA.length; ++i) {
                repA[i] = repV.elementAt(i);
            }

            // Now we have to worry that the extracted items array contains
            // the referant (the item relative to which the new items will
            // be inserted.) If so, adjust the referant to make it valid.
            if (result == INS_AFTER) {
                SpItem parent = referant.parent();

                validateReferant: while (true) {
                    for (int i = 0; i < repA.length; ++i) {
                        if (referant == repA[i]) {
                            referant = referant.prev();

                            if (referant == null) {
                                referant = parent;
                                result = INS_INSIDE;

                                break validateReferant;

                            } else {
                                continue validateReferant;
                            }
                        }
                    }

                    break validateReferant;
                }
            }
        }

        // Create and return the SpInsertData.
        return new SpInsertData(result, newItems, referant, repA);
    }

    /**
     * Insert items into a program hierarchy according to the information in
     * the SpInsertData structure.
     *
     * This method is typically called after one of the "eval" methods has
     * returned a non-null SpInsertData to use as the argument.
     */
    public static void insert(SpInsertData spID) {
        if (spID.result == INS_INSIDE) {
            if (spID.replaceItems != null) {
                spID.referant.extract(spID.replaceItems);
            }

            spID.referant.insert(spID.items, null);

        } else {
            SpItem parent = spID.referant.parent();

            if (spID.replaceItems != null) {
                parent.extract(spID.replaceItems);
            }

            parent.insert(spID.items, spID.referant);
        }
    }

    /**
     * Move the items in the program hierarchy according to the information in
     * the SpInsertData structure.
     *
     * This method is typically called after one of the "eval" methods has
     * returned a non-null SpInsertData to use as the argument.
     */
    public static void move(SpInsertData spID) {
        SpItem curParent = spID.items[0].parent();
        SpItem newParent;
        SpItem afterChild;

        if (spID.result == INS_INSIDE) {
            newParent = spID.referant;
            afterChild = null;

        } else {
            newParent = spID.referant.parent();
            afterChild = spID.referant;
        }

        if (spID.replaceItems != null) {
            newParent.extract(spID.replaceItems);
        }

        curParent.move(spID.items, newParent, afterChild);
    }

    /**
     * Returns true if the extraction is legal (would succeed).
     */
    public static boolean evalExtract(SpItem spItem) {
        SpItem parent = spItem.parent();
        if (parent == null) {
            return false;
        }

        // So far there's only one illegal case.
        if ((spItem instanceof SpObs) && (parent instanceof SpObsLink)) {
            return false;
        }

        return true;
    }

    /**
     * Evaluate the set of items for extraction, returning true if this
     * operation would succeed.
     */
    public static boolean evalExtract(SpItem[] spItems) {
        for (int i = 0; i < spItems.length; ++i) {
            if (!evalExtract(spItems[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Extract the given spItem from its place in its program.
     */
    public static boolean extract(SpItem spItem) {
        if (!evalExtract(spItem)) {
            return false;
        }

        spItem.extract();

        return true;
    }

    /**
     * Extract the given spItems from their place in the program.
     */
    public static boolean extract(SpItem[] spItems) {
        if (!evalExtract(spItems)) {
            return false;
        }

        SpItem parent = spItems[0].parent();
        parent.extract(spItems);

        return true;
    }
}
