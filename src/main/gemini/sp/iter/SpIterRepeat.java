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

package gemini.sp.iter;

import gemini.sp.SpItem;
import gemini.sp.SpType;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Defines the attributes used by the repeat iterator.
 */
interface SpIterRepeatConstants {
    public static final String COUNT = "repeatCount";
    public static final int COUNT_DEF = 1;
}

/**
 * Enumeration of the repeat iterator's values.
 */
@SuppressWarnings("serial")
class SpIterRepeatEnumeration extends SpIterEnumeration implements
        SpIterRepeatConstants {
    private int _curCount = 0;
    private int _maxCount;

    SpIterRepeatEnumeration(SpIterRepeat iterRepeat) {
        super(iterRepeat);

        _maxCount = iterRepeat.getCount();
    }

    protected boolean _thisHasMoreElements() {
        return (_curCount < _maxCount);
    }

    protected SpIterStep _thisFirstElement() {
        return _thisNextElement();
    }

    protected SpIterStep _thisNextElement() {
        SpIterValue siv = new SpIterValue("loop",
                String.valueOf(_curCount + 1));

        return new SpIterStep("comment", _curCount++, _iterComp, siv);
    }

}

/**
 * A simple iterator that repeats the steps of nested iterators the specified
 * number of times.
 */
@SuppressWarnings("serial")
public class SpIterRepeat extends SpIterComp implements SpIterRepeatConstants {
    /**
     * Default constructor.
     */
    public SpIterRepeat() {
        super(SpType.ITERATOR_COMPONENT_REPEAT);

        _avTable.noNotifySet(COUNT, String.valueOf(COUNT_DEF), 0);
    }

    /**
     * Override getTitle to return the repeat count.
     */
    public String getTitle() {
        return "Repeat (" + getCount() + "X)";
    }

    /**
     * Get the repeat count.
     */
    public int getCount() {
        return _avTable.getInt(COUNT, COUNT_DEF);
    }

    /**
     * Set the repeat count as an integer.
     */
    public void setCount(int i) {
        _avTable.set(COUNT, i);
    }

    /**
     * Set the repeat count as a String.
     */
    public void setCount(String s) {
        _avTable.set(COUNT, s);
    }

    /**
     * Enumerate the values of this iterator.
     */
    public SpIterEnumeration elements() {
        return new SpIterRepeatEnumeration(this);
    }
}
