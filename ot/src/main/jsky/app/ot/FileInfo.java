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

import java.util.Observable;

/**
 * This class stores "file" data associated with a program.
 *
 * This includes the filename and directory path, and whether or not
 * the program has ever been saved under the given filename.
 */
public class FileInfo extends Observable {
    public String dir;
    public String filename;
    public boolean hasBeenSaved;

    public FileInfo() {
        filename = "Untitled";
        hasBeenSaved = false;
    }

    public FileInfo(String dir, String filename, boolean hasBeenSaved) {
        this.dir = dir;
        this.filename = filename;
        this.hasBeenSaved = hasBeenSaved;
    }

    public void setDir(String newDir) {
        dir = newDir;
    }

    public void setFilename(String newFilename) {
        filename = newFilename;
        setChanged();
        notifyObservers();
    }

    public void setHasBeenSaved(boolean newHasBeenSaved) {
        hasBeenSaved = newHasBeenSaved;
    }

    public String toString() {
        return "dir = " + dir
                + ", file = " + filename
                + ", hasBeenSaved = " + hasBeenSaved;
    }
}
