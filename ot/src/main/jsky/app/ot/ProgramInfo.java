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

import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;

import java.util.Hashtable;

/**
 * This class groups information that should be global to a given program.
 *
 * There is one ProgramInfo class per open program.
 */
public final class ProgramInfo {
    /**
     * The isPlan variable is true when the object being edited is a
     * Science Plan, and false when it is a Science Program.
     */
    public boolean isPlan = false;

    /**
     * When online is true, every edit made to the program or plan is
     * reflected to the database.
     */
    public boolean online = false;

    /**
     * The filename, save state etc.
     */
    public FileInfo file = null;

    /**
     * The user name under which the program or plan is stored in the database.
     */
    public LoginInfo login = null;

    /**
     * Contains a mapping of Science Program/Plan items to ProgramInfo
     * structures.
     */
    private static Hashtable<SpItem, ProgramInfo> _map =
            new Hashtable<SpItem, ProgramInfo>();

    /**
     * Register ProgramInfo for a  given root item.
     */
    public static void register(ProgramInfo pi, SpItem rootSpItem) {
        _map.put(rootSpItem, pi);
    }

    /**
     * Get the ProgramInfo for a given item.
     *
     * This method looks up the root item, and then uses that to look in
     * the hastable for the ProgramInfo.
     */
    public static ProgramInfo get(SpItem spItem) {
        SpItem root = SpTreeMan.findRootItem(spItem);
        return _map.get(root);
    }
}
